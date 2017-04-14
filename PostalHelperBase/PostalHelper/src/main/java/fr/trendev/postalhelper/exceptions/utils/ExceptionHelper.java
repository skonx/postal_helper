/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.postalhelper.exceptions.utils;

/**
 *
 * @author jsie
 */
public class ExceptionHelper {

    @SuppressWarnings("empty-statement")
    public final static Throwable findRootCauseException(Exception e) {
        Throwable t = e;
        for (; t.getCause() != null; t = t.getCause());
        return t;
    }
}
