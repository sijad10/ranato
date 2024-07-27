/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.jsf.util;

/**
 *
 * @author gchavez
 */
public class StringUtil {

    public final static String VACIO = "";
    public final static String ESPACIO = " ";
    public final static String COMA = ",";
    public final static String PUNTOCOMA = ";";
    public final static String GUION = "-";
    public final static String NULO = "null";
    public final static String SLASH = "/";

    public static Boolean isNullOrEmpty(String dato) {
        if (dato == null || VACIO.equals(dato.trim())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Boolean isDigit(String cad) {
        cad = cad == null ? null : cad.trim();
        if (!isNullOrEmpty(cad)) {
            for (int i = 0; i < cad.length(); i++) {
                if (!Character.isDigit(cad.charAt(i))) {
                    return Boolean.FALSE;
                }
            }
        } else {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
