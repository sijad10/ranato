/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.citas.data.CitaTurLicenciaReg;

/**
 *
 * @author msalinas
 */
@Stateless
public class CitaTurLicenciaRegFacade extends AbstractFacade<CitaTurLicenciaReg> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurLicenciaRegFacade() {
        super(CitaTurLicenciaReg.class);
    }

    public List<CitaTurLicenciaReg> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select t from CitaCitaTurLicenciaReg t where trim(t.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<CitaTurLicenciaReg> listarLicenciasXExpedienteYDia(String nroExp, Date fecha, Long sedeId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurLicenciaReg t "
                + "where t.turnoId.fechaTurno = :fecha "
                + "and (t.nroExpediente = :nroExpediente or :nroExpediente = null) "
                + "and t.turnoId.estado.codProg = 'TP_ESTUR_CON' "
                + "and t.activo = 1 "
//                + "and t.turnoId.tipoSedeId.id = :sedeId "
                + "and t.tipoEstado is not null "
                + "order by t.id"
        );
        q.setParameter("fecha", fecha);
        q.setParameter("nroExpediente", "".equals(nroExp) ? null : Long.parseLong(nroExp));
//        q.setParameter("sedeId", sedeId);
        return q.getResultList();
    }

    public List<CitaTurLicenciaReg> listarArmasVerificaAprob(Long armaId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurLicenciaReg t "
                + "where t.turnoId.tipoTramiteId.codProg = 'TP_TRAM_VER' "
                + "and t.activo = 1 "
                + "and t.actual = 1 "
                + "and t.tipoEstado.codProg = 'TP_ESTA_OPE' "
                + "and t.tipoEvaluado.codProg = 'TP_EVALU_APR' "
                + "and t.armaId.id = :armaId "
                + "order by t.id"
        );
        q.setParameter("armaId", armaId);
        return q.getResultList();
    }

    public List<CitaTurLicenciaReg> listarArmasVerificaAprobXSerie(String serie) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurLicenciaReg t "
                + "where t.turnoId.tipoTramiteId.codProg = 'TP_TRAM_VER' "
                + "and t.activo = 1 "
                + "and t.actual = 1 "
                + "and t.tipoEstado.codProg = 'TP_ESTA_OPE' "
                + "and t.tipoEvaluado.codProg = 'TP_EVALU_APR' "
                + "and t.serie = :serie "
                + "order by t.id"
        );
        q.setParameter("serie", serie);
        return q.getResultList();
    }

    public List<CitaTurLicenciaReg> listarArmasVerificaRen(Long armaId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurLicenciaReg t "
                + "where t.turnoId.tipoTramiteId.codProg = 'TP_TRAM_VER' "
                        + "and t.turnoId.subTramiteId.codProg = 'TP_PROG_VER_RLU'"
                + "and t.activo = 1 "
                + "and t.actual = 1 "
                + "and t.tipoEstado.codProg = 'TP_ESTA_OPE' "
                //+ "and t.tipoEvaluado.codProg = 'TP_EVALU_APR' "
                + "and t.armaId.id = :armaId "
                + "order by t.id"
        );
        q.setParameter("armaId", armaId);
        return q.getResultList();
    }

    public List<CitaTurLicenciaReg> listarLicenciasPorTurno(Long turnoId) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurLicenciaReg t "
                + "where t.activo = 1 "
                + "and t.turnoId.id = :turnoId "
                + "order by t.id"
        );
        q.setParameter("turnoId", turnoId);
        return q.getResultList();
    }

}
