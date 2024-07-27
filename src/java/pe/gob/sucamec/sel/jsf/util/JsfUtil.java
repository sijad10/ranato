package pe.gob.sucamec.sel.jsf.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 * Clase con funciones utiles para usar JSF
 */
public class JsfUtil {

    /**
     * Muestra errores cuando se trata de descargas y no se puede utilizar un Mensaje JSF en el Growl.
     *
     * @param mensaje
     * @param ex
     * @return
     */
    public static StreamedContent errorDescarga(String mensaje, Exception ex) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        String s = "<html><body style='font-family: Arial;'><center>"
                + "<br><br><br>"
                + "<img src='" + ec.getRequestContextPath() + "/resources/imagenes/logo_sucamec.png" + "'>"
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
     * Llave para encriptar los datos: 128 bits
     */
    private static final String llaveEnc = "!QazSuc$%LKHGasn";

    /**
     * Función para encriptar cadenas de texto.
     *
     * @param s texto a desencriptar.
     * @return texto desencriptado.
     */
    public static String desencriptar(String s) {
        try {
            // Create key and cipher
            Key aesKey = new SecretKeySpec(llaveEnc.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] encriptado = Base64.decodeBase64(s);
            String desencriptado = new String(cipher.doFinal(encriptado));
            return desencriptado;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Función para encriptar cadenas de texto.
     *
     * @param s Texto a encriptar.
     * @return Texto encriptado.
     */
    public static String encriptar(String s) {
        try {
            // Create key and cipher
            Key aesKey = new SecretKeySpec(llaveEnc.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encriptado = cipher.doFinal(s.getBytes());
            return new String(Base64.encodeBase64(encriptado));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * Lista de errores personalizados para el ORACLE
     */
    private static String[][] listaErrores = {
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
     * Permite mostrar un mensaje de error en <h: messages>
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
     * Permite agregar varios mensajes de error en forma simultanea en <h: messages>
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
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    /**
     * Permite mostrar un mensaje informativo de una accion ejecutada con éxito en <h: messages>
     *
     * @param msg El mensaje exitoso a mostrar.
     */
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    /**
     * Permite obtener texto del archivo Bundle
     *
     * @param s Nombre del text
     * @return El texto asociado al nombre
     */
    public static String bundle(String s) {
        return ResourceBundle.getBundle("/BundleSistemaBase").getString(s);
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
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
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
            email.setHostName(JsfUtil.bundle("Correo_Servidor"));
            email.setSmtpPort(25);
            email.setFrom(JsfUtil.bundle("Correo_Cuenta"));
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
    
    public static String formatoFechaHhMmSs(Date param) {
        if (param != null) {            
            SimpleDateFormat formatHora = new SimpleDateFormat("HH", new Locale("ES"));
            SimpleDateFormat formatMin = new SimpleDateFormat("mm", new Locale("ES"));
            SimpleDateFormat formatSeg = new SimpleDateFormat("ss", new Locale("ES"));
            String h = formatHora.format(param);
            String m = formatMin.format(param);
            String s = formatSeg.format(param);

            return h +":"+ m+":" + s;
        } else {
            return "BORRADOR";
        }
    }
    
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
    
    public static String dateToString(Date fecha, String formato) {
        SimpleDateFormat form = new SimpleDateFormat(formato);
        return form.format(fecha);
    }
    
}
