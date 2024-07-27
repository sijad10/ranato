/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.turreg.data.TurProgramacion;

/**
 *
 * @author mespinoza
 */
public class ReportUtilTurno {

    public static String mostrarFechaDdMmYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            //mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());
            return dia + " / " + mes + " / " + anio;
        } else {
            return "-";
        }
    }

    public static String mostrarFechaDdMmYyyyString(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + " de " + mes + " del " + anio;
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
     * subir un pdf al repositorio
     *
     * @param id
     * @param parametros
     * @param datos
     * @param pathIn
     * @param pathOut
     * @throws java.lang.Exception
     */
    public static void subirPdf(String id, HashMap parametros, List datos, String pathIn, String pathOut) throws Exception {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        InputStream is = ec.getResourceAsStream(pathIn);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
        JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
        JasperExportManager.exportReportToPdfFile(jp, pathOut + id + ".pdf");
    }

    /**
     * Permite generar reportes optimizados y comprimidos en PDF
     *
     * @param reporte Archivo con el reporte.
     * @param datos Lista con datos para el reporte.
     * @param parametros Parametros para enviar al reporte.
     * @param nombreArchivo Nombre del archivo para descargar el reporte.
     * @return Un stream con el contenido del reporte en PDF.
     * @throws Exception
     */
    public static StreamedContent generarReportePdf(String reporte, List datos, HashMap parametros, String nombreArchivo) throws Exception {
        return generarReportePdf(reporte, datos, parametros, nombreArchivo, null, null, null, null);
    }

    /**
     * Permite generar reportes optimizados y comprimidos en PDF
     *
     * @param reporte Archivo con el reporte.
     * @param datos Lista con datos para el reporte.
     * @param parametros Parametros para enviar al reporte.
     * @param nombreArchivo Nombre del archivo para descargar el reporte.
     * @param autor Autor del reporte, sale en la metadata del pdf, puede ser
     * null.
     * @param titulo Titulo del reporte, sale en la metadata del pdf, puede ser
     * null.
     * @return Un stream con el contenido del reporte en PDF.
     * @throws Exception
     */
    public static StreamedContent generarReportePdf(String reporte, List datos, HashMap parametros, String nombreArchivo, String autor, String titulo) throws Exception {
        return generarReportePdf(reporte, datos, parametros, nombreArchivo, autor, titulo, null, null);
    }

    /**
     * Permite generar reportes optimizados y comprimidos en PDF
     *
     * @param reporte Archivo con el reporte.
     * @param datos Lista con datos para el reporte.
     * @param parametros Parametros para enviar al reporte.
     * @param nombreArchivo Nombre del archivo para descargar el reporte.
     * @param autor Autor del reporte, sale en la metadata del pdf, puede ser
     * null.
     * @param titulo Titulo del reporte, sale en la metadata del pdf, puede ser
     * null.
     * @param asunto Asunto del reporte, sale en la metadata del pdf, puede ser
     * null.
     * @param keywords Palabras clave del reporte, sale en la metadata del pdf,
     * puede ser null.
     * @return Un stream con el contenido del reporte en PDF.
     * @throws Exception
     */
    public static StreamedContent generarReportePdf(String reporte, List datos, HashMap parametros, String nombreArchivo, String autor, String titulo, String asunto, String keywords) throws Exception {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        InputStream is = ec.getResourceAsStream(reporte);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
        // Generar Reporte //
        JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
        //Agregando la segunda hoja
        JasperReport jreport2 = (JasperReport) JRLoader.loadObjectFromFile(ec.getRealPath("/aplicacion/turTurno/reportes/Formulario.jasper"));
        JasperPrint jprint2 = JasperFillManager.fillReport(jreport2, parametros, ds);

        List pages = jprint2.getPages();
        for (Object page : pages) {
            JRPrintPage object = (JRPrintPage) page;
            jp.addPage(object);
        }
        // Exportar Reporte //
        JRPdfExporter exp = new JRPdfExporter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Cargar configuración para PDF customizados //
        SimplePdfExporterConfiguration exc = new SimplePdfExporterConfiguration();
        exp.setExporterInput(new SimpleExporterInput(jp));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
        // Configuración //
        exc.setCompressed(Boolean.TRUE);
        exc.setMetadataAuthor(autor);
        exc.setMetadataCreator("SUCAMEC");
        exc.setMetadataTitle(titulo);
        exc.setMetadataKeywords(keywords);
        exc.setMetadataSubject(asunto);
        // Generar Reporte //
        exp.setConfiguration(exc);
        exp.exportReport();
        is.close();
        return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "application/pdf", nombreArchivo);
    }

    public static String mostrarHoraTurno(TurProgramacion prog) {
//        if (prog.getId() == 1 || prog.getId() == 2 || prog.getId() == 3) {
//            return prog.getHora().substring(0, 4);
//        } else {
            return prog.getHora().substring(0, 5);
//        }
    }
    
    public static String mostrarHoraTurnoCitas(TurProgramacion prog) {
//        if (prog.getId() == 19 || prog.getId() == 20 || prog.getId() == 21) {
//            return prog.getHora().substring(0, 4);
//        } else {
            return prog.getHora().substring(0, 5);
//        }
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
    
    public static String mostrarClienteString(SbPersona cli) {
        return cli.getRznSocial() == null ? cli.getNombreCompleto() : cli.getRznSocial();
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
    
    public static String mostrarDireccionOficio(SbPersona param) {
        String direccion = "";
        for (SbDireccion sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                direccion = (sbd.getViaId().getNombre().equals("OTRO") ? "" : sbd.getViaId().getNombre())
                        + " " + sbd.getDireccion()
                        + (sbd.getNumero() == null ? "" : (" " + sbd.getNumero()));
                direccion = direccion + ", " + sbd.getDistritoId().getNombre() + ", " + sbd.getDistritoId().getProvinciaId().getNombre() + ", " + sbd.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
                break;
            }
        }
        return direccion;
    }
    
    public static String mostrarTipoYNroDocumento(SbPersona emp) {
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
        
        return cadena;
    }
    
    public static String mostrarFechaDdMmYyyyHoraString(Date param) {
        if (param != null) {
            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
            SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));

            SimpleDateFormat formatHora = new SimpleDateFormat("hh:mm a");

            String dia = formatDia.format(param);
            String mes = formatMes.format(param);
            String anio = formatAnio.format(param);
            String hora = formatHora.format(param);

            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1, mes.length());

            return dia + " de " + mes + " del " + anio + " - " + hora;
        } else {
            return "BORRADOR";
        }
    }
    
}
