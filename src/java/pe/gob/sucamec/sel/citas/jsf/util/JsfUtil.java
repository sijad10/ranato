package pe.gob.sucamec.sel.citas.jsf.util;

import com.itextpdf.text.pdf.PdfReader;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import org.apache.commons.codec.binary.Hex;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.sistemabase.data.SbDireccion;
import pe.gob.sucamec.sistemabase.seguridad.DatosUsuario;

/**
 * Clase con funciones utiles para usar JSF
 */
public class JsfUtil {

    public final static short TRUE = 1;
    public final static short FALSE = 0;
    public final static short DOS = 2;
    
    private static long tempIdValN = -1;
    
    public static String getNumIP() {
        String numIP;
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        numIP = request.getHeader("X-FORWARDED-FOR");
        if (numIP == null) {
            numIP = request.getRemoteAddr();
        }
        return numIP;
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
     * Clase para enviar parámetros a los ListDialogs
     */
    public static class ListDialogParams {

        public String field;
        public Object item;
    }

    /**
     * Permite obtener una lista de Items de una entidad para un SelectOneMenu. El valor para el item es equivalente a
     * la funcion toString().
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
     * Permite obtener una lista de Items de una entidad para un SelectOneMenu. Se puede seleccionar el campo que se
     * desea visualizar.
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

    /**
     * Funcion para validar un DNI o un RUC
     *
     * @param doc El número de documento a validar.
     * @param tipo El tipo de documento a validar "DNI"/"RUC".
     * @return true si el documento es valido, false sino.
     */
    public static boolean ValidarDniRuc(String doc, String tipo) {
        if (tipo == "DNI" && doc.length() != 9) {
            return false;
        } else if (tipo == "RUC" && doc.length() != 11) {
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
     * Permite obtener un objeto que se encuentra en la sesion similar a #{} usado en jsf. Mayormente usado para obtener
     * controllers.
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
     * Lista de errores personalizados para el ORACLE Incluir la restriccion, o texto del error y el mensaje que se
     * desea mostrar
     */
    private final static String[][] listaErrores = {
        {"SISTEMABASE.PERSONA__UN", "Error RUC duplicado"}
    };

    /**
     * Permite mostrar mensajes personalizados en base al ERROR, principalmente para errores del ORACLE
     *
     * @param ex Excepcion con el error
     * @return El mensaje de error
     */
    public static String obtenerMensajeError(Exception ex) {
        if (ex.getCause() != null) {
            if (ex.getCause().getCause() != null) {
                String error = ex.getCause().getCause().toString();
                for (int i = 0; i < listaErrores.length; i++) {
                    if (error.contains(listaErrores[i][0])) {
                        return listaErrores[i][1];
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
     * Permite agregar varios mensajes de error en forma simultanea en
     * <h: messages>
     *
     * @param messages Lista con los mensajes de error.
     */
    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    /**
     * Permite mostrar un mensaje de error en <h: messages>
     *
     * @param msg El mensaje de error a mostrar.
     */
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    /**
     * Permite mostrar un mensaje informativo de una accion ejecutada con éxito en <h: messages>
     *
     * @param msg El mensaje exitoso a mostrar.
     */
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Permite mostrar un mensaje de alerta de una accion ejecutada con éxito en
     * <h: messages>
     *
     * @param msg El mensaje exitoso a mostrar.
     */
    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null);
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
     * Permite obtener texto del archivo Bundle
     *
     * @param s Nombre del text
     * @return El texto asociado al nombre
     */
    public static String bundle(String s) {
        return ResourceBundle.getBundle("/BundleCitas").getString(s);
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
     * Permite obtener un parámetro o propiedad de los componentes de la página web.
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

    public static String obtenertNumIP() {
        String numIP = null;
        if (numIP == null) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            numIP = request.getHeader("X-FORWARDED-FOR");
            if (numIP == null) {
                numIP = request.getRemoteAddr();
            }
        }
        return numIP;
    }

    public static Date getFechaSinHora(Date fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date fechaFormateada = dateFormat.parse(dateFormat.format(fecha));
            return fechaFormateada;
        } catch (ParseException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void mostrarReporte(Map parameters, String reporte, List lista, String nombreReporte) throws JRException, IOException {
        try {
            byte[] bytes = null;
            //parameters.put("PV_RUTA_IMAGEN", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/logos/logo_mtpe.png"));
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            //JasperDesign jasperDesign = JRXmlLoader.load(servletContext.getRealPath("/resources/reportes/" + reporte));
            JasperDesign jasperDesign = JRXmlLoader.load(servletContext.getRealPath(reporte));
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            bytes = JasperRunManager.runReportToPdf(
                    jasperReport,
                    parameters,
                    new JRBeanCollectionDataSource(lista));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(lista));

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            response.setHeader("Content-Disposition", "inline; filename=\"" + nombreReporte + ".pdf\";");
            response.setHeader("Content-Type", "application/pdf");

            ServletOutputStream ouputStream = response.getOutputStream();

            FacesContext facesContext = FacesContext.getCurrentInstance();

            if (bytes != null && bytes.length > 0) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, ResourceBundle.getBundle("/BundleCitaInterna").getString("Documentos_pathUpload_constancia") + parameters.get("PARAMIDREG").toString() + ".pdf");
            }
            ouputStream.flush();
            ouputStream.close();
            facesContext.responseComplete();
            facesContext.renderResponse();
        } catch (JRException e) {
            e.printStackTrace();
        } finally {

        }

    }

    public static void generaPdfNoEncontrado(Map parameters,
            String reporte,
            List lista,
            String nombreReporte) throws JRException, IOException {
        try {
            byte[] bytes = null;
            //parameters.put("PV_RUTA_IMAGEN", FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/logos/logo_mtpe.png"));
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            //JasperDesign jasperDesign = JRXmlLoader.load(servletContext.getRealPath("/resources/reportes/" + reporte));
            JasperDesign jasperDesign = JRXmlLoader.load(servletContext.getRealPath(reporte));
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            bytes = JasperRunManager.runReportToPdf(
                    jasperReport,
                    parameters,
                    new JRBeanCollectionDataSource(lista));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(lista));


            if (bytes != null && bytes.length > 0) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, ResourceBundle.getBundle("/BundleCitaInterna").getString("Documentos_pathUpload_constancia") + parameters.get("PARAMIDREG").toString() + ".pdf");
            }
            showRedirectHtmlError("PDF generado, debe regresar al menu principal");
        } catch (JRException e) {
            e.printStackTrace();
        } finally {

        }

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

    public static String readBundle(String s) {
        try {
            return ResourceBundle.getBundle("/BundleCitaInterna").getString(s);
        } catch (Exception e) {
            e.printStackTrace();;
            return null;
        }
    }

    public static Connection obtenerConexion() {
        try {
//            Connection connection = null;
//            connection = DriverManager.getConnection(readBundle("ConexionInstancia"), readBundle("ConexionUsr"), readBundle("ConexionPwd"));
            //java.sql.Connection connection = entityManager.unwrap(java.sql.Connection.class);  
            javax.naming.InitialContext ctx = new javax.naming.InitialContext();
            javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(readBundle("PoolConexion"));
            java.sql.Connection connection = ds.getConnection();
            return connection;
        } catch (Exception e) {
            return null;
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

    public static String obtenerFechaXCriterio(Date fechaEvaluada, int campoRequerido) {
        try {
            //Fecha actual desglosada:
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

    public static String getIpAddress() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = "";
        ip = request.getRemoteAddr();
        return ip;
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
    public static void subirPdf(String id, HashMap parametros, List datos, String pathIn, String pathOut) throws Exception {
        try {
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            InputStream is = ec.getResourceAsStream(pathIn);
            //REEMPLAZAR POR ESTE PARA QUE LOS REPORTES SE EJECUTEN DESDE RUTA EN EL SERVIDOR
            //InputStream is = new FileInputStream(pathIn);
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(datos);
            JasperPrint jp = JasperFillManager.fillReport(is, parametros, ds);
            JasperExportManager.exportReportToPdfFile(jp, pathOut + id + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void PdfDownload(String path, String fileName) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            InputStream in = new FileInputStream(path + fileName);
            //InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(pathAppDownload + fileName);
            //file = new DefaultStreamedContent(stream,"application/pdf", fileName);

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "inline; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                input = new BufferedInputStream(in);
                output = new BufferedOutputStream(response.getOutputStream());

                byte[] buffer = new byte[10240];
                for (int length; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception e) {
            } finally {
                output.close();
                input.close();
            }
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

        } catch (Exception e) {
            JsfUtil.showRedirectHtmlError("No se encontró el archivo: <font color=\"red\">" + fileName + "</font> en la ruta especificada, debe revisar.");
        }

    }
    
    public static void PdfDownloadImagen(String path, String fileName) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            InputStream in = new FileInputStream(path + fileName);
            //InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(pathAppDownload + fileName);
            //file = new DefaultStreamedContent(stream,"application/pdf", fileName);

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("image/jpeg"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "inline; filename=\"" + fileName + "\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                input = new BufferedInputStream(in);
                output = new BufferedOutputStream(response.getOutputStream());

                byte[] buffer = new byte[10240];
                for (int length; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception e) {
            } finally {
                output.close();
                input.close();
            }
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

        } catch (Exception e) {
            JsfUtil.showRedirectHtmlError("No se encontró el archivo: <font color=\"red\">" + fileName + "</font> en la ruta especificada, debe revisar.");
        }

    }
    
    public static void PdfDownloadFormatos(String pathCompleto) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        try {
            InputStream in = new FileInputStream(pathCompleto);
            //InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(pathAppDownload + fileName);
            //file = new DefaultStreamedContent(stream,"application/pdf", fileName);

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
            response.setHeader("Content-disposition", "inline; filename=\"" + pathCompleto + "\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                input = new BufferedInputStream(in);
                output = new BufferedOutputStream(response.getOutputStream());

                byte[] buffer = new byte[10240];
                for (int length; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception e) {
            } finally {
                output.close();
                input.close();
            }
            facesContext.responseComplete(); // Important! Else JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.

        } catch (Exception e) {
            JsfUtil.showRedirectHtmlError("No se encontró el archivo: <font color=\"red\">" + pathCompleto + "</font> en la ruta especificada, debe revisar.");
        }

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
    
    public static String dateToString(Date fecha, String formato) {
        SimpleDateFormat form = new SimpleDateFormat(formato);
        return form.format(fecha);
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
    
    public static Date stringToDate(String fecha, String formato) {
        try {
            DateFormat format = new SimpleDateFormat(formato);
            return format.parse(fecha);
        } catch (Exception e) {
        }
        return null;
    }
    
    public static Date getFechaConHora(Date fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date fechaFormateada = dateFormat.parse(dateFormat.format(fecha));
            return fechaFormateada;
        } catch (ParseException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean validaFechaMayorMenor(Date fechaini, Date fechafin) {
        String resultado = "";
        boolean compara = false;
        try {
            if (fechaini.before(fechafin)) {
                resultado = "La Fecha 1 es menor ";
                compara = true;
            } else if (fechafin.before(fechaini)) {
                resultado = "La Fecha 1 es Mayor ";
                compara = false;
            } else {
                resultado = "Las Fechas Son iguales ";
                compara = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compara;
    }
    
    public static int calcularAnios(Date inicio, Date fin ) {
        int edad = 0;
        
        LocalDate birthdate = new LocalDate (inicio);
        LocalDate now = new LocalDate(fin);
        Years age = Years.yearsBetween(birthdate, now);
        edad = age.getYears();

        return edad;
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
            System.out.println("Error:JsfUtil.verificarPDF:" + ex.getMessage());
        }
        return false;
    }

    /**
     * Verifica si un archivo es efectivamente un PNG a través de los magic
     * numbers.
     *
     * @param uf Archivo cargado.
     * @return true si el archivo es correcto
     */
    public static boolean verificarPNG(UploadedFile uf) {
        try {
            InputStream i = uf.getInputstream();
            byte[] id = new byte[4];
            if (i.read(id) == 4) {
                if ((id[0] == (byte) 0x89)
                        && (id[1] == (byte) 0x50)
                        && (id[2] == (byte) 0x4E)
                        && (id[3] == (byte) 0x47)) {
                    return true;
                }
            }

        } catch (Exception ex) {
            System.out.println("Error:JsfUtil.verificarPNG:" + ex.getMessage());
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
            System.out.println("Error:JsfUtil.verificarPDF:" + ex.getMessage());
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
    
    public static boolean analyzeImageJPG(UploadedFile file) throws NoSuchAlgorithmException, IOException {
        try {
            final ImageInputStream imageInputStream = ImageIO.createImageInputStream(file.getInputstream());
            final Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
            if (!imageReaders.hasNext()) {
                return false;
            }
            final ImageReader imageReader = imageReaders.next();
            imageReader.setInput(imageInputStream);
            final BufferedImage image = imageReader.read(0);
            if (image == null) {
                return false;
            }
            image.flush();
            if (imageReader.getFormatName().equals("JPEG")) {
                imageInputStream.seek(imageInputStream.getStreamPosition() - 2);
                final byte[] lastTwoBytes = new byte[2];
                imageInputStream.read(lastTwoBytes);
                if (lastTwoBytes[0] != (byte) 0xff || lastTwoBytes[1] != (byte) 0xd9) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } catch (Exception E) {
            return false;
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

    public static String mostrarDireccion(SbDireccion sbd) {
        String direccion = "";
        //if (sbd.getTipoId().getCodProg().equals("TP_DIRECB_FIS") && sbd.getActivo() == 1) {
            direccion = (sbd.getViaId().getNombre().equals("OTRO") ? "" : sbd.getViaId().getNombre())
                    + " " + sbd.getDireccion()
                    + (sbd.getNumero() == null ? "" : (" " + sbd.getNumero()));
            direccion = direccion + ", distrito de " + sbd.getDistritoId().getNombre() + ", provincia de " + sbd.getDistritoId().getProvinciaId().getNombre() + ", departamento de " + sbd.getDistritoId().getProvinciaId().getDepartamentoId().getNombre();
        //}
        return direccion;
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
    
     /**
     * Obtiene un archivo del repositorio para poderlo enviar a através de la
     * web.
     *
     * @param rutaPath Nombre en el bundle de la Ruta.
     * @param nombre Nombre del archivo en el servidor, usualmente id mas la
     * extensión.
     * @param descarga Nombre que va a tener el archivo al momento de realizar
     * la descarga.
     * @param mime Tipo mime para la descarga, los mas usados son:
     * application/pdf, text/plain, text/html,
     * @return
     * @throws Exception
     */
    public static StreamedContent obtenerArchivo(String rutaPath, String nombre, String descarga, String mime) throws Exception {
        FileInputStream f = new FileInputStream(rutaPath + File.separator + nombre);
        return new DefaultStreamedContent(f, mime, descarga);
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
    
}
