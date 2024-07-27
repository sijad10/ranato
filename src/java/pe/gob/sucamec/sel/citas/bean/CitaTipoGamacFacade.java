/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTipoGamacFacade extends AbstractFacade<CitaTipoGamac> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTipoGamacFacade() {
        super(CitaTipoGamac.class);
    }

    public CitaTipoGamac buscarTipoGamacXId(Long id) {
        CitaTipoGamac respuesta = new CitaTipoGamac();
        String sentencia = "select t from CitaTipoGamac t where t.id = :id";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("id", id);
        respuesta = (CitaTipoGamac) query.getSingleResult();
        return respuesta;
    }

    public CitaTipoGamac buscarTipoGamacXCodProg(String codProg) {
        CitaTipoGamac respuesta = new CitaTipoGamac();
        String sentencia = "select t from CitaTipoGamac t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = (CitaTipoGamac) query.getSingleResult();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoGamac(String s) {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 and t.tipoId.codProg = :parametro order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", s != null ? s : "");
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstDefPerTipoArma() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 and (t.codProg = 'TP_ARM_REV' or t.codProg = 'TP_ARM_PIS') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<CitaTipoGamac> lstTipoTraPol() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and (t.codProg = 'TP_TRA_CAZ' or t.codProg = 'TP_TRA_DEF' or t.codProg = 'TP_TRA_DEP' or t.codProg = 'TP_TRA_SIS' or t.codProg = 'TP_TRA_VP' or t.codProg = 'TP_TRA_COL' or t.codProg = 'TP_TRA_MUL') "
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoTraPol20() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and (t.codProg = 'TP_TRA_VP') "
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<CitaTipoGamac> lstTipoTraMulti() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and (t.codProg = 'TP_TRA_CAZ' or t.codProg = 'TP_TRA_DEF' or t.codProg = 'TP_TRA_DEP' or t.codProg = 'TP_TRA_COL') "
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoTramite() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and t.codProg IN('TP_TRAM_VAL', 'TP_TRAM_POL', 'TP_TRAM_VER','TP_TRAM_TD','TP_TRAM_EMP') "
                + " order by t.orden, t.nombre";
//        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
//                + " and t.codProg IN('TP_TRAM_VAL', 'TP_TRAM_POL', 'TP_TRAM_VER') "
//                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<CitaTipoGamac> lstTipoSubTramite() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and t.tipoId.codProg IN('TP_TRAM_TD') "
                + " order by t.orden, t.nombre";
//        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
//                + " and t.codProg IN('TP_TRAM_VAL', 'TP_TRAM_POL', 'TP_TRAM_VER') "
//                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoSubTramiteVerNat() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and t.tipoId.codProg IN('TP_TRAM_VER') "
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoSubTramiteVerJur() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and t.tipoId.codProg IN('TP_TRAM_VER') and t.codProg NOT IN('TP_PROG_VER_RLU', 'TP_PROG_VER_ANR', 'TP_PROG_VER_CM')"
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> listaTipoGamacCodProg(String cod) {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo = 1 and  t.codProg in (" + cod + ")";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoOpePolRen() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and t.tipoId.codProg IN('TP_TRAM_POL') "
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoGamac> lstTipoOpePol() {
        List<CitaTipoGamac> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoGamac t where t.activo =1 "
                + " and t.tipoId.codProg IN('TP_TRAM_POL') and t.codProg NOT IN('TP_TRAM_POL_REN')"
                + " order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
}
