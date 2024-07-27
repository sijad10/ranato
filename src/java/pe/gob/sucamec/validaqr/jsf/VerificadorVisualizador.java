/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.jsf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import javax.faces.context.FacesContext;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.sel.jsf.util.JsfUtil;

/**
 * Las funciones de este visualizador convierten los documentos en PNG altamente
 * comprimidos, esto permite a todos los dispositivos incluyendo tablets y
 * moviles ver los documentos, como se haría en google. Sustituye al
 * DocumentViewer de PF que carga mucho con el JS a los equipos pequeños y a los
 * datos móviles.
 *
 * @author rmoscoso
 */
public class VerificadorVisualizador {

    boolean verDocumento;
    ArrayList imagenes;
    String textOpcional;

    String pathPdf = "",
            imgmkPath = JsfUtil.bundle("PathImgMk"),
            selloVer = JsfUtil.bundle("SelloVerificador"),
            pathImg = "";

    public enum Orientacion {
        HORIZONTAL, VERTICAL
    };

    public Orientacion orientacion;

    public VerificadorVisualizador(String pathPdf, String pathImg, Orientacion o) {
        verDocumento = false;
        this.pathPdf = pathPdf;
        this.pathImg = pathImg;
        orientacion = o;
    }

    public boolean isVerDocumento() {
        return verDocumento;
    }

    public void setVerDocumento(boolean verDocumento) {
        this.verDocumento = verDocumento;
    }

    /**
     * Funcion para obtener el tiempo en milisegundos, se usa en el URL para
     * evitar el cache del navegador.
     *
     * @return Tiempo en milisegundos del sistema.
     */
    public String getMillis() {
        return "" + System.currentTimeMillis();
    }

    public ArrayList getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList imagenes) {
        this.imagenes = imagenes;
    }

    /**
     * Funcion para descargar PDF, no se va a utilizar actualmente
     *
     * @return
     */
    public StreamedContent descargarPdf() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String arch = (String) context.getExternalContext().getRequestParameterMap().get("archivoPdf");
            FileInputStream f = new FileInputStream(pathPdf + arch);
            return new DefaultStreamedContent(f, "application/pdf", arch);
        } catch (Exception ex) {
            return JsfUtil.errorDescarga("Error:ValidaQrController.descargarPdf: El archivo no existe :", ex);
        }
    }

    /**
     * Convierte un pdf en png con ImageMagik, y le pone un sello de agua. Si la
     * imagen existe solo la regenera cuando el archivo pdf es mas reciente,
     * teniendo una especie de cache.
     *
     * @param pdf Nombre del archivo pdf
     * @param png Nombre del png de destino, si el pdf tiene multiples páginas
     * va a generar el nombre seguido de -[0..n].png
     * @param ultimaMod Fecha con la ultima moficacón del pdf en milisegundos.
     * @return
     */
    public boolean convertirPdfEnPng(String pdf, String png, long ultimaMod) {
        try {
            // Verificar Archivos de una pagina //
            File aPng = new File(png);
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            // Verificar Archivos de varias paginas //
            aPng = new File(png.replace(".png", "-0.png"));
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;    
            }
            ConvertCmd cmd = new ConvertCmd();
            //cmd.setSearchPath(imgmkPath); asigna ruta del programa imagemagick; se comenta para usar la configuracion del path del sistema operativo
            IMOperation op = new IMOperation();
            // El orden importa para que las operaciones funcionen OK.
            if (orientacion == Orientacion.VERTICAL) {
                op.density(115, 115); // Resolucion 115 dpi para vertical y 82 para horizontal
            } else {
                op.density(82, 82); // Resolucion 115 dpi para vertical y 82 para horizontal
            }
            op.gravity("NorthWest"); // Posición inicia arriba izquierda
            op.alpha("Opaque"); // Que la transparencia este ok
            op.background("#FFFFFF"); // Evitar fondos negros o transparentes
            op.fill("#FFFFFF"); // Evitar fondos negros o transparentes
            op.transparentColor("#FFFFFF"); // Evitar fondos negros o transparentes
            if (orientacion == Orientacion.VERTICAL) {
                op.draw("image over 150,300,0,0 '" + selloVer + "'"); // Sello de agua 120,300 Vertical y 150, 0 Horizontal
            } else {
                op.draw("image over 150,10,0,0 '" + selloVer + "'"); // Sello de agua 120,300 Vertical y 150, 0 Horizontal
            }
            op.addImage(pdf); // Archivo pdf de origen 1ro
            op.depth(4); // Colores 2^4 = 16 colores.
            op.quality(0d); // Maxima compresion en png
            op.type("Grayscale"); // Que sea escala de grises puro
            op.addImage(png); // Archivo png de destino 2do
            cmd.run(op);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean convertirPdfEnPngColor(String pdf, String png, long ultimaMod) {
        try {
            // Verificar Archivos de una pagina //
            File aPng = new File(png);
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            // Verificar Archivos de varias paginas //
            aPng = new File(png.replace(".png", "-0.png"));
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            ConvertCmd cmd = new ConvertCmd();
            //cmd.setSearchPath(imgmkPath);
            IMOperation op = new IMOperation();
            // El orden importa para que las operaciones funcionen OK.
            if (orientacion == Orientacion.VERTICAL) {
                op.density(115, 115); // Resolucion 115 dpi para vertical y 82 para horizontal
            } else {
                op.density(82, 82); // Resolucion 115 dpi para vertical y 82 para horizontal
            }
            op.gravity("NorthWest"); // Posición inicia arriba izquierda
            op.alpha("Opaque"); // Que la transparencia este ok
            op.background("#FFFFFF"); // Evitar fondos negros o transparentes
            op.fill("#FFFFFF"); // Evitar fondos negros o transparentes
            op.transparentColor("#FFFFFF"); // Evitar fondos negros o transparentes
            if (orientacion == Orientacion.VERTICAL) {
                op.draw("image over 150,300,0,0 '" + selloVer + "'"); // Sello de agua 120,300 Vertical y 150, 0 Horizontal
            } else {
                op.draw("image over 150,10,0,0 '" + selloVer + "'"); // Sello de agua 120,300 Vertical y 150, 0 Horizontal
            }
            op.addImage(pdf); // Archivo pdf de origen 1ro
            op.depth(4); // Colores 2^4 = 16 colores.
            op.quality(0d); // Maxima compresion en png
            //op.type("Grayscale"); // Que sea escala de grises puro
            op.addImage(png); // Archivo png de destino 2do
            cmd.run(op);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean convertirPdfEnPng_sinSello(String pdf, String png, long ultimaMod) {
        try {
            // Verificar Archivos de una pagina //
            File aPng = new File(png);
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            // Verificar Archivos de varias paginas //
            aPng = new File(png.replace(".png", "-0.png"));
            if (aPng.exists() && aPng.lastModified() > ultimaMod) {
                return true;
            }
            ConvertCmd cmd = new ConvertCmd();
            cmd.setSearchPath(imgmkPath);
            IMOperation op = new IMOperation();
            // El orden importa para que las operaciones funcionen OK.
            if (orientacion == Orientacion.VERTICAL) {
                op.density(115, 115); // Resolucion 115 dpi para vertical y 82 para horizontal
            } else {
                op.density(82, 82); // Resolucion 115 dpi para vertical y 82 para horizontal
            }
            op.gravity("NorthWest"); // Posición inicia arriba izquierda
            op.alpha("Opaque"); // Que la transparencia este ok
            op.background("#FFFFFF"); // Evitar fondos negros o transparentes
            op.fill("#FFFFFF"); // Evitar fondos negros o transparentes
            op.transparentColor("#FFFFFF"); // Evitar fondos negros o transparentes
            op.addImage(pdf); // Archivo pdf de origen 1ro
            op.depth(4); // Colores 2^4 = 16 colores.
            op.quality(0d); // Maxima compresion en png
            op.type("Grayscale"); // Que sea escala de grises puro
            op.addImage(png); // Archivo png de destino 2do
            cmd.run(op);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Filtra archivos con las imágenes del pdf
     */
    private class FiltroArchivos implements FilenameFilter {

        String nombre = "";

        public FiltroArchivos(String nombre) {
            this.nombre = nombre.replace(".pdf", "");
        }
        
        public FiltroArchivos(String nombre, boolean mayusculas) {
            this.nombre = nombre.replace(".PDF", "");
        }

        @Override
        public boolean accept(File dir, String name) {
            return (name.startsWith(nombre + "-") || name.startsWith(nombre + ".")) && name.endsWith(".png");
        }

    }

    public void generarArchivos(String id, String idDinamico, boolean pathDinamico, String id2, String idDinamico2, boolean pathDinamico2) throws Exception {
        String arch = id + ".pdf", img = id + ".png";
        if(pathDinamico){
            img = idDinamico + ".png";
        }
        File f;
        f = new File(pathPdf + arch);
        File dir;
        //System.out.println("pathPdf: "  + pathPdf + arch + " pathImg: " + pathImg + img);
        imagenes = null;
        //El primer nombre del archivo
        if (f.exists()) {
            if (convertirPdfEnPng(pathPdf + arch, pathImg + img, f.lastModified())) {
                dir = new File(pathImg);
                if(pathDinamico){
                    arch = idDinamico + ".pdf";
                }
                FiltroArchivos fa = new FiltroArchivos(arch);
                String[] l = dir.list(fa);
                Arrays.sort(l);
                for (int i = 0; i < l.length; i++) {
                    l[i] = URLEncoder.encode(JsfUtil.encriptar(l[i]), "UTF-8");
                }
                verDocumento = true;
                imagenes = new ArrayList(Arrays.asList(l));
            }
        }
        
        if (!idDinamico2.equals("")) {
            arch = id2 + ".pdf";
            img = id2 + ".png";
            if(pathDinamico2){
                img = idDinamico2 + ".png";
            }
            f = new File(pathPdf + arch);
                //La segunda forma de buscar el nombre del archivo
                if (f.exists()) {
                    if (convertirPdfEnPng(pathPdf + arch, pathImg + img, f.lastModified())) {
                        dir = new File(pathImg);
                        if(pathDinamico){
                            arch = idDinamico + ".pdf";
                        }
                        FiltroArchivos fa = new FiltroArchivos(arch);
                        String[] l = dir.list(fa);
                        Arrays.sort(l);
                        for (int i = 0; i < l.length; i++) {
                            l[i] = URLEncoder.encode(JsfUtil.encriptar(l[i]), "UTF-8");
                        }
                        verDocumento = true;
                        imagenes = new ArrayList(Arrays.asList(l));
                    }
                }
        }
    }
    
    public void generarArchivos_sinSelloAgua(String id, String idDinamico, boolean pathDinamico) throws Exception {
        String arch = id + ".pdf", img = id + ".png";
        if(pathDinamico){
            img = idDinamico + ".png";
        }
        File f = new File(pathPdf + arch);
        imagenes = null;
        if (f.exists() && convertirPdfEnPng_sinSello(pathPdf + arch, pathImg + img, f.lastModified())) {
            File dir = new File(pathImg);
            if(pathDinamico){
                arch = idDinamico + ".pdf";
            }
            FiltroArchivos fa = new FiltroArchivos(arch);
            String[] l = dir.list(fa);
            Arrays.sort(l);
            for (int i = 0; i < l.length; i++) {
                l[i] = URLEncoder.encode(JsfUtil.encriptar(l[i]), "UTF-8");
            }
            verDocumento = true;
            imagenes = new ArrayList(Arrays.asList(l));
        }
    }
    
    public void generarArchivosReportesGssp(String id) throws Exception {
        String arch = id + ".PDF", img = id + ".png";
        
        File f = new File(pathPdf + arch);
        imagenes = null;
        if (f.exists() && convertirPdfEnPng(pathPdf + arch, pathImg + img, f.lastModified())) {
            File dir = new File(pathImg);
            FiltroArchivos fa = new FiltroArchivos(arch, true);
            String[] l = dir.list(fa);
            Arrays.sort(l);
            for (int i = 0; i < l.length; i++) {
                l[i] = URLEncoder.encode(JsfUtil.encriptar(l[i]), "UTF-8");
            }
            verDocumento = true;
            imagenes = new ArrayList(Arrays.asList(l));
        }
    }

    public String getTextOpcional() {
        return textOpcional;
    }

    public void setTextOpcional(String textOpcional) {
        this.textOpcional = textOpcional;
    }

    public void generarArchivosCarneDigital(String id) throws Exception {
        String arch = id + ".pdf", img = id + ".png";
        File f = new File(pathPdf + arch);
        imagenes = null;
        if (f.exists() && convertirPdfEnPngColor(pathPdf + arch, pathImg + img, f.lastModified())) {
            File dir = new File(pathImg);
            FiltroArchivos fa = new FiltroArchivos(arch);
            String[] l = dir.list(fa);
            Arrays.sort(l);
            for (int i = 0; i < l.length; i++) {
                l[i] = URLEncoder.encode(JsfUtil.encriptar(l[i]), "UTF-8");
            }
            verDocumento = true;
            imagenes = new ArrayList(Arrays.asList(l));
        }
    }

}
