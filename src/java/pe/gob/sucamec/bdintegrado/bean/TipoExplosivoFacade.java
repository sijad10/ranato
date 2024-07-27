/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class TipoExplosivoFacade extends AbstractFacade<TipoExplosivoGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoExplosivoFacade() {
        super(TipoExplosivoGt.class);
    }

    public List<TipoExplosivoGt> findByCodProg(String s) {
        Query q = em.createNamedQuery("TipoExplosivoGt.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }

    public List<TipoExplosivoGt> lstTipoODGuiaTransito(String paramCodProgs) {
        String codprog = "";
        switch (paramCodProgs) {
            case "1":
                codprog = "'TP_ODRGT_POL','TP_ODRGT_FAB'";
                break;
            case "1D":
                codprog = "'TP_ODRGT_POL','TP_ALMEMDE_LUSO'";
                break;            
            case "2":
                codprog = "'TP_ODRGT_POL','TP_ALMEMDE_LUSO'";
                break;
            case "3":
                codprog = "'TP_ODRGT_POL'";
                break;
            case "4":
                codprog = "'TP_ODRGT_POL','TP_ALMEMDE_LUSO'";
                break;
            case "5":
                codprog = "'TP_ODRGT_POL','TP_ODRGT_FAB'";
                break;
            case "6":
                codprog = "'TP_ODRGT_POL','TP_ODRGT_FAB'";
                break;
            case "7":
                codprog = "'TP_ALMEMDE_PUE','TP_ALMEMDE_ALM'";
                break;
            case "8":
                codprog = "'TP_ALMEMDE_PUE','TP_ALMEMDE_ALM'";
                break;
            case "9":
                codprog = "'TP_ALMEMDE_PUE'";
                break;
            default:
                codprog = "'TP_ALMEMDE_PUE','TP_ALMEMDE_ALM','TP_ODRGT_ALM','TP_ODRGT_POL','TP_ALMEMDE_LUSO','TP_ODRGT_FAB'";
                break;
        }
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in (" + codprog + ") order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    public List<TipoExplosivoGt> lstTipoEstadoGuiaExternaAdministrado() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_REGEV_CRE','TP_REGEV_FIN') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    public List<TipoExplosivoGt> lstTipoEstadoGuiaExternaListado() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_REGEV_CRE','TP_REGEV_ANU','TP_REGEV_TRAN','TP_REGEV_FIN') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    public List<TipoExplosivoGt> lstTipoEstadoGuiaExternaFabricante() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_REGEV_TRAN','TP_REGEV_FIN') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    /**
     * TIPO DOCUMENTO PARA REGISTRO GUIA TRANSITO
     *
     * @return
     */
    public List<TipoExplosivoGt> lstTipoDocGuiaTransito() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_DOC_RMTC','TP_DOC_CMTC') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    public List<TipoExplosivoGt> lstTipoExplosivo(String s) {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.tipoId.codProg = :codProg order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("codProg", s != null ? s : "");
        return query.getResultList();
    }

    public List<TipoExplosivoGt> lstTipoDocGtExt() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_DOC_FA','TP_DOC_FE','TP_DOC_OP') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    /**
     * TIPO EXPLOSIVO POR CODIGO DE PROGRAMADOR
     *
     * @param codprog
     * @return
     */
    public TipoExplosivoGt tipoExplosivoXCodProg(String codprog) {
        TipoExplosivoGt tipo = null;
        String sentencia = "select t from TipoExplosivoGt t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (TipoExplosivoGt) query.getSingleResult();
        return tipo;
    }

    public List<TipoExplosivoGt> lstTipoDocumentoAutorizacionUso(String paramTipodoc) {

        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in (" + paramTipodoc + ") order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }
    
    public List<TipoExplosivoGt> lstTipoEstadoGuiaExternaSinTransfer() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_REGEV_CRE','TP_REGEV_FIN') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }
    
    public List<TipoExplosivoGt> lstTipoDocGtExtTipo6() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.codProg in ('TP_DOC_GR') order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }
    /**
     * TIPO EXPLOSIVO PARA CATEGORIA DE LME
     *
     * @param codprog
     * @return
     */
    
    public List<TipoExplosivoGt> lstCategoriaLME() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.tipoId.codProg = 'TP_CATLME' order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }
    public List<TipoExplosivoGt> lstCategoriaLMPP() {
        String sentencia = "select t from TipoExplosivoGt t where t.activo =1 and t.tipoId.codProg = 'TP_CATP' order by t.orden, t.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }
}
