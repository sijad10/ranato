/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.SbRelacionPersonaGt;


/**
 *
 * @author mespinoza
 */
@Stateless
public class SbRelacionPersonaFacadeGt extends AbstractFacade<SbRelacionPersonaGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbRelacionPersonaFacadeGt() {
        super(SbRelacionPersonaGt.class);
    }

    public List<SbRelacionPersonaGt> listarRepresentantesPorPersona(SbPersonaGt persona) {
        Query q = em.createQuery("SELECT p FROM SbRelacionPersonaGt p WHERE p.personaOriId = :parametroId  AND p.activo= 1 ");
        q.setParameter("parametroId", persona);
        // q.setParameter("ape", persona.getApePat());

        return q.getResultList();
    }
    

    public List<SbRelacionPersonaGt> listRepresentantes(SbPersonaGt persona, SbPersonaGt pori) {
        Query q = em.createQuery("SELECT p FROM SbRelacionPersonaGt p WHERE (p.personaDestId = :parametroId and p.personaOriId= :Pori)  AND (p.activo= 1) ");
        q.setParameter("parametroId", persona);
        q.setParameter("Pori", pori);
        // q.setParameter("ape", persona.getApePat());

        return q.getResultList();
    }

    /**
     * BUSCAR REPRESENTANTE LEGAL
     *
     * @param perorigen
     * @param perdestino
     * @param tipoconsulta
     * @return
     */
    public List<SbPersonaGt> lstPersona(SbPersonaGt perorigen, SbPersonaGt perdestino, String tipoconsulta) {
        List<SbPersonaGt> respuesta = new ArrayList();
        StringBuilder sqlBuilder = new StringBuilder();
        switch (tipoconsulta) {
            case "S":
                //modificado and ruc is null
                sqlBuilder.append("select x from SbRelacionPersonaGt r join r.personaDestId x where r.personaOriId.id = :perorigen ");
                if (perdestino.getTipoDoc() != null) {
                    sqlBuilder.append("and x.tipoDoc = :tipodoc ");
                }
                if (perdestino.getNumDoc() != null) {
                    sqlBuilder.append("and x.numDoc like :numdoc ");
                }
                Query q = em.createQuery(sqlBuilder.toString());
                q.setParameter("perorigen", perorigen.getId());
                if (perdestino.getTipoDoc() != null) {
                    q.setParameter("tipodoc", perdestino.getTipoDoc());
                }
                if (perdestino.getNumDoc() != null) {
                    q.setParameter("numdoc", "%" + perdestino.getNumDoc() + "%");
                }
                respuesta = q.getResultList();
                break;
            case "C":
                //modificado and ruc is null
                sqlBuilder.append("select x from SbRelacionPersonaGt r join r.personaDestId x where r.personaOriId.id = :perorigen "
                        + " and x.tipoId.codProg = 'TP_PER_NAT' ");
                if (!perdestino.getApePat().equals("")) {
                    sqlBuilder.append(" and x.apePat like :apepater ");
                }
                if (!perdestino.getApeMat().equals("")) {
                    sqlBuilder.append(" and x.apeMat like :apemater ");
                }
                if (!perdestino.getNombres().equals("")) {
                    sqlBuilder.append(" and x.nombres like :nombres ");
                }
                Query query = getEntityManager().createQuery(sqlBuilder.toString());
                query.setParameter("perorigen", perorigen.getId());
                if (!perdestino.getApePat().equals("")) {
                    query.setParameter("apepater", "%" + perdestino.getApePat() + "%");
                }
                if (!perdestino.getApeMat().equals("")) {
                    query.setParameter("apemater", "%" + perdestino.getApeMat() + "%");
                }
                if (!perdestino.getNombres().equals("")) {
                    query.setParameter("nombres", "%" + perdestino.getNombres() + "%");
                }
                respuesta = query.getResultList();
                break;
            default:
                respuesta = new ArrayList();
                break;
        }
        return respuesta;
    }

}
