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
public class FormatoEntidades {

    /**
     * @param args the command line arguments
     */
    Scanner in = new Scanner(System.in);
    String datos = "";

    String path = "C:\\Users\\rmoscoso\\Documents\\NetBeansProjects\\sel\\src\\java\\pe\\gob\\sucamec\\epp\\data\\", nl = "\r\n",
            path2 = "C:\\Users\\rmoscoso\\Documents\\NetBeansProjects\\sel\\src\\java\\pe\\gob\\sucamec\\epp\\data\\";

    /*
     String path = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\CarnetExp\\src\\java\\pe\\gob\\sucamec\\migracion\\data\\", nl = "\r\n",
     path2 = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\CarnetExp\\src\\java\\pe\\gob\\sucamec\\migracion\\data\\";
     /* 
     String path = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\SucamecBase\\src\\java\\pe\\gob\\sucamec\\explosivos\\data\\", nl = "\r\n",
     path2 = "C:\\Users\\Renato\\Documents\\NetBeansProjects\\SucamecBase\\src\\java\\pe\\gob\\sucamec\\explosivos\\data\\";
     */
   public static void main(String[] args) {
        // TODO code application logic here
        new FormatoEntidades().ejecutar();
    }

    public void println(String s) {
    }

    public String obtenerTabla() {
        int p1 = datos.indexOf("@Table("), p2 = datos.indexOf(nl, p1);
        String s = datos.substring(p1, p2).replace(" ", "");
        p1 = s.indexOf("name=\"") + 6;
        p2 = s.indexOf("\"", p1);
        return s.substring(p1, p2);
    }

    public String obtenerEsquema() {
        int p1 = datos.indexOf("@Table("), p2 = datos.indexOf(nl, p1);
        String s = datos.substring(p1, p2).replace(" ", "");
        p1 = s.indexOf("schema=\"") + 8;
        p2 = s.indexOf("\"", p1);
        return s.substring(p1, p2);
    }

    public void limpiarNotNull(String campo) {
        int p1 = datos.indexOf(campo);
        if (p1 == -1) {
            println("Sin auditoria");
            return;
        }
        p1 = datos.indexOf("@NotNull", p1 - 110);
        if (p1 == -1) {
            println("Auditoria Nula");
            return;
        }
        String s = datos.substring(p1 + 8);
        s = s.substring(s.indexOf("@"));
        datos = datos.substring(0, p1) + s;
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
                if (datos.contains("@Column(name = \"ID\")" + nl) && !datos.contains("@Customizer")) {
                    String tabla = obtenerTabla(), secuencia = "SEQ_" + tabla,
                            esquema = obtenerEsquema();
                    // Agregar Customizer //
                    datos = datos.replace(
                            "@Table(",
                            "@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min."+ nl +
                            "@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD"
                            + nl + "@Table(");
                    // Ver imports //
                    datos = datos.replace(
                            "import java.io.Serializable;",
                            "import java.io.Serializable;" + nl
                            + "import org.eclipse.persistence.annotations.Customizer;" + nl
                            + "import javax.persistence.GeneratedValue;" + nl
                            + "import javax.persistence.GenerationType;" + nl
                            + "import org.eclipse.persistence.annotations.Cache;" + nl
                            + "import org.eclipse.persistence.annotations.CacheType;" + nl
                            + "import javax.persistence.SequenceGenerator;"
                    );
                    // Reemplazar //
                    datos = datos.replace(
                            "@Column(name = \"ID\")",
                            "@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"" + secuencia + "\")" + nl
                            + "    @SequenceGenerator(name = \"" + secuencia + "\", schema = \"" + esquema + "\", sequenceName = \"" + secuencia + "\", allocationSize = 1)" + nl
                            + "    @Column(name = \"ID\")"
                    );
                    datos = datos.replace(
                            "@JoinTable(",
                            "@JoinTable(schema=\"" + esquema + "\", "
                    );
                    limpiarNotNull("AUD_LOGIN");
                    limpiarNotNull("AUD_NUM_IP");
                    // Acomodar números //
                    datos = datos.replace(" long ", " Long ");
                    datos = datos.replace("(long ", "(Long ");
                    datos = datos.replace(" BigInteger ", " Long ");
                    datos = datos.replace("(BigInteger ", "(Long ");
                    datos = datos.replace(" double ", " Double ");
                    datos = datos.replace("(double ", "(Double ");
                    datos = datos.replace(" BigDecimal ", " Double ");
                    datos = datos.replace("(BigDecimal ", "(Double ");
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
