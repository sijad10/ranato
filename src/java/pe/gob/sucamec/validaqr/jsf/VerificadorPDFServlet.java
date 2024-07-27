/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.validaqr.jsf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import pe.gob.sucamec.bdintegrado.bean.SbParametroFacade;

/**
 *
 * @author vverastegui
 */
//@WebServlet(name = "VerificadorPDFServlet", urlPatterns = {"/VerificadorPDFServlet"})
@WebServlet("/qr/pdf/*")
public class VerificadorPDFServlet extends HttpServlet {

    @EJB
    private SbParametroFacade ejbSbParametroFacade;
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String uri = request.getRequestURI();
            byte[] buffer;
            File f = null;
            if(uri.endsWith("/NOTQR")){
                String nomArchivo = (String) request.getParameter("n");
                //String rutaPath = ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_PATH_1NOTIFQR",1L).getValor();
                String rutaPath = ejbSbParametroFacade.obtenerParametroXNombre("PathUpload").getValor();
                //System.out.println("Servlet:" + rutaPath + nomArchivo);
                InputStream in = new FileInputStream(rutaPath + nomArchivo);
                BufferedInputStream input = null;
                BufferedOutputStream output = null;
                f = new File(rutaPath + nomArchivo);

                if (f != null && f.exists() && f.isFile()) {
                    buffer = FileUtils.readFileToByteArray(f);
                    response.setContentType("application/pdf");
                    response.setContentLength(buffer.length);
                    response.getOutputStream().write(buffer);
                    response.flushBuffer();
                }
                return;  
            }
        } catch (Exception ex) {
        }
        response.setContentType("text/plain");
        response.getWriter().print("Ha ocurrido un error al tratar de generar el archivo");
        response.flushBuffer();
        //byte[] content = (byte[]) request.getSession().getAttribute("reportBytes");
        //response.setContentType("application/pdf");
        //response.setContentLength(content.length);
        //response.getOutputStream().write(content);
    }

    
}
