/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.citas.data.CitaTurProgramacion;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTurProgramacionFacade extends AbstractFacade<CitaTurProgramacion> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurProgramacionFacade() {
        super(CitaTurProgramacion.class);
    }

    public List<CitaTurProgramacion> lstTurProgramacion() {
        Query query = getEntityManager().createQuery("select t from CitaTurProgramacion t where t.activo = 1 and t.tipoTurno.codProg = 'TP_PROG_POL' order by t.hora asc");
        return query.getResultList();
    }

    public List<CitaTurProgramacion> lstTurProgramacion(String tipo) {
        Query query = getEntityManager().createQuery("select t from CitaTurProgramacion t where t.activo = 1 and t.tipoTurno.codProg = :tipo order by t.hora asc");
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    public List<CitaTurProgramacion> lstTurProgramacion(long sedeId) {
        Query query = getEntityManager().createQuery("select t from CitaTurProgramacion t where t.activo = 1 and t.tipoTurno.codProg = 'TP_PROG_POL' and t.sedeId.id = :sedeId order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        return query.getResultList();
    }

    public List<CitaTurProgramacion> lstTurProgramacionXSedeYSubTramite(long sedeId, String subTramiteCodProg) {
        Query query = getEntityManager().createQuery("select t from CitaTurProgramacion t where t.activo = 1 and t.tipoTurno.codProg = :subTramiteCodProg and t.sedeId.id = :sedeId order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        query.setParameter("subTramiteCodProg", subTramiteCodProg);
        return query.getResultList();
    }

    public List<CitaTurProgramacion> lstTurProgramacion(long sedeId, String tipo) {
        Query query = getEntityManager().createQuery("select t from CitaTurProgramacion t where t.activo = 1 and t.tipoTurno.codProg = :tipo and t.sedeId.id = :sedeId order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }
    
    public List<CitaTurProgramacion> lstTurProgramacionXTipoCita(long sedeId, String tipoTurno, String tipoCita) {
        Query query = getEntityManager().createQuery("select t from CitaTurProgramacion t where t.activo = 1 and t.tipoTurno.codProg = :tipoTurno and t.sedeId.id = :sedeId and t.tipoCitaId.codProg = :tipoCita order by t.hora asc");
        query.setParameter("sedeId", sedeId);
        query.setParameter("tipoTurno", tipoTurno);
        query.setParameter("tipoCita", tipoCita);
        return query.getResultList();
    }

    public CitaTurProgramacion buscarTurProgramacionXId(Long id) {
        CitaTurProgramacion respuesta = new CitaTurProgramacion();
        String sentencia = "select t from CitaTurProgramacion t where t.id = :id";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("id", id);
        respuesta = (CitaTurProgramacion) query.getSingleResult();
        return respuesta;
    }
}
