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
import pe.gob.sucamec.sel.citas.data.CitaTipoBase;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTipoBaseFacade extends AbstractFacade<CitaTipoBase> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTipoBaseFacade() {
        super(CitaTipoBase.class);
    }

    public CitaTipoBase buscarTipoBaseXId(Long id) {
        CitaTipoBase respuesta = new CitaTipoBase();
        String sentencia = "select t from CitaTipoBase t where t.id = :id";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("id", id);
        respuesta = (CitaTipoBase) query.getSingleResult();
        return respuesta;
    }

    public CitaTipoBase buscarTipoBaseXCodProg(String codProg) {
        CitaTipoBase respuesta = new CitaTipoBase();
        String sentencia = "select t from CitaTipoBase t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = (CitaTipoBase) query.getSingleResult();
        return respuesta;
    }

    public List<CitaTipoBase> lstTipoBase(String s) {
        List<CitaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoBase t where t.activo =1 and t.tipoId.codProg = :codProg order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", s != null ? s : "");
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoBase> lstTipoDoc() {
        List<CitaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoBase t "
                + "where t.activo = 1 "
                + "and (t.codProg = 'TP_DOCID_DNI' "
                + "or t.codProg = 'TP_DOCID_CE') "
                + "order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoBase> listarSedes() {
        List<CitaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoBase t where t.activo = 1 "
                //                + "and (t.codProg like 'TP_AREA_OD_%' or t.codProg like 'TP_AREA_TRAM') "
                + "and t.codProg in ('TP_AREA_OD_JUNIN','TP_AREA_TRAM','TP_AREA_OD_ANCASH','TP_AREA_OD_AQP','TP_AREA_OD_CHICLA','TP_AREA_OD_CUSCO','TP_AREA_OD_LIBERTA','TP_AREA_OD_PIURA','TP_AREA_OD_PUNO','TP_AREA_OD_TACNA','TP_AREA_OD_ICA','TP_AREA_OD_CAJAMARCA','TP_AREA_OD_LOR') "
                + "order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<CitaTipoBase> listarSedesNoPresencial() {
        List<CitaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoBase t where t.activo = 1 "
                + "and t.codProg in ('TP_AREA_TRAM'"
                //+ ",'TP_AREA_OD_JUNIN','TP_AREA_OD_ANCASH','TP_AREA_OD_AQP',"
                //+ "'TP_AREA_OD_CHICLA','TP_AREA_OD_CUSCO','TP_AREA_OD_LIBERTA','TP_AREA_OD_PIURA',"
                //+ "'TP_AREA_OD_PUNO','TP_AREA_OD_TACNA','TP_AREA_OD_ICA','TP_AREA_OD_CAJAMARCA','TP_AREA_OD_LOR'
                + " ) "
                + "order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<CitaTipoBase> lstInstitucion() {
        List<CitaTipoBase> respuesta = new ArrayList();
        String sentencia = "select t from CitaTipoBase t "
                + "where t.activo = 1 "
                + "and t.tipoId.codProg = 'TP_INS' "
                + "order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
}
