/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author msalinas
 */
@Stateless
public class TipoBaseFacadeGt extends AbstractFacade<TipoBaseGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoBaseFacadeGt() {
        super(TipoBaseGt.class);
    }

    public List<TipoBaseGt> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from TipoBaseGt t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public TipoBaseGt buscarTipoBaseXCodProg(String codProg) {
        TipoBaseGt respuesta = new TipoBaseGt();
        String sentencia = "select t from TipoBaseGt t where t.codProg = :codProg";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = (TipoBaseGt) query.getSingleResult();
        return respuesta;
    }

    public List<TipoBaseGt> listarTipoBaseXCodProg(String codProg) {
        List<TipoBaseGt> respuesta = new ArrayList();
        String sentencia = "select t from TipoBaseGt t where t.tipoId.codProg = :codProg and t.activo = 1 order by t.nombre asc";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<TipoBaseGt> likeTipoBaseXCodProg(String codProg) {
        List<TipoBaseGt> respuesta = new ArrayList();
        String sentencia = "select t from TipoBaseGt t where t.codProg like :codProg and t.activo = 1";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", codProg + "%");
        respuesta = query.getResultList();
        return respuesta;
    }

    public TipoBaseGt tipoPorCodProg(String s) {
        List<TipoBaseGt> r = findByCodProg(s);
        return r.size() == 1 ? r.get(0) : null;
    }

    public List<TipoBaseGt> findByCodProg(String s) {
        Query q = em.createNamedQuery("TipoBaseGt.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }
    
    public List<TipoBaseGt> listarTipoBaseXCodProgs(String codigos) {
        List<TipoBaseGt> respuesta = new ArrayList();
        String sentencia = "select t from TipoBaseGt t where t.codProg in ("+codigos+") and t.activo = 1 ORDER BY t.id asc";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<TipoBaseGt> listarTipoBaseXCodProgsV2(String codProg) {
        List<TipoBaseGt> respuesta = new ArrayList();
        String sentencia = "SELECT TB.* \n" +
                            " FROM BDINTEGRADO.TIPO_BASE TB\n" +
                            " INNER JOIN BDINTEGRADO.TIPO_BASE TB01 ON (TB01.ID = TB.TIPO_ID AND TB01.COD_PROG = ?1)\n" +
                            " WHERE TB.ACTIVO = 1\n" +
                            " ORDER BY TB.ID ASC ";
        Query query = getEntityManager().createNativeQuery(sentencia, TipoBaseGt.class);
        query.setParameter(1, codProg);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<TipoBaseGt> listarAreasLimaOD(){
        List<TipoBaseGt> respuesta = new ArrayList();
        //String sentencia = "select t from TipoBaseGt t where (t.codProg like 'TP_AREA_OD_%' OR t.codProg = 'TP_AREA_TRAM') and t.activo = 1 and t.codProg != 'TP_AREA_OD_PUC' ";
        String sentencia = "select t from TipoBaseGt t where t.codProg = 'TP_AREA_TRAM' and t.activo = 1";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public List<TipoBaseGt> listarAreasSedesCarnesByResolucionDisca(String ruc){
        List<TipoBaseGt> respuesta = new ArrayList();
        String sentencia = "SELECT DISTINCT TB.* \n" +
                            " FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC R \n" +
                            " INNER JOIN RMA1369.DEPARTAMENTO@SUCAMEC D ON D.NOM_DPTO = R.NOM_DPTO \n" +
                            " INNER JOIN RMA1369.HOMOLOGAR_MODALIDAD@SUCAMEC H ON H.EMPMOD_ID = D.COD_DPTO \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD TMD ON TMD.ID = H.TIPO_ID \n" +
                            " INNER JOIN BDINTEGRADO.SB_ASIGNACION_UBICACION AU ON AU.DEPARTAMENTO_ID = H.TIPO_SEGURIDAD_ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_BASE TB ON TB.ID = AU.AREA_ID \n" +
                            " INNER JOIN BDINTEGRADO.TIPO_BASE TP ON TP.ID = AU.TIPO_PROCESO_ID " +
                            " WHERE R.RUC = ?1 \n" +
                            "  AND R.FEC_VEN >= CURRENT_DATE\n" +
                            "  AND TMD.COD_PROG = 'TP_HOMOL_DPTO' \n" +
                            "  AND TP.COD_PROG = 'TP_GSSP_ECRN' ";
        Query query = getEntityManager().createNativeQuery(sentencia, TipoBaseGt.class);
        query.setParameter(1, ruc);
        respuesta = query.getResultList();
        return respuesta;
    }
    
    public TipoBaseGt selectAreaPorSiglas(String s) {
        Query q = em.createQuery("select t from TipoBase t where t.tipoId.codProg='TP_AREA' and t.abreviatura = :abreviatura");
        q.setParameter("abreviatura", s != null ? s : "");
        List<TipoBaseGt> tb = q.getResultList();
        if (tb.isEmpty()) {
            return null;
        }
        return tb.get(0);
    }
    
    
    public List<TipoBaseGt> selectItemsTipo(String s) {
        Query q = em.createQuery("select t from TipoBase t where t.activo =true and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }
    
    public TipoBaseGt verDatosTipoBaseXId(Long xId) {
        String sentencia = "select s from TipoBaseGt s where s.activo = 1 and s.id = :xId ";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("xId", xId);
        return (TipoBaseGt) query.getSingleResult();
    }
}
