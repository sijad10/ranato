package pe.gob.sucamec.sel.disca.jsf.util;

import com.itextpdf.text.pdf.PdfReader;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.bdintegrado.data.AmaLicenciaDeUso;

/**
 * Clase con funciones utiles para usar JSF
 */
public class JsfUtil {

    /**
     * Valor para generar Id temporales.
     */
    private static long tempIdVal = 0;

    /**
     * Devuelte un valor temporal para listas que requieran datasets en memoria.
     *
     * @return Un Id temporal.
     */
    public static Long tempId() {
        tempIdVal++;
        if (tempIdVal > Long.MAX_VALUE - 100) {
            tempIdVal = 0;
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
            m.invoke(o, (Object) null);
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
        FileInputStream f = new FileInputStream(bundle(rutaBundle) + File.separator + nombre);
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
        return ResourceBundle.getBundle("/BundleDisca").getString(s);
    }

    public static String bundleBDIntegrado(String s) {
        return ResourceBundle.getBundle("/BundleBDIntegrado").getString(s);
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
     * Obtiene los ultimos n años, para listas de selección
     *
     * @param n Ultimos años
     * @return Lista con los ultimos años.
     */
    public static SelectItem[] listaUltimosAnhos(int n) {
        int anho = Calendar.getInstance().get(Calendar.YEAR);
        SelectItem[] r = new SelectItem[n];
        for (int i = 0; i < n; i++) {
            r[i] = new SelectItem("" + (anho - i), "" + (anho - i));
        }
        return r;
    }

    /**
     * Convierte un Tiff de Oracle en JPG que se pueda visualizar via web
     *
     * @param b Array de bytes de la imagen
     * @param w Ancho de la imagen
     * @param h Alto de la imagen
     * @return
     */
    public static byte[] oracleTiffenJpg(byte[] b, int w, int h) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(b);
            Pipe pipeOut = new Pipe(null, os);
            Pipe pipeIn = new Pipe(is, null);
            ConvertCmd cmd = new ConvertCmd();
            cmd.setSearchPath(bundle("PathInstalacionImageMagick"));
            cmd.setOutputConsumer(pipeOut);
            cmd.setInputProvider(pipeIn);
            IMOperation op = new IMOperation();
            op.addImage("-");
            op.resize(w, h);
            op.interlace("line");
            op.addImage("jpg:-");
            cmd.run(op);
            byte[] r = os.toByteArray();
            os.close();
            is.close();
            return r;
        } catch (Exception ex) {
            mensajeAdvertencia("No es posible mostrar la imagen.");
//            ex.printStackTrace();
        }
        return null;
    }
    
    public static String dateToString(Date fecha, String formato) {
        SimpleDateFormat form = new SimpleDateFormat(formato);
        return form.format(fecha);
    }
    
    public static Map obtenerNombrePlantilaLicenciaGamac(boolean isFront) {
        Map nmap = new HashMap();

        if(isFront){
            nmap.put("nombreJas", "licenciaGamac.jasper");
            nmap.put("nombreRep", "licenciaGamac.pdf");
            nmap.put("nombreJasPath", "/m/pub/gamac/amaLicenciaUso/reportes/licenciaGamac.jasper");
        }else{
            nmap.put("nombreJas", "licenciaGamacBack.jasper");
            nmap.put("nombreRep", "licenciaGamacBack.pdf");
            nmap.put("nombreJasPath", "/m/pub/gamac/amaLicenciaUso/reportes/licenciaGamacBack.jasper");
        }

        return nmap;
    }
    
    
    public static StreamedContent generaPdfLicenciaGamac(Map licencia, String pathFoto, String gerente, boolean isFront) throws Exception {
        try {
            Map mapNombres = obtenerNombrePlantilaLicenciaGamac(isFront);
            ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
            JasperPrint jpTotal = new JasperPrint();
            List<AmaLicenciaDeUso> datos = new ArrayList();
            Float zoom = new Float(1.8);
            datos.add(new AmaLicenciaDeUso());
            HashMap parametros = parametrosPdfLicenciaGamac(licencia, gerente, isFront);

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
    
    private static HashMap parametrosPdfLicenciaGamac(Map item, String gerente, boolean isFront) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
        Date fechaEmision = (Date) item.get("FECHA_EMISION");
        InputStream is = null, is2 = null;

        if(isFront){
            is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg");
            is2 = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/avatar.jpeg");
            
            ph.put("P_FOTO_PERSONA", is);
            ph.put("P_FOTO_PERSONA_PEQ", is2);
        }       

        ph.put("P_FIRMA", ec.getRealPath("/resources/imagenes/firma_GAMAC.png"));
        ph.put("P_FONDO", ec.getRealPath("/resources/imagenes/fondoCMP.jpg"));
        ph.put("P_ANIO", formateador.format(fechaEmision));
        ph.put("P_qr", "https://www.sucamec.gob.pe/sel/faces/qr/cmp.xhtml?h=" + "");
        ph.put("P_FECHA_INICIO", formatoFechaDdMmYyyy(fechaEmision));
        ph.put("P_FECHA_FIN", formatoFechaDdMmYyyy((Date) item.get("FECHA_VENCIMIENTO")));
        ph.put("P_DOMICILIO", (item.get("DIRECCION") != null) ? item.get("DIRECCION").toString() : null);
        ph.put("P_DIST", (item.get("DISTRITO") != null) ? item.get("DISTRITO").toString() : null);
        ph.put("P_PROV", (item.get("PROVINCIA") != null) ? item.get("PROVINCIA") : null);
        ph.put("P_DPTO", (item.get("DEPARTAMENTO") != null) ? item.get("DEPARTAMENTO") : null);
        ph.put("P_MODALIDAD", (item.get("MODALIDAD") != null) ? item.get("MODALIDAD").toString() : "");
        ph.put("P_GERENTE", gerente);
        ph.put("P_NRO_EXPEDIENTE", item.get("NRO_EXPEDIENTE").toString());        
        ph.put("P_NUM_DOC", item.get("NUM_DOC").toString());
        ph.put("P_APEPAT", item.get("APE_PAT").toString());
        ph.put("P_APEMAT", (item.get("APE_MAT") != null)?item.get("APE_MAT").toString():"" );
        ph.put("P_NOMBRES", item.get("NOMBRES").toString());
        ph.put("P_NRO_LICENCIA", item.get("NRO_LICENCIA").toString());
        
        return ph;
    }
    
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
    
}
