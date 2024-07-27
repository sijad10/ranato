/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbRelacionPersona;

/**
 *
 * @author Renato
 */
@Stateless
public class SbRelacionPersonaFacade extends AbstractFacade<SbRelacionPersona> {
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbRelacionPersonaFacade() {
        super(SbRelacionPersona.class);
    }
    
     /**
     * BUSCAR PERSONAS RELACIONADAS A LA PERSONA ORIGEN
     *
     * @param idPerOrigen
     * @return lista de Personas
     */
    public List<SbPersona> listarPersonasAutorizadas(Long idPerOrigen) {
        List<SbPersona> listaRes = null;
        String jpql = "Select pe from SbRelacionPersona rp, SbPersona pe "
                + "where rp.tipoId.codProg = 'TP_REL_AUT' and  pe.id = rp.personaDestId.id and  rp.personaOriId.id = :idO and rp.activo = 1 and pe.activo = 1";
        Query q = em.createQuery(jpql);
        q.setParameter("idO", idPerOrigen);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listaRes = q.getResultList();
        }
        return listaRes;
    }
    
}
