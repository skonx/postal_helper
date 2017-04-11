/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.beans;

import fr.trendev.postalhelper.ejbsessions.PostalCodeFRFacade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
        Pattern pattern = Pattern.compile(";");
        String csvFile = path + file;
        try (BufferedReader in = new BufferedReader(new FileReader(csvFile))) {
            in.lines().skip(1).forEach(l -> {
                String[] values = pattern.split(l);

                if (values.length == 4) {
                    facade.
                            create(values[2], values[3],
                                    null);
                } else {
                    if (values.length >= 5) {
                        facade.
                                create(values[2], (values[4] == null
                                        || values[4].isEmpty()) ? values[3] : values[3]
                                        + " ; " + values[4],
                                        (values.length == 6) ? values[5] : null);
                    } else {
                        LOG.log(Level.WARNING, "Cannot treat line : {0}", l);
                    }
                }
            });
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }
    }
}
