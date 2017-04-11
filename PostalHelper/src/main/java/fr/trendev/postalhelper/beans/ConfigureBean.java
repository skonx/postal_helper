/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.beans;

import fr.trendev.postalhelper.ejbsessions.PostalCodeFRFacade;
import fr.trendev.postalhelper.entities.PostalCodeFR;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class ConfigureBean {

    private static final Logger LOG = Logger.getLogger(ConfigureBean.class.
            getName());

    private final String path = "/Users/jsie/Google Drive/";
    private final String file = "laposte_hexasmal.csv";

    @EJB
    private PostalCodeFRFacade facade;

    @PostConstruct
    public void init() {
        getPostalCodes().forEach(facade::persist);
    }

    private Set<PostalCodeFR> getPostalCodes() {
        Pattern pattern = Pattern.compile(";");
        String csvFile = path + file;

        Set<PostalCodeFR> set = new TreeSet<>(Comparator.comparing(
                PostalCodeFR::getCode).thenComparing(PostalCodeFR::getTown));

        try (BufferedReader in = new BufferedReader(new FileReader(csvFile))) {
            set.addAll(in.lines().skip(1).map(l -> {
                String[] values = pattern.split(l);

                if (values.length == 4) {
                    return facade.
                            create(values[2], values[3],
                                    null);
                } else {
                    if (values.length >= 5) {
                        return facade.
                                create(values[2], (values[4] == null
                                        || values[4].isEmpty()) ? values[3] : values[3]
                                        + " ; " + values[4],
                                        (values.length == 6) ? values[5] : null);
                    } else {
                        LOG.log(Level.WARNING, "Cannot treat line : {0}", l);
                        return null;
                    }
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet()));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Exception parsing {0} : {1}", new Object[]{
                csvFile, ex.getMessage()});
        }
        LOG.log(Level.INFO, "PostalCode to Store : {0} ", set.size());
        return set;
    }
}
