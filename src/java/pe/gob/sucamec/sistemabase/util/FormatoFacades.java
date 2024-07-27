/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.io.*;

/**
 *
 * @author Renato
 */
public class FormatoFacades {

    /**
     * @param args the command line arguments
     */
    Scanner in = new Scanner(System.in);
    String datos = "";

    String path = "C:\\Users\\rmoscoso\\Documents\\NetBeansProjects\\sel\\src\\java\\pe\\gob\\sucamec\\epp\\beans\\", nl = "\r\n",
            path2 = "C:\\Users\\rmoscoso\\Documents\\NetBeansProjects\\sel\\src\\java\\pe\\gob\\sucamec\\epp\\beans\\";

    /*
     String path = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\CarnetExp\\src\\java\\pe\\gob\\sucamec\\migracion\\data\\", nl = "\r\n",
     path2 = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\CarnetExp\\src\\java\\pe\\gob\\sucamec\\migracion\\data\\";
     /* 
     String path = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\SucamecBase\\src\\java\\pe\\gob\\sucamec\\explosivos\\data\\", nl = "\r\n",
     path2 = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\SucamecBase\\src\\java\\pe\\gob\\sucamec\\explosivos\\data\\";
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new FormatoFacades().ejecutar();
    }

    public void println(String s) {
    }

    public String obtenerEntidad() {
        int p1 = datos.indexOf("AbstractFacade<") + 15, p2 = datos.indexOf(">", p1);
        String s = datos.substring(p1, p2).replace(" ", "");
        return s;
    }

    public void ejecutar() {
        try {
            println("Formato de Entidades...");
            String[] t = {"java"};
            ArrayList<File> fs = new ArrayList(FileUtils.listFiles(new File(path), t, false));
            for (File f : fs) {
                String archivo = f.getName();
                datos = FileUtils.readFileToString(f);
                println("Analizando: " + archivo);
                if (datos.contains("AbstractFacade<T>")) {
                    if (!datos.contains("MAX_RES")) {
                        datos = datos.replace(
                                "private Class<T> entityClass;",
                                "private Class<T> entityClass;" + nl
                                + "    public final int MAX_RES = 500;" + nl);
                    }
                    println("OK");
                    FileUtils.writeStringToFile(new File(path2 + archivo), datos);
                } else if (datos.contains("AbstractFacade") && !datos.contains("selectLike")) {
                    String entidad = obtenerEntidad(), epl = entidad.substring(0, 1).toLowerCase();
                    // Agregar Select Like //
                    datos = datos.replace(
                            nl + "}" + nl,
                            "public List<" + entidad + "> selectLike(String s) {\n"
                            + "        if (s == null) {\n"
                            + "            s = \"\";\n"
                            + "        }\n"
                            + "        Query q = em.createQuery(\"select " + epl + " from " + entidad + " " + epl + " where trim(" + epl + ".id) like :id\");\n"
                            + "        q.setParameter(\"id\", \"%\" + s + \"%\");\n"
                            + "        q.setMaxResults(MAX_RES);\n"
                            + "        q.setHint(\"eclipselink.batch.type\", \"IN\");\n"
                            + "        return q.getResultList();\n"
                            + "    }\n"
                            + ""
                            + "}" + nl);
                    // Arreglar includes //
                    datos = datos.replace(
                            "import javax.ejb.Stateless;",
                            "import java.util.List;\n"
                            + "import javax.persistence.Query;\n"
                            + "import javax.ejb.Stateless;"
                    );
                    println("OK");
                    FileUtils.writeStringToFile(new File(path2 + archivo), datos);
                    //   println(datos);
                } else {
                    println("Archivo ignorado");
                }
            }
            println("Fin");
        } catch (Exception ex) {
            println("Error: " + ex.getMessage());
        }
    }
}
