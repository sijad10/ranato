/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SbRegistroUsuarioGt;

/**
 *
 * @author Renato
 */
@Stateless
public class SbRegistroUsuarioFacadeGt extends AbstractFacade<SbRegistroUsuarioGt> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbRegistroUsuarioFacadeGt() {
        super(SbRegistroUsuarioGt.class);
    }
    
    public SbRegistroUsuarioGt obtenerRegUsuarioPorIdUsu(Long idUsu) {
        SbRegistroUsuarioGt res=null;
        if (idUsu == null) {
            return null;
        }
        javax.persistence.Query q = em.createQuery(
                "select ru from SbRegistroUsuarioGt ru where ru.usuarioId.id = :idUsu and ru.activo = 1 "
        );
        q.setParameter("idUsu", idUsu);
        q.setMaxResults(1);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            res=(SbRegistroUsuarioGt)q.getResultList().get(0);
        }
        return res;
    }
    
}
