/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.jsf.util;

/**
 *
 * @author rfernandezv
 */
public class TableDynamic {
    private String header;
    private String property;
    private boolean exportable;
    private String width;

    public TableDynamic(String header, String property) {
        this.header = header;
        this.property = property;
    }
    
    public TableDynamic(String header, String property, boolean exportable) {
        this.header = header;
        this.property = property;
        this.exportable = exportable;
    }
    
    public TableDynamic(String header, String property, boolean exportable, String width) {
        this.header = header;
        this.property = property;
        this.exportable = exportable;
        this.width = width;
    }   
    
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean isExportable() {
        return exportable;
    }

    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
    
    
}
