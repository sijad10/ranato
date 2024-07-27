/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.jsf;

import java.io.File;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import pe.gob.sucamec.sel.jsf.util.JsfUtil;

/**
 * Servlet que brinda imágenes para el visualizador.
 *
 * @author rmoscoso
 */
@WebServlet("/qr/img/*")
public class VerificadorImagenServlet extends HttpServlet {

    /**
     * Función que envía las imágenes a la visualizador, los nombres son
     * encriptados para que no hackeen la lista de datos. Esta función debe
     * usarse para todos los validadores, solo hay que agregar paths.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String imagen = (String) request.getParameter("imagen");
            imagen = JsfUtil.desencriptar(imagen);
            String uri = request.getRequestURI();
            byte[] buffer;
            // Trabajar imagenes para RESGEPP, se acá se agregan mas tipos con paths //
            File f = null;
            if (uri.endsWith("/GEPP_RES")) {
                f = new File(JsfUtil.bundle("PathGeppResImg") + imagen);
            }
            if (uri.endsWith("/GAMAC_POL")) {
                f = new File(JsfUtil.bundle("PathGamacPolImg") + imagen);
            }
            if (uri.endsWith("/GSSP_INS")) {
                f = new File(JsfUtil.bundle("PathGsspInsImg") + imagen);
            }
            if (uri.endsWith("/GAMAC_RAESS")) {
                f = new File(JsfUtil.bundle("PathGamacResolucionArmaImg") + imagen);
            }
            if (uri.endsWith("/GAMAC_GTRAN")) {
                f = new File(JsfUtil.bundle("PathGamacAmaImg") + imagen);
            }
            if (uri.endsWith("/GAMAC_ACTA")) {
                f = new File(JsfUtil.bundle("PathGamacActaImg") + imagen);
            }
            if (uri.endsWith("/GAMAC_CONST_VAL")) {
                f = new File(JsfUtil.bundle("PathConstanciaValImg") + imagen);
            }
            if (uri.endsWith("/GAMAC_CONSTLICTAR")) {
                f = new File(JsfUtil.bundle("PathConstanciaLicTarImg") + imagen);
            }
            if (uri.endsWith("/GSSP_CCLI")) {
                f = new File(JsfUtil.bundle("PathGsspConstanciaCarteraCImg") + imagen);
            }
            if (uri.endsWith("/GSSP_CURSO")) {
                f = new File(JsfUtil.bundle("PathGsspConstanciaCursosImg") + imagen);
            }
            if (uri.endsWith("/GSSP_CONST")) {
                f = new File(JsfUtil.bundle("PathGsspReportesConstanciasImg") + imagen);
            }
            if (uri.endsWith("/GSSP_CRNDIG")) {
                f = new File(JsfUtil.bundle("PathGsspReportesCarnesGsspDigitalImg") + imagen);
            }
            if (uri.endsWith("/GEPP_CMED")) {
                String pathImg=JsfUtil.bundle("PathGeppCmedImg")+ imagen;
                f = new File(pathImg);
                response.setCharacterEncoding("ISO-8859-1");
            }
            if (uri.endsWith("/GEPP_CMPPD")) {
                String pathImg=JsfUtil.bundle("PathGeppCmppdImg")+ imagen;
                f = new File(pathImg);
                response.setCharacterEncoding("ISO-8859-1");
            }
            //Constancias de verificación de datos de armas de fuego 
            if (uri.endsWith("/GAMAC_CONST_VER")) {
                f = new File(JsfUtil.bundle("PathConstanciaVerImg") + imagen);
            }
            //Examen de poligono de Tiro
            if (uri.endsWith("/GAMAC_EXAMEN_POLI")) {
                f = new File(JsfUtil.bundle("PathConstanciaExaPoliImg") + imagen);
            }
            if (f != null && f.exists() && f.isFile()) {
                buffer = FileUtils.readFileToByteArray(f);
                response.setContentType("image/png");
                response.getOutputStream().write(buffer);
                response.flushBuffer();
            }
            return;
        } catch (Exception ex) {
        }
        response.setContentType("text/plain");
        response.getWriter().print("Ha ocurrido un error al tratar de generar la imagen");
        response.flushBuffer();
    }
}
