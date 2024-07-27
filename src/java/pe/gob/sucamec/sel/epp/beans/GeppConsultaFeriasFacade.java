/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.epp.data.Registro;

/**
 *
 * @author rmoscoso
 */
@Stateless
public class GeppConsultaFeriasFacade extends AbstractFacade<Registro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GeppConsultaFeriasFacade() {
        super(Registro.class);
    }

    public String nullATodo(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return s.replace("%", "");
    }

    public String nullATodoParcial(String s) {
        if ((s == null) || (s.equals(""))) {
            return "%";
        }
        return "%" + s.replace("%", "") + "%";
    }

    public List<Map> listarFerias() {
        String sql = "SELECT * FROM BDINTEGRADO.TMP_FERIAS WHERE ESTADO = 1";
        Query q = em.createNativeQuery( sql );
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
}
