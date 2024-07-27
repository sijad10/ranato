/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.rma1369.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author msalinas
 */
@Stateless
public class Rma1369Facade{

    private static final int MAX_RES = 500;
    
    @PersistenceContext(unitName = "SISucamecPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Rma1369Facade() {
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

    public List<Map> listarArmasCanceladasxFiltro(String serie, String licencia) {
        String campo = "";
        campo = "li.NRO_LIC = ?1 AND li.NRO_SERIE = ?2 ";

        String sql = "SELECT li.NRO_SERIE, li.NRO_LIC, li.ESTADO, an.FEC_PRC"
                + " FROM RMA1369.WS_LICENCIAS@SUCAMEC li"
                + " LEFT JOIN RMA1369.AM_LIC_ANULADA@SUCAMEC an ON li.NRO_LIC=an.NRO_LIC"
                + " WHERE " + campo
                + " AND li.SISTEMA='DISCA' AND li.TIPO_PROPIETARIO in('PERS. NATURAL', 'EXTRANJERO')"
                + " AND an.FEC_DOC>=TO_DATE('17-05-2017','DD-MM-YYYY')"
                + " AND an.MOT_ID=11"
                //+ " AND ID IN(SELECT MIN(ID) FROM RMA1369.WS_LICENCIAS@SUCAMEC GROUP BY NRO_LIC)"
                //+ " ORDER BY li.NRO_LIC"
                + "";

        try {
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, Long.valueOf(licencia.trim()));
            q.setParameter(2, serie.trim().toUpperCase());

            q.setHint("eclipselink.result-type", "Map");
            return q.setMaxResults(MAX_RES).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Map> listResolucionesGsspPortaArmas(String ruc) {
        List<Map> listRes = null;
        String sql = "SELECT DISTINCT RS.NRO_RD as nroRd, RS.DES_MOD as desMod, RS.FEC_VEN as fecVen FROM RMA1369.WS_RESOLUCION_GSSP@SUCAMEC RS "
                + "WHERE RS.TIP_MOD IN (1,3,5,7) AND RS.MODALIDAD NOT LIKE '%SIN ARMAS%' AND RS.RUC = ?1 ";
        Query q = em.createNativeQuery(sql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, ruc);
        q.setParameter(2, new Date());
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
}
