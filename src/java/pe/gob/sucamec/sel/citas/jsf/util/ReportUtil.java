/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.jsf.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author mespinoza
 */
public class ReportUtil {

    public static String mostrarAnio(Date param) {
        if (param != null) {            
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            return formatAnio.format(param);
        } else {
            return "BORRADOR";
        }
    }

    public static String mostrarMes(Date param) {
        if (param != null) {            
            SimpleDateFormat formatAnio = new SimpleDateFormat("MM", new Locale("ES"));
            return formatAnio.format(param);
        } else {
            return "BORRADOR";
        }
    }
    
    public static String mostrarFechaDdMmYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return dia + "/" + mes + "/" + anio;
        } else {
            return "BORRADOR";
        }
    }

    public static String mostrarFechaHhMmSs(Date param) {
        if (param != null) {
            SimpleDateFormat formatHora = new SimpleDateFormat("HH", new Locale("ES"));
            SimpleDateFormat formatMin = new SimpleDateFormat("mm", new Locale("ES"));
            SimpleDateFormat formatSeg = new SimpleDateFormat("ss", new Locale("ES"));
            String h = formatHora.format(param);
            String m = formatMin.format(param);
            String s = formatSeg.format(param);

            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return h + ":" + m + ":" + s;
        } else {
            return "BORRADOR";
        }
    }

    public static String mostrarFechaString(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + " de " + mes + " de " + anio;
        } else {
            return "BORRADOR";
        }
    }

    public static String mostrarFechaFormato(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + "" + mes.substring(0, 3).toUpperCase() + "" + anio;
        } else {
            return "BORRADOR";
        }
    }

    /**
     * *
     * FORMATEAR FECHA: dd/mm/yyyy
     *
     * @param param
     * @return
     */
    public static String formatoFechaDdMmYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return dia + "/" + mes + "/" + anio;
        } else {
            return "BORRADOR";
        }
    }

    public static String formatoFechaYyyyMmDdHhMmSs(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            SimpleDateFormat formatHora = new SimpleDateFormat("HH", new Locale("ES"));
            SimpleDateFormat formatMin = new SimpleDateFormat("mm", new Locale("ES"));
            SimpleDateFormat formatSeg = new SimpleDateFormat("ss", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);
            String h = formatHora.format(param);
            String m = formatMin.format(param);
            String s = formatSeg.format(param);
            
            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return anio + mes + dia + h + m + s;
        } else {
            return "BORRADOR";
        }
    }
    
    /**
     * OBTENER FORMATO DE FECHAS DE RESOLUCION PARA REPORTE MODIFICATORIA
     *
     * @param ini
     * @param fin
     * @return
     */
    public static String mostrarFechaRangoString(Date ini, Date fin) {
        String cadena = "";
        if (ini != null && fin != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(ini);
            String mes = formatMes.format(ini);
            String anio = formatAnio.format(ini);
            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            cadena = " desde el " + dia + " de " + mes + " de " + anio;

            dia = formatDia.format(fin);
            mes = formatMes.format(fin);
            anio = formatAnio.format(fin);
            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            cadena = cadena + " hasta el " + dia + " de " + mes + " de " + anio;

            return cadena;
        } else {
            return "BORRADOR";
        }
    }

}
