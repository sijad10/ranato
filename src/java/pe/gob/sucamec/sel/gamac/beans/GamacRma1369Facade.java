/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author msalinas
 */
@Stateless
public class GamacRma1369Facade{

    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public GamacRma1369Facade() {
        super();
    }

    public List<String> buscarVigilanteEmpresa(String RUC, String nroDoc) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT S.COD_USR "
                + "FROM RMA1369.SS_EMP_VIG@SUCAMEC S "
                + "WHERE S.RUC = ? "
                + "AND S.COD_USR = ? "
                + "AND FEC_VENC > SYSDATE"
        );
        q.setParameter(1, RUC);
        q.setParameter(2, nroDoc);
        return q.getResultList();
    }
    
    public List<Map> buscarArmasConRUA(String nroLic) {
        javax.persistence.Query q = em.createNativeQuery(
                "select nro_lic, nro_serie, marca FROM RMA1369.AM_ARMAS_MARCACION@SUCAMEC WHERE nro_lic like ?1"
        );
        q.setParameter(1, nroLic);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarArmasAntiguas(String nroLic) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT nro_lic, nro_serie, ano_rd FROM RMA1369.AM_ARMA@SUCAMEC WHERE nro_lic like ?1"
        );
        q.setParameter(1, nroLic);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarArmasNuevasXNroLic(String nroLic) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT nro_lic, nro_serie, ano_rd FROM RMA1369.AM_ALMACEN@SUCAMEC WHERE nro_lic like ?1"
        );
        q.setParameter(1, nroLic);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarArmasNuevasXSerie(String serie) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT nro_lic, nro_serie, ano_rd FROM RMA1369.AM_ALMACEN@SUCAMEC WHERE nro_serie like ?1"
        );
        q.setParameter(1, serie);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarCasaComercial(String ruc) {
        javax.persistence.Query q = em.createNativeQuery(
                "select * from RMA1369.WS_RESOLUCION_GAMAC@SUCAMEC WHERE RUC = ?1"
        );
        q.setParameter(1, ruc);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    public List<Map> buscarRDCasaCom(String ruc) {
        javax.persistence.Query q = em.createNativeQuery(
                "SELECT * FROM RMA1369.RESOLUCION@SUCAMEC WHERE COD_AREA=3 AND COD_SUB_AREA=1 AND COD_TRA IN ('1','2') AND TIP_USR = 1 AND FEC_VEN >= SYSDATE AND COD_USR LIKE ?1 "
        );
        q.setParameter(1, ruc);
        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

}
