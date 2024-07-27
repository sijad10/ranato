/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbCsMedico;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbCsMedicoFacade extends AbstractFacade<SbCsMedico> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbCsMedicoFacade() {
        super(SbCsMedico.class);
    }
    public List<SbCsMedico> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbCsMedico s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SbCsMedico> selectMedico(String documento) {
        if (documento == null) {
            documento = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select p from SbCsMedico p where p.activo = 1 "
                + " and ( p.personaId.numDoc like :documento OR p.nroCmp like :documento ) "
                + " order by p.personaId.apePat asc"
        );
        q.setParameter("documento", "%" + documento.trim() + "%");
        return q.setMaxResults(300).getResultList();
    }
    
    public int contarNroCmpMedico(String nroCMP) {
        int cont = 0;
        String plsql = "select count(a) from SbCsMedico a where a.activo = 1 "+
                       " and a.nroCmp = :nroCMP ";

        Query q = em.createQuery(plsql);
        q.setParameter("nroCMP", nroCMP.trim());
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarDNIPersonaMedico(String numDoc) {
        int cont = 0;
        String plsql = "select count(a) from SbCsMedico a where a.activo = 1 "+
                       " and a.personaId.numDoc = :numDoc ";

        Query q = em.createQuery(plsql);
        q.setParameter("numDoc", numDoc);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }

}
