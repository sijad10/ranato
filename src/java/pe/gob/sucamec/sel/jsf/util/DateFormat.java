/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.sucamec.sel.jsf.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Renato
 */
public class DateFormat {
    private final static String format = "yyyy-MM-dd hh:mm:ss";
    private final static SimpleDateFormat sdfLog = new SimpleDateFormat(format,new Locale("ES"));
    public static String logFormat(Date d)
    {
        return sdfLog.format(d);
    }
}
