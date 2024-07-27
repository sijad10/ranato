/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap; 
import java.util.List; 
import java.util.Locale;
import javax.faces.context.ExternalContext;  
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;
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
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.bean.ExpedienteFacade;
import pe.gob.sucamec.bdintegrado.data.EppCom;
import pe.gob.sucamec.bdintegrado.data.EppLugarUso;
import pe.gob.sucamec.bdintegrado.data.EppLugarUsoUbigeo;
import pe.gob.sucamec.bdintegrado.data.Expediente;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;

/**
 *
 * @author mespinoza
 */
public class ReportUtilGuiaTransito {

    public static String mostrarDireccionPersona(SbPersonaGt param) {
        String direccion = "";
        for (SbDireccionGt sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                direccion = (sbd.getViaId().getNombre().equals("OTRO") ? "" : sbd.getViaId().getNombre()) + " " + sbd.getDireccion() + (sbd.getNumero() == null ? "" : (" " + sbd.getNumero()));
                direccion = direccion + ", Distrito de " + sbd.getDistritoId().getNombre() + ", Provincia de " + sbd.getDistritoId().getProvinciaId().getNombre() + ", Departamento de " + sbd.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
                break;
            }
        }
        return direccion;
    }

    public static String mostrarDetalleLugarUso(EppCom param) {
        String direccion = "";
        for (EppLugarUso sbd : param.getEppLugarUsoList()) {
            if (sbd.getActivo() == 1) {
                direccion = direccion + sbd.getTipoLugarId().getNombre() + " " + sbd.getNombre();
                direccion = direccion + ", ubicada en el ";
                for (EppLugarUsoUbigeo luu : sbd.getEppLugarUsoUbigeoList()) {
                    if (luu.getActivo() == 1) {
                        direccion = direccion + "Distrito de " + luu.getDistId().getNombre() + ", Provincia de " + luu.getDistId().getProvinciaId().getNombre() + ", Departamento de " + luu.getDistId().getProvinciaId().getDepartamentoId().getNombre() + ", ";
                    }
                }
            }
        }
        return direccion;
    }

    public static String mostrarUbigeos(List<EppLugarUsoUbigeo> lst) {
        String respuesta = "";
        for (EppLugarUsoUbigeo l : lst) {
            if (l.getActivo() == 1) {
                respuesta = respuesta + l.getDistId().getProvinciaId().getDepartamentoId().getNombre() + " / "
                        + l.getDistId().getProvinciaId().getNombre() + " / "
                        + l.getDistId().getNombre() + "<br/>";
            }
        }
        return respuesta;
    }

    public static String mostrarFechaExpediente(ExpedienteFacade expedienteFacade, String param) {
        String fecha;
        Expediente e = expedienteFacade.obtenerExpedienteXNumeroList(param).get(0);
        SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("ES"));
        fecha = formateador.format(e.getFechaCreacion());
        return fecha;
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

    /**
     * subir un pdf al repositorio
     *
     * @param id
     * @param parametros
     * @param datos
     * @param pathIn
     * @param pathOut
     */
    public static void subirPdf(String id, HashMap parametros, List datos, String pathIn, String pathOut) {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            InputStream is = ec.getResourceAsStream(pathIn);
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
            JasperExportManager.exportReportToPdfFile(jp, pathOut + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permite generar reportes optimizados y comprimidos en PDF
     *
     * @param reporte Archivo con el reporte.
     * @param datos Lista con datos para el reporte.
     * @param parametros Parametros para enviar al reporte.
     * @param nombreArchivo Nombre del archivo para descargar el reporte.
     * @return Un stream con el contenido del reporte en PDF.
     */
    public static StreamedContent generarReportePdf(String reporte, List datos, HashMap parametros, String nombreArchivo) {
        try {
            return generarReportePdf(reporte, datos, parametros, nombreArchivo, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
     */
    public static StreamedContent generarReportePdf(String reporte, List datos, HashMap parametros, String nombreArchivo, String autor, String titulo) {
        try {
            return generarReportePdf(reporte, datos, parametros, nombreArchivo, autor, titulo, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
     */
    public static StreamedContent generarReportePdf(String reporte, List datos, HashMap parametros, String nombreArchivo, String autor, String titulo, String asunto, String keywords) {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            InputStream is = ec.getResourceAsStream(reporte);
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            // Generar Reporte //
            JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
            //Agregando la segunda hoja
            JasperReport jreport2 = (JasperReport) JRLoader.loadObjectFromFile(ec.getRealPath("/aplicacion/registroGuiaTransito/reportes/guiaTransitoBack.jasper"));
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
        } catch (JRException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
