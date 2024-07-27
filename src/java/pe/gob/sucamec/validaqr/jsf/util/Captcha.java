/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.jsf.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author rmoscoso
 */
public class Captcha {

    private String texto;
    private String textoInput;
    private byte[] imagen;

    public Captcha() {
        generarCaptcha();
    }

    /**
     * Compara si el texto del input es igual al de la imagen
     *
     * @return true si es igual
     */
    public boolean ok() {
        boolean r = texto.equals(textoInput);
        generarCaptcha();
        return r;
    }

    /**
     * Propiedad de solo escritura donde se guarda el texto de un input para
     * compararlo con el de la imagen
     *
     * @return Siempre un texto vacío
     */
    public String getTextoInput() {
        return "";
    }

    /**
     * Propiedad de solo escritura donde se guarda el texto de un input para
     * compararlo con el de la imagen
     *
     * @param s texto a comparar con el texto de la imagen
     */
    public void setTextoInput(String s) {
        textoInput = s.toUpperCase();
    }

    /**
     * Obtiene el texto de la imagen
     *
     * @return Texto de la imagen
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Obtiene un array de bytes que representa a la imagen en PNG
     *
     * @return un array de bytes
     */
    public byte[] getBytesImagen() {
        return imagen;
    }

    /**
     * Devuelve un Streamed Content para mostrar la imagen en un navegador
     *
     * @return Stream de la imagen en PNG
     */
    public DefaultStreamedContent getImagen() {
        return new DefaultStreamedContent(new ByteArrayInputStream(imagen), "image/png");
    }

    /**
     * Genera números aleatoreos para la generacion de la imagen
     *
     * @param max entero maximo
     * @return numero aleatoreo
     */
    private int random(int max) {
        int i = (int) Math.round(Math.random() * max);
        return i;
    }

    /**
     * Genera la imagen del captcha, basicamente selecciona 5 letras al azar, se
     * obviaron la O y el cero para evitar confusiones; dibuja la imagen en gris
     * oscuro con fondo gris, distorsiona la imagen moviendola de izquierda a
     * derecha y genera un grupo de polígonos supersupestos con XOR sobre las
     * letras. La imagen queda almacenada en un array de bytes y el texto en un
     * String.
     */
    public final void generarCaptcha() {
        try {
            textoInput = "";
            texto = "";
            int i;
            // Generar texto //
            final String letras = "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
            final int ll = letras.length() - 1;
            for (i = 0; i < 5; i++) {
                int r = random(ll);
                texto += letras.substring(r, r + 1);
            }
            // Definir colores aleatoreos oscuros //
            Color[] colores = new Color[50];
            for (i = 0; i < colores.length; i++) {
                colores[i] = new Color(random(128), random(128), random(128));
            }
            // Crear Imagen //
            BufferedImage bi = new BufferedImage(200, 50, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bi.createGraphics();
            //AffineTransform at = g2.getTransform();
            g2.setBackground(Color.lightGray);
            g2.clearRect(0, 0, bi.getWidth(), bi.getHeight());
            // Dibujar Texto
            Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
            g2.setFont(f);
            g2.setColor(new Color(120, 120, 120));
            for (i = 0; i < texto.length(); i++) {
                g2.drawString(texto.substring(i, i + 1), 10 + (i * 35) + random(6), 34 + random(6));
            }
            // Deformar Texto
            int x = 0;
            //int xx = random (2);
            for (int y = 0; y < 50; y++) {
                x += random(2) - 1;
                BufferedImage bi2 = bi.getSubimage(0, y, 200, 1);
                g2.drawImage(bi2, null, x, y);
            }
            // Titulo
            f = new Font(Font.SANS_SERIF, Font.BOLD, 10);
            g2.setColor(new Color(100, 100, 100));
            g2.setFont(f);
            g2.drawString("Captcha", 160, 47);
            // Cambiar Imagen con colores
            g2.setXORMode(Color.black);
            for (i = 0; i < 60; i++) {
                g2.setColor(colores[random(49)]);
                g2.fillRect(random(150), random(40), 40 + random(40), 40 + random(40));
            }
            // Devolver un PNG //
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", os);
            imagen = os.toByteArray();
            //Liberar recursos //
            g2.dispose();
            bi.flush();
            return;
        } catch (Exception ex) {
        }
        imagen = null;
    }

}
