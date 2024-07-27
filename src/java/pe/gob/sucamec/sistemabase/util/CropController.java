/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
// Nombre de la instancia en la aplicacion //
@Named("CropController")
// La instancia se crea al momento de iniciar una session http //
@SessionScoped
public class CropController implements Serializable {

    String archNombre;
    byte[] archImagen;

    private CroppedImage croppedImage;

    boolean hacerCrop = false, imagenOk = false;

    public String getUrlImagen() {
        String arch = "http://localhost:8080/sel2/faces/pub/tmp/" + archNombre;
        return arch;
    }

    public boolean isImagenOk() {
        return imagenOk;
    }

    public void setImagenOk(boolean imagenOk) {
        this.imagenOk = imagenOk;
    }

    public boolean isHacerCrop() {
        return hacerCrop;
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public void setHacerCrop(boolean hacerCrop) {
        this.hacerCrop = hacerCrop;
    }

    public void limpiar() {
        archImagen = null;
        archNombre = "";
        hacerCrop = false;
        imagenOk = false;
    }

    public void recortar() {
        if (croppedImage == null) {
            return;
        }
        try {
            archImagen = croppedImage.getBytes();
            imagenOk = true;
            hacerCrop = false;
            //FileUtils.deleteQuietly(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pub/tmp") + File.separator + archNombre));
            JsfUtil.mensaje("La imagen se recorto con éxito.");
        } catch (Exception e) {
            JsfUtil.mensajeError("Hubo un problema al recortar la imagen.");
        }
    }

    public StreamedContent getImagen() {
        try {
            return new DefaultStreamedContent(new ByteArrayInputStream(archImagen), "image/jpeg");
        } catch (Exception ex) {
            JsfUtil.mensajeError("Error: " + ex.getMessage());
        }
        return null;
    }

    public String comprobarJPG(InputStream is) {
        try {
            Iterator<ImageReader> r = ImageIO.getImageReadersByFormatName("jpg");
            while (r.hasNext()) {
                ImageReader ir = r.next();
                ImageInputStream iis = ImageIO.createImageInputStream(is);
                ir.setInput(iis);
                if (ir.getNumImages(true) == 1) {
                    if ((ir.getHeight(0) == 640) && (ir.getWidth(0) == 480)) {
                        return "OK";
                    } else {
                        return "Error Dimensiones";
                    }
                }
            }
        } catch (Exception ex) {
            // Si hay error no es un jpg //
        }
        return "No es un archivo JPG";
    }

    public void cargarArchivo(FileUploadEvent event) {
        if (event.getFile() == null) {
            return;
        }
        try {
            hacerCrop = imagenOk = false;
            UploadedFile f = event.getFile();
            BufferedImage i = ImageIO.read(f.getInputstream());
            int w = i.getWidth(), h = i.getHeight();
            if ((w != 480) || (h != 640)) {
                int w2 = 0, h2 = 0;
                if (w > h) {
                    w2 = 640 * w / h;
                    h2 = 640;
                } else {
                    w2 = 480;
                    h2 = 480 * h / w;
                }
                // Escalar la imagen con smooth para evitar pixeleo //
                BufferedImage i2 = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = i2.createGraphics();
                g.drawImage(i.getScaledInstance(w2, h2, Image.SCALE_SMOOTH), 0, 0, null);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.setColor(Color.LIGHT_GRAY);
                g.drawString("2\n0\n1\n5", 10, 80);
                g.dispose();
                // Ver si el ratio es correcto y quedarse con la imagen, sino hacer crop //
                if ((w2 == 480) && (h2 == 640)) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(i2, "jpg", os);
                    archImagen = os.toByteArray();
                    imagenOk = true;
                    JsfUtil.mensaje("El tamaño se cambió automaticamente.");
                    JsfUtil.mensaje("Archivo cargado");
                } else {
                    // Crop requiere un archivo - para produccion crear archivos aleatoreos y eliminarlos con un cron//
                    archNombre = "" + System.currentTimeMillis() + "_" + Math.round(Math.random() * 5) + ".jpg";
                    String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pub/tmp");
                    ImageIO.write(i2, "jpg", new File(path + File.separator + archNombre));
                    i2.flush();
                    hacerCrop = true;
                    JsfUtil.mensajeError("Error las dimensiones no son correctas, recortar la imagen.");
                }
            } else {
                imagenOk = true;
                archImagen = f.getContents();
                JsfUtil.mensaje("Archivo cargado");
            }
            i.flush();
        } catch (Exception ex) {
            JsfUtil.mensajeError("Error: " + ex.getMessage());
        }
    }

}
