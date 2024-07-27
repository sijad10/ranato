/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

import java.io.Serializable;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author mespinoza
 */
public class DocumentoTemp implements Serializable{
    
    private Long id;
    private String ruta;
    private UploadedFile upfile;

    public DocumentoTemp() {
        super();
    }

    public DocumentoTemp(Long id, String ruta, UploadedFile upfile) {
        this.id = id;
        this.ruta = ruta;
        this.upfile = upfile;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UploadedFile getUpfile() {
        return upfile;
    }

    public void setUpfile(UploadedFile upfile) {
        this.upfile = upfile;
    }
    
    
    
}
