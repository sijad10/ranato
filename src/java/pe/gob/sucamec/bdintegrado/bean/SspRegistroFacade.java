/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.SbDepartamentoGt;
import pe.gob.sucamec.bdintegrado.data.SbProvinciaGt;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SspCartaFianza;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SspRegistroFacade extends AbstractFacade<SspRegistro> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SspRegistroFacade() {
        super(SspRegistro.class);
    }

    public SspRegistroFacade(EntityManager em, Class<SspRegistro> entityClass) {
        super(entityClass);
        this.em = em;
    }
    
    public String getAmbito_ByRegistroID(Long regId) {       

        StringBuilder jpql = new StringBuilder()
                .append("SELECT (DEPA.NOMBRE || ' / ' || PROV.NOMBRE || ' / ' || DIST.NOMBRE) AS AMBITO ")
                .append(" FROM BDINTEGRADO.SSP_REGISTRO R ")
                .append(" LEFT JOIN BDINTEGRADO.SSP_LOCAL_REGISTRO LR ON (R.ID = LR.REGISTRO_ID) ")
                .append(" LEFT JOIN BDINTEGRADO.ssp_local_autorizacion L ON (L.ID = LR.LOCAL_ID) ")
                .append(" LEFT JOIN BDINTEGRADO.sb_distrito DIST ON (DIST.ID = L.DISTRITO) ")
		.append(" LEFT JOIN BDINTEGRADO.sb_provincia PROV ON (PROV.ID = dist.provincia_id) ")
		.append(" LEFT JOIN BDINTEGRADO.sb_DEPARTAMENTO DEPA ON (DEPA.ID = prov.departamento_id) ")
                .append(" WHERE R.ACTIVO = 1 AND R.ID = ?1 ");

        Query queryDisca = em.createNativeQuery(jpql.toString());
        queryDisca.setParameter(1, regId);
        
        String nomDepa = queryDisca.getSingleResult().toString();        
        return nomDepa;
    }

    public List<SspRegistro> traerRegistros(Long empresaId, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, String cod_modalidad) {
//        TypedQuery<SspRegistro> consulta = em.createQuery("", SspRegistro.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SspRegistro> criteriaQuery = cb.createQuery(SspRegistro.class);
        Root<SspRegistro> root = criteriaQuery.from(SspRegistro.class);
        Join<SspRegistro, SbPersonaGt> joinP = root.join("empresaId");
        Join<SspRegistro, TipoBaseGt> joinModalidades = root.join("tipoProId");
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        Predicate condicionActivo = cb.equal(root.get("activo"), 1);
        Predicate condicion = cb.equal(joinP.get("id"), empresaId);
        Predicate condicion2 = null;
        System.out.println("cod_modalidad:   " + cod_modalidad);
        if (cod_modalidad != "") {
//            condicion2 = joinModalidades.get("tipoId").get("codProg").in('"'+cod_modalidad+'"');
            condicion2 = cb.equal(joinModalidades.get("codProg"), cod_modalidad);
            System.out.println("condicion2 1");
        } else {
            condicion2 = joinModalidades.get("tipoId").get("codProg").in("TP_GSSP_AUTORIZACION", "TP_AUTORIZACION_SEL");
            System.out.println("condicion2 2");
        }
        //and s.tipoProId.id = :tipoProId
//        criteriaQuery.where(condicion);
        criteriaQuery.where(cb.and(condicionActivo, condicion, condicion2));
        criteriaQuery.select(root).distinct(true).orderBy(cb.desc(root.get("id")));
        List<SspRegistro> result
                = em
                .createQuery(criteriaQuery)
                .setMaxResults(pageSize)
                .setFirstResult(first)
                .getResultList();

        return result;

        /*
        System.out.println("REG FACA ENTRÓ");
        String jpql = "select s "
                + " from SspRegistro s"
                + " where s.empresaId.id = :empresaId "
                + " and s.activo = 1" 
                + " AND s.tipoProId.id in (select ts.id from TipoBaseGt ts where ts.tipoId.codProg IN ('TP_GSSP_AUTORIZACION', 'TP_AUTORIZACION_SEL')) "
                + " ORDER BY s.id desc";
//
        Query q = em.createQuery(jpql);  
        q.setParameter("empresaId", empresaId );
        q.setFirstResult(first);
        q.setMaxResults(pageSize);
        
        // Si filters no está vacío, aplica los filtros a la consulta
        if (filters != null && !filters.isEmpty()) {
           for (Map.Entry<String, Object> entry : filters.entrySet()) {
              String filterProperty = entry.getKey();
              Object filterValue = entry.getValue();
              if (filterValue != null) {
                 q.where(builder.like(root.get(filterProperty), "%" + filterValue.toString() + "%"));
              }
           }
        }
        
        return q.getResultList();*/
    }

    public int counttraerRegistros(Long empresaId, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        String jpql = "select count(s) "
                + " from SspRegistro s"
                + " where s.empresaId.id = :empresaId "
                + " and s.activo = 1"
                + " AND s.tipoProId.id in (select ts.id from TipoBaseGt ts where ts.tipoId.codProg IN ('TP_GSSP_AUTORIZACION', 'TP_AUTORIZACION_SEL')) "
                + " ORDER BY s.id desc";
//
        Query q = em.createQuery(jpql);

        q.setParameter("empresaId", empresaId);

//        return (Long)q.getSingleResult();
        return ((Number) q.getSingleResult()).intValue();

    }

    public List<SspRegistro> buscarRegistroEmpresaXDepartamentoPrincipal(Long empresaId, Long registroId, Long distritoId, Long modalidadId) {
        //Todos los estados menos NO PRESENTADO	TP_ECC_NPR, CANCELADO TP_ECC_CAN, DESISTIMIENTO	TP_ECC_DES, ANULADO TP_ECC_ANU
        String jpql = "select r "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc "
                + " where r.activo = 1 and r.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_FDP') "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "
                + " and lr.activo = 1"
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1"
                //Busca todos los distritos del Departamento
                + " and loc.distritoId.id in (select dist.id from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id = :distritoId ) )";

        //Que no sea el mismo Registro de consulta
        if (registroId != null) {
            jpql += " and r.id != :registroId ";
        }

        jpql += " order by r.fecha desc ";

        Query q = em.createQuery(jpql);
        if (empresaId != null) {
            q.setParameter("empresaId", empresaId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }
        if (distritoId != null) {
            q.setParameter("distritoId", distritoId);
        }
        if (modalidadId != null) {
            q.setParameter("modalidadId", modalidadId);
        }
        return q.getResultList();
    }

    public SspRegistro buscarRegistroXRucXModalidadXDocVigente(Long administradoId, Long modalidadId, Long vigilanteId, Long registroId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.activo = 1 "
                + " and s.carneId.modalidadId.id = :modalidadId "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_FDP','TP_ECC_DES') "
                + " and s.tipoOpeId.codProg != 'TP_REGIST_NULL' ";
        if (administradoId != null) {
            jpql += " and s.empresaId.id = :administradoId ";
        }
        if (registroId != null) {
            jpql += " and s.id != :registroId ";
        }
        jpql += " order by s.fecha desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("modalidadId", modalidadId);
        q.setParameter("vigilanteId", vigilanteId);
        if (administradoId != null) {
            q.setParameter("administradoId", administradoId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }
        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }

        return null;
    }

    public SspRegistro buscarRegistroCesadoXRucXModalidadXDocNoDuplicado(Long administradoId, Long modalidadId, Long vigilanteId, Long registroId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.activo = 1 "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.tipoProId.codProg in ('TP_GSSP_CCRN') "
                + " and s.tipoOpeId.codProg != 'TP_OPE_COP' ";
        if (administradoId != null) {
            jpql += " and s.empresaId.id = :administradoId ";
        }
        if (modalidadId != null) {
            jpql += "  and s.carneId.modalidadId.id = :modalidadId ";
        }
        if (registroId != null) {
            jpql += " and s.id != :registroId ";
        }
        jpql += " order by s.carneId.fechaBaja desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        if (administradoId != null) {
            q.setParameter("administradoId", administradoId);
        }
        if (modalidadId != null) {
            q.setParameter("modalidadId", modalidadId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }

        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public SspRegistro buscarRegistroCesadoXRucXModalidadXDoc(Long administradoId, Long modalidadId, Long vigilanteId, Long registroId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.activo = 1 "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.tipoProId.codProg = 'TP_GSSP_CCRN' " // procedimiento cesado
                + " and s.tipoOpeId.codProg != 'TP_REGIST_NULL' ";

        if (administradoId != null) {
            jpql += " and s.empresaId.id = :administradoId ";
        }
        if (modalidadId != null) {
            jpql += "  and s.carneId.modalidadId.id = :modalidadId ";
        }
        if (registroId != null) {
            jpql += " and s.id != :registroId ";
        }
        jpql += " order by s.carneId.fechaBaja desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        if (administradoId != null) {
            q.setParameter("administradoId", administradoId);
        }
        if (modalidadId != null) {
            q.setParameter("modalidadId", modalidadId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }

        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public SspRegistro buscarUltimoRegistroXRucXModalidadXDoc(Long administradoId, Long modalidadId, Long vigilanteId, Long registroId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.activo = 1 "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_FDP','TP_ECC_DES') "
                + " and s.tipoProId.codProg in ('TP_GSSP_ECRN','TP_GSSP_CCRN') "
                + " and s.tipoOpeId.codProg != 'TP_REGIST_NULL' ";

        if (administradoId != null) {
            jpql += " and s.empresaId.id = :administradoId ";
        }
        if (modalidadId != null) {
            jpql += "  and s.carneId.modalidadId.id = :modalidadId ";
        }
        if (registroId != null) {
            jpql += " and s.id != :registroId ";
        }
        jpql += " order by s.carneId.fechaBaja desc, s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        if (administradoId != null) {
            q.setParameter("administradoId", administradoId);
        }
        if (modalidadId != null) {
            q.setParameter("modalidadId", modalidadId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }

        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public SspRegistro buscarRegistroById(Long registroId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.id = :registroId and s.activo = 1";

        Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId);
        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public SspRegistro buscarRegistroByNroSolicitud(String nroSolicitiud) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.nroSolicitiud = :nroSolicitiud ";

        Query q = em.createQuery(jpql);
        q.setParameter("nroSolicitiud", nroSolicitiud);
        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public SspRegistro buscarRegistroReferenciadoCesadoById(Long registroId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.registroId.id = :registroId and s.activo = 1 "
                + " and s.tipoProId.codProg in ('TP_GSSP_CCRN') ";    // procedimiento cesado

        Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId);

        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public SspRegistro buscarRegistrActivosVigenteNoDuplicado(Long modalidadId, Long vigilanteId) {
        String jpql = "select s "
                + " from SspRegistro s "
                + " where s.activo = 1  "
                + " and s.carneId.modalidadId.id = :modalidadId "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.tipoProId.codProg not in ('TP_GSSP_CCRN','TP_GSSP_DCRN') " // no contar procedimientos de cese y devolución
                + " and s.estadoId.codProg not in ('TP_ECC_NPR','TP_ECC_CRE','TP_ECC_OBS','TP_ECC_TRA','TP_ECC_FDP','TP_ECC_DES') "
                + " and s.tipoOpeId.codProg not in ('TP_REGIST_NULL') ";
        jpql += " order by s.fecha desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("modalidadId", modalidadId);
        q.setParameter("vigilanteId", vigilanteId);

        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public int obtenerTotalRegistrosObservados(String doc) {
        int cont = 0;
        String jpql = "SELECT COUNT(c) "
                + " FROM SspRegistro c "
                + " WHERE c.activo = 1 and c.carneId.activo = 1 "
                + " and (c.empresaId.ruc = :doc or c.empresaId.numDoc = :doc) "
                + " and c.estadoId.codProg = 'TP_ECC_OBS' ";

        Query q = em.createQuery(jpql);
        q.setParameter("doc", doc);

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }

    public Long obtenerNroSolicitudEmisionRegistro() {
        Long secuencia = 0L;
        String jpql = "SELECT BDINTEGRADO.SEQ_SSPREG_NROSOL_EMISION.NEXTVAL FROM DUAL";
        Query q = em.createNativeQuery(jpql);
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            secuencia = _values.longValue();
            break;
        }
        return secuencia;
    }

    public Long obtenerNroSolicitudCeseRegistro() {
        Long secuencia = 0L;
        String jpql = "SELECT BDINTEGRADO.SEQ_SSPREG_NROSOL_CESE.NEXTVAL FROM DUAL";
        Query q = em.createNativeQuery(jpql);
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            secuencia = _values.longValue();
            break;
        }
        return secuencia;
    }

    public Long obtenerNroSolicitudDevolucionRegistro() {
        Long secuencia = 0L;
        String jpql = "SELECT BDINTEGRADO.SEQ_SSPREG_NROSOL_DEVOLUCION.NEXTVAL FROM DUAL";
        Query q = em.createNativeQuery(jpql);
        List<BigDecimal> results = q.getResultList();
        for (BigDecimal _values : results) {
            secuencia = _values.longValue();
            break;
        }
        return secuencia;
    }

    public List<SspRegistro> buscarCarnesByNroSolicitudCese(String nroSolicitud) {
        String jpql = "select r "
                + " from SspCarne s "
                + " left join s.sspRegistroList r "
                + " where r.activo = 1 and s.emitida = 1 "
                + "  and r.nroSolicitiud = :nroSolicitud "
                + "  and r.tipoProId.codProg = 'TP_GSSP_CCRN' ";

        Query q = em.createQuery(jpql);
        q.setParameter("nroSolicitud", nroSolicitud);

        return q.getResultList();
    }

    public int buscarRegistroOtraEmpresaXRuc(String ruc, Long vigilanteId, Long registroId) {
        int cont = 0;
        String jpql = "select count(s) "
                + " from SspRegistro s "
                + " where s.activo = 1 and s.carneId.activo = 1 "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.carneId.modalidadId.codProg not in ('TP_MCO_SIS','TP_MCO_PAT')"
                + " and s.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_FDP','TP_ECC_DES') "
                + " and s.tipoOpeId.codProg != 'TP_REGIST_NULL' ";
        if (ruc != null) {
            jpql += " and s.empresaId.ruc != :ruc ";
        }
        if (registroId != null) {
            jpql += " and s.id != :registroId ";
        }
        jpql += " order by s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        if (ruc != null) {
            q.setParameter("ruc", ruc);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }

    public int contarRegistroAnteriorXRucXModalidadXDoc(Long administradoId, Long modalidadId, Long vigilanteId, Long registroId) {
        int cont = 0;
        String jpql = "select count(s) "
                + " from SspRegistro s "
                + " where s.activo = 1 "
                + " and s.carneId.vigilanteId.id = :vigilanteId  "
                + " and s.tipoOpeId.codProg != 'TP_REGIST_NULL' ";

        if (administradoId != null) {
            jpql += " and s.empresaId.id = :administradoId ";
        }
        if (modalidadId != null) {
            jpql += " and s.carneId.modalidadId.id = :modalidadId ";
        }
        if (registroId != null) {
            jpql += " and s.id != :registroId ";
        }

        Query q = em.createQuery(jpql);
        q.setParameter("vigilanteId", vigilanteId);
        if (administradoId != null) {
            q.setParameter("administradoId", administradoId);
        }
        if (modalidadId != null) {
            q.setParameter("modalidadId", modalidadId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }

    public int buscarRegistroNoFinalizadoVigenteReferenciado(Long modalidadId, Long vigilanteId, Long registroId) {
        int cont = 0;
        String jpql = "select count(s) "
                + " from SspRegistro s "
                + " where s.activo = 1  "
                + " and s.carneId.modalidadId.id = :modalidadId "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.tipoProId.codProg = 'TP_GSSP_ECRN' "
                + " and s.estadoId.codProg not in ('TP_ECC_APR','TP_ECC_NPR','TP_ECC_FDP','TP_ECC_DES') "
                + " and s.tipoOpeId.codProg not in ('TP_REGIST_NULL') "
                + " and s.registroId.id = :registroId ";
        jpql += " order by s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("modalidadId", modalidadId);
        q.setParameter("vigilanteId", vigilanteId);
        q.setParameter("registroId", registroId);

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }

    public int buscarRegistroVigenteReferenciado(Long modalidadId, Long vigilanteId, Long registroId) {
        int cont = 0;
        String jpql = "select count(s) "
                + " from SspRegistro s "
                + " where s.activo = 1  "
                + " and s.carneId.modalidadId.id = :modalidadId "
                + " and s.carneId.vigilanteId.id = :vigilanteId "
                + " and s.tipoProId.codProg = 'TP_GSSP_ECRN' "
                + " and s.tipoOpeId.codProg not in ('TP_REGIST_NULL') "
                + " and s.estadoId.codProg not in ('TP_ECC_NPR','TP_ECC_FDP','TP_ECC_DES')"
                + " and s.registroId.id = :registroId ";
        jpql += " order by s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("modalidadId", modalidadId);
        q.setParameter("vigilanteId", vigilanteId);
        q.setParameter("registroId", registroId);

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }

    public List<Map> buscarBandejaSolicitudAutorizacion(HashMap nmap) {       
        
        String xRucIni = nmap.get("filtroAdministradoNumDoc").toString(); 
        xRucIni = xRucIni.substring(0, 2);
        
        System.out.println("xRucIni->"+xRucIni);
        
        String jpql = "";            
        if(xRucIni.equals("20")){
            
                jpql = "SELECT "
                + " s.id as ID, "
                + " s.nroExpediente as NRO_EXPEDIENTE, "
                + " s.empresaId.id as ADMINISTRADO_ID, "
                + " (case when (s.empresaId.rznSocial != null) then s.empresaId.rznSocial else  concat(s.empresaId.nombres, ' ', s.empresaId.apePat, ' ', s.empresaId.apeMat) end) as ADMINISTRADO, "
                + " s.nroSolicitiud as NRO_SOLICITUD, "
                + " s.tipoProId.id as MODALIDAD_ID, "
                + " s.tipoProId.nombre as MODALIDAD_NOMBRE, "
                + " s.tipoProId.codProg as MODALIDAD_CODPROG, "
                + " s.tipoAutId.id as TIP_AUT_ID, "
                + " s.tipoAutId.nombre as TIP_AUT_NOMBRE, "
                + " s.tipoOpeId.id as TIP_OPE_ID, "
                + " s.tipoOpeId.nombre as TIP_OPE_NOMBRE, "
                + " s.tipoRegId.id as TIP_REG_ID, "
                + " s.tipoRegId.nombre as TIP_REG_NOMBRE, "
                + " s.audLogin as USUARIO, "
                + " s.usuarioCreacionId.id as USUARIO_ID, "
                + " s.usuarioCreacionId.login as USUARIO_LOGIN, "
                + " s.estadoId.id as ESTADO_ID, "
                + " s.estadoId.nombre as ESTADO_NOMBRE, "
                + " s.estadoId.codProg AS ESTADO_CODPROG,"
                + " s.fecha as FECHA, "
                + " s.activo as ACTIVO, "
                + " s.observacion AS OBSERVACION, "                
                + " lr.localId.distritoId.nombre AS DISTRITO, "
                + " lr.localId.distritoId.provinciaId.nombre AS PROVINCIA, "
                + " lr.localId.distritoId.provinciaId.departamentoId.nombre AS DEPARTAMENTO "                
                + " FROM SspRegistro s "
                + " LEFT JOIN s.sspLocalRegistroList lr"
                + " WHERE s.activo = 1 "
                + " AND s.empresaId.id = :empresaId "
                + " AND s.tipoProId.id in (select ts.id from TipoBaseGt ts where ts.tipoId.codProg IN ('TP_GSSP_AUTORIZACION', 'TP_AUTORIZACION_SEL')) "
                + "";
                
        }else{
            
            jpql = "SELECT "
                + " s.id as ID, "
                + " s.nroExpediente as NRO_EXPEDIENTE, "
                + " s.empresaId.id as ADMINISTRADO_ID, "
                + " (case when (s.empresaId.rznSocial != null) then s.empresaId.rznSocial else  concat(s.empresaId.nombres, ' ', s.empresaId.apePat, ' ', s.empresaId.apeMat) end) as ADMINISTRADO, "
                + " s.nroSolicitiud as NRO_SOLICITUD, "
                + " s.tipoProId.id as MODALIDAD_ID, "
                + " s.tipoProId.nombre as MODALIDAD_NOMBRE, "
                + " s.tipoProId.codProg as MODALIDAD_CODPROG, "
                + " s.tipoAutId.id as TIP_AUT_ID, "
                + " s.tipoAutId.nombre as TIP_AUT_NOMBRE, "
                + " s.tipoOpeId.id as TIP_OPE_ID, "
                + " s.tipoOpeId.nombre as TIP_OPE_NOMBRE, "
                + " s.tipoRegId.id as TIP_REG_ID, "
                + " s.tipoRegId.nombre as TIP_REG_NOMBRE, "
                + " s.audLogin as USUARIO, "
                + " s.usuarioCreacionId.id as USUARIO_ID, "
                + " s.usuarioCreacionId.login as USUARIO_LOGIN, "
                + " s.estadoId.id as ESTADO_ID, "
                + " s.estadoId.nombre as ESTADO_NOMBRE, "
                + " s.estadoId.codProg AS ESTADO_CODPROG,"
                + " s.fecha as FECHA, "
                + " s.activo as ACTIVO, "
                + " s.observacion AS OBSERVACION, "                
                + " '---' AS DISTRITO, "
                + " '---' AS PROVINCIA, "
                + " '---' AS DEPARTAMENTO "                
                + " FROM SspRegistro s "
                + " WHERE s.activo = 1 "
                + " AND s.empresaId.id = :empresaId "
                + " AND s.tipoProId.id in (select ts.id from TipoBaseGt ts where ts.tipoId.codProg IN ('TP_GSSP_AUTORIZACION', 'TP_AUTORIZACION_SEL')) "
                + ""; 
                              
        }

        if (nmap.get("buscarPor") != null) {
            switch (nmap.get("buscarPor").toString()) {
                case "1": //NRO_SOLICITUD
                    jpql += " and s.nroSolicitiud like :nroSolicitiud";
                    break;
                case "2": //NRO_EXPEDIENTE
                    jpql += " and s.nroExpediente like :nroExpediente";
                    break;
                case "4": //MODALIDAD O  TIPO DE SEGURIDAD
                    jpql += " and s.tipoProId.id = :tipoProId";
                    break;
            }
        }

        if (nmap.get("filtroTipoEstadoId") != null) {
            jpql += " and s.estadoId.id = :estadoId";
        }

        if (nmap.get("filtroTipoAutID") != null) {
            jpql += " and s.tipoAutId.id = :tipoAutId";
        }

        if (nmap.get("filtroTipoOpeID") != null) {
            jpql += " and s.tipoOpeId.id = :tipoOpeId";
        }

        if (nmap.get("filtroFechaIni") != null && nmap.get("filtroFechaFin") != null) {
            jpql += " and (FUNC('trunc',s.fecha) BETWEEN FUNC('trunc',:fechaIni) and FUNC('trunc',:fechaFin) )";
        }

        jpql += " ORDER BY s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("empresaId", (Long) nmap.get("filtroAdministradoId"));

        if (nmap.get("buscarPor") != null) {
            switch (nmap.get("buscarPor").toString()) {
                case "1": //NRO_SOLICITUD
                    q.setParameter("nroSolicitiud", "%" + nmap.get("filtroNumero") + "%");
                    break;
                case "2": //NRO_EXPEDIENTE
                    q.setParameter("nroExpediente", "%" + nmap.get("filtroNumero") + "%");
                    break;
                case "4": //MODALIDAD O TIPO SEGURIDAD
                    q.setParameter("tipoProId", (Long) nmap.get("filtroTipoSeguridadId"));
                    break;
            }
        }

        if (nmap.get("filtroTipoEstadoId") != null) {
            q.setParameter("estadoId", (Long) nmap.get("filtroTipoEstadoId"));
        }

        if (nmap.get("filtroTipoOpeID") != null) {
            q.setParameter("tipoOpeId", (Long) nmap.get("filtroTipoOpeID"));
        }

        if (nmap.get("filtroTipoAutID") != null) {
            q.setParameter("tipoAutId", (Long) nmap.get("filtroTipoAutID"));
        }

        if (nmap.get("filtroFechaIni") != null && nmap.get("filtroFechaFin") != null) {
            q.setParameter("fechaIni", (Date) nmap.get("filtroFechaIni"), TemporalType.DATE);
            q.setParameter("fechaFin", (Date) nmap.get("filtroFechaFin"), TemporalType.DATE);
        }

        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }

    public List<SspRegistro> listarRegistroVoucherPago(Long idComprobante) {
        String jpql = "select r "
                + " from SspRegistro r "
                + " where r.activo = 1 and r.comprobanteId = :idComprobante";
        Query q = em.createQuery(jpql);
        q.setParameter("idComprobante", idComprobante);
        return q.getResultList();
    }

    public Long obtenerIdUsuarioTDparaTransmitirAutorizacionSeguridad(String strListaUsuTD) {
        String sql = "SELECT USUARIO FROM (\n"
                + "SELECT U.ID USUARIO,COUNT(EVS.ID) CANT FROM (\n"
                + "SELECT \n"
                + "regexp_substr(regexp_substr('" + strListaUsuTD.trim() + "','[^,]+', 1, level),'[^+]+',1,1) USU\n"
                + "FROM DUAL connect by regexp_substr('" + strListaUsuTD.trim() + "', '[^,]+', 1, level) is not null) UP\n"
                + "INNER JOIN BDINTEGRADO.SB_USUARIO U ON U.LOGIN = UP.USU AND U.ACTIVO = 1\n"
                + "LEFT JOIN BDINTEGRADO.SSP_REGISTRO EVS ON EVS.USUARIO_RECEPCION_ID = U.ID AND EVS.ACTIVO =1 AND TRUNC(SYSDATE) = TRUNC(EVS.FECHA) \n"
                + "GROUP BY U.ID ORDER BY COUNT(EVS.ID) ASC, U.ID ASC\n"
                + ") WHERE ROWNUM = 1";
        Query query = em.createNativeQuery(sql);
        Long cant = ((BigDecimal) query.getSingleResult()).longValue();
        return cant;
    }

    public List<SspRegistro> listaSolitudesPresentados_Vigentes(String modalidadCodProg, Long vigilanteId) {
        String jpql = "select s from SspRegistro s "
                + " where s.activo = 1  "
                + " and s.tipoProId.codProg = :modalidadCodProg "
                + " and s.empresaId.id = :vigilanteId "
                + " and s.estadoId.codProg in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_XFIR','TP_ECC_APR') ";
        jpql += " order by s.fecha desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("modalidadCodProg", modalidadCodProg);
        q.setParameter("vigilanteId", vigilanteId);

        if (!q.getResultList().isEmpty()) {
            return q.getResultList();
        }
        return null;
    }

    public List<SspRegistro> buscarRegistroEmpresaXDepartamentoTipoLocalPrincipal(Long empresaId, Long registroId, Long distritoId, Long modalidadId, Long idTipoLocal) {
        //Todos los estados menos NO PRESENTADO	TP_ECC_NPR, CANCELADO TP_ECC_CAN, DESISTIMIENTO	TP_ECC_DES, ANULADO TP_ECC_ANU
        String jpql = "select r "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl "
                + " where r.activo = 1 and r.estadoId.codProg not in ('TP_ECC_NPR', 'TP_ECC_CAN','TP_ECC_DES','TP_ECC_ANU','TP_ECC_FDP') "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and tl.tipoLocalId.id = :idTipoLocal "
                //Busca todos los distritos del Departamento
                + " and loc.distritoId.id in (select dist.id from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id = :distritoId ) )";

        //Que no sea el mismo Registro de consulta
        if (registroId != null) {
            jpql += " and r.id != :registroId ";
        }

        //Las Autorizaciones Aprobadas Vigentes
        /*jpql += " union select r "
                + " from SspRegistro r, SspLocalRegistro lr, SspLocalAutorizacion loc, SspTipoUsoLocal tl, SspResolucion reso "
                + " where r.activo = 1 and r.estadoId.codProg = 'TP_ECC_APR' "
                + " and r.empresaId.id = :empresaId "
                + " and r.tipoProId.id = :modalidadId "
                + " and r.id = lr.registroId.id "                
                + " and lr.activo = 1 "
                + " and lr.localId.id = loc.id"
                + " and loc.activo = 1 "
                + " and r.id = tl.registroId.id "                
                + " and loc.id = tl.localId.id "
                + " and tl.activo = 1 "
                + " and tl.tipoLocalId.codProg = 'TP_LOC_PRIN'"
                + " and r.id = reso.registroId.id "                
                + " and reso.activo = 1 "
                + " and func('trunc',current_date) between func('trunc',reso.fechaIni) and func('trunc',reso.fechaFin) "
                //Busca todos los distritos del Departamento
                + " and loc.distritoId.id in (select dist.id from SbDistritoGt dist, SbProvinciaGt prov, SbDepartamentoGt dept "
                + " where dist.provinciaId.id = prov.id and prov.departamentoId.id = dept.id and dept.id in "
                //Busca el departamento
                + " (select dept2.id from SbDepartamentoGt dept2, SbProvinciaGt prov2, SbDistritoGt dist2 "
                + " where dept2.id = prov2.departamentoId.id and prov2.id = dist2.provinciaId.id and dist2.id = :distritoId ) ) ";
         */
        jpql += " order by r.fecha desc ";

        Query q = em.createQuery(jpql);
        if (empresaId != null) {
            q.setParameter("empresaId", empresaId);
        }
        if (registroId != null) {
            q.setParameter("registroId", registroId);
        }
        if (distritoId != null) {
            q.setParameter("distritoId", distritoId);
        }
        if (modalidadId != null) {
            q.setParameter("modalidadId", modalidadId);
        }
        if (modalidadId != null) {
            q.setParameter("idTipoLocal", idTipoLocal);
        }
        return q.getResultList();
    }

    /*
    * QUERYS PARA SEGURIDAD PROTECCION PERSONAL
     */
    //Consulta para devolver la cantidad total de registros 
    //parametros usados
    //tipo_pro_id(TIPO DE PROCEDIMIENTO) 
    //empresa_id (EMPRESA)
    /*
    select 
    count(*)
    from bdintegrado.ssp_registro r
        where tipo_reg_id= 196 AND  tipo_ope_id= 201 
            and activo = 1
            and tipo_pro_id = 2698 -- ID TIPO DE PROCEDIMIENTO 
            and EMPRESA_ID = 77155; -- solicitud modalidad SPP
     */
    public int obtenerContadorRegistrosSPP(String tipo_pro_cod_prog, long empresa_id) {
        int cont = 0;
        String jpql = "SELECT COUNT(c) "
                + " FROM SspRegistro c "
                + " WHERE c.activo = 1  "
                + " and c.tipoProId.codProg = :tp_cod "
                + " and c.empresaId.id = :e_id ";

        Query q = em.createQuery(jpql);
        q.setParameter("e_id", empresa_id);
        q.setParameter("tp_cod", tipo_pro_cod_prog);

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }

    //obtner registro por codigo de empresa y tipo procedimiento 
    //que estén aprobados o que esten negados y que suu autorización esté vigente
    public SspRegistro obtenerRegistroSPP_xEmp_codProg(long empresa_id, String tipo_pro_cod_prog) {
        int cont = 0;
        String jpql = "SELECT c "
                + " FROM SspRegistro c "
                + " WHERE c.activo = 1  "
                + " and c.estadoId.codProg  = 'TP_ECC_APR'"
                + " and c.tipoProId.codProg = :tp_cod "
                + " and c.empresaId.id = :e_id ";

        Query q = em.createQuery(jpql);
        q.setParameter("e_id", empresa_id);
        q.setParameter("tp_cod", tipo_pro_cod_prog);

        if (!q.getResultList().isEmpty()) {
            return (SspRegistro) q.getResultList().get(0);
        }
        return null;
    }

    public Integer verificar_principal_x_emp(Integer empresa_id) {
        try {

            int result = 0;
            String mensaje = "";

            StoredProcedureQuery sp = em.createStoredProcedureQuery("PROC_VERIF_PRINC_SPP_X_EMP");
            sp.registerStoredProcedureParameter("p_empresa_id", Integer.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("p_resultado", Integer.class, ParameterMode.OUT);

            sp.setParameter("p_empresa_id", empresa_id);
            sp.setParameter("p_resultado", result);

            boolean resultSet = sp.execute();
            result = (Integer) sp.getOutputParameterValue("p_resultado");

            if (result < 0) {
                System.err.println("Error: " + mensaje);
                return 0;
            }
//            return true;

            switch (result) {
                case 0:
                    // Se puede crear principal
                    return 1;

                case 1:
                    // Se pueden crear sucursales
//                    break;
                    return 2;
                case 2:
                    // No se puede crear principal
//                    break;
                    return 3;
                // Puedes agregar más casos según sea necesario
                default:
                    // Bloque de código si la expresión no coincide con ningún caso anterior
                    return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int existenRegistros_Aprob_Vigentes_xModalidad_Principales(long empresa_id, String modalidad) {

        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM bdintegrado.ssp_registro r ")
                .append("inner join BDINTEGRADO.tipo_base tb_mod   on r.tipo_pro_id  = tb_mod.id ")
                .append("INNER JOIN BDINTEGRADO.TIPO_BASE tb_reg ON r.tipo_reg_id  = tb_reg.id ")
                .append("inner join BDINTEGRADO.tipo_seguridad ts_est   on r.estado_id  = ts_est.id  ")
                .append(" inner join BDINTEGRADO.ssp_tipo_uso_local tu_loc   on r.id = tu_loc.registro_id  ")
                .append(" inner join BDINTEGRADO.tipo_seguridad ts_loc   on tu_loc.tipo_local = ts_loc.id ")
                .append("WHERE  ")
                .append("r.empresa_id          = ?1 ")
                .append("and r.activo          = 1 ")
                .append("and tb_mod.cod_prog   = ?2 ")
                .append("AND tb_reg.cod_prog   IN( 'TP_REGIST_NOR' ,'TP_REGIST_ADECU')")
                .append(" and ts_loc.cod_prog     = 'TP_LOC_PRIN' ")
                .append(" and ts_est.cod_prog    not in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP') ");
        Query q_0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        q_0.setParameter(1, empresa_id);
        q_0.setParameter(2, modalidad);
        BigDecimal count_regs = (BigDecimal) q_0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();

        if (totalCount_Regs > 0) {
            // EXISTEn registros para esta empresa y modalidad, No se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL

            return 1;
        }
        return -1;
    }



    /*

    public int obtenerContadorRegistrosSPP_Aprob_Vigentes(long empresa_id) {

        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM bdintegrado.ssp_registro r ")
                .append("inner join BDINTEGRADO.tipo_base tb_mod   on r.tipo_pro_id  = tb_mod.id ")
                .append("WHERE  ")
                .append("r.empresa_id          = ? ")
                .append("and r.activo          = 1")
                .append("and tb_mod.cod_prog     = 'TP_GSSP_MOD_PP'");
        Query q_0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        q_0.setParameter(1, empresa_id);
        BigDecimal count_regs = (BigDecimal) q_0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();

        if (totalCount_Regs == 0) {
            
            //No EXISTE ningún registro para esta empresa y modalidad, se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL
            System.out.println("cond-01-si");
            return 3;
            
        } else {
            
            System.out.println("cond-01-else");
            int tipo_query = 0; //0 con resolucion vigente y aprobado , 1 en tramite, 2 negados 

            StringBuilder jpql_principal = new StringBuilder();
            StringBuilder jpql1 = new StringBuilder()
                    .append("SELECT  COUNT(reg.id) ")
                    .append("FROM bdintegrado.ssp_registro reg");
            
            if (tipo_query == 0) {
                jpql_principal.append(jpql1.toString());
                jpql_principal.append(" inner join BDINTEGRADO.SSP_RESOLUCION res on reg.id           = res.registro_id ");
            }
            StringBuilder jpql2 = new StringBuilder()
                    .append(" inner join BDINTEGRADO.tipo_base tb_reg   on reg.tipo_reg_id  = tb_reg.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_ope   on reg.tipo_ope_id  = tb_ope.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_mod   on reg.tipo_pro_id  = tb_mod.id ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_est   on reg.estado_id  = ts_est.id ")
                    .append(" inner join BDINTEGRADO.ssp_tipo_uso_local tu_loc   on reg.id = tu_loc.registro_id  ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_loc   on tu_loc.tipo_local = ts_loc.id ")
                    .append("WHERE  ")
                    .append(" reg.empresa_id          = ?")
                    .append(" and reg.activo          = 1")
                    .append(" and tb_reg.cod_prog     IN( 'TP_REGIST_NOR' ,'TP_REGIST_ADECU')  and tb_ope.cod_prog = 'TP_OPE_INI'")
                    .append(" and tb_mod.cod_prog     = 'TP_GSSP_MOD_PP'         ")
                    .append(" and ts_loc.cod_prog     = 'TP_LOC_PRIN'");
            if (tipo_query == 0) {
                jpql_principal.append(jpql2.toString());

                jpql_principal.append(" and ts_est.cod_prog     = 'TP_ECC_APR'          ")
                        .append(" and  SYSDATE between  res.fecha_ini  and  res.fecha_fin  ");
            }
            jpql1.append(jpql2.toString());

            Query q = em.createNativeQuery(jpql_principal.toString());
            q.setParameter(1, empresa_id);
            BigDecimal count = (BigDecimal) q.getSingleResult();
            int totalCount = count.intValue();

            if (totalCount == 1) {
                //EXISTE UNA PRINCIPAL VIGENTE, SE PUEDEN CREAR SUCURSALES
                System.out.println("cond-02-si");
                return 1;
            } else {
                
                System.out.println("cond-02-else");
                tipo_query++;

                StringBuilder jpql_tramite = new StringBuilder();
                jpql_tramite.append(jpql1.toString());
                if (tipo_query == 1) {
                    jpql_tramite.append(" and ts_est.cod_prog     in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE','TP_ECC_XFIR', 'TP_ECC_XFISC', 'TP_ECC_FISCDO' )  ");
                }

                Query q_tramite = em.createNativeQuery(jpql_tramite.toString());
                //            System.out.println("jpql_tramite.toString()-...------>"+ jpql_tramite.toString());
                q_tramite.setParameter(1, empresa_id);
                BigDecimal count_tramite = (BigDecimal) q_tramite.getSingleResult();
                int totalCount_tramite = count_tramite.intValue();
                //            System.out.println("totalCount_tramite-...------>"+ totalCount_tramite);
                if (totalCount_tramite >= 1) {
                    //EXISTE UNA O MAS PRINCIPAL en TRAMITE, no se pueden crear ni PRINCIPALES NI SUCURSALES
                    System.out.println("cond-03-si");
                    return 2;
                } else {
                    System.out.println("cond-03-else");
                    //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                    tipo_query++;
                    StringBuilder jpql_negados = new StringBuilder();
                    jpql_negados.append(jpql1.toString());
                    if (tipo_query == 2) {
                        jpql_negados.append(" and ts_est.cod_prog     in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP')  ");
                    }
                    Query q_negados = em.createNativeQuery(jpql_negados.toString());
                    //                System.out.println("jpql_negados.toString()-...------>"+ jpql_negados.toString());
                    q_negados.setParameter(1, empresa_id);
                    BigDecimal count_negados = (BigDecimal) q_negados.getSingleResult();
                    int totalCount_negados = count_negados.intValue();
                    //                System.out.println("totalCount_negados-...------>"+ totalCount_negados);
                    if (totalCount_negados >= 1) {
                        System.out.println("cond-04-si");
                        //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                        return 3;
                    }else{
                        System.out.println("cond-04-else");
                    }

                }

            }
        }

        return -1;

    }

    */

    public int obtenerContadorRegistrosSPP_Aprob_Vigentes(Long empresa_id) {

        if (empresa_id <= 0) {
            return -1; //{}  
        }

        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT NVL(COUNT(*),0) AS TOTAL ")
                .append(" FROM BDINTEGRADO.SSP_REGISTRO r ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_mod ON (r.tipo_pro_id  = tb_mod.id) ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_reg ON (r.tipo_reg_id  = tb_reg.id) ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_ope ON (r.tipo_ope_id  = tb_ope.id) ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD tb_est ON (r.estado_id  = tb_est.id) ")
                .append(" WHERE r.activo = 1 ")
                .append(" AND r.empresa_id      = ? ")
                .append(" AND tb_reg.cod_prog   = 'TP_REGIST_NOR' ")
                .append(" AND tb_ope.cod_prog   = 'TP_OPE_INI' ")
                .append(" AND tb_mod.cod_prog   = 'TP_GSSP_MOD_PP' ")
                .append(" AND tb_est.cod_prog  NOT IN ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP') ");

        Query query0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        query0.setParameter(1, empresa_id);
        BigDecimal count_regs = (BigDecimal) query0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();
        System.out.println("[#01#-totalCount_Regs>" + totalCount_Regs);

        if (totalCount_Regs == 0) {
            //No EXISTE ningún registro para esta empresa y modalidad
            //Se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL
            return 3;

        } else {
            //0 con resolucion vigente y aprobado , 1 en tramite, 2 negados 

            StringBuilder jpql_principal = new StringBuilder();
            StringBuilder jpql1 = new StringBuilder()
                    .append("SELECT NVL(COUNT(reg.id),0) AS TOTAL ")
                    .append(" FROM BDINTEGRADO.SSP_REGISTRO reg")
                    .append(" INNER JOIN BDINTEGRADO.SSP_RESOLUCION res ON (reg.id = res.registro_id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_reg   ON (reg.tipo_reg_id  = tb_reg.id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_ope   ON (reg.tipo_ope_id  = tb_ope.id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_mod   ON (reg.tipo_pro_id  = tb_mod.id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD ts_est ON (reg.estado_id  = ts_est.id) ")
                    .append(" INNER JOIN BDINTEGRADO.SSP_TIPO_USO_LOCAL tu_loc ON (reg.id = tu_loc.registro_id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD ts_loc ON (tu_loc.tipo_local = ts_loc.id) ")
                    .append(" WHERE reg.activo = 1")
                    .append(" AND reg.empresa_id      = ?")
                    .append(" AND tb_reg.cod_prog     = 'TP_REGIST_NOR' ")
                    .append(" AND tb_ope.cod_prog     = 'TP_OPE_INI' ")
                    .append(" AND tb_mod.cod_prog     = 'TP_GSSP_MOD_PP' ")
                    .append(" AND ts_loc.cod_prog  NOT IN ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP') ")
                    .append(" AND SYSDATE between  res.fecha_ini  and  res.fecha_fin ");

            jpql_principal.append(jpql1.toString());

            Query q = em.createNativeQuery(jpql_principal.toString());
            q.setParameter(1, empresa_id);
            BigDecimal count = (BigDecimal) q.getSingleResult();
            int totalCount = count.intValue();
            System.out.println("[#02#-totalCount>" + totalCount);

            if (totalCount == 1) {
                //EXISTE UNA PRINCIPAL VIGENTE, SE PUEDEN CREAR SUCURSALES
                return 1;

            } else {

                StringBuilder jpql_tramite = new StringBuilder();
                jpql_tramite.append(jpql_principal.toString());
                jpql_tramite.append(" AND ts_est.cod_prog  in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE', 'TP_ECC_XFIR', 'TP_ECC_XFISC', 'TP_ECC_FISCDO') ");

                Query q_tramite = em.createNativeQuery(jpql_tramite.toString());
                q_tramite.setParameter(1, empresa_id);
                BigDecimal count_tramite = (BigDecimal) q_tramite.getSingleResult();
                int totalCount_tramite = count_tramite.intValue();
                System.out.println("[#03#-totalCount_tramite>" + totalCount_tramite);

                if (totalCount_tramite >= 1) {
                    //EXISTE UNA O MAS PRINCIPAL en TRAMITE, no se pueden crear ni PRINCIPALES NI SUCURSALES
                    return 2;
                } else {

                    //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                    StringBuilder jpql_negados = new StringBuilder();
                    jpql_negados.append(jpql_principal.toString());
                    jpql_negados.append(" and ts_est.cod_prog in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP')  ");

                    Query q_negados = em.createNativeQuery(jpql_negados.toString());
                    q_negados.setParameter(1, empresa_id);
                    BigDecimal count_negados = (BigDecimal) q_negados.getSingleResult();
                    int totalCount_negados = count_negados.intValue();
                    System.out.println("[#04#-totalCount_negados>" + totalCount_negados);

                    if (totalCount_negados >= 1) {
                        //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                        return 3;
                    } else {
                        return -3;
                    }

                }

            }
        }

    }

    public int obtenerContadorRegistrosSPP_Aprob_Vigentes_ADECUACION(long empresa_id, String modalidad) {

        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM bdintegrado.ssp_registro r ")
                .append("inner join BDINTEGRADO.tipo_base tb_mod   on r.tipo_pro_id  = tb_mod.id ")
                .append("INNER JOIN BDINTEGRADO.TIPO_BASE tb_reg ON r.tipo_reg_id  = tb_reg.id ")
                .append("WHERE  ")
                .append("r.empresa_id          = ?1 ")
                .append("and r.activo          = 1 ")
                .append("and tb_mod.cod_prog   = ?2 ")
                .append("AND tb_reg.cod_prog   IN( 'TP_REGIST_NOR' ,'TP_REGIST_ADECU')");
        Query q_0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        q_0.setParameter(1, empresa_id);
        q_0.setParameter(2, modalidad);
        BigDecimal count_regs = (BigDecimal) q_0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();

        if (totalCount_Regs == 0) {
            //No EXISTE ningún registro para esta empresa y modalidad, se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL

            return 3;
        } else {
            int tipo_query = 0; //0 con resolucion vigente y aprobado , 1 en tramite, 2 negados 

            StringBuilder jpql_principal = new StringBuilder();
            StringBuilder jpql1 = new StringBuilder()
                    .append("SELECT  COUNT(reg.id) ")
                    .append("FROM bdintegrado.ssp_registro reg");
            if (tipo_query == 0) {//VALIDACION EXTRA SI SE QUIERE HACER ALGO CON SUCURSAL
                jpql_principal.append(jpql1.toString());
                jpql_principal.append(" LEFT join BDINTEGRADO.SSP_RESOLUCION res on reg.id           = res.registro_id ");
            }
            StringBuilder jpql2 = new StringBuilder()
                    .append(" inner join BDINTEGRADO.tipo_base tb_reg   on reg.tipo_reg_id  = tb_reg.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_ope   on reg.tipo_ope_id  = tb_ope.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_mod   on reg.tipo_pro_id  = tb_mod.id ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_est   on reg.estado_id  = ts_est.id ")
                    .append(" inner join BDINTEGRADO.ssp_tipo_uso_local tu_loc   on reg.id = tu_loc.registro_id  ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_loc   on tu_loc.tipo_local = ts_loc.id ")
                    .append("WHERE  ")
                    .append(" reg.empresa_id          = ?1 ")
                    .append(" and reg.activo          = 1 ")
                    .append(" and tb_reg.cod_prog     IN( 'TP_REGIST_NOR' ,'TP_REGIST_ADECU')  and tb_ope.cod_prog = 'TP_OPE_INI'")
                    .append(" and tb_mod.cod_prog     = ?2       ")
                    .append(" and ts_loc.cod_prog     = 'TP_LOC_PRIN'");
            if (tipo_query == 0) {
                jpql_principal.append(jpql2.toString());

                jpql_principal.append(" and ts_est.cod_prog     = 'TP_ECC_APR'")
                        .append(" and  SYSDATE between  res.fecha_ini  and  res.fecha_fin");
            }
            jpql1.append(jpql2.toString());

            Query q = em.createNativeQuery(jpql_principal.toString());
            q.setParameter(1, empresa_id);
            q.setParameter(2, modalidad);
            BigDecimal count = (BigDecimal) q.getSingleResult();
            int totalCount = count.intValue();

            if (totalCount >= 1) {
                //EXISTE UNA O MÁS PRINCIPAL VIGENTE, YA NO  SE PUEDEN CREAR SOLICITUDES

                return 1;

            } else {
                tipo_query++;

                StringBuilder jpql_tramite = new StringBuilder();
                jpql_tramite.append(jpql1.toString());
                if (tipo_query == 1) {
                    jpql_tramite.append(" and ts_est.cod_prog in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE','TP_ECC_XFIR' , 'TP_ECC_XFISC', 'TP_ECC_FISCDO' )  ");
                }

                Query q_tramite = em.createNativeQuery(jpql_tramite.toString());
                //            System.out.println("jpql_tramite.toString()-...------>"+ jpql_tramite.toString());
                q_tramite.setParameter(1, empresa_id);
                BigDecimal count_tramite = (BigDecimal) q_tramite.getSingleResult();
                int totalCount_tramite = count_tramite.intValue();
                //            System.out.println("totalCount_tramite-...------>"+ totalCount_tramite);
                if (totalCount_tramite >= 1) {
                    //EXISTE UNA O MAS PRINCIPAL en TRAMITE, no se pueden crear ni PRINCIPALES NI SUCURSALES
                    return 2;
                } else {
                    //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                    tipo_query++;
                    StringBuilder jpql_negados = new StringBuilder();
                    jpql_negados.append(jpql1.toString());
                    if (tipo_query == 2) {
                        jpql_negados.append(" and ts_est.cod_prog in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP')");
                    }
                    Query q_negados = em.createNativeQuery(jpql_negados.toString());
                    //                System.out.println("jpql_negados.toString()-...------>"+ jpql_negados.toString());
                    q_negados.setParameter(1, empresa_id);
                    BigDecimal count_negados = (BigDecimal) q_negados.getSingleResult();
                    int totalCount_negados = count_negados.intValue();
                    //                System.out.println("totalCount_negados-...------>"+ totalCount_negados);
                    if (totalCount_negados >= 1) {
                        //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                        return 3;
                    }else{
                        return -3;
                    }

                }

            }
        }

    }

    public String verificaDISCA_ExisteResolucionVigentePrincipal_ByRUC(SbPersonaGt empresa) {

        String xValidacion = "NO";

        StringBuilder jpqlContResolDISCA = new StringBuilder()
                .append("SELECT NVL(COUNT(DISCA.ID),0) AS TOTAL ")
                .append(" FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC DISCA ")
                .append(" WHERE DISCA.PRINCIPAL = 'PRINCIPAL' ")
                .append(" AND DISCA.RUC = ?1 ")
                .append(" AND SYSDATE BETWEEN DISCA.FEC_EMI AND DISCA.FEC_VEN ");

        Query queryDisca = em.createNativeQuery(jpqlContResolDISCA.toString());
        queryDisca.setParameter(1, empresa.getRuc());
        BigDecimal count_regResolDisca = (BigDecimal) queryDisca.getSingleResult();
        int totalCount_RegResolDisca = count_regResolDisca.intValue();

        if (totalCount_RegResolDisca > 0) {
            xValidacion = "SI";
        } else {
            xValidacion = "NO";
        }
        return xValidacion;
    }

    public String verificaDISCA_ExisteResolucionVigentePrincipal_BYModalidad(SbPersonaGt empresa, String valDiscaTipoModalidad) {

        String xValidacion = "NO";

        StringBuilder jpqlContResolDISCA = new StringBuilder()
                .append("SELECT NVL(COUNT(DISCA.ID),0) AS TOTAL ")
                .append(" FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC DISCA ")
                .append(" WHERE DISCA.PRINCIPAL = 'PRINCIPAL' ")
                .append(" AND DISCA.RUC = ?1 ")
                .append(" AND DISCA.TIP_MOD = ?2 ")
                .append(" AND SYSDATE BETWEEN DISCA.FEC_EMI AND DISCA.FEC_VEN ");

        Query queryDisca = em.createNativeQuery(jpqlContResolDISCA.toString());
        queryDisca.setParameter(1, empresa.getRuc());
        queryDisca.setParameter(2, valDiscaTipoModalidad);
        BigDecimal count_regResolDisca = (BigDecimal) queryDisca.getSingleResult();
        int totalCount_RegResolDisca = count_regResolDisca.intValue();

        if (totalCount_RegResolDisca > 0) {
            xValidacion = "SI";
        } else {
            xValidacion = "NO";
        }
        return xValidacion;
    }

    public String verificaDISCA_ExisteResolucionVigentePrincipal_Tecnologia(SbPersonaGt empresa, String valDiscaTipoModalidad) {

        String xValidacion = "NO";
        StringBuilder jpqlContResolDISCA = new StringBuilder()
                .append("SELECT NVL(COUNT(DISCA.ID),0) AS TOTAL ")
                .append(" FROM RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC DISCA ")
                .append(" WHERE DISCA.PRINCIPAL = 'PRINCIPAL' ")
                .append(" AND DISCA.RUC = ?1 ")
                .append(" AND DISCA.TIP_MOD in (" + valDiscaTipoModalidad + ") ")
                .append(" AND SYSDATE BETWEEN DISCA.FEC_EMI AND DISCA.FEC_VEN ");

        Query queryDisca = em.createNativeQuery(jpqlContResolDISCA.toString());
        queryDisca.setParameter(1, empresa.getRuc());
//        queryDisca.setParameter(2, "('20','13','16','19','15','12','21')");
        BigDecimal count_regResolDisca = (BigDecimal) queryDisca.getSingleResult();
        int totalCount_RegResolDisca = count_regResolDisca.intValue();
        if (totalCount_RegResolDisca > 0) {
            xValidacion = "SI";
        } else {
            xValidacion = "NO";
        }
        return xValidacion;
    }

    public int obtenerContadorRegistrosSPP_Aprob_Vigentes_VP(Long empresa_id) {

        if (empresa_id <= 0) {
            return -1; //{}  
        }

        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT NVL(COUNT(*),0) AS TOTAL ")
                .append(" FROM BDINTEGRADO.SSP_REGISTRO r ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_mod ON (r.tipo_pro_id  = tb_mod.id) ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_reg ON (r.tipo_reg_id  = tb_reg.id) ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_ope ON (r.tipo_ope_id  = tb_ope.id) ")
                .append(" INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD tb_est ON (r.estado_id  = tb_est.id) ")
                .append(" WHERE r.activo = 1 ")
                .append(" AND r.empresa_id      = ? ")
                .append(" AND tb_reg.cod_prog   = 'TP_REGIST_NOR' ")
                .append(" AND tb_ope.cod_prog   = 'TP_OPE_INI' ")
                .append(" AND tb_mod.cod_prog   = 'TP_GSSP_MOD_VP' ")
                .append(" AND tb_est.cod_prog  NOT IN ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP') ");

        Query query0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        query0.setParameter(1, empresa_id);
        BigDecimal count_regs = (BigDecimal) query0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();
        System.out.println("[#01#-totalCount_Regs>" + totalCount_Regs);

        if (totalCount_Regs == 0) {
            //No EXISTE ningún registro para esta empresa y modalidad
            //Se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL
            return 3;

        } else {
            //0 con resolucion vigente y aprobado , 1 en tramite, 2 negados 

            StringBuilder jpql_principal = new StringBuilder();
            StringBuilder jpql1 = new StringBuilder()
                    .append("SELECT NVL(COUNT(reg.id),0) AS TOTAL ")
                    .append(" FROM BDINTEGRADO.SSP_REGISTRO reg")
                    .append(" INNER JOIN BDINTEGRADO.SSP_RESOLUCION res ON (reg.id = res.registro_id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_reg   ON (reg.tipo_reg_id  = tb_reg.id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_ope   ON (reg.tipo_ope_id  = tb_ope.id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_BASE tb_mod   ON (reg.tipo_pro_id  = tb_mod.id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD ts_est ON (reg.estado_id  = ts_est.id) ")
                    .append(" INNER JOIN BDINTEGRADO.SSP_TIPO_USO_LOCAL tu_loc ON (reg.id = tu_loc.registro_id) ")
                    .append(" INNER JOIN BDINTEGRADO.TIPO_SEGURIDAD ts_loc ON (tu_loc.tipo_local = ts_loc.id) ")
                    .append(" WHERE reg.activo = 1")
                    .append(" AND reg.empresa_id      = ?")
                    .append(" AND tb_reg.cod_prog     = 'TP_REGIST_NOR' ")
                    .append(" AND tb_ope.cod_prog     = 'TP_OPE_INI' ")
                    .append(" AND tb_mod.cod_prog     = 'TP_GSSP_MOD_VP' ")
                    .append(" AND ts_loc.cod_prog  NOT IN ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP') ")
                    .append(" AND SYSDATE between  res.fecha_ini  and  res.fecha_fin ");

            jpql_principal.append(jpql1.toString());

            Query q = em.createNativeQuery(jpql_principal.toString());
            q.setParameter(1, empresa_id);
            BigDecimal count = (BigDecimal) q.getSingleResult();
            int totalCount = count.intValue();
            System.out.println("[#02#-totalCount>" + totalCount);

            if (totalCount == 1) {
                //EXISTE UNA PRINCIPAL VIGENTE, SE PUEDEN CREAR SUCURSALES
                return 1;

            } else {

                StringBuilder jpql_tramite = new StringBuilder();
                jpql_tramite.append(jpql_principal.toString());
                jpql_tramite.append(" AND ts_est.cod_prog  in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE', 'TP_ECC_XFIR', 'TP_ECC_XFISC', 'TP_ECC_FISCDO') ");

                Query q_tramite = em.createNativeQuery(jpql_tramite.toString());
                q_tramite.setParameter(1, empresa_id);
                BigDecimal count_tramite = (BigDecimal) q_tramite.getSingleResult();
                int totalCount_tramite = count_tramite.intValue();
                System.out.println("[#03#-totalCount_tramite>" + totalCount_tramite);

                if (totalCount_tramite >= 1) {
                    //EXISTE UNA O MAS PRINCIPAL en TRAMITE, no se pueden crear ni PRINCIPALES NI SUCURSALES
                    return 2;
                } else {

                    //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                    StringBuilder jpql_negados = new StringBuilder();
                    jpql_negados.append(jpql_principal.toString());
                    jpql_negados.append(" and ts_est.cod_prog in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP')  ");

                    Query q_negados = em.createNativeQuery(jpql_negados.toString());
                    q_negados.setParameter(1, empresa_id);
                    BigDecimal count_negados = (BigDecimal) q_negados.getSingleResult();
                    int totalCount_negados = count_negados.intValue();
                    System.out.println("[#04#-totalCount_negados>" + totalCount_negados);

                    if (totalCount_negados >= 1) {
                        //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                        return 3;
                    } else {
                        return -3;
                    }

                }

            }
        }

    }

    public List<SspRegistro> buscarRegistrosAprobXnroRD_DISCA(Integer nro_rd) {
        //Busca los registros   que esten asociados en reg_disca y aprobados

        String jpql = "select r "
                + " from SspRegistro r, SspRegistroRegDisca rd "
                + " where r.id = rd.registroId.id "
                + " and  rd.nroRd = :nroRd "
                + " and r.activo = 1 and r.estadoId.codProg in ('TP_ECC_APR')";
        jpql += " order by r.fecha desc ";

        javax.persistence.Query q = em.createQuery(jpql);
//        if(nro_rd != null){
        q.setParameter("nroRd", nro_rd);
//        }

        return q.getResultList();
    }

    public List<SspRegistro> buscarRegistrosTramiteXnroRD_DISCA(String registro_id_vistaDISCA) {
        //Busca los registros en tramite  que esten asociados en reg_disca  

        String jpql = "select r "
                + " from SspRegistro r, SspRegistroRegDisca rd "
                + " where r.id = rd.registroId.id "
                + " and  rd.idResolucionDISCA = :registro_id_vistaDISCA "
                + " and r.activo = 1 and r.estadoId.codProg in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE','TP_ECC_XFIR')";
        jpql += " order by r.fecha desc ";

        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("registro_id_vistaDISCA", registro_id_vistaDISCA);

        return q.getResultList();
    }

    public List<SspRegistro> buscarRegistrosAprobXregistroId_RENAGI(Long registro_id) {
        //Busca los registros  con la modalidad cambio local, aprobados y activos

        String jpql = "select r "
                + " from SspRegistro r  "
                + " where r.registroId.id = :registro_id "
                + " and r.tipoProId.codProg = 'TP_AUTORIZ_SEL_CLO' "
                + " and r.activo = 1 and r.estadoId.codProg in ('TP_ECC_APR')";
        jpql += " order by r.fecha desc ";

        javax.persistence.Query q = em.createQuery(jpql);
//        if(nro_rd != null){
        q.setParameter("registro_id", registro_id);
//        }

        return q.getResultList();
    }

    public List<SspRegistro> buscarRegistrosTramiteXXregistroId_RENAGI(Long registro_id) {
        //Busca los registros en tramite  con la modalidad cambio local y activos  

        String jpql = "select r "
                + " from SspRegistro r  "
                + " where r.registroId.id = :registro_id "
                + " and r.tipoProId.codProg = 'TP_AUTORIZ_SEL_CLO' "
                + " and r.activo = 1 and r.estadoId.codProg in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE','TP_ECC_XFIR')";
        jpql += " order by r.fecha desc ";

        javax.persistence.Query q = em.createQuery(jpql);
//        if(nro_rd != null){
        q.setParameter("registro_id", registro_id);
//        }

        return q.getResultList();
    }

    public int obtenerContadorRegistrosSTS_Aprob_Vigentes(long empresa_id) {

        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM bdintegrado.ssp_registro r ")
                .append("inner join BDINTEGRADO.tipo_base tb_mod   on r.tipo_pro_id  = tb_mod.id ")
                .append("WHERE  ")
                .append("r.empresa_id          = ? ")
                .append("and r.activo          = 1")
                .append("and tb_mod.cod_prog     = 'TP_GSSP_MOD_TEC'");
        Query q_0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        q_0.setParameter(1, empresa_id);
        BigDecimal count_regs = (BigDecimal) q_0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();

        if (totalCount_Regs == 0) {
            //No EXISTE ningún registro para esta empresa y modalidad, se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL

            return 3;
        } else {

            int tipo_query = 0; //0 con resolucion vigente y aprobado , 1 en tramite, 2 negados 

            StringBuilder jpql_principal = new StringBuilder();
            StringBuilder jpql1 = new StringBuilder()
                    .append("SELECT  COUNT(reg.id) ")
                    .append("FROM bdintegrado.ssp_registro reg");
            if (tipo_query == 0) {
                jpql_principal.append(jpql1.toString());
                jpql_principal.append(" inner join BDINTEGRADO.SSP_RESOLUCION res on reg.id = res.registro_id ");
            }
            StringBuilder jpql2 = new StringBuilder()
                    .append(" inner join BDINTEGRADO.tipo_base tb_reg   on reg.tipo_reg_id  = tb_reg.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_ope   on reg.tipo_ope_id  = tb_ope.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_mod   on reg.tipo_pro_id  = tb_mod.id ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_est   on reg.estado_id  = ts_est.id ")
                    .append(" inner join BDINTEGRADO.ssp_tipo_uso_local tu_loc   on reg.id = tu_loc.registro_id  ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_loc   on tu_loc.tipo_local = ts_loc.id ")
                    .append("WHERE  ")
                    .append(" reg.empresa_id          = ?")
                    .append(" and reg.activo          = 1")
                    .append(" and tb_reg.cod_prog     = 'TP_REGIST_NOR'  and tb_ope.cod_prog = 'TP_OPE_INI'")
                    .append(" and tb_mod.cod_prog     = 'TP_GSSP_MOD_TEC'")
                    .append(" and ts_loc.cod_prog     = 'TP_LOC_PRIN'");
            if (tipo_query == 0) {
                jpql_principal.append(jpql2.toString());

                jpql_principal.append(" and ts_est.cod_prog     = 'TP_ECC_APR'")
                        .append(" and  SYSDATE between  res.fecha_ini  and  res.fecha_fin");
            }
            jpql1.append(jpql2.toString());

            Query q = em.createNativeQuery(jpql_principal.toString());
            q.setParameter(1, empresa_id);
            BigDecimal count = (BigDecimal) q.getSingleResult();
            int totalCount = count.intValue();

            if (totalCount == 1) {
                //EXISTE UNA PRINCIPAL VIGENTE, SE PUEDEN CREAR SUCURSALES

                return 1;
            } else {
                tipo_query++;

                StringBuilder jpql_tramite = new StringBuilder();
                jpql_tramite.append(jpql1.toString());
                if (tipo_query == 1) {
                    jpql_tramite.append(" and ts_est.cod_prog in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE','TP_ECC_XFIR' , 'TP_ECC_XFISC', 'TP_ECC_FISCDO' )  ");
                }

                Query q_tramite = em.createNativeQuery(jpql_tramite.toString());
                //            System.out.println("jpql_tramite.toString()-...------>"+ jpql_tramite.toString());
                q_tramite.setParameter(1, empresa_id);
                BigDecimal count_tramite = (BigDecimal) q_tramite.getSingleResult();
                int totalCount_tramite = count_tramite.intValue();
                //            System.out.println("totalCount_tramite-...------>"+ totalCount_tramite);
                if (totalCount_tramite >= 1) {
                    //EXISTE UNA O MAS PRINCIPAL en TRAMITE, no se pueden crear ni PRINCIPALES NI SUCURSALES
                    return 2;
                } else {
                    //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                    tipo_query++;
                    StringBuilder jpql_negados = new StringBuilder();
                    jpql_negados.append(jpql1.toString());
                    if (tipo_query == 2) {
                        jpql_negados.append(" and ts_est.cod_prog in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP')");
                    }
                    Query q_negados = em.createNativeQuery(jpql_negados.toString());
                    //                System.out.println("jpql_negados.toString()-...------>"+ jpql_negados.toString());
                    q_negados.setParameter(1, empresa_id);
                    BigDecimal count_negados = (BigDecimal) q_negados.getSingleResult();
                    int totalCount_negados = count_negados.intValue();
                    //                System.out.println("totalCount_negados-...------>"+ totalCount_negados);
                    if (totalCount_negados >= 1) {
                        //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                        return 3;
                    } else {
                        return -3;
                    }

                }

            }
        }

    }

    public int obtenerContadorRegistrosSPCP_Aprob_Vigentes(long empresa_id) {

        if (empresa_id <= 0) {
            return -1; //{}  
        }
        StringBuilder jpqlContRegxModadidad = new StringBuilder()
                .append("SELECT COUNT(*) ")
                .append("FROM bdintegrado.ssp_registro r ")
                .append("inner join BDINTEGRADO.tipo_base tb_mod   on r.tipo_pro_id  = tb_mod.id ")
                .append("WHERE  ")
                .append("r.empresa_id          = ? ")
                .append("and r.activo          = 1")
                .append("and tb_mod.cod_prog     = 'TP_GSSP_MOD_PCP'");
        Query q_0 = em.createNativeQuery(jpqlContRegxModadidad.toString());
        q_0.setParameter(1, empresa_id);
        BigDecimal count_regs = (BigDecimal) q_0.getSingleResult();
        int totalCount_Regs = count_regs.intValue();

        if (totalCount_Regs == 0) {
            //No EXISTE ningún registro para esta empresa y modalidad, se peuden CREAR solicitudes en este caso al ser 1ro tiene que ser una PRINCIPAL

            return 3;
        } else {

            int tipo_query = 0; //0 con resolucion vigente y aprobado , 1 en tramite, 2 negados 

            StringBuilder jpql_principal = new StringBuilder();
            StringBuilder jpql1 = new StringBuilder()
                    .append("SELECT  COUNT(reg.id) ")
                    .append("FROM bdintegrado.ssp_registro reg");
            if (tipo_query == 0) {
                jpql_principal.append(jpql1.toString());
                jpql_principal.append(" inner join BDINTEGRADO.SSP_RESOLUCION res on reg.id = res.registro_id ");
            }
            StringBuilder jpql2 = new StringBuilder()
                    .append(" inner join BDINTEGRADO.tipo_base tb_reg   on reg.tipo_reg_id  = tb_reg.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_ope   on reg.tipo_ope_id  = tb_ope.id ")
                    .append(" inner join BDINTEGRADO.tipo_base tb_mod   on reg.tipo_pro_id  = tb_mod.id ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_est   on reg.estado_id  = ts_est.id ")
                    .append(" inner join BDINTEGRADO.ssp_tipo_uso_local tu_loc   on reg.id = tu_loc.registro_id  ")
                    .append(" inner join BDINTEGRADO.tipo_seguridad ts_loc   on tu_loc.tipo_local = ts_loc.id ")
                    .append("WHERE  ")
                    .append(" reg.empresa_id          = ?")
                    .append(" and reg.activo          = 1")
                    .append(" and tb_reg.cod_prog     = 'TP_REGIST_NOR'  and tb_ope.cod_prog = 'TP_OPE_INI'")
                    .append(" and tb_mod.cod_prog     = 'TP_GSSP_MOD_PCP'")
                    .append(" and ts_loc.cod_prog     = 'TP_LOC_PRIN'");
            if (tipo_query == 0) {
                jpql_principal.append(jpql2.toString());

                jpql_principal.append(" and ts_est.cod_prog     = 'TP_ECC_APR'")
                        .append(" and  SYSDATE between  res.fecha_ini  and  res.fecha_fin");
            }
            jpql1.append(jpql2.toString());

            Query q = em.createNativeQuery(jpql_principal.toString());
            q.setParameter(1, empresa_id);
            BigDecimal count = (BigDecimal) q.getSingleResult();
            int totalCount = count.intValue();

            if (totalCount == 1) {
                //EXISTE UNA PRINCIPAL VIGENTE, SE PUEDEN CREAR SUCURSALES

                return 1;
            } else {
                tipo_query++;

                StringBuilder jpql_tramite = new StringBuilder();
                jpql_tramite.append(jpql1.toString());
                if (tipo_query == 1) {
                    jpql_tramite.append(" and ts_est.cod_prog in ('TP_ECC_CRE','TP_ECC_TRA','TP_ECC_OBS','TP_ECC_PRE','TP_ECC_XFIR')  ");
                }

                Query q_tramite = em.createNativeQuery(jpql_tramite.toString());
                //            System.out.println("jpql_tramite.toString()-...------>"+ jpql_tramite.toString());
                q_tramite.setParameter(1, empresa_id);
                BigDecimal count_tramite = (BigDecimal) q_tramite.getSingleResult();
                int totalCount_tramite = count_tramite.intValue();
                //            System.out.println("totalCount_tramite-...------>"+ totalCount_tramite);
                if (totalCount_tramite >= 1) {
                    //EXISTE UNA O MAS PRINCIPAL en TRAMITE, no se pueden crear ni PRINCIPALES NI SUCURSALES
                    return 2;
                } else {
                    //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                    tipo_query++;
                    StringBuilder jpql_negados = new StringBuilder();
                    jpql_negados.append(jpql1.toString());
                    if (tipo_query == 2) {
                        jpql_negados.append(" and ts_est.cod_prog in ('TP_ECC_ANU','TP_ECC_NPR','TP_ECC_CAN','TP_ECC_DES','TP_ECC_FDP')");
                    }
                    Query q_negados = em.createNativeQuery(jpql_negados.toString());
                    //                System.out.println("jpql_negados.toString()-...------>"+ jpql_negados.toString());
                    q_negados.setParameter(1, empresa_id);
                    BigDecimal count_negados = (BigDecimal) q_negados.getSingleResult();
                    int totalCount_negados = count_negados.intValue();
                    //                System.out.println("totalCount_negados-...------>"+ totalCount_negados);
                    if (totalCount_negados >= 1) {
                        //EXISTE UNA O MAS PRINCIPAL en ESTADO NEGATIVO, SE pueden crear  PRINCIPALES
                        return 3;
                    }

                }

            }
        }

        return -1;

    }
    

    public List<Map> buscarBandejaSolicitudCertificacionEF(HashMap nmap) {
    
        String xRucIni = nmap.get("filtroAdministradoNumDoc").toString(); 
        xRucIni = xRucIni.substring(0, 2);
        
        System.out.println("xRucIni->"+xRucIni);
        
        String jpql = "";    

        jpql = "SELECT "
                + " s.id as ID, "
                + " s.nroExpediente as NRO_EXPEDIENTE, "
                + " s.empresaId.id as ADMINISTRADO_ID, "
                + " (case when (s.empresaId.rznSocial != null) then s.empresaId.rznSocial else  concat(s.empresaId.nombres, ' ', s.empresaId.apePat, ' ', s.empresaId.apeMat) end) as ADMINISTRADO, "
                + " s.nroSolicitiud as NRO_SOLICITUD, "
                + " s.tipoProId.id as MODALIDAD_ID, "
                + " s.tipoProId.nombre as MODALIDAD_NOMBRE, "
                + " s.tipoProId.codProg as MODALIDAD_CODPROG, "
                + " s.tipoAutId.id as TIP_AUT_ID, "
                + " s.tipoAutId.nombre as TIP_AUT_NOMBRE, "
                + " s.tipoOpeId.id as TIP_OPE_ID, "
                + " s.tipoOpeId.nombre as TIP_OPE_NOMBRE, "
                + " s.tipoRegId.id as TIP_REG_ID, "
                + " s.tipoRegId.nombre as TIP_REG_NOMBRE, "
                + " s.audLogin as USUARIO, "
                + " s.usuarioCreacionId.id as USUARIO_ID, "
                + " s.usuarioCreacionId.login as USUARIO_LOGIN, "
                + " s.estadoId.id as ESTADO_ID, "
                + " s.estadoId.nombre as ESTADO_NOMBRE, "
                + " s.estadoId.codProg AS ESTADO_CODPROG,"
                + " s.fecha as FECHA, "
                + " s.activo as ACTIVO, "
                + " s.observacion AS OBSERVACION, "                
                + " lr.distritoId.nombre AS DISTRITO, "
                + " lr.distritoId.provinciaId.nombre AS PROVINCIA, "
                + " lr.distritoId.provinciaId.departamentoId.nombre AS DEPARTAMENTO "                
                + " FROM SspRegistro s, SspAgenciaFinanciera lr "
                + " where s.id = lr.registroId.id "
                + " and s.activo = 1 "
                + " AND s.empresaId.id = :empresaId "
                + " AND s.tipoProId.codProg in ('TP_GSSP_CERT_CEF') "
                + "";
        
        if (nmap.get("buscarPor") != null) {
            switch (nmap.get("buscarPor").toString()) {
                case "1": //NRO_SOLICITUD
                    jpql += " and s.nroSolicitiud like :nroSolicitiud";
                    break;
                case "2": //NRO_EXPEDIENTE
                    jpql += " and s.nroExpediente like :nroExpediente";
                    break;
                case "4": //MODALIDAD O  TIPO DE SEGURIDAD
                    jpql += " and s.tipoProId.id = :tipoProId";
                    break;
            }
        }
        
        if (nmap.get("filtroTipoEstadoId") != null) {
            jpql += " and s.estadoId.id = :estadoId";
        }

        /*if (nmap.get("filtroTipoAutID") != null) {
            jpql += " and s.tipoAutId.id = :tipoAutId";
        }

        if (nmap.get("filtroTipoOpeID") != null) {
            jpql += " and s.tipoOpeId.id = :tipoOpeId";
        }*/

        if (nmap.get("filtroFechaIni") != null && nmap.get("filtroFechaFin") != null) {
            jpql += " and (FUNC('trunc',s.fecha) BETWEEN FUNC('trunc',:fechaIni) and FUNC('trunc',:fechaFin) )";
        }

        jpql += " ORDER BY s.id desc ";

        Query q = em.createQuery(jpql);
        q.setParameter("empresaId", (Long) nmap.get("filtroAdministradoId"));
        
        if (nmap.get("buscarPor") != null) {
            switch (nmap.get("buscarPor").toString()) {
                case "1": //NRO_SOLICITUD
                    q.setParameter("nroSolicitiud", "%" + nmap.get("filtroNumero") + "%");
                    break;
                case "2": //NRO_EXPEDIENTE
                    q.setParameter("nroExpediente", "%" + nmap.get("filtroNumero") + "%");
                    break;
                case "4": //MODALIDAD O TIPO SEGURIDAD
                    q.setParameter("tipoProId", (Long) nmap.get("filtroTipoSeguridadId"));
                    break;
            }
        }

        if (nmap.get("filtroTipoEstadoId") != null) {
            q.setParameter("estadoId", (Long) nmap.get("filtroTipoEstadoId"));
        }

        if (nmap.get("filtroTipoOpeID") != null) {
            q.setParameter("tipoOpeId", (Long) nmap.get("filtroTipoOpeID"));
        }

        if (nmap.get("filtroTipoAutID") != null) {
            q.setParameter("tipoAutId", (Long) nmap.get("filtroTipoAutID"));
        }

        if (nmap.get("filtroFechaIni") != null && nmap.get("filtroFechaFin") != null) {
            q.setParameter("fechaIni", (Date) nmap.get("filtroFechaIni"), TemporalType.DATE);
            q.setParameter("fechaFin", (Date) nmap.get("filtroFechaFin"), TemporalType.DATE);
        }

        q.setHint("eclipselink.result-type", "Map");
        return q.getResultList();
    }
    
    
    public List<Map> buscarBandejaSolicitudCertificacionGral(HashMap nmap) {       
                
        String jpql = "";
            
                jpql = "SELECT "
                + " REG.ID, "
                + " REG.NRO_EXPEDIENTE as NRO_EXPEDIENTE, "
                + " REG.EMPRESA_ID as ADMINISTRADO_ID, "
                + " (case when EMP.RZN_SOCIAL is not null then EMP.RZN_SOCIAL else  (EMP.APE_PAT || ' ' || EMP.APE_MAT || ' ' || EMP.NOMBRES) end) as ADMINISTRADO, "
                + " REG.NRO_SOLICITIUD as NRO_SOLICITUD, "
                + " REG.TIPO_PRO_ID as MODALIDAD_ID, "
                + " TIPPRO.NOMBRE as MODALIDAD_NOMBRE, "
                + " TIPPRO.COD_PROG as MODALIDAD_CODPROG, "                
                + " REG.AUD_LOGIN as USUARIO, "
                + " REG.ESTADO_ID as ESTADO_ID, "
                + " TIPESTADO.NOMBRE as ESTADO_NOMBRE, "
                + " TIPESTADO.COD_PROG AS ESTADO_CODPROG,"
                + " REG.FECHA as FECHA, "
                + " REG.ACTIVO as ACTIVO, "
                + " REG.OBSERVACION AS OBSERVACION, "  
                + " DIST.NOMBRE AS DISTRITO, "
                + " PROV.NOMBRE AS PROVINCIA, "
                + " DEPA.NOMBRE AS DEPARTAMENTO, "   
                + " (CASE WHEN CERT.DISTRITO_ID IS NULL THEN NULL ELSE DEPA.NOMBRE || ' / ' || PROV.NOMBRE || ' / ' || DIST.NOMBRE END) AS UBIGEO "
                + " FROM BDINTEGRADO.SSP_REGISTRO REG "
                + " LEFT JOIN BDINTEGRADO.SB_PERSONA EMP ON (REG.EMPRESA_ID = EMP.ID)"
                + " LEFT JOIN BDINTEGRADO.SSP_VEHICULO_CERTIFICACION CERT ON (REG.ID = CERT.REGISTRO_ID)"
                + " LEFT JOIN BDINTEGRADO.TIPO_BASE TIPPRO ON (TIPPRO.ID = REG.TIPO_PRO_ID)"
                + " LEFT JOIN BDINTEGRADO.TIPO_SEGURIDAD TIPESTADO ON (TIPESTADO.ID = REG.ESTADO_ID)"
                + " LEFT JOIN BDINTEGRADO.SB_DISTRITO DIST ON (DIST.ID = CERT.DISTRITO_ID)"
                + " LEFT JOIN BDINTEGRADO.SB_PROVINCIA PROV ON (PROV.ID = DIST.PROVINCIA_ID)"
                + " LEFT JOIN BDINTEGRADO.SB_DEPARTAMENTO DEPA ON (DEPA.ID = PROV.DEPARTAMENTO_ID)"        
                + " WHERE REG.ACTIVO = 1 "
                + " AND REG.EMPRESA_ID = ?1 "
                + " AND TIPPRO.COD_PROG IN ('TP_GSSP_CERT_CVB', 'TP_GSSP_CERT_CEF') "
                + "";
                
                
                        
        if (nmap.get("filtroTipo") != null) {
            switch (nmap.get("filtroTipo").toString()) {
                case "1": //NRO_SOLICITUD
                    jpql += " AND REG.NRO_SOLICITIUD like ?2";
                    break;
                case "2": //NRO_EXPEDIENTE
                    jpql += " AND REG.NRO_EXPEDIENTE like ?2";
                    break;
            }
        }
        
        if (nmap.get("filtroFechaIni") != null && nmap.get("filtroFechaFin") != null) {
            jpql += " AND TRUNC(REG.FECHA) BETWEEN TRUNC(?3) AND TRUNC(?4) ";
        }

        if (nmap.get("filtroTipoEstadoId") != null) {
            jpql += " and REG.ESTADO_ID = ?5";
        }
        
        if (nmap.get("filtroTipoCertificacionId") != null) {
            jpql += " and REG.TIPO_PRO_ID = ?6";
        }

        jpql += " ORDER BY REG.ID desc ";
        
        Query q = em.createNativeQuery(jpql);
        q.setParameter(1, (Long) nmap.get("filtroAdministradoId"));

        if (nmap.get("filtroTipo") != null) {
            switch (nmap.get("filtroTipo").toString()) {
                case "1": //NRO_SOLICITUD
                    q.setParameter(2, "%" + nmap.get("filtroNumero") + "%");
                    break;
                case "2": //NRO_EXPEDIENTE
                    q.setParameter(2, "%" + nmap.get("filtroNumero") + "%");
                    break;
            }
        }

        if (nmap.get("filtroFechaIni") != null && nmap.get("filtroFechaFin") != null) {
            q.setParameter(3, (Date) nmap.get("filtroFechaIni"), TemporalType.DATE);
            q.setParameter(4, (Date) nmap.get("filtroFechaFin"), TemporalType.DATE);
        }
        
        if (nmap.get("filtroTipoEstadoId") != null) {
            q.setParameter(5, (Long) nmap.get("filtroTipoEstadoId"));
        }
        
        if (nmap.get("filtroTipoCertificacionId") != null) {
            q.setParameter(6, (Long) nmap.get("filtroTipoCertificacionId"));
        }

        q.setHint("eclipselink.result-type", "Map");        
        return q.getResultList();
        
    }
}
