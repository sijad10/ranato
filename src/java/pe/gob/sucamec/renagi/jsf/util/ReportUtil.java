/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.jsf.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
public class ReportUtil {

    public static String mostrarClienteString(SbPersona cli) {
        return cli.getRznSocial() == null ? cli.getNombreCompleto() : cli.getRznSocial();
    }

    public static String mostrarDireccionPersonaComercio(SbPersona param) {
        String direccion = "";
        for (SbDireccion sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                direccion = (sbd.getViaId().getNombre().equals("OTRO") ? "" : sbd.getViaId().getNombre())
                        + " " + sbd.getDireccion()
                        + (sbd.getNumero() == null ? "" : (" " + sbd.getNumero()));
                direccion = direccion + ", distrito de " + sbd.getDistritoId().getNombre() + ", provincia de " + sbd.getDistritoId().getProvinciaId().getNombre() + ", departamento de " + sbd.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
                break;
            }
        }
        return direccion;
    }

    public static String mostrarDireccionPersona(SbPersona param) {
        String direccion = "";
        for (SbDireccion sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                direccion = (sbd.getViaId().getNombre().equals("OTRO") ? "" : sbd.getViaId().getNombre())
                        + " " + sbd.getDireccion()
                        + (sbd.getNumero() == null ? "" : (" " + sbd.getNumero()));
                direccion = direccion + ", distrito de " + sbd.getDistritoId().getNombre() + ", provincia de " + sbd.getDistritoId().getProvinciaId().getNombre() + ", departamento de " + sbd.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
                break;
            }
        }
        return direccion;
    }

    public static String[] mostrarDireccionPersonaAr(SbPersona param) {
        String[] direccion = new String[4];
        for (SbDireccion sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                direccion[0] = (sbd.getViaId().getNombre().equals("OTRO") ? "" : sbd.getViaId().getNombre()) 
                        + " " + sbd.getDireccion() 
                        + (sbd.getNumero() == null ? "" : (" " + sbd.getNumero()));
                direccion[1] = sbd.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
                direccion[2] = sbd.getDistritoId().getProvinciaId().getNombre();
                direccion[3] = sbd.getDistritoId().getNombre();
                break;
            }
        }
        return direccion;
    }
    
    public static String mostrarAnio(Date param) {
        if (param != null) {            
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
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
            return "-";
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

    public static String mostrarMes(Date param) {
        if (param != null) {
            SimpleDateFormat formatAnio = new SimpleDateFormat("MM", new Locale("ES"));
            return formatAnio.format(param);
        } else {
            return "BORRADOR";
        }
    }
    
    public static String mostrarTipoYNroDocumemtoString(SbPersona emp) {
        String cadena = "";
        if(emp.getTipoId().getCodProg().equals("TP_PER_JUR")){
            if(emp.getRuc() != null){
                cadena = "RUC";
            }
            cadena = cadena+ " N° " + emp.getRuc();
        }else{
            if(emp.getRuc() != null){
                cadena = "RUC"+ " N° " + emp.getRuc();
            }else{
                cadena = emp.getTipoDoc().getNombre()+ " - " + emp.getNumDoc();
            }                    
        }
        
        //return emp.getTipoDoc().getNombre() + " N° " + (emp.getTipoDoc().getCodProg().equals("TP_DOCID_RUC") ? emp.getRuc() : emp.getNumDoc());
        return cadena;
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

    // Mostrar direcciones
    public static List<SbDireccion> mostrarDirecciones(SbPersona param) {
        List<SbDireccion> lst = new ArrayList();
        for (SbDireccion sbd : param.getSbDireccionList()) {
            if (sbd.getActivo() == 1) {
                lst.add(sbd);
            }
        }
        return lst;
    }

}
