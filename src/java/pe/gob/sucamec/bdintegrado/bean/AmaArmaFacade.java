/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaArma;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaArmaFacade extends AbstractFacade<AmaArma> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaArmaFacade() {
        super(AmaArma.class);
    }
    public List<AmaArma> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaArma a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    public List<AmaArma> listaArmasPorSolicitudRecojo(Long idSol){
        List<AmaArma> listRes=null;
        Query q = em.createQuery("select ar from AmaTarjetaPropiedad tp, AmaArma ar where tp.id in "
                + "(select distinct tp.id "
                + "from AmaSolicitudRecojo sr  LEFT JOIN sr.amaTarjetaPropiedadList tp "
                + "where  sr.id = :idSol and sr.activo=1 ) and ar.id = tp.armaId.id ");
        q.setParameter("idSol",idSol);
        q.setMaxResults(MAX_RES);
        if(q.getResultList()!=null&&!q.getResultList().isEmpty()){
            listRes=q.getResultList();
        }
        return listRes;
    }
    
    public List<AmaArma> listarArmasXCriteriosAnterior(String filtro, Long idCriterio, SbPersonaGt perVendedor) {
        StringBuilder sbQuery = new StringBuilder();
        List<AmaArma> listRes = new ArrayList<>();
        try {
            sbQuery.append("select a.armaId from AmaTarjetaPropiedad a where a.activo = 1 and a.armaId.activo = 1 and a.personaVendedorId.id = :idVend ");
            if (idCriterio == 1L) {
                sbQuery.append("and a.armaId.serie like :filtro ");
            }
            if (idCriterio == 2L) {
                sbQuery.append("and a.armaId.nroRua like :filtro ");
            }
            if (idCriterio == 3L) {
                sbQuery.append("and a.armaId.serie like :filtro or a.armaId.nroRua like :filtro ");
            }

            Query q = em.createQuery(sbQuery.toString());
            q.setParameter("filtro", "%"+filtro+"%");
            q.setParameter("idVend", perVendedor.getId());
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }
    
    public List<Map> listarArmasXCriterios(String filtro, Long idCriterio, SbPersona perComprador) {
        StringBuilder sbQuery = new StringBuilder();
        List<Map> listRes = new ArrayList<>();
        try {
            sbQuery.append("select a.armaId.id, a.armaId.nroRua, a.armaId.modeloId.tipoArmaId.nombre as tipoArma, a.armaId.modeloId.marcaId.nombre as marca, a.armaId.modeloId.modelo, a.armaId.serie, a.personaCompradorId.id as propietarioId, a.armaId.modeloId.id as modeloId "
                    + "from AmaTarjetaPropiedad a where a.activo = 1 and a.armaId.activo = 1 ");
            if (perComprador != null) {
                sbQuery.append("and a.personaCompradorId.id = :idComp ");
            }
            if (idCriterio == 1L) {
                sbQuery.append("and trim(a.armaId.serie) = :filtro ");
            }
            if (idCriterio == 2L) {
                sbQuery.append("and trim(a.armaId.nroRua) = :filtro ");
            }
            if (idCriterio == 3L) {
                sbQuery.append("and (trim(a.armaId.serie) = :filtro or trim(a.armaId.nroRua) = :filtro) ");
            }

            Query q = em.createQuery(sbQuery.toString());
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("filtro", filtro);
            if (perComprador != null) {
                q.setParameter("idComp", perComprador.getId());
            }
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listRes;
    }

    public Map buscarDatosEmitidas(Long nroLicencia){
        try {
            String jpql = "select a.estadoId.id as estadoId, a.situacionId.id as situacionId"+
                          " from AmaArma a where a.activo = 1 and a.licenciaDisca = :licencia ";
            
            Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("licencia", nroLicencia);
            
            if(q.getResultList().isEmpty()){
                return null;
            }else{
                return (Map) q.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public int contarTarjetaPropiedadEmitidasByIdEmpresa(Long idEmpresa) {
        int cont = 0;
        
        String jpql = "SELECT COUNT(*) " +
                      " FROM BDINTEGRADO.AMA_ARMA A " +
                      " INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = A.ID " +
                      " INNER JOIN RMA1369.AM_ARMA@SUCAMEC AM ON AM.NRO_LIC = A.LICENCIA_DISCA " +
                      " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = AM.NRO_LIC " +
                      " WHERE T.ACTIVO = 1 AND A.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?1 ";

        Query q = em.createNativeQuery(jpql);
        q.setParameter(1, idEmpresa);
         
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
    
    public int contarArmasDiscaBySerie(String serie) {
        int cont = 0;
        
        String jpql = "SELECT COUNT(*) FROM RMA1369.AM_ARMA@SUCAMEC A " +
                      " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC " +
                      " WHERE A.NRO_SERIE = ?1 ";

        Query q = em.createNativeQuery(jpql);
        q.setParameter("1", serie);
         
       List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
    
    public AmaArma obtenerArmaPorModeloSerie(Long modeloId, String serie, String rua) {
        String plsql = "select a from AmaArma a where a.activo = 1 ";
        if (modeloId != null) {
            plsql += "and a.modeloId.id = :modeloId ";
        }
        if (serie != null && !"".equals(serie)) {
            plsql += "and a.serie = :serie ";
        }
        if (rua != null && !"".equals(rua.trim())) {
            plsql += "and a.nroRua = :rua ";
        }
        plsql += "order by a.id desc ";
        javax.persistence.Query q = em.createQuery(plsql);

        if (modeloId != null) {
            q.setParameter("modeloId", modeloId);
        }
        if (serie != null && !"".equals(serie)) {
            q.setParameter("serie", serie.toUpperCase().trim());
        }
        if (rua != null && !"".equals(rua.trim())) {
            q.setParameter("rua", rua.toUpperCase().trim());
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaArma) q.getResultList().get(0);
        }
        return null;
    }

    public AmaArma obtenerArmaPorSerie(String serie, String rua) {
        String plsql = "select a from AmaArma a where a.activo = 1 ";
        if (serie != null && !"".equals(serie)) {
            plsql += "and a.serie = :serie ";
        }
        if (rua != null && !"".equals(rua.trim())) {
            plsql += "and a.nroRua = :rua ";
        }
        plsql += "order by a.id desc ";
        javax.persistence.Query q = em.createQuery(plsql);

        if (serie != null && !"".equals(serie)) {
            q.setParameter("serie", serie.toUpperCase().trim());
        }
        if (rua != null && !"".equals(rua.trim())) {
            q.setParameter("rua", rua.toUpperCase().trim());
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaArma) q.getResultList().get(0);
        }
        return null;
    }
    
    public Long obtenerLicenciaDiscaBySerieByTipoArma(String serie, Integer tipoArma) {
        Long nroLic = null;
        
        String jpql = "SELECT A.NRO_LIC FROM RMA1369.AM_ARMA@SUCAMEC A " +
                      " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC " +
                      " WHERE A.NRO_SERIE = ?1 AND A.TIP_ARM = ?2 AND A.SIT_ARM != 16 ";

        Query q = em.createNativeQuery(jpql);
        q.setParameter("1", serie);
        q.setParameter("2", tipoArma);
         
       List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            nroLic = _values.longValue();
            break;
        }
        return nroLic;
    }
    
    public AmaArma buscarArmaById(Long armaId) {
        String plsql = "select a from AmaArma a where a.id = :armaId  ";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("armaId", armaId);
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaArma) q.getResultList().get(0);
        }
        return null;
    }
    
    public List<Map> buscarLicenciasDiscaXNroSerieXTipoArma(String ruc, String nroSerie, Integer tipoArma) {
        try{
            // Sin verificar
            String jpql = "SELECT DISTINCT A.NRO_LIC, A.NRO_SERIE, A.TIP_ARM, ART.DES_ARM, A.COD_MARCA, \n"+
                            " M.DES_MARCA, A.COD_MODELO, M.DES_MODELO, A.CALIBRE, A.SIT_ARM AS COD_ESTADO, SI.DES_SIT ESTADO \n" +
                            " FROM RMA1369.AM_ARMA@SUCAMEC A \n" +
                            " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC \n" +
                            " INNER JOIN RMA1369.SIT_ARMA@SUCAMEC SI ON SI.SIT_ARMA = A.SIT_ARM \n" +
                            " LEFT JOIN RMA1369.ARTICULO@SUCAMEC ART ON ART.TIP_ART = A.TIP_ART AND ART.TIP_ARM = A.TIP_ARM \n" +
                            " LEFT JOIN RMA1369.AM_MARCA@SUCAMEC M ON M.TIP_ART = A.TIP_ART AND M.TIP_ARM = A.TIP_ARM AND M.COD_MARCA = A.COD_MARCA AND M.COD_MODELO = A.COD_MODELO \n" +
                            " WHERE L.COD_USR IN ("+ruc+") AND A.NRO_SERIE = ?2 AND A.TIP_ARM = ?3 AND L.TIP_USR = 1 AND A.SIT_ARM != 16 ";            
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(2, nroSerie);
            q.setParameter(3, tipoArma);

            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    public AmaArma buscarArmaBySerieyTipoArma(String serie, String TipoArmaCodProg) {
        String plsql = "select a from AmaArma a where a.serie = :serie and a.activo = 1 and a.modeloId.tipoArmaId.codProg = :tipoArma";
        javax.persistence.Query q = em.createQuery(plsql);
        q.setParameter("serie", serie);
        q.setParameter("tipoArma", TipoArmaCodProg);
        
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaArma) q.getResultList().get(0);
        }
        return null;
    }

    public AmaArma buscaArmasXSerieRUA(String serie, String rua) {
        Query q = em.createQuery("select a from AmaArma a where a.activo = 1 and a.serie = :serie and a.nroRua = :rua ");
        q.setParameter("serie", serie == null ? "" : serie.toUpperCase().trim());
        q.setParameter("rua", rua == null ? "" : rua.toUpperCase().trim());
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            return (AmaArma) q.getResultList().get(0);
        }
        return null;
    }

    public List<Map> listarArmasXCriterios(String filtro, Long idCriterio, SbPersonaGt perComprador) {
        StringBuilder sbQuery = new StringBuilder();
        List<Map> listRes = new ArrayList<>();
        try {
            sbQuery.append("select a.armaId.id, a.armaId.nroRua, a.armaId.modeloId.tipoArmaId.nombre as tipoArma, a.armaId.modeloId.marcaId.nombre as marca, a.armaId.modeloId.modelo, a.armaId.serie, a.personaCompradorId.id as propietarioId, a.armaId.modeloId.id as modeloId "
                    + "from AmaTarjetaPropiedad a where a.activo = 1 and a.armaId.activo = 1 ");
            if (perComprador != null) {
                sbQuery.append("and a.personaCompradorId.id = :idComp ");
            }
            if (idCriterio == 1L) {
                sbQuery.append("and trim(a.armaId.serie) = :filtro ");
            }
            if (idCriterio == 2L) {
                sbQuery.append("and trim(a.armaId.nroRua) = :filtro ");
            }
            if (idCriterio == 3L) {
                sbQuery.append("and (trim(a.armaId.serie) = :filtro or trim(a.armaId.nroRua) = :filtro) ");
            }

            Query q = em.createQuery(sbQuery.toString());
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("filtro", filtro);
            if (perComprador != null) {
                q.setParameter("idComp", perComprador.getId());
            }
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listRes;
    }
    
}
