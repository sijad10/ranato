/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.EppGteDeposito;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class EppGteDepositoFacade extends AbstractFacade<EppGteDeposito> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppGteDepositoFacade() {
        super(EppGteDeposito.class);
    }
    
    public List<EppGteDeposito> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select e from EppGteDeposito e where trim(e.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public int contarDepositosGteByNroDeposito(Long nroDeposito, Long depositoId) {
        int cont = 0;
        String jpql = "select count(a) from EppGteDeposito a where a.activo = 1 "+
                      " and a.nroDeposito = :nroDeposito ";
        if(depositoId != null){
            jpql += " and a.id != :depositoId  ";
        }
        
        Query q = em.createQuery( jpql );
        q.setParameter("nroDeposito", nroDeposito);
        if(depositoId != null){
            q.setParameter("depositoId", depositoId);    
        }
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
}
