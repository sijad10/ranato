package pe.gob.sucamec.bdintegrado.jsf.util;

import com.itextpdf.text.pdf.PdfReader;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.commons.codec.binary.Hex;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.export.Graphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.primefaces.context.RequestContext;
import pe.gob.sucamec.bdintegrado.data.EppCarne;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.EppRegistroGuiaTransito;
import pe.gob.sucamec.bdintegrado.data.SbDireccionGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SspAlumnoCurso;
import pe.gob.sucamec.bdintegrado.data.SspModulo;
import pe.gob.sucamec.bdintegrado.data.SspProgramacion;
import pe.gob.sucamec.bdintegrado.data.SspRegistroCurso;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;
import javax.imageio.*;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;
import pe.gob.sucamec.bdintegrado.data.EppLicencia;
import pe.gob.sucamec.bdintegrado.data.SspCarne;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 * Clase con funciones utiles para usar JSF
 */
public class JsfUtil {

    private static long tempIdValN = -1;
    public final static short TRUE = 1;
    public final static short FALSE = 0;
    public final static short TIENETRAZA = 2;

    /**
     * Valor para generar Id temporales.
     */
    
    /**
     * Obtiene un archivo del repositorio para poderlo enviar a através de la
     * web.
     *
     * @param rutaBundle Nombre en el bundle de la Ruta.
     * @param nombre Nombre del archivo en el servidor, usualmente id mas la
     * extensión.
     * @param descarga Nombre que va a tener el archivo al momento de realizar
     * la descarga.
     * @param mime Tipo mime para la descarga, los mas usados son:
     * application/pdf, text/plain, text/html,
     * @return
     * @throws Exception
     */
    public static StreamedContent obtenerArchivo(String rutaBundle, String nombre, String descarga, String mime) throws Exception {
        FileInputStream f = new FileInputStream(bundleGt(rutaBundle) + File.separator + nombre);
        return new DefaultStreamedContent(f, mime, descarga);
    }
    
    public static StreamedContent obtenerArchivoPath(String path, String nombre, String descarga, String mime) throws Exception {
        FileInputStream f = new FileInputStream(path + File.separator + nombre);
        return new DefaultStreamedContent(f, mime, descarga);
    }
    
    public static StreamedContent obtenerArchivoCME(String rutaBundle, String nombre, String descarga, String mime) throws Exception {
        FileInputStream f = new FileInputStream(rutaBundle + File.separator + nombre);
        return new DefaultStreamedContent(f, mime, descarga);
    }

    public static StreamedContent obtenerArchivoBDIntegrado(String rutaBundle, String nombre, String descarga, String mime) throws Exception {
        FileInputStream f = new FileInputStream(rutaBundle + File.separator + nombre);
        return new DefaultStreamedContent(f, mime, descarga);
    }

    /**
     * Guarda los archivos que se cargan en el sistema, usando determinada ruta.
     *
     * @param rutaBundle Nombre en el bundle de la Ruta.
     * @param nombre Nombre del archivo.
     * @param arch Archivo a guardar en el sistema.
     * @throws Exception
     */
    public static void guardarArchivo(String rutaBundle, String nombre, UploadedFile arch) throws Exception {
        FileUtils.writeByteArrayToFile(
                new File(bundle(rutaBundle) + File.separator + nombre),
                arch.getContents());
    }

    /**
     * Verifica si un archivo es efectivamente un JPG a través de los magic
     * numbers.
     *
     * @param uf Archivo cargado.
     * @return true si el archivo es correcto
     */
    public static boolean verificarJPG(UploadedFile uf) {
        try {
            InputStream i = uf.getInputstream();
            byte[] id = new byte[4];
            if (i.read(id) == 4) {
                if ((id[0] == (byte) 0xFF)
                        && (id[1] == (byte) 0xD8)
                        && (id[2] == (byte) 0xFF)
                        && (id[3] == (byte) 0xE0)) {
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica si un archivo es efectivamente un PDF a través de los magic
     * numbers.
     *
     * @param uf El archivo cargado.
     * @return true si el archivo es correcto
     */
    public static boolean verificarPDF(UploadedFile uf) {
        try {
            InputStream i = uf.getInputstream();
            byte[] id = new byte[4];
            if (i.read(id) == 4) {
                if (new String(id).equals("%PDF")) {
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Permite contar la cantidad de páginas que contiene un archivo PDF
     *
     * @param uf El archivo cargado.
     * @return La cantidad de páginas que contiene el archivo.
     * @throws Exception
     */
    public static int contarPaginasPDF(UploadedFile uf) throws Exception {
        InputStream i = uf.getInputstream();
        PdfReader reader = new PdfReader(i);
        int p = reader.getNumberOfPages();
        reader.close();
        return p;
    }

    /**
     * Muestra errores cuando se trata de descargas y no se puede utilizar un
     * Mensaje JSF en el Growl.
     *
     * @param mensaje
     * @param ex
     * @return
     */
    public static StreamedContent errorDescarga(String mensaje, Exception ex) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        String s = "<html><body style='font-family: Arial;'><center>"
                + "<br><br><br>"
                + "<img src='" + ec.getRequestContextPath() + "/resources/imagenes/logo_sucamec_of.png" + "'>"
                + "<br><br>"
                + "<div style='width: 80%; border: 1px solid #A8A8A8;'><h2>Ha ocurrido un error:</h2><br><br><br>"
                + mensaje
                + "<br><br><br>"
                + ex.getMessage()
                + "<br><br><br>"
                + "</center></body></html>";
        return new DefaultStreamedContent(new ByteArrayInputStream(s.getBytes()), "text/html");
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

    /**
     * Permite convertir los campos de una lista a mayusculas.
     *
     * @param l la lista de entidades
     * @param exclusiones lista de campos a excluir, separados por coma.
     * @return
     */
    public static List listaMayusculas(List l, String exclusiones) {
        for (int i = 0; i < l.size(); i++) {
            l.set(i, JsfUtil.entidadMayusculas(l.get(i), exclusiones));
        }
        return l;
    }

    /**
     * Permite convertir los campos de una entidad a mayusculas.
     *
     * @param o la entidad
     * @param exclusiones lista de campos a excluir, separados por coma.
     * @return
     */
    public static Object entidadMayusculas(Object o, String exclusiones) {
        try {
            List le = Arrays.asList(exclusiones.split(","));
            Field fa[] = o.getClass().getDeclaredFields();
            for (Field f : fa) {
                if (f.getType().getName().equals("java.lang.String") && !le.contains(f.getName())) {
                    f.setAccessible(true);
                    String v = (String) f.get(o);
                    if (v != null) {
                        v = v.toUpperCase();
                        f.set(o, v);
                    }
                    f.setAccessible(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return o;
    }

    /**
     * Permite crear un hash MD5, para ser usado en contraseñas.
     *
     * @param s El texto a convertir en hash.
     * @return Devuelve el hash en MD5
     */
    public static String crearHash(String s) {
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bc = s.getBytes("UTF-8");
            hash = new String(Hex.encodeHex(md.digest(bc)));
        } catch (Exception ex) {
            System.err.println("Error:Utilidades.crearHash:" + ex.getMessage());
        }
        return hash;
    }

    public static enum TipoDoc {

        RUC, DNI
    }

    /**
     * Funcion para validar un DNI o un RUC
     *
     * @param doc El número de documento a validar.
     * @param tipo El tipo de documento a validar "DNI"/"RUC".
     * @return true si el documento es valido, false sino.
     */
    public static boolean ValidarDniRuc(String doc, TipoDoc tipo) {
        if (tipo == TipoDoc.DNI && doc.length() != 9) {
            return false;
        } else if (tipo == TipoDoc.RUC && doc.length() != 11) {
            return false;
        }
        if ((doc != null) && (doc.length() > 8)) {
            int suma = 0;
            int[] hash = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
            int docLength = doc.length();
            String id = doc.substring(0, docLength - 1);
            int idl = id.length();
            int diff = hash.length - idl;
            for (int i = idl - 1; i >= 0; i--) {
                suma += (id.charAt(i) - '0') * hash[i + diff];
            }
            suma = 11 - (suma % 11);

            char last = Character.toUpperCase(doc.charAt(docLength - 1));
            if (docLength == 11) {
                // RUC.
                if (suma >= 10) {
                    suma -= 10;
                }
                return (suma == (last - '0'));
            } else {
                if (suma == 11) {
                    suma = 0;
                }
                if (Character.isDigit(last)) {
                    // DNI con numero
                    char[] hashNumbers = {'6', '7', '8', '9', '0', '1', '1', '2', '3', '4', '5'};
                    return (last == hashNumbers[suma]);
                } else if (Character.isLetter(last)) {
                    // DNI con letra
                    char[] hashLetters = {'K', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
                    return (last == hashLetters[suma]);
                }
            }
        }
        return false;
    }

    /**
     * Permite obtener un objeto que se encuentra en la sesion similar a #{}
     * usado en jsf. Mayormente usado para obtener controllers.
     *
     * @param s Nombre del objeto en la sesion.
     * @param c Clase del objeto en la sesion.
     * @return La instancia del objeto de la sesion.
     */
    public static Object obtenerBean(String s, Class c) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().evaluateExpressionGet(facesContext, "#{" + s + "}", c);
    }

    /**
     * Lista de errores personalizados para el ORACLE Incluir la restriccion, o
     * texto del error y el mensaje que se desea mostrar
     */
    private final static String[][] listaErrores = {
        {"SISTEMABASE.PERSONA__UN", "Error RUC duplicado"}
    };

    /**
     * Permite mostrar mensajes personalizados en base al ERROR, principalmente
     * para errores del ORACLE
     *
     * @param ex Excepcion con el error
     * @return El mensaje de error
     */
    public static String obtenerMensajeError(Exception ex) {
        if (ex.getCause() != null) {
            if (ex.getCause().getCause() != null) {
                String error = ex.getCause().getCause().toString();
                for (String[] l : listaErrores) {
                    if (error.contains(l[0])) {
                        return l[1];
                    }
                }
            }
        }
        return ex.getLocalizedMessage();
    }

    /**
     * Permite mostrar un mensaje de error en Growl o <h: messages>
     *
     * @param ex La excepción con el error.
     * @param s Mensaje por defecto en caso de no haber ninguno.
     */
    public static void mensajeError(Exception ex, String s) {
        String msg = obtenerMensajeError(ex);
        if (msg != null && msg.length() > 0) {
            mensajeError(msg);
        } else {
            mensajeError(s);
        }
    }

    /**
     * Permite agregar varios mensajes de error en forma simultanea en
     * <h: messages> o un growl
     *
     * @param messages Lista con los mensajes de error.
     */
    public static void mensajeError(List<String> messages) {
        for (String message : messages) {
            mensajeError(message);
        }
    }

    /**
     * Permite mostrar un mensaje de error en <h: messages> o un growl
     *
     * @param msg El mensaje de error a mostrar.
     */
    public static void mensajeError(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    /**
     * Permite mostrar un mensaje informativo de una accion ejecutada con éxito
     * en <h: messages> o un growl
     *
     * @param msg El mensaje exitoso a mostrar.
     */
    public static void mensaje(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Permite mostrar un mensaje de advertencia de una accion ejecutada con
     * éxito en <h: messages> o un growl
     *
     * @param msg El mensaje exitoso a mostrar.
     */
    public static void mensajeAdvertencia(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Permite obtener texto del archivo Bundle
     *
     * @param s Nombre del text
     * @return El texto asociado al nombre
     */
    public static String bundle(String s) {
        return ResourceBundle.getBundle("/Bundle").getString(s);
    }

    public static String bundleGt(String s) {
        return ResourceBundle.getBundle("/BundleGeppGte").getString(s);
    }

    public static String bundleBDIntegrado(String s) {
        return ResourceBundle.getBundle("/BundleBDIntegrado").getString(s);
    }
    
    public static String bundleGeppLME(String s) {
        return ResourceBundle.getBundle("/BundleGeppLme").getString(s);
    }
    
    public static String bundleGeppLMPP(String s) {
        return ResourceBundle.getBundle("/BundleGeppLmpp").getString(s);
    }

    public static String bundleGamac(String s) {
        return ResourceBundle.getBundle("/BundleGamac").getString(s);
    }
    
    public static String bundleBase(String s) {
        return ResourceBundle.getBundle("/BundleSistemaBase").getString(s);
    }
    
    public static String bundleNotificacion(String s) {
        return ResourceBundle.getBundle("/BundleNotificacion").getString(s);
    }
    
    public static String bundleMicroservicios(String s) {
        return ResourceBundle.getBundle("/BundleMicroservicios").getString(s);
    }

    /**
     * Setea un componente como inválido, para marcarlo en caso de error.
     *
     * @param c Id del componente ej. form:login
     * @param v valor, true o false
     */
    public static void invalidar(String c) {
        UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent(c);
        if ((comp != null) && (comp instanceof UIInput)) {
            ((UIInput) comp).setValid(false);
        }
    }

    /**
     * Permite obtener un parametro del HTTP Request.
     *
     * @param key Nombre del parámetro.
     * @return La información contenida en el parámetro.
     */
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    /**
     * Permite obtener un parámetro o propiedad de los componentes de la página
     * web.
     *
     * @param requestParameterName Nombre del parámetro.
     * @param converter Convertidor.
     * @param component Componente del cual se desea obtener el parámetro.
     * @return El objeto que hace referencia al componente de la página web.
     */
    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String id = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, id);
    }

    /**
     * Esta función convierte las imágenes tiff Tag 0006 almacenadas en el
     * Oracle a un JPG, requiere Imagemagik. Pocas librerias soportan esta
     * versión de tiff, para mas información revisar:
     * https://en.wikipedia.org/wiki/Tagged_Image_File_Format
     *
     * @param b Array con los datos de la imagen
     * @param w El ancho de la imagen a obtener
     * @param h El alto de la imagen a obtener.
     * @return un Array con la imagen en Jpg o nulo si huno un problema.
     */
    public static byte[] oracleTiffEnJpg(byte[] b, int w, int h) {
        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(b);
            Pipe pipeOut = new Pipe(null, os);
            Pipe pipeIn = new Pipe(is, null);
            ConvertCmd cmd = new ConvertCmd();
            cmd.setSearchPath("C:\\Program Files\\ImageMagick-6.8.8-Q16");
            cmd.setOutputConsumer(pipeOut);
            cmd.setInputProvider(pipeIn);
            IMOperation op = new IMOperation();
            op.addImage("-");
            op.resize(w, h);
            op.addImage("jpg:-");
            cmd.run(op);
            byte[] r = os.toByteArray();
            os.close();
            is.close();
            return r;
        } catch (IOException | InterruptedException | IM4JavaException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Valor para generar Id temporales.
     */
    private static long tempIdVal = -1;

    /**
     * Devuelte un valor temporal para listas que requieran datasets en memoria.
     *
     * @return Un Id temporal.
     */
    public static Long tempId() {
        tempIdVal--;
        if (tempIdVal < Long.MIN_VALUE + 100) {
            tempIdVal = -1;
        }
        return tempIdVal;
    }

    /**
     * Borra los IDs temporales en para listas que los requieran en Datasets. La
     * idea es generar ids temporales, para poder trabajar la información de las
     * listas y de ahi, borrarlos para que el ORM coloque aquellos a ser
     * reemplazados por la secuqncia de la base de datos.
     *
     * @param datos Lista con datos a borrar IDs.
     * @throws Exception en caso ocurra un error o no existan los campos.
     */
    public static void borrarIds(List datos) throws Exception {
        // Usar Iterator que es por referencia, For each no sirve, copia el valor.
        for (Iterator it = datos.iterator(); it.hasNext();) {
            Object o = it.next();
            Method m = o.getClass().getDeclaredMethod("setId", Long.class);
            Method mg = o.getClass().getDeclaredMethod("getId");
            Long id = (Long) mg.invoke(o);
            if (id < 0) {
                m.invoke(o, (Object) null);
            }
        }
    }

    /**
     * Convierte un list datamodel a una Lista, util para trabajar con
     * DataModels y Listas en memoria.
     *
     * @param dm El ListDataModel a ser convertido.
     * @return La lista de datos para ser guardada utilizada con la entidad.
     */
    public static ArrayList listDataModelALista(ListDataModel dm) {
        ArrayList l = new ArrayList();
        for (Iterator it = dm.iterator(); it.hasNext();) {
            l.add(it.next());
        }
        return l;
    }

    public static String parametroHtml(String keyParam) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> p = fc.getExternalContext().getRequestParameterMap();
        return "" + p.get(keyParam);
    }

    public static String getIpAddress() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = "";
        ip = request.getRemoteAddr();
        return ip;
    }

    public static DatosUsuario getLoggedUser() {
        try {
            DatosUsuario p_user = (DatosUsuario) JsfUtil.getSessionAttribute("seguridad_usuario");
            return p_user;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getSessionAttribute(String attribute) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        Object o = null;
        if (session != null) {
            o = session.getAttribute(attribute);
        }
        return o;
    }

    public static void setSessionAttribute(String attribute, Object value) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.setAttribute(attribute, value);
    }

    public static void removeSessionAttribute(String attribute) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.removeAttribute(attribute);
    }

    public static String obtenerFechaXCriterio(Date fechaEvaluada, int campoRequerido) {
        try {
            //FechaCalendar actual desglosada:
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(fechaEvaluada);
            int anho = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH) + 1;
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            int hora = fecha.get(Calendar.HOUR_OF_DAY);
            int minuto = fecha.get(Calendar.MINUTE);
            int segundo = fecha.get(Calendar.SECOND);

            switch (campoRequerido) {
                case 1:
                    return "" + anho;
                case 2:
                    return "" + mes;
                case 3:
                    return "" + dia;
                case 4:
                    return "" + hora;
                case 5:
                    return "" + minuto;
                case 6:
                    return "" + segundo;
                default:
                    return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date getFechaSinHora(Date fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date fechaFormateada = dateFormat.parse(dateFormat.format(fecha));
            return fechaFormateada;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String ubicacionLocalPlanta(EppRegistro ep) {
        String distrito = ep.getEppLocalList().get(0).getUbigeoId().getNombre();
        String provincia = ep.getEppLocalList().get(0).getUbigeoId().getProvinciaId().getNombre();
        String departamento = ep.getEppLocalList().get(0).getUbigeoId().getProvinciaId().getDepartamentoId().getNombre();
        String direccion = ep.getEppLocalList().get(0).getDireccion();
        String dir = direccion + ", distrito de " + distrito + ", provincia de " + provincia + ", departamento de " + departamento;

        return dir;

    }

    /**
     * buscar un pdf en repositorio
     *
     * @param idRegistro
     * @param ruta
     * @return
     * @throws java.io.FileNotFoundException
     */
    public static boolean buscarPdfRepositorio(String idRegistro, String ruta) throws FileNotFoundException {
        boolean flat = false;
        try {
            String file = (idRegistro + ".pdf");
            String path = ruta;
            InputStream in = new FileInputStream(path + file);
            if (in != null) {
                flat = true;
            } else {
                flat = false;
            }
        } catch (Exception e) {
            flat = false;
        }
        return flat;

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
    public static void subirPdfGte(String id, HashMap parametros, List datos, String pathIn, String pathOut) throws Exception {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();

            InputStream is = ec.getResourceAsStream(pathIn);
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
            String rutaback = bundleGt("rutaPdfBackGte");
            InputStream isback = ec.getResourceAsStream(rutaback);
            JRBeanCollectionDataSource dsback = new JRBeanCollectionDataSource(datos);
            JasperPrint jpback = JasperFillManager.fillReport(isback, parametros, dsback);

            List pages = jpback.getPages();
            for (Object page : pages) {
                JRPrintPage object = (JRPrintPage) page;
                jp.addPage(object);
            }

            JasperExportManager.exportReportToPdfFile(jp, pathOut + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
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
    public static void subirPdfSel(String id, HashMap parametros, List datos, String pathIn, String pathOut) throws Exception {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            InputStream is = ec.getResourceAsStream(pathIn);
            //REEMPLAZAR POR ESTE PARA QUE LOS REPORTES SE EJECUTEN DESDE RUTA EN EL SERVIDOR
            //InputStream is = new FileInputStream(pathIn)
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
            JasperExportManager.exportReportToPdfFile(jp, pathOut + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Funcion para escalar y rotar una imagen usando las librerías de Java De
     * preferencia usar un mismo ratio w/h de la imagen original para mejores
     * resultados
     *
     * @param i imagen en cualquier formato, dentro de un bytearray.
     * @param w nuevo ancho de la imagen
     * @param h nuevo alto de la imagen
     * @param angulo angulo en grados, que se desea rotar la imagen
     * @return bytearray con un JPG de la imagen
     * @throws Exception con algun problema con la conversión de la imagen.
     */
    public static byte[] escalarRotarImagen(byte[] i, int w, int h, int angulo) throws Exception {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(i)),
                canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int iw = img.getWidth(), ih = img.getHeight();
        double escala = (float) Math.max(-w, h) / (float) Math.max(-iw, ih);
        AffineTransform at = new AffineTransform();
        Graphics2D g = canvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // Calidad de Imagen
        at.translate(w / 2, h / 2); // Posicionar en el centro del canvas;
        at.scale(escala, escala); // Escalar imagen
        at.rotate(Math.toRadians(angulo)); // Rotar imagen
        at.translate(-iw / 2, -ih / 2); // Ajustar al centro de la imagen ;
        //g.drawRenderedImage(img, at);
        g.drawImage(img, 0, 0, w, h, 0, 0, iw, ih, null);
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        ImageIO.write(canvas, "jpg", o);
        g.dispose();
        return o.toByteArray();
    }

    /**
     * Exportar valores de tabla en excel
     *
     * @param table
     * @param camposExcel
     * @return
     */
    public static byte[] List2Excel(List<EppRegistroGuiaTransito> table, List<String> camposExcel, int tipoRpt, boolean tipoTbl) {
        Row row = null;
        Cell cell = null;
        try {
            Workbook wb = new HSSFWorkbook();
            HSSFCellStyle styleHeader = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont fontHeader = (HSSFFont) wb.createFont();
            fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            styleHeader.setFont(fontHeader);
            Sheet sheet = wb.createSheet("sheet");
            row = sheet.createRow((short) 0);

            int i = 0;
            for (String campo : camposExcel) {
                cell = row.createCell(i++);
                cell.setCellValue(campo.toUpperCase());
                cell.setCellStyle(styleHeader);
            }

            int j = 1;

            for (EppRegistroGuiaTransito gt : table) {
                row = sheet.createRow((short) j++);
                i = 0;
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getRegistroId().getId());
                cell = row.createCell(i++);
                cell.setCellValue("" + gt.getRegistroId().getId());
            }

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            wb.write(file);
            return file.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * QUITAR TILDES A CADENA
     *
     * @author Richar Fernández
     * @version 1.0
     * @param texto con tildes
     * @return texto sin tildes
     */
    public static String quitarTildes(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    /**
     * Muestra mensaje HTML
     *
     * @param mensaje
     * @return
     */
    public static StreamedContent mensajeHtml(String mensaje) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        String s = "<html><body style='font-family: Arial;'><center>"
                + "<br><br><br>"
                + "<img src='" + ec.getRequestContextPath() + "/resources/imagenes/logo_sucamec_of.png" + "'>"
                + "<br><br>"
                + "<div style='width: 80%; border: 1px solid #A8A8A8;'><h2>Mensaje del Sistema:</h2><br><br><br>"
                + mensaje
                + "<br><br><br>"
                + "</center></body></html>";
        return new DefaultStreamedContent(new ByteArrayInputStream(s.getBytes()), "text/html");
    }

    public static Date getFechaSinHoraddmmyy(Date fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaFormateada = dateFormat.parse(dateFormat.format(fecha));
            return fechaFormateada;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
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

    /**
     * METODO PARA MOSTRAR MENSAJES
     *
     * @param texto
     * @param tipo
     */
    public static void mostrarMensajeDialog(String texto, int tipo) {
        FacesMessage message = new FacesMessage();
        switch (tipo) {
            case 1:
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", texto);
                break;
            case 2:
                message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", texto);
                break;
            case 3:
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informacion", texto);
                break;
            case 4:
                message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", texto);
                break;
            default:
                break;
        }
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }

    /*  
     * Calcula la diferencia entre dos fechas. Devuelve el resultado en días, meses o años según sea el valor del parámetro 'tipo'
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @param tipo 0=TotalAños; 1=TotalMeses; 2=TotalDías; 3=MesesDelAnio; 4=DiasDelMes
     * @return numero de días, meses o años de diferencia
     */
    public static long getDiffDates(Date fechaInicio, Date fechaFin, int tipo) {
        // Fecha inicio
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTime(fechaInicio);
        int diaInicio = calendarInicio.get(Calendar.DAY_OF_MONTH);
        int mesInicio = calendarInicio.get(Calendar.MONTH);// + 1; // 0 Enero, 11 Diciembre
        int anioInicio = calendarInicio.get(Calendar.YEAR);

        // Fecha fin
        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTime(fechaFin);
        int diaFin = calendarFin.get(Calendar.DAY_OF_MONTH);
        int mesFin = calendarFin.get(Calendar.MONTH);//+ 1; // 0 Enero, 11 Diciembre
        int anioFin = calendarFin.get(Calendar.YEAR);

        int anios = 0;
        int mesesPorAnio = 0;
        int diasPorMes = 0;
        int diasTipoMes = 0;

        //
        // Calculo de días del mes
        //
        if (mesInicio == 2) {
            // Febrero
            if ((anioFin % 4 == 0) && ((anioFin % 100 != 0) || (anioFin % 400 == 0))) {
                // Bisiesto
                diasTipoMes = 29;
            } else {
                // No bisiesto
                diasTipoMes = 28;
            }
        } else if (mesInicio <= 7) {
            // De Enero a Julio los meses pares tienen 30 y los impares 31
            if (mesInicio % 2 == 0) {
                diasTipoMes = 30;
            } else {
                diasTipoMes = 31;
            }
        } else if (mesInicio > 7) {
            // De Julio a Diciembre los meses pares tienen 31 y los impares 30
            if (mesInicio % 2 == 0) {
                diasTipoMes = 31;
            } else {
                diasTipoMes = 30;
            }
        }

        //
        // Calculo de diferencia de año, mes y dia
        //
        if ((anioInicio > anioFin) || (anioInicio == anioFin && mesInicio > mesFin)
                || (anioInicio == anioFin && mesInicio == mesFin && diaInicio > diaFin)) {
            // La fecha de inicio es posterior a la fecha fin
            return -1;
        } else if (mesInicio <= mesFin) {
            anios = anioFin - anioInicio;
            if (diaInicio <= diaFin) {
                mesesPorAnio = mesFin - mesInicio;
                diasPorMes = diaFin - diaInicio;
            } else {
                if (mesFin == mesInicio) {
                    anios = anios - 1;
                }
                mesesPorAnio = (mesFin - mesInicio - 1 + 12) % 12;
                diasPorMes = diasTipoMes - (diaInicio - diaFin);
            }
        } else {
            anios = anioFin - anioInicio - 1;
            if (diaInicio > diaFin) {
                mesesPorAnio = mesFin - mesInicio - 1 + 12;
                diasPorMes = diasTipoMes - (diaInicio - diaFin);
            } else {
                mesesPorAnio = mesFin - mesInicio + 12;
                diasPorMes = diaFin - diaInicio;
            }
        }
        // resultado = anios + " Años, " + mesesPorAnio + " Meses y " + diasPorMes + " Días.";
        //
        // Totales
        //
        long returnValue = -1;

        switch (tipo) {
            case 0:
                // Total Años
                returnValue = anios;
                break;

            case 1:
                // Total Meses
                returnValue = anios * 12 + mesesPorAnio;
                break;

            case 2:
                // Total Dias (se calcula a partir de los milisegundos por día)
                long millsecsPerDay = 86400000; // Milisegundos al día
                returnValue = (fechaFin.getTime() - fechaInicio.getTime()) / millsecsPerDay;
                break;

            case 3:
                // Meses del año
                returnValue = mesesPorAnio;
                break;

            case 4:
                // Dias del mes
                returnValue = diasPorMes;
                break;

            default:
                break;
        }

        return returnValue;

    }

    public static Integer diasSegunMes(int mes, int anio) {
        int diasTipomes = 0;
        if (mes == 2) {
            // Febrero
            if ((anio % 4 == 0) && ((anio % 100 != 0) || (anio % 400 == 0))) {
                // Bisiesto
                diasTipomes = 29;
            } else {
                // No bisiesto
                diasTipomes = 28;
            }
        } else if (mes <= 7) {
            // De Enero a Julio los meses pares tienen 30 y los impares 31
            if (mes % 2 == 0) {
                diasTipomes = 30;
            } else {
                diasTipomes = 31;
            }
        } else if (mes > 7) {
            // De Julio a Diciembre los meses pares tienen 31 y los impares 30
            if (mes % 2 == 0) {
                diasTipomes = 31;
            } else {
                diasTipomes = 30;
            }
        }

        return diasTipomes;
    }
    
    /**
     * OBTENER PEERFIL DE USUARIO
     *
     * @author Richar Fernández
     * @version 1.0
     * @param perfil Perfil de usuario a buscar
     * @return Flag de Búsqueda si encontró Perfil de usuario
     */
    public static boolean buscarPerfilUsuario(String perfil) {
        if (!JsfUtil.getLoggedUser().getPerfiles().isEmpty()) {
            for (String itemPerfil : JsfUtil.getLoggedUser().getPerfiles()) {
                if (itemPerfil.equals(perfil)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * CALCULAR NRO. DE DIAS ENTRE 2 FECHAS
     * @author Richar Fernández
     * @version 2.0
     * @param fechaIni Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Nro. de dias entre 2 fechas
     */
    public static int calculaDiasEntreFechas(Date fechaIni, Date fechaFin){
               
        long diferenciaEn_ms = fechaFin.getTime() - fechaIni.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);

        return (int) dias;
    }
    
    public static Date stringToDate(String fecha, String formato) {
        try {
            DateFormat format = new SimpleDateFormat(formato);
            return format.parse(fecha);
        } catch (Exception e) {
        }
        return null;
    }
    
    /**
     * CALCULAR NRO. DE MINUTOS ENTRE 2 HORAS
     * @author Richar Fernández
     * @version 2.0
     * @param horaIni Hora de inicio
     * @param horaFin Hora de fin
     * @return Nro. de minutos entre 2 horas
     */
    public static long calculaMinutosEntreHoras(Date horaIni, Date horaFin){
               
        long diferenciaEn_ms = horaFin.getTime() - horaIni.getTime();
        long minutos = diferenciaEn_ms / (1000 * 60);

        return (long) minutos;
    }
    
    public static String dateToString(Date fecha, String formato) {
        try {
            DateFormat format = new SimpleDateFormat(formato);
            return format.format(fecha);
        } catch (Exception e) {
        }
        return null;
    }
    
    public static Date getFechaSoloHora(Date fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date fechaFormateada = dateFormat.parse(dateFormat.format(fecha));
            return fechaFormateada;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static boolean validateExpresionRegular(String email, String expReg) {
        Pattern pattern = Pattern.compile(expReg);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
     public static Integer formatoFechaYyyy(Date param) {
        if (param != null) {
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            String anio = formatAnio.format(param);

            return Integer.parseInt(anio);
        }
        return null;
    }
     
     public static boolean validateImageDimension(InputStream is, int widthLimit, int heightLimit) throws IOException {
        boolean validacion = true;
        try {
            BufferedImage buf = ImageIO.read(is);
            int height = buf.getHeight();
            int width = buf.getWidth();
            
            if(height != heightLimit){
                validacion = false;
            }
            if(width != widthLimit){
                validacion = false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            validacion = false;
        }
        return validacion;
    }
     
    public static String nullATodo(String s) {
        return nullATodo(s, false);
    }

    public static String nullATodo(String s, boolean parcial) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        if (parcial) {
            return "%" + s.replace("%", "") + "%";
        }
        return s.replace("%", "");
    }

    public static String nullATodoComodin(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s + "%";
    }
    
    public static String mostrarAnio(Date param) {
        if (param != null) {            
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
            return formatAnio.format(param);
        } else {
            return "BORRADOR";
        }
    }
    
    /**
     * Permite generar reportes optimizados y comprimidos en PDF
     *
     * @param reporte Archivo con el reporte.
     * @param datos Lista con datos para el reporte.
     * @param nombreArchivo Nombre del archivo para descargar el reporte.
     * @param instructor Id persona del instructor
     * @return Un stream con el contenido del reporte en PDF.
     * @throws Exception
     */
    public static StreamedContent generarPdfCursoAsistencia(String reporte, List datos, String nombreArchivo, SbPersonaGt instructor) throws Exception {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        JasperPrint jpTotal = new JasperPrint(), jp = null;
        int cont = 0;
        SspRegistroCurso curso = (SspRegistroCurso) datos.get(0);
        List<SspModulo> modulos = new ArrayList();
        
        for(SspProgramacion progra : curso.getSspProgramacionList()){
            if(progra.getActivo() == 1 && progra.getInstructorId() != null && Objects.equals(progra.getInstructorId().getPersonaId().getId(), instructor.getId()) ){
                // UN PDF POR CADA CURSO
                cont++;
                if(!modulos.contains(progra.getModuloId())){
                    modulos.add(progra.getModuloId());
                }else{
                    continue;
                }
                
                HashMap parametros = parametrosPdfListadoAsistencia( curso, progra );
                if(parametros == null){
                    return null;                    
                }
                if (cont == 1) {
                    InputStream is = ec.getResourceAsStream(reporte);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
                    jpTotal = JasperFillManager.fillReport(is, parametros, ds);
                }else{
                    InputStream is = ec.getResourceAsStream(reporte);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
                    jp = JasperFillManager.fillReport(is, parametros, ds);
                    
                    List pages = jp.getPages();
                    for (Object page : pages) {
                        JRPrintPage object = (JRPrintPage) page;
                        jpTotal.addPage(object);
                    }
                }
            }
        }

        // Exportar Reporte //
        JRPdfExporter exp = new JRPdfExporter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Cargar configuración para PDF customizados //
        SimplePdfExporterConfiguration exc = new SimplePdfExporterConfiguration();
        exp.setExporterInput(new SimpleExporterInput(jpTotal));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
        // Configuración //
        exc.setCompressed(Boolean.TRUE);
        exc.setMetadataAuthor(null);
        exc.setMetadataCreator("SUCAMEC");
        exc.setMetadataTitle(null);
        exc.setMetadataKeywords(null);
        exc.setMetadataSubject(null);
        // Generar Reporte //
        exp.setConfiguration(exc);
        exp.exportReport();
        return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "application/pdf", nombreArchivo);
    }
    
     /**
     * PARAMETROS PARA GENERAR PDF
     * @author Richar Fernández
     * @version 1.0
     * @param ppdf Registro de cursos
     * @return Listado de parámetros para constancia
     */
    private static HashMap parametrosPdfListadoAsistencia(SspRegistroCurso ppdf, SspProgramacion progra){
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        
        ph.put("P_LOGOSUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec_h.png"));
        ph.put("P_FECHASTRING", ReportUtil.mostrarFechaString(ppdf.getFechaRegistro()) );
        ph.put("P_TIPOFORMACION", progra.getModuloId().getTipoCursoId().getCodProg() );
        ph.put("P_CURSO", progra.getModuloId().getNombre() );
        ph.put("P_INSTRUCTOR", progra.getInstructorId().getPersonaId().getApellidosyNombres() );
        ph.put("P_ADMINISTRADO", ppdf.getAdministradoId().getRznSocial() + ", RUC: " + ppdf.getAdministradoId().getRuc() );
        ph.put("P_LISTA_ALUMNOS", listarALumnosListadoConstancia(ppdf) );

        return ph;
    }
    
    public static List<Map> listarALumnosListadoConstancia(SspRegistroCurso cRegistro){
        List<Map> lista = new ArrayList();
        Map nmap;
        int cont = 0;
        
        /////////////// VIGILANTE ///////////////
        for(SspAlumnoCurso alumn : cRegistro.getSspAlumnoCursoList() ){
            if(alumn.getActivo() == 1){
                nmap = new HashMap();
                nmap.put("id", 0);
                nmap.put("apellido_pat", alumn.getPersonaId().getApePat() );
                nmap.put("apellido_mat", alumn.getPersonaId().getApeMat() );
                nmap.put("nombres", alumn.getPersonaId().getNombres());
                nmap.put("numDoc", alumn.getPersonaId().getNumDoc());
                lista.add(nmap);
            }
        }
        /////////////////////////////////////////
        
        Collections.sort(lista, new Comparator<Map>() {
            @Override
            public int compare(Map one, Map other) {
                return one.get("apellido_pat").toString().compareTo(other.get("apellido_pat").toString());
            }
        });
        
        for(Map item : lista ){
            cont++;
            item.put("id", cont);
        }
        
        return lista;
    }
    
    /**
     * Envía un correo electrónico
     *
     * @param destino correo de destino
     * @param asunto asunto
     * @param mensaje mensaje de correo
     * @return
     */
    public static boolean enviarCorreo(String destino, String asunto, String mensaje) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(JsfUtil.bundleBDIntegrado("Correo_Servidor"));
            email.setSmtpPort(25);
            email.setFrom(JsfUtil.bundleBDIntegrado("Correo_Cuenta"));
            email.setCharset("UTF-8");
            email.setSubject(asunto);
            email.setMsg(mensaje);
            email.addTo(destino);
            email.send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static int calcularNroDiasDomingo(Date fechaIni, Date fechaFin){
        int dias = 0;
        int diaSemana = 0;
        int rangoDias = JsfUtil.calculaDiasEntreFechas(fechaIni, fechaFin);
        Calendar c_hoy = Calendar.getInstance();
        c_hoy.setTime(fechaIni);

        for(int i=0; i<rangoDias; i++){            
            diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
            c_hoy.add(Calendar.DATE, 1);
            
            if(diaSemana == 1){
                dias++;
            }
        }

        return dias;
    }
    
    public static int calcularNroDiasSabadoYDomingo(Date fechaIni, Date fechaFin){
        int dias = 0;
        int diaSemana = 0;
        int rangoDias = JsfUtil.calculaDiasEntreFechas(fechaIni, fechaFin);
        Calendar c_hoy = Calendar.getInstance();
        c_hoy.setTime(fechaIni);

        for(int i=0; i<rangoDias; i++){            
            diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
            c_hoy.add(Calendar.DATE, 1);
            
            if(diaSemana == 1 || diaSemana == 7){
                dias++;
            }
        }

        return dias;
    }
    
    
    public static int calcularNroDiasSabadoYDomingoRango(Date fechaIni, Date fechaFin){
        int dias = 0;
        int diaSemana = 0;
        int rangoDias = JsfUtil.calculaDiasEntreFechas(fechaIni, fechaFin);
        Calendar c_hoy = Calendar.getInstance();
        c_hoy.setTime(fechaIni);

        for(int i=0; i<= rangoDias; i++){            
            diaSemana = c_hoy.get(Calendar.DAY_OF_WEEK);
            c_hoy.add(Calendar.DATE, 1);
            
            if(diaSemana == 1 || diaSemana == 7){
                dias++;
            }
        }

        return dias;
    }
    
    public static String formatearFecha(Date param, String formato) {
        if (param != null) {
            SimpleDateFormat format = new SimpleDateFormat(formato, new Locale("ES"));
            String result = format.format(param);

            return result;
        } else {
            return null;
        }
    }
    
    public static StreamedContent generaPdfCMP_front(EppCarne carne, boolean blnBorrador, String pathFoto) throws Exception {
        try {
            Map mapNombres = obtenerNombrePlantilaCMP();
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            JasperPrint jpTotal = new JasperPrint();
            List<EppCarne> datos = null;
            Float zoom = new Float(1.8);

            datos = new ArrayList();
            datos.add(carne);
            HashMap parametros = parametrosPdf(carne, blnBorrador, pathFoto, true);

            if (parametros == null) {
                return null;
            }

            InputStream is = ec.getResourceAsStream(mapNombres.get("nombreJasPath").toString());
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            jpTotal = JasperFillManager.fillReport(is, parametros, ds);
            
            Image pageImage = new BufferedImage(
                    (int)(jpTotal.getPageWidth() * zoom) + 1,
                    (int)(jpTotal.getPageHeight() * zoom) + 1,
                    BufferedImage.TYPE_INT_RGB
                    );            
            JRGraphics2DExporter exporter = new JRGraphics2DExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jpTotal));
            SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
            output.setGraphics2D((Graphics2D)pageImage.getGraphics());
            exporter.setExporterOutput(output);
            SimpleGraphics2DReportConfiguration configuration = new SimpleGraphics2DReportConfiguration();
            configuration.setPageIndex(0);
            configuration.setZoomRatio( zoom);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            
            ImageIO.write((BufferedImage)pageImage, "jpg", os );
            
            return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/jpeg", mapNombres.get("nombreRep").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static StreamedContent generaPdfCMP_back(EppCarne carne, boolean blnBorrador, String pathFoto) throws Exception {
        try {
            Map mapNombres = obtenerNombrePlantilaCMP();
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            JasperPrint jpTotal = new JasperPrint();
            List<EppCarne> datos = null;
            Float zoom = new Float(1.8);
            
            datos = new ArrayList();
            datos.add(carne);
            HashMap parametros = parametrosPdf(carne, blnBorrador, pathFoto, false);

            if (parametros == null) {
                return null;
            }

            InputStream is = ec.getResourceAsStream(mapNombres.get("nombreJasBackPath").toString());
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            jpTotal = JasperFillManager.fillReport(is, parametros, ds);

           Image pageImage = new BufferedImage(
                    (int)(jpTotal.getPageWidth() * zoom) + 1,
                    (int)(jpTotal.getPageHeight() * zoom) + 1,
                    BufferedImage.TYPE_INT_RGB
                    );            
            JRGraphics2DExporter exporter = new JRGraphics2DExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jpTotal));
            SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
            output.setGraphics2D((Graphics2D)pageImage.getGraphics());
            exporter.setExporterOutput(output);
            SimpleGraphics2DReportConfiguration configuration = new SimpleGraphics2DReportConfiguration();
            configuration.setPageIndex(0);
            configuration.setZoomRatio( zoom);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            
            ImageIO.write((BufferedImage)pageImage, "jpg", os );
            
            return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/jpeg", mapNombres.get("nombreRep").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * OBTENER NOMBRE JASPER DE PLANTILLA REPORTE DE CARNÉ MANIPULADOR DE
     * PIROTÉCNICOS
     *
     * @author Richar Fernández
     * @version 1.0
     * @return Listado de nombres de reporte (.jasper, .pdf)
     */
    public static Map obtenerNombrePlantilaCMP() {
        Map nmap = new HashMap();

        nmap.put("nombreJas", "CMP.jasper");
        nmap.put("nombreRep", "CMP.pdf");
        nmap.put("nombreJasPath", "/m/pub/gepp/eppCarne/reportes/CMP.jasper");
        nmap.put("nombreJasBack", "CMPback.jasper");
        nmap.put("nombreRepBack", "CMPback.pdf");
        nmap.put("nombreJasBackPath", "/m/pub/gepp/eppCarne/reportes/CMPback.jasper");

        return nmap;
    }

    /**
     * PARAMETROS PARA GENERAR PDF
     *
     * @author Richar Fernández
     * @version 2.0
     * @param ppdf Registro a crear reporte
     * @return Listado de parámetros para reporte
     */
    private static HashMap parametrosPdf(EppCarne reg, boolean blnBorrador, String pathFoto, boolean isFront) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
        byte[] fotoByte = null;
        boolean tieneFoto = true;
        InputStream is = null, is2 = null;
        SbDireccionGt direccion = mostrarDireccionPersonaCMP(reg.getPersonaId());

        if(isFront){
            try {
                if (reg.getFotoId() != null) {
                    fotoByte = FileUtils.readFileToByteArray(new File(pathFoto + reg.getFotoId().getRutaFile()));
                    is = new ByteArrayInputStream(escalarRotarImagen(fotoByte, 340, 433, 0));
                    is2 = new ByteArrayInputStream(escalarRotarImagen(fotoByte, 200, 246, 0));
                }else {
                    mensajeError("El carné "+reg.getNroCarne()+" no tiene una foto registrada");
                    tieneFoto = false;                    
                }

            } catch (Exception e) {
                mensajeError("Hubo un error al cargar la foto en el carné "+ reg.getNroCarne());
                tieneFoto = false;
            }
            if(!tieneFoto){
                try {
                    is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg");
                    is2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg");
                } catch (Exception e) {
                    return null;
                }
            }
            ph.put("P_FOTO_PERSONA", is);
            ph.put("P_FOTO_PERSONA_PEQ", is2);
        }       

        ph.put("P_FIRMA", ec.getRealPath("/resources/imagenes/firmaGEPP.png"));
        ph.put("P_BORRADOR", blnBorrador);
        if (blnBorrador) {
            ph.put("P_IMG_BORRADOR", ec.getRealPath("/resources/imagenes/") + "/borrador_pdf_licencia.png");
        }        
        ph.put("P_FONDO_CMP", ec.getRealPath("/resources/imagenes/fondoCMP.jpg"));
        ph.put("P_ANIO", formateador.format(reg.getRegistroId().getFecha()));
        ph.put("P_qr", "https://www.sucamec.gob.pe/sel/faces/qr/cmp.xhtml?h=" + reg.getHashQr());
        ph.put("P_FECHA_EMISION", formatoFechaDdMmYyyy(reg.getFechaEmision()));
        ph.put("P_FECHA_VENCIMIENTO", formatoFechaDdMmYyyy(reg.getFechaVencimiento()));
        ph.put("P_DOMICILIO", (direccion != null) ? direccion.getDireccion() : null);
        ph.put("P_DIST", (direccion != null) ? direccion.getDistritoId().getNombre() : null);
        ph.put("P_PROV", (direccion != null) ? direccion.getDistritoId().getProvinciaId().getNombre() : null);
        ph.put("P_DPTO", (direccion != null) ? direccion.getDistritoId().getProvinciaId().getDepartamentoId().getNombre() : null);
        ph.put("P_CATEGORIA", (reg.getTipoActividadId() != null) ? reg.getTipoActividadId().getAbreviatura() : "");

        return ph;
    }
    
    public static SbDireccionGt mostrarDireccionPersonaCMP(SbPersonaGt param) {
        for (SbDireccionGt sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                return sbd;                
            }
        }
        return null;
    }
    
    public static StreamedContent generaPdfCarneGssp(List<Map> lstCarne,String pathFoto, String pathFirma, String pathFondo, String linkCarne) throws Exception {
        try {
            Map mapNombres = obtenerNombrePlantilaCarneGssp();
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            JasperPrint jpTotal = new JasperPrint(), jp = null;
            List<SspCarne> datos = new ArrayList();
            datos.add(new SspCarne());
            int cont = 0;
            String nombreJas = null;

            for (Map carne : lstCarne) {
                cont++;
                HashMap parametros = parametrosPdfCarneGssp(carne, pathFoto, pathFirma, pathFondo, linkCarne);

                if (parametros == null) {
                    return null;
                }
                if (carne.get("RZN_SOCIAL")==null){
                    nombreJas="/aplicacion/gssp/sspCarne/reportes/carneGsspSinRz.jasper";
                }else{
                    nombreJas=mapNombres.get("nombreJasPath").toString();
                }

                if (cont == 1) {
                    InputStream is = ec.getResourceAsStream(nombreJas);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
                    jpTotal = JasperFillManager.fillReport(is, parametros, ds);
                } else {
                    InputStream is = ec.getResourceAsStream(nombreJas);
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
                    jp = JasperFillManager.fillReport(is, parametros, ds);

                    List pages = jp.getPages();
                    for (Object page : pages) {
                        JRPrintPage object = (JRPrintPage) page;
                        jpTotal.addPage(object);
                    }
                }

                InputStream isback = ec.getResourceAsStream(mapNombres.get("nombreJasBackPath").toString());
                JRBeanCollectionDataSource dsback = new JRBeanCollectionDataSource(datos);
                JasperPrint jpback = JasperFillManager.fillReport(isback, parametros, dsback);

                List pages = jpback.getPages();
                for (Object page : pages) {
                    JRPrintPage object = (JRPrintPage) page;
                    jpTotal.addPage(object);
                }
            }

            // Exportar Reporte //
            JRPdfExporter exp = new JRPdfExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // Cargar configuración para PDF customizados //
            SimplePdfExporterConfiguration exc = new SimplePdfExporterConfiguration();
            exp.setExporterInput(new SimpleExporterInput(jpTotal));
            exp.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
            // Configuración //
            exc.setCompressed(Boolean.TRUE);
            exc.setMetadataAuthor(null);
            exc.setMetadataCreator("SUCAMEC");
            exc.setMetadataTitle(null);
            exc.setMetadataKeywords(null);
            exc.setMetadataSubject(null);
            // Generar Reporte //
            exp.setConfiguration(exc);
            exp.exportReport();
            return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "application/pdf", mapNombres.get("nombreRep").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Map obtenerNombrePlantilaCarneGssp() {
        Map nmap = new HashMap();

        nmap.put("nombreJas", "carneGssp.jasper");
        nmap.put("nombreRep", "carneGssp.pdf");
        nmap.put("nombreJasPath", "/aplicacion/gssp/sspCarne/reportes/carneGssp.jasper");
        nmap.put("nombreJasBack", "carneGsspBack.jasper");
        nmap.put("nombreRepBack", "carneGsspBack.pdf");
        nmap.put("nombreJasBackPath", "/aplicacion/gssp/sspCarne/reportes/carneGsspBack.jasper");

        return nmap;
    }
    
    private static HashMap parametrosPdfCarneGssp(Map item, String pathFoto, String pathFirma, String pathFondo, String linkCarne) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        String nombreFoto = "";
        boolean cargarFotoDefault = false;
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
        byte[] fotoByte = null, firmaByte = null, fondoByte = null ;;
        InputStream is = null, is2 = null, isFirma = null, isFondo = null;

        if(item.get("CARNE_FOTO") != null && !item.get("CARNE_FOTO").toString().equals("S/F")){            
            try {
                nombreFoto = item.get("CARNE_FOTO").toString();
                nombreFoto = pathFoto + nombreFoto.substring(0,nombreFoto.lastIndexOf('.')) + nombreFoto.substring(nombreFoto.lastIndexOf('.'), nombreFoto.length()).toUpperCase();
                fotoByte = FileUtils.readFileToByteArray(new File(nombreFoto));                
                
                is = new ByteArrayInputStream(escalarRotarImagen(fotoByte, 340, 433, 0));
                is2 = new ByteArrayInputStream(escalarRotarImagen(fotoByte, 200, 246, 0));
            } catch (Exception e) {
                try {
                    nombreFoto = item.get("CARNE_FOTO").toString();
                    nombreFoto = pathFoto + nombreFoto.substring(0,nombreFoto.lastIndexOf('.')) + nombreFoto.substring(nombreFoto.lastIndexOf('.'), nombreFoto.length()).toLowerCase();
                    fotoByte = FileUtils.readFileToByteArray(new File(nombreFoto));
                    is = new ByteArrayInputStream(escalarRotarImagen(fotoByte, 340, 433, 0));
                    is2 = new ByteArrayInputStream(escalarRotarImagen(fotoByte, 200, 246, 0));
                } catch (Exception e2) {
                    cargarFotoDefault = true;
                }
            }
        }else{
            cargarFotoDefault = true;
        }
        if(cargarFotoDefault){
            is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg");
            is2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg");
        }
        try{
            firmaByte = FileUtils.readFileToByteArray(new File( pathFirma +item.get("NOMBRE_FIRMA").toString() ));
            fondoByte = FileUtils.readFileToByteArray(new File( pathFondo + item.get("NOMBRE_FONDO").toString() ));

            isFirma = new ByteArrayInputStream(firmaByte);
            isFondo = new ByteArrayInputStream(fondoByte);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;                
        }

        ph.put("P_FIRMA", isFirma);
        ph.put("P_FONDO", isFondo);
        ph.put("P_FOTO_PERSONA", is);
        ph.put("P_FOTO_PERSONA_PEQ", is2);        
        ph.put("P_ANIO", formateador.format((Date)item.get("FECHA_INI")));
        ph.put("P_qr", linkCarne + item.get("HASH_QR"));
        ph.put("P_FECHA_INICIO", formatoFechaDdMmYyyy((Date)item.get("FECHA_INI") ));
        ph.put("P_FECHA_FIN", formatoFechaDdMmYyyy((Date)item.get("FECHA_FIN")) );
        ph.put("P_GERENTE", item.get("NOMBRE_JEFE")+" " + item.get("APE_PAT_JEFE")+" " + item.get("APE_MAT_JEFE")+" ");
        ph.put("P_POSTFIRMA", item.get("POSTFIRMA").toString());
        ph.put("P_NRO_EXPEDIENTE", item.get("NRO_EXPEDIENTE").toString());        
        ph.put("P_VIGILANTE_NUMDOC", item.get("NUM_DOC").toString());
        ph.put("P_VIGILANTE_TIPODOC", item.get("TIPO_DOC").toString());
        ph.put("P_VIGILANTE_APEPAT", item.get("APE_PAT").toString());
        ph.put("P_VIGILANTE_APEMAT", (item.get("APE_MAT") != null)?item.get("APE_MAT").toString():"" );
        ph.put("P_VIGILANTE_NOMBRES", item.get("NOMBRES").toString());
        ph.put("P_EMPRESA_RUC", item.get("RUC").toString());
        ph.put("P_EMPRESA_RZNSOCIAL", (item.get("RZN_SOCIAL")!=null)?item.get("RZN_SOCIAL").toString():null );
        ph.put("P_EMPRESA_NOMBRES", ((item.get("EMPRESA_NOMBRES")!=null)?item.get("EMPRESA_NOMBRES").toString():"")+" "+((item.get("EMPRESA_APE_PAT")!=null)?item.get("EMPRESA_APE_PAT").toString():"")+" "+((item.get("EMPRESA_APE_MAT")!=null)?item.get("EMPRESA_APE_MAT").toString():"") );
        ph.put("P_MODALIDAD", item.get("MODALIDAD_DESCRIPCION").toString());
        ph.put("P_NRO_CARNE", item.get("NRO_CARNE").toString());
        return ph;
    }
        
    public static void showRedirectHtmlError(String p_msg) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        PrintWriter pw = response.getWriter();
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<title>Información del Sistema</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<center>");
        pw.println("<h2>" + p_msg + "</h2>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");
        FacesContext.getCurrentInstance().responseComplete();
    }
    
     /**
     * Permite mostrar un mensaje informativo de una accion ejecutada con Ã©xito
     * en <h: messages>
     *
     * @param msg El mensaje exitoso a mostrar.
     */
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }
    
    /**
     * Permite mostrar un mensaje de error en <h: messages>
     *
     * @param ex La excepciÃ³n con el error.
     * @param defaultMsg Mensaje por defecto en caso de no haber ninguno.
     */
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = obtenerMensajeError(ex);
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }
    
    /**
     * Permite mostrar un mensaje de error en <h: messages>
     *
     * @param msg El mensaje de error a mostrar.
     */
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }
    
    /**
     * Permite obtener una lista de Items de una entidad para un SelectOneMenu.
     * El valor para el item es equivalente a la funcion toString().
     *
     * @param entities La lista de entidades a mostrar.
     * @param selectOne Si permite seleccionar nulos.
     * @return Una lista del tipo array SelectItem.
     */
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        return getSelectItems(entities, selectOne, null);
    }
    
    /**
     *
     * Permite obtener una lista de Items de una entidad para un SelectOneMenu.
     * Se puede seleccionar el campo que se desea visualizar.
     *
     * @param entities La lista de entidades a mostrar.
     * @param selectOne Si permite seleccionar nulos.
     * @param field Campo que se desea mostrar.
     * @return Una lista del tipo array SelectItem.
     */
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne, String field) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        try {
            int i = 0;
            if (selectOne) {
                items[0] = new SelectItem("", "---");
                i++;
            }
            for (Object x : entities) {
                if (field == null) {
                    items[i++] = new SelectItem(x, x.toString());
                } else {
                    Field f = x.getClass().getDeclaredField(field);
                    f.setAccessible(true);
                    String val = f.get(x).toString();
                    items[i++] = new SelectItem(x, val);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return items;
    }
    /**
     * Generar pdf de Licencia Manipulador de Explosivos
     *
     * @param lstLicencia
     * @param borrador
     * @param pathFoto
     * @return
     * @throws java.lang.Exception
     */
    public static StreamedContent generaPdfLME(List<EppLicencia> lstLicencia, boolean borrador, String pathFoto) throws Exception {
        try {
            Map mapNombres = obtenerNombrePlantilaLME();
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            JasperPrint jpTotal = new JasperPrint(), jp = null;
            List<EppLicencia> datos = null;
            int cont = 0;

            for (EppLicencia licencia : lstLicencia) {
                cont++;
                datos = new ArrayList();
                datos.add(licencia);
                HashMap parametros = parametrosPdfLicencia(licencia, borrador, pathFoto);

                if (parametros == null) {
                    return null;
                }

                if (cont == 1) {
                    InputStream is = ec.getResourceAsStream(mapNombres.get("nombreJasPath").toString());
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
                    jpTotal = JasperFillManager.fillReport(is, parametros, ds);
                } else {
                    InputStream is = ec.getResourceAsStream(mapNombres.get("nombreJasPath").toString());
                    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
                    jp = JasperFillManager.fillReport(is, parametros, ds);

                    List pages = jp.getPages();
                    for (Object page : pages) {
                        JRPrintPage object = (JRPrintPage) page;
                        jpTotal.addPage(object);
                    }
                }

                InputStream isback = ec.getResourceAsStream(mapNombres.get("nombreJasBackPath").toString());
                JRBeanCollectionDataSource dsback = new JRBeanCollectionDataSource(datos);
                JasperPrint jpback = JasperFillManager.fillReport(isback, parametros, dsback);

                List pages = jpback.getPages();
                for (Object page : pages) {
                    JRPrintPage object = (JRPrintPage) page;
                    jpTotal.addPage(object);
                }

            }

            // Exportar Reporte //
            JRPdfExporter exp = new JRPdfExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // Cargar configuración para PDF customizados //
            SimplePdfExporterConfiguration exc = new SimplePdfExporterConfiguration();
            exp.setExporterInput(new SimpleExporterInput(jpTotal));
            exp.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
            // Configuración //
            exc.setCompressed(Boolean.TRUE);
            exc.setMetadataAuthor(null);
            exc.setMetadataCreator("SUCAMEC");
            exc.setMetadataTitle(null);
            exc.setMetadataKeywords(null);
            exc.setMetadataSubject(null);
            // Generar Reporte //
            exp.setConfiguration(exc);
            exp.exportReport();
            return new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "application/pdf", mapNombres.get("nombreRep").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Map obtenerNombrePlantilaLME() {
        Map nmap = new HashMap();

        nmap.put("nombreJas", "LME.jasper");
        nmap.put("nombreRep", "LME.pdf");
        nmap.put("nombreJasPath", "/aplicacion/gepp/reportes/LME.jasper");
        nmap.put("nombreJasBack", "LMEback.jasper");
        nmap.put("nombreRepBack", "LMEback.pdf");
        nmap.put("nombreJasBackPath", "/aplicacion/gepp/reportes/LMEback.jasper");

        return nmap;
    }
    private static HashMap parametrosPdfLicencia(EppLicencia reg, boolean borrador, String pathFoto) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
        byte[] fotoByte = null;
        SbDireccion direccion = mostrarDireccionPersonaCMP(reg.getPersonaId());

        try {
            if (reg.getFotoId() != null) {
                fotoByte = FileUtils.readFileToByteArray(new File(pathFoto + reg.getFotoId().getRutaFile()));
                ph.put("P_FOTO_PERSONA", new ByteArrayInputStream(escalarRotarImagen(fotoByte, 340, 433, 0)));
                ph.put("P_FOTO_PERSONA_PEQ", new ByteArrayInputStream(escalarRotarImagen(fotoByte, 200, 246, 0)));

                //ph.put("P_FOTO_PERSONA", new ByteArrayInputStream(fotoByte) );
                //ph.put("P_FOTO_PERSONA_PEQ", new ByteArrayInputStream(fotoByte) );
            } else {
                mensajeError("El carné "+reg.getNroLicencia()+" no tiene una foto registrada");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensajeError("Hubo un error al cargar la foto con el carné "+ reg.getNroLicencia());
            return null;
        }
        ph.put("P_FIRMA", ec.getRealPath("/resources/imagenes/firmaGEPP.png"));
        ph.put("P_BORRADOR", borrador);
        if (borrador) {
            ph.put("P_IMG_BORRADOR", ec.getRealPath("/resources/imagenes/") + "/borrador_pdf_licencia.png");
        }
        ph.put("P_FONDO_LME", ec.getRealPath("/resources/imagenes/fondoLME.jpg"));
        ph.put("P_FONDO_LME_BACK", ec.getRealPath("/resources/imagenes/fondoLME_back.jpg"));
        ph.put("P_ANIO", formateador.format(reg.getRegistroId().getFecha()));
        ph.put("P_qr", "https://www.sucamec.gob.pe/sel/faces/qr/lme.xhtml?h=" + reg.getHashQr());
        ph.put("P_FECHA_EMISION", formatoFechaDdMmYyyy(reg.getFecEmi()));
        ph.put("P_FECHA_VENCIMIENTO", formatoFechaDdMmYyyy(reg.getFecVenc()));
        ph.put("P_DOMICILIO", (direccion != null) ? direccion.getDireccion() : null);
        ph.put("P_DIST", (direccion != null) ? direccion.getDistritoId().getNombre() : null);
        ph.put("P_PROV", (direccion != null) ? direccion.getDistritoId().getProvinciaId().getNombre() : null);
        ph.put("P_DPTO", (direccion != null) ? direccion.getDistritoId().getProvinciaId().getDepartamentoId().getNombre() : null);
        ph.put("P_CATEGORIA", (reg.getTipoCargoId() != null) ? reg.getTipoCargoId().getAbreviatura() : "");

        return ph;
    }
    public static SbDireccion mostrarDireccionPersonaCMP(SbPersona param) {
        for (SbDireccion sbd : param.getSbDireccionList()) {
            if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
                return sbd;                
            }
        }
        return null;
    }

    public static Boolean isNullOrEmpty(List datos) {
        if (datos == null || datos.isEmpty()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static Long tempIdN() {
        tempIdValN--;
        if (tempIdValN < Long.MIN_VALUE + 100) {
            tempIdValN = -1;
        }
        return tempIdValN;
    }

    public static Date obtenerFechaPorDatos(String dia, String mes, String anio) {
        Date fecha = null;
        try {
            if (anio != null && mes != null && dia != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateInString = dia + "/" + mes + "/" + anio;
                fecha = formatter.parse(dateInString);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fecha;
    }

    public static String mostrarFechaDateFormat(Date fechaIn) {
        String fecha;
        if (fechaIn == null) {
            fecha = "BORRADOR";
            return fecha;
        }
        SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("ES"));
        SimpleDateFormat formatMes = new SimpleDateFormat("MMMM", new Locale("ES"));
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy", new Locale("ES"));
        String dia = formatDia.format(fechaIn);
        String mes = formatMes.format(fechaIn);
        String anio = formatAnio.format(fechaIn);
        mes = mes.substring(0, 1).toLowerCase() + mes.substring(1, mes.length());
        fecha = dia + " de " + mes + " del " + anio;
        return fecha;
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
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            InputStream is = ec.getResourceAsStream(pathIn);
            //REEMPLAZAR POR ESTE PARA QUE LOS REPORTES SE EJECUTEN DESDE RUTA EN EL SERVIDOR
            //InputStream is = new FileInputStream(pathIn)
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
            JasperExportManager.exportReportToPdfFile(jp, pathOut + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void borrarIdsN(List datos) throws Exception {
        // Usar Iterator que es por referencia, For each no sirve, copia el valor.
        if (!isNullOrEmpty(datos)) {
            for (Iterator it = datos.iterator(); it.hasNext();) {
                Object o = it.next();
                Method m = o.getClass().getDeclaredMethod("setId", Long.class);
                Method mg = o.getClass().getDeclaredMethod("getId");
                if (mg.invoke(o) != null) {
                    Long id = (Long) mg.invoke(o);
                    if (id < 0) {
                        m.invoke(o, (Object) null);
                    }
                }
            }
        }
    }

    public static byte[] obtenerBytesFromFile(String rutaPath, String nombre) {
        try {
            return Files.readAllBytes(new File(rutaPath + File.separator + nombre).toPath());
        } catch (Exception ex) {
            // null si no existe el archivo físicamente.
            return null;
        }
    }

    public static String getFechaFormato(Date fecha, String formato) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
            return dateFormat.format(fecha);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Función que convierte a nombre Propio omitiendo texto de 2 caracteres
     *
     * @author Gino Chávez
     * @param texto
     * @return
     */
    public static String convertirNombrePropio(String texto) {
        if (texto != null && !"".equals(texto.trim())) {
            String[] cadena = texto.split(" ");
            String concat = "";
            for (String p : cadena) {
                if (p.length() > 2) {
                    concat += " " + StringUtils.capitalize(p.toLowerCase());
                } else {
                    concat += " " + p.toLowerCase();
                }
            }
            return concat;
        }
        return null;
    }
    
}
