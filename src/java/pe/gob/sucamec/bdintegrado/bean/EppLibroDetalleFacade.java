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
import pe.gob.sucamec.bdintegrado.data.EppLibro;
import pe.gob.sucamec.bdintegrado.data.EppLibroDetalle;
import pe.gob.sucamec.bdintegrado.data.EppLibroMes;
import pe.gob.sucamec.bdintegrado.data.EppLibroUsoDiario;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppLibroDetalleFacade extends AbstractFacade<EppLibroDetalle> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppLibroDetalleFacade() {
        super(EppLibroDetalle.class);
    }

    public List<EppLibroDetalle> listEppdetalle(EppLibroUsoDiario epp) {
        try {
            Query query = em.createQuery("select l from EppLibroDetalle l where l.libroUsoDiarioId = :parametro and l.activo = 1");
            query.setParameter("parametro", epp);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public List<EppLibroDetalle> listEppdetalle(EppLibroMes lmes) {
        try {
            Query query = em.createQuery("select l from EppLibroDetalle l where l.libroUsoDiarioId.libroMesId = :parametro and l.activo = 1 order by l.id desc");
            query.setParameter("parametro", lmes);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
