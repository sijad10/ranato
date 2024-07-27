/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;

/**
 *
 * @author Renato
 */
// Nombre de la instancia en la aplicacion //
@Named("jpqlQueryController")
// La instancia se crea al momento de iniciar una session http //
@SessionScoped
public class JPQLQueryController implements Serializable {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    String consulta, resultado;

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public boolean isEntidad(Annotation[] aa) {
        for (Annotation a : aa) {
            if (a.toString().contains("javax.persistence.Entity")) {
                return true;
            }
        }
        return false;
    }

    public String obtenerCampos(Object o) {
        String r = "";
        try {
            Field[] fa = o.getClass().getDeclaredFields();
            for (Field f : fa) {
                if (!f.getName().startsWith("_")) {
                    f.setAccessible(true);
                    Object v = f.get(o);
                    r += ObjectUtils.toString(v, "NULL") + '\t';
                }
            }
        } catch (Exception ex) {
            return (ex.getMessage());
        }
        return r;
    }

    public String obtenerCabeceras(Object o) {
        String r = "";
        try {
            Field[] fa = o.getClass().getDeclaredFields();
            for (Field f : fa) {
                if (!f.getName().startsWith("_")) {
                    f.setAccessible(true);
                    Object v = f.getName();
                    r += ObjectUtils.toString(v, "NULL") + '\t';
                }
            }
        } catch (Exception ex) {
            return (ex.getMessage());
        }
        return r;
    }

    public void ejecutar() {
        String cabecera = "";
        try {
            resultado = "";
            Query q = em.createQuery(consulta);
            q.setMaxResults(100);
            List<Object> r = q.getResultList();
            boolean inicio = true;
            for (Object oa : r) {
                if (oa == null) {
                    resultado += "NULL\t";
                    if (inicio) {
                        cabecera += "????\t";
                    }
                } else if (oa.getClass().isArray()) {
                    for (Object o : ((Object[]) oa)) {
                        if (o == null) {
                            resultado += "NULL\t";
                            if (inicio) {
                                cabecera += "????\t";
                            }
                        } else if (isEntidad(o.getClass().getAnnotations())) {
                            resultado += obtenerCampos(o) + "\t";
                            if (inicio) {
                                cabecera += obtenerCabeceras(o) + "\t";
                            }
                        } else {
                            resultado += ObjectUtils.toString(o, "NULL") + "\t";
                            if (inicio) {
                                cabecera += o.getClass().getSimpleName() + "\t";
                            }
                        }
                    }
                } else if (isEntidad(oa.getClass().getAnnotations())) {
                    resultado += obtenerCampos(oa) + "\t";
                    if (inicio) {
                        cabecera += obtenerCabeceras(oa) + "\t";
                    }
                } else {
                    resultado += ObjectUtils.toString(oa, "NULL") + "\t";
                    if (inicio) {
                        cabecera += oa.getClass().getSimpleName() + "\t";
                    }
                }
                resultado += "\n";
                inicio = false;
            }
        } catch (Exception ex) {
            resultado = "Error:\n" + ex.getMessage();
        }
        resultado = resultado.replace("\t\t", "\t").replace("\t\n", "\n").replace("{IndirectList: not instantiated}", "{IL}");
        resultado = resultado.replace("\t\t", "\t").replace("\t\n", "\n").replace("{IndirectList: not instantiated}", "{IL}");
        resultado = "<table class='res' cellspacing='0'><tr><th><b>" + cabecera.trim().replace("\t", "<th><b>") + "<tr><td>"
                + resultado.trim().replace("\t", "<td>").replace("\n", "<tr><td>") + "</table>";
    }
}
