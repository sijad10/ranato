/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
/**
 *
 * @author Renato
 */
@Stateless
public class SbPersonaFacade extends AbstractFacade<SbPersona> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPersonaFacade() {
        super(SbPersona.class);
    }

    public List<SbPersona> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbPersona s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<SbPersona> selectLikeAdministrado() {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersona p where p.activo = 1 and p.ruc is not null order by p.rznSocial, p.apePat, p.apeMat, p.nombres");
        return q.getResultList();

    }

    public List<SbPersona> selectLikePersonaNatural() {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersona p where p.activo = 1 and p.numDoc is not null order by p.apePat, p.apeMat, p.nombres");
        return q.getResultList();

    }

    /**
     * BUSCAR PERSONAS POR DOCUMENTOS ACTIVAS
     *
     * @param doc
     * @return
     */
    public List<SbPersona> selectPersonaActivaxDoc(String doc) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersona p where p.numDoc = :doc AND p.activo = 1"
        );
        q.setParameter("doc", doc);
        return q.getResultList();
    }

    /**
     * BUSCAR A PERSONA LOGUEADA EN SISTEMA SEL
     *
     * @param tipoDoc
     * @param numDoc
     * @return
     */
    public SbPersona buscarPersonaSel(String tipoDoc, String numDoc) {
        if (tipoDoc == null) {
            tipoDoc = "";
        }
        if (numDoc == null) {
            numDoc = "";
        }

        javax.persistence.Query q = em.createQuery(
                "select p from SbPersona p where (p.ruc = :numDoc or p.numDoc = :numDoc) AND "
                + " trim(p.tipoDoc.nombre) = :tipoDoc  AND p.activo = 1 order by p.id desc"
        );
        q.setParameter("numDoc", numDoc.toUpperCase());
        q.setParameter("tipoDoc", tipoDoc.toUpperCase());

        return (SbPersona) q.getSingleResult();
    }

    /**
     * BUSCAR A PERSONA LOGUEADA EN SISTEMA SEL
     * @param numDoc
     * @return 
     */
    public SbPersona buscarPersonaSelNumDoc(String numDoc){
        if (numDoc == null) {
            numDoc = "";
        }

        javax.persistence.Query q = em.createQuery(
                "select p from SbPersona p where (p.ruc = :numDoc or p.numDoc = :numDoc) AND "
                + " p.activo = 1 order by p.id desc"
        );
        q.setParameter("numDoc", numDoc.toUpperCase() );
        
        if(q.getResultList().isEmpty()){
            return null;
        }else{
            return (SbPersona) q.getResultList().get(0);
        }
    }
    
    /**
     * BUSCAR PERSONAS PARA LICENCIA MANIPULADOR EXPLOSIVOS
     *
     * @param tipoBusq
     * @param campo
     * @return
     */
    public List<SbPersona> buscarPersonaLME(String tipoBusq, String campo) {
        if (tipoBusq == null) {
            tipoBusq = "";
        }

        String jpql = "SELECT p FROM SbPersona p "
                + "WHERE p.activo = 1 and p.tipoDoc is not null ";

        if (!tipoBusq.isEmpty()) {
            switch (tipoBusq) {
                case "nomb":
                    jpql = jpql + " and (CONCAT(p.nombres ,' ', p.apePat ,' ', p.apeMat ) like :campo)";
                    break;
                case "ndoc":
                    jpql = jpql + " and p.numDoc like :campo";
                    break;
            }
        }
        javax.persistence.Query q = em.createQuery(jpql);
        if (!tipoBusq.isEmpty()) {
            q.setParameter("campo", "%" + campo + "%");
        }
        return q.setMaxResults(200).getResultList();
    }

    public SbPersona findByNumDoc(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("SbPersona.findByNumDoc", SbPersona.class).setParameter("numDoc", filtro.trim());
                q.setMaxResults(MAX_RES);
                q.setHint("eclipselink.batch.type", "IN");
                List<SbPersona> list = q.getResultList();
                if (list.size() > 0) {
                    return list.get(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public SbPersona findByNroCip(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("SbPersona.findByNroCip", SbPersona.class).setParameter("nroCip", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<SbPersona> list = q.getResultList();
                if (list.size() > 0) {
                    return list.get(0);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public SbPersona findByRuc(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("SbPersona.findByRuc", SbPersona.class).setParameter("ruc", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<SbPersona> list = q.getResultList();
                if (list.size() > 0) {
                    return list.get(0);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }
    
    /**
     * BUSCAR PERSONAS RELACIONADAS A LA PERSONA ORIGEN
     *
     * @param idPersona
     * @return lista de Personas
     */
    public List<SbPersona> listarPersonaXIdRelacionPersona(Long idPersona) {
        List<SbPersona> listaRes = null;
        String jpql = "Select pe from SbRelacionPersona rp, SbPersona pe "
                + "where pe.id = rp.personaDestId.id and  rp.personaOriId.id = :idO and rp.activo = 1 ";
        Query q = em.createQuery(jpql);
        q.setParameter("idO", idPersona);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listaRes = q.getResultList();
        }
        return listaRes;
    }

    public List<SbPersona> buscarEmpresaXDoc(String doc) {
        Query q = em.createQuery("select s from SbPersona s where (s.ruc = :numRuc or s.numDoc = :numDoc) and s.activo = 1");
        q.setParameter("numRuc", doc);
        q.setParameter("numDoc", doc);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<SbPersona> listxRepresentante(Long idPersona) {
        if (idPersona != null) {
            String jpql = "Select distinct pe from SbRelacionPersona rp, SbPersona pe "
                            + " where pe.id = rp.personaDestId.id and rp.personaOriId.id = :idOrigen and rp.activo = 1 "
                            + " and pe.activo = 1 and pe.numDoc is not null"
                            + " and pe.id not in(0, 50682, 2)"
                            + " order by pe.apePat, pe.apeMat, pe.nombres";
            Query q = em.createQuery(jpql);
            
            q.setParameter("idOrigen", idPersona); 
            return q.getResultList();

        }else{
            return null;
        }

    }

    public List<SbPersona> buscarPersonaXNumDoc(String numDoc) {
        if (numDoc == null) {
            numDoc = "";
        }
        Query q = em.createQuery("select s from SbPersona s where (s.numDoc = :numDoc or s.ruc = :numDoc) and s.activo = 1");
        q.setParameter("numDoc", numDoc);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    /**
     * FUNCION QUE OBTIENE EL LISTADO DE PERSONAS POR NOMBRES, RAZON SOCIAL,
     * NUMERO DOCUMENTO O RUC, Y EL TIPO DE PERSONA (NATURAL, JURIDICO O AMBOS)
     *
     * @param valor, Valor a buscar
     * @param tipoPersona, Tipo de persona: 0 = natural, 1 = juridica, null
     * (nulo)= ambos
     * @return
     */
    public List<SbPersona> listarPorNombresORznSocialODoc(String valor, Long tipoPersona) {
        String jpql = "select p from SbPersona p where p.activo = 1 "
                + " and (CONCAT(p.nombres ,' ', p.apePat ,' ', p.apeMat) like :valor "
                + " OR p.rznSocial like :valor "
                + " OR p.ruc like :valor or p.numDoc like :valor) ";
        if (tipoPersona != null) {
            if (tipoPersona == 0L) {
                jpql += "and p.tipoId.codProg = 'TP_PER_NAT' ";
            } else if (tipoPersona == 1L) {
                jpql += "and p.tipoId.codProg = 'TP_PER_JUR' ";
            }
        }
        jpql += " order by p.id asc";
        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("valor", valor == null ? "" : "%" + valor.toUpperCase().trim() + "%");
        return q.setMaxResults(200).getResultList();
    }

    public List<SbPersona> selectLikePersonasNoSucamec(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersona p where p.activo = 1 and p.id != 1 and (p.nombres like :valor or p.apeMat  "
                + "like :valor or p.apePat like :valor or p.rznSocial like :valor or "
                + "p.ruc like :valor or p.numDoc like :valor "
                + "or CONCAT(p.nombres ,' ', p.apePat ,' ', p.apeMat ) like :valor )  "
        );
        q.setParameter("valor", "%" + s.toUpperCase().trim() + "%");
        return q.setMaxResults(200).getResultList();
    }

    /**
     * BUSCAR REPRESENTANTES POR ID DE PERSONA PADRE
     *
     * @param idPersona
     * @return
     */
    public List<SbPersona> obtenerRepresentantes(Long idPersona) {
        List<SbPersona> listResp = new ArrayList<>();
        Query q = em.createQuery(
                "select rp.personaDestId from SbRelacionPersona rp where rp.personaOriId.id = :id and rp.tipoId.codProg = 'TP_REL_RL' and rp.activo = 1"
        );
        q.setParameter("id", idPersona);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listResp = q.getResultList();
        }
        return listResp;
    }
    
    /**
     * BUSCAR PERSONAS POR DOCUMENTOS ACTIVAS
     *
     * @param numDoc
     * @return
     */
    public SbPersona selectPersonaxRucNumDoc(String numDoc) {
        SbPersona spersona = null;
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where p.ruc = :numDoc or p.numDoc = :numDoc AND p.activo = 1"
        );
        q.setParameter("numDoc", numDoc);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            spersona = (SbPersona) q.getResultList().get(0);
        }
        return spersona;
    }
 
}
