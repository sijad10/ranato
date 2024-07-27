/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.citas.data.CitaTurComprobante;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitaTurComprobanteFacade extends AbstractFacade<CitaTurComprobante> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitaTurComprobanteFacade() {
        super(CitaTurComprobante.class);
    }

    public List<CitaTurComprobante> listarComprobantesXPersonaVigentes(String nroDoc) {
        Calendar hoy = Calendar.getInstance();
        Date fechaIni;
        Date fechaFin = new Date();
        hoy.setTime(fechaFin);
        hoy.add(Calendar.MONTH, -1);
        fechaIni = hoy.getTime();
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurComprobante t"
                + " where t.fechaPago >= fechaIni"
                + " and t.turTurnoList.perExamenId.numDoc = :nroDoc"
                + " and t.activo = 1 and t.turTurnoList.activo = 1"
        );
        q.setParameter("nroDoc", nroDoc);
        q.setParameter("fechaIni", fechaIni);
        return q.getResultList();
    }

    public List<CitaTurComprobante> listarComprobantesActivos() {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurComprobante t where t.activo = 1"
        );
        return q.getResultList();
    }

    public List<CitaTurComprobante> listarComprobantesXVoucher(String vTasa, String vSeq, String vCta, String vAgencia, String vAutentica) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurComprobante t "
                + "where t.voucherTasa = :vTasa "
                + "and t.voucherSeq = :vSeq "
                + "and t.voucherCta = :vCta "
                + "and t.voucherAgencia = :vAgencia "
                + "and t.voucherAutentica = :vAutentica "
                + "and t.activo = 1"
        );
        q.setParameter("vTasa", vTasa);
        q.setParameter("vSeq", vSeq);
        q.setParameter("vCta", vCta);
        q.setParameter("vAgencia", vAgencia);
        q.setParameter("vAutentica", vAutentica);
        return q.getResultList();
    }

    public List<CitaTurComprobante> listarComprobantePorSecuencia(String vTasa, String vSeq, String vCta) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurComprobante t "
                + "where t.voucherTasa = :vTasa "
                + "and t.voucherSeq = :vSeq "
                + "and t.voucherCta = :vCta "
                + "and t.activo = 1"
        );
        q.setParameter("vTasa", vTasa);
        q.setParameter("vSeq", vSeq);
        q.setParameter("vCta", vCta);
        return q.getResultList();
    }
    
    public List<CitaTurComprobante> listarComprobantePorTurno(Long vIdComprobante) {
        javax.persistence.Query q = em.createQuery(
                "SELECT t FROM CitaTurComprobante t "
                + "where t.id = :vIdComprobante "
                + "and t.activo = 1"
        );
        q.setParameter("vIdComprobante", vIdComprobante);
        return q.getResultList();
    }
    

}
