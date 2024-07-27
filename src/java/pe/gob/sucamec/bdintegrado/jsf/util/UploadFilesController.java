/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Michel
 */
@Named("uploadFilesController")
// La instancia se crea al momento de iniciar una session http //
@SessionScoped
public class UploadFilesController implements Serializable {

    // private final String pathUpload = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_pathUpload");
    private final String pathUploadDocu = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_pathUpload_documento");
    private final String pathUploadTemp = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_path_upload_temp");
    private final String pathUploadFoto = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_pathUpload_foto");
    private final String pathUploadReso = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_pathUpload_resolucion");
    // private final String pathDownload = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_pathDownload");
    //  private final String pathAppDownload = ResourceBundle.getBundle("/BundleGeppGte").getString("Documentos_pathAppDownload");

//    private final String pathUpload = "/opt/glassfish4/glassfish/domains/domain1/applications/NotificacionApp/resources/files/uploads/";
//    private final String pathDownload = "/opt/glassfish4/glassfish/domains/domain1/applications/NotificacionApp/resources/files/downloads/";
//    private final String pathAppDownload = "/resources/files/upload/";
    private StreamedContent file;

    /**
     * @return the file
     */
    public StreamedContent getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public boolean uploadDocumento(UploadedFile file, String newfilename, boolean temporal) {
        boolean res = false;
        try {
            res = copyFileUploadDocumento(newfilename, file.getInputstream(),(temporal?pathUploadTemp:pathUploadDocu));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public boolean copyFileUploadDocumento(String fileName, InputStream in, String path) {
        boolean res = false;
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(path + fileName));
            int read = 0;

            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            res = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public void FileDownload(String fileName) {
        try {
            InputStream stream = new FileInputStream(pathUploadDocu + fileName);
            file = new DefaultStreamedContent(stream, "application/pdf", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findFiles(String p_file_name) {
        File[] filesInDirectory = new File(JsfUtil.bundle("Documentos_pathUpload_resolucion")).listFiles();
        for (File f : filesInDirectory) {
            if (f.isFile()) {
                if (f.getName().equals(p_file_name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean findFilesGt(String p_file_name) {
        File[] filesInDirectory = new File(JsfUtil.bundleGt("Documentos_pathUpload_documento")).listFiles();
        for (File f : filesInDirectory) {
            if (f.isFile()) {
                if (f.getName().startsWith(p_file_name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean renameFile(String oldFile, String newFile) {
        File f = null;
        File f1 = null;
        boolean bool = false;

        try {
            // create new File objects
            f = new File(JsfUtil.bundle("Documentos_pathUpload_resolucion") + oldFile);
            f1 = new File(JsfUtil.bundle("Documentos_pathUpload_resolucion") + newFile);

            // rename file
            bool = f.renameTo(f1);

            // print
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    public void PdfDownload(String fileName) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            try {
                InputStream in = new FileInputStream(pathUploadReso + fileName);
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
//            JsfUtil.showRedirectHtmlError("No se encontró el archivo: <font color=\"red\">"+fileName+"</font> en la ruta especificada, debe revisar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StreamedContent FotoDownload(String fileName) {
        try {
            StreamedContent fotoFile;
            InputStream stream = new FileInputStream(pathUploadFoto + fileName);
            //InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(pathAppDownload + fileName);
            fotoFile = new DefaultStreamedContent(stream, "image/jpeg", fileName);
            return fotoFile;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public boolean uploadFoto(UploadedFile file, String newfilename) {
        boolean res = false;
        try {
            res = copyFileUploadFoto(newfilename, file.getInputstream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    public boolean copyFileUploadFoto(String fileName, InputStream in) {
        boolean res = false;
        try {
            // write the inputStream to a FileOutputStream
            OutputStream out = new FileOutputStream(new File(pathUploadFoto + fileName));
            int read = 0;

            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            res = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }

    }
    
    public boolean moveFile(String oldFile, String newFile) {
        File f = null;
        File f1 = null;
        boolean bool = false;

        try {
            // create new File objects
            f = new File(pathUploadTemp + oldFile);
            f1 = new File(pathUploadDocu + newFile);

            // rename file
            bool = f.renameTo(f1);

            // print
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
    
    public boolean moveFilePath(String pathOrigen, String pathDestino, String oldFile, String newFile) {
        File f = null;
        File f1 = null;
        boolean bool = false;

        try {
            // create new File objects
            f = new File(pathOrigen + oldFile);
            f1 = new File(pathDestino + newFile);

            // rename file
            bool = f.renameTo(f1);
            System.err.println("Can read:"+ f.canRead());
            System.err.println("Can write:"+ f.canWrite());
            System.err.println("Can execute:"+ f.canExecute());
            // print
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
    
    public boolean renameFilePath(String path, String oldFile, String newFile) {
        File f = null;
        File f1 = null;
        boolean bool = false;

        try {
            // create new File objects
            f = new File(path + oldFile);
            f1 = new File(path + newFile);

            // rename file
            bool = f.renameTo(f1);

            // print
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

}
