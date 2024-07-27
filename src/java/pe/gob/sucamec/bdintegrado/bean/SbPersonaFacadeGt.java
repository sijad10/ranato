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
import pe.gob.sucamec.sel.gssp.jsf.GsspSolicitudAutorizacionSPPController;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author mespinoza
 */
@Stateless
public class SbPersonaFacadeGt extends AbstractFacade<SbPersonaGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbPersonaFacadeGt() {
        super(SbPersonaGt.class);
    }    
    
        /**
     * OBTIENE UNA PERSONA JURIDICA(EMPRESA) DE ACUERDO A UN RUC
     *
     * @param s
     * @return persona juridica que coincide con el criterio
     */
    public SbPersonaGt obtenerEmpresaXRuc(String s) {
        try {
            if (s == null) {
                s = "";
            }
            Query q = getEntityManager().createQuery("SELECT p FROM SbPersonaGt p WHERE p.ruc = :ruc");//and p.rznSocial IS NOT NULL
            q.setParameter("ruc", s);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (SbPersonaGt) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
        /**
     *
     * @param s
     * @return
     */
    public List<SbPersonaGt> buscar(String s) {
        if (s == null) {
            s = "";
        }
        Query q = getEntityManager().createQuery("SELECT p FROM SbPersonaGt p WHERE p.rznSocial LIKE :parametroRznSocial or p.ruc like :parametroRuc");
        q.setParameter("parametroRznSocial", "%" + s + "%");
        q.setParameter("parametroRuc", "%" + s + "%");
        return q.getResultList();
    }
    
       /**
     *
     * @param s
     * @return
     */
    public List<SbPersonaGt> buscarEmpresaTransporteGtExterna() {
        
        String query = "SELECT p FROM SbPersonaGt p "
                + "WHERE "
                + "p.ruc is not null and "
                + "p.activo = 1";
        Query q = getEntityManager().createQuery(query);        
        return q.getResultList();
    }
    
    /**
     * BUSCAR A PERSONA LOGUEADA EN SISTEMA SEL
     * @param tipoDoc
     * @param numDoc
     * @return 
     */
    
        public SbPersonaGt buscarPersonaSelCarneInstructor(String tipoDoc, String numDoc){
        
        if (tipoDoc == null) {
            tipoDoc = "";
        }
        if (numDoc == null) {
            numDoc = "";
        }
        
                
       String jpql ="select per"
               + " from SbPersonaGt per, SspCarneInstructor ci, SspModuloCarne mc, SspRegistro rs"
               + " where rs.empresaId.id=per.id and rs.activo = 1 and ci.instructorId.id = per.id and ci.activo = 1"
               + " and ci.registroId.id = mc.registroId.id and CURRENT_DATE between mc.fechaIni and mc.fechaFin "
               + " and per.numDoc =:numDoc";
        
        
         javax.persistence.Query q = em.createQuery(jpql);      
         q.setParameter("numDoc", numDoc.toUpperCase() );
         
        if(q.getResultList().isEmpty()){
            return null;
        }else{
            
            return (SbPersonaGt)q.getResultList().get(0);
        }
    }
    /**
     * BUSCAR A PERSONA LOGUEADA EN SISTEMA SEL
     * @param tipoDoc
     * @param numDoc
     * @return 
     */
    
    public SbPersonaGt buscarPersonaSel(String tipoDoc, String numDoc){
        if (tipoDoc == null) {
            tipoDoc = "";
        }
        if (numDoc == null) {
            numDoc = "";
        }

        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where (p.ruc = :numDoc or p.numDoc = :numDoc) AND "
                + " p.activo = 1 order by p.id desc"
        );
        q.setParameter("numDoc", numDoc.toUpperCase() );
        
        if(q.getResultList().isEmpty()){
            return null;
        }else{
            return (SbPersonaGt) q.getResultList().get(0);
        }
    }
    
    /**
     * BUSCAR PERSONAS PARA LICENCIA MANIPULADOR EXPLOSIVOS
     * @param tipoBusq
     * @param campo
     * @return 
     */
    public List<SbPersonaGt> buscarPersonaLME(String tipoBusq, String campo) {
        if (tipoBusq == null) {
            tipoBusq = "";
        }

        String jpql = "SELECT p FROM SbPersonaGt p "
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

    /**
     * BUSCAR PERSONAS POR DOCUMENTOS ACTIVAS
     * @param doc
     * @return 
     */
    public List<SbPersonaGt> selectPersonaActivaxDoc(String doc) {
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where p.numDoc = :doc AND p.activo = 1"
        );
        q.setParameter("doc", doc);
        return q.getResultList();
    }    
    /**
     * BUSCAR PERSONAS RELACIONADAS A LA PERSONA ORIGEN
     * @param idPersona
     * @return lista de Personas
     */
    public List<SbPersonaGt> listarPersonaXIdRelacionPersona(Long idPersona){
        List<SbPersonaGt> listaRes=null;
        String jpql="Select pe from SbRelacionPersonaGt rp, SbPersonaGt pe "
                + "where pe.id = rp.personaDestId.id and  rp.personaOriId.id = :idO and rp.activo = 1 ";
        Query q=em.createQuery(jpql);
        q.setParameter("idO", idPersona);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            listaRes=q.getResultList();
        }
        return listaRes;
    }

    public List<SbPersonaGt> listarPersonaXIdRelacionPersonaUnionSspRegistro(Long idPersona, Long registroId, Long tipoId){
        List<SbPersonaGt> listaRes=null;
        String jpql="Select distinct pe from SbRelacionPersonaGt rp, SbPersonaGt pe "
                + "where pe.id = rp.personaDestId.id and  rp.personaOriId.id = :idO and pe.activo = 1 and rp.activo = 1 and LENGTH(pe.numDoc) >= 8";
                //+ " and rp.tipoId.id = :tipoId and pe.tipoId.codProg='TP_PER_NAT' "
        if(registroId != null){
            jpql += " union Select pe from SbPersonaGt pe, SspRepresentanteRegistro repre " 
                 + " where pe.id = repre.representanteId.id and repre.activo=1 and repre.registroId.id = :registroId and LENGTH(pe.numDoc) >= 8";
                //+ " and repre.tipoId.id = :tipoId and pe.tipoId.codProg='TP_PER_NAT'"
        }
        Query q=em.createQuery(jpql);
        q.setParameter("idO", idPersona);
        //q.setParameter("tipoId", tipoId);
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            listaRes=q.getResultList();
        }
        return listaRes;
    }
        
    public List<SbPersonaGt> listarRelacionPersonaXTipoId_idPersOri_idPersDest(Long tipoId, Long idPersonaOrigen, Long idPersonaDestino){
        List<SbPersonaGt> listaRes=null;
        String jpql="Select pe from SbRelacionPersonaGt rp, SbPersonaGt pe "
                + " where pe.id = rp.personaDestId.id and rp.activo = 1 and pe.activo = 1"
                + " and rp.personaOriId.id = :idPersonaOrigen "
                + " and rp.personaDestId.id = :idPersonaDestino "
                + " and rp.tipoId.id = :tipoId ";
        Query q = em.createQuery(jpql);
        q.setParameter("tipoId", tipoId);
        q.setParameter("idPersonaOrigen", idPersonaOrigen);
        q.setParameter("idPersonaDestino", idPersonaDestino);
        listaRes=q.getResultList();
        return listaRes;
    }
    
    /**
     * BUSCAR PERSONAS POR DOCUMENTOS ACTIVAS
     * @param ruc
     * @return 
     */
    public SbPersonaGt selectPersonaActivaxRuc(String ruc) {
        SbPersonaGt spersona=new SbPersonaGt();
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where p.ruc = :ruc AND p.activo = 1"
        );
        q.setParameter("ruc", ruc);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            spersona=(SbPersonaGt)q.getResultList().get(0);
        }
        return spersona;
    }
    
    /**
     * BUSCAR PERSONAS POR DOCUMENTOS ACTIVAS
     * @return 
     */
    public SbPersonaGt selectPersonaSucamec() {
        SbPersonaGt spersona=new SbPersonaGt();
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where p.nomCom like :nomCom AND p.activo = 1"
        );
        q.setParameter("nomCom", "SUCAMEC%");
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            spersona=(SbPersonaGt)q.getResultList().get(0);
        }
        return spersona;
    }
    
    public List<SbPersonaGt> selectPersonaListado(String documento) {
        if (documento == null) {
            documento = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select p from SbPersonaGt p where p.activo = 1 "
                + " and p.numDoc like :documento "
                + " order by p.apePat asc"
        );
        q.setParameter("documento", "%" + documento.trim() + "%");
        return q.setMaxResults(300).getResultList();
    }
    
    
    public SbPersonaGt obtenerEmpresaId(Long id) {
        try {
            Query q = getEntityManager().createQuery("SELECT p FROM SbPersonaGt p WHERE p.id = :id");
            q.setParameter("id", id);
            if (q.getResultList().isEmpty()) {
                return null;
            } else {
                return (SbPersonaGt) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public SbPersonaGt selectPersonaActivaxTipoDocyNumDoc(Long tipoDoc, String doc) {
        try {
            javax.persistence.Query q = em.createQuery(
                    "select p from SbPersonaGt p where p.activo = 1 and p.tipoDoc.id = :tipoDoc and p.numDoc = :doc "
            );
            q.setParameter("tipoDoc", tipoDoc);
            q.setParameter("doc", doc);

            if(!q.getResultList().isEmpty()){
                return (SbPersonaGt) q.getResultList().get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<SbPersonaGt> selectPersonaActivoInactivoxTipoDocyNumDoc(Long tipoDoc, String doc) {
        List<SbPersonaGt> listaPersona = new ArrayList<SbPersonaGt>();
        try {
            javax.persistence.Query q = em.createQuery("select p from SbPersonaGt p where p.activo in (0,1) and p.tipoDoc.id = :tipoDoc and p.numDoc = :doc ");
            q.setParameter("tipoDoc", tipoDoc);
            q.setParameter("doc", doc);
            
            listaPersona = q.getResultList();            
        } catch (Exception e) {
            listaPersona = null;
            e.printStackTrace();
        }
        
        return listaPersona;
    }
    
    public SbPersonaGt buscarPersonaXNumDoc(String numDoc) {
        if (numDoc == null) {
            numDoc = "";
        }
        Query q = em.createQuery("select s from SbPersonaGt s where s.numDoc = :numDoc and s.activo = 1");
        q.setParameter("numDoc", numDoc);
        q.setMaxResults(MAX_RES);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            return (SbPersonaGt) q.getResultList().get(0);
        }
        return null;
    }
    
    
    public SbPersonaGt buscarPersonaXUserLogin(String nomLogin) {
        if (nomLogin == null) {
            nomLogin = "";
        }
        Query q = em.createQuery("select p from SbPersonaGt p, SbUsuarioGt u where p.id = u.personaId.id and u.activo = 1 and p.activo = 1 and u.login = :nomLogin ");
        q.setParameter("nomLogin", nomLogin);
        q.setMaxResults(MAX_RES);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            return (SbPersonaGt) q.getResultList().get(0);
        }
        return null;
    }
}
