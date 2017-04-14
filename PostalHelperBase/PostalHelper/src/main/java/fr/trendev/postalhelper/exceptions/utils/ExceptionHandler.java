/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.exceptions.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class ExceptionHandler implements
        org.eclipse.persistence.exceptions.ExceptionHandler {

    private static final Logger LOG = Logger.getLogger(ExceptionHandler.class.
            getName());

    public ExceptionHandler() {
    }

    @Override
    public Object handleException(RuntimeException re) {
        Throwable t = ExceptionHelper.
                findRootCauseException(re);
        LOG.log(Level.WARNING, "#### EXCEPTION HANDLER #### {0} : {1}",
                new Object[]{t.getClass(), t.getMessage()});
        throw re;
    }

}
