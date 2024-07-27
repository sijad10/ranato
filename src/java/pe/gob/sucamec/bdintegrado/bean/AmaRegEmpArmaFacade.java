/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaRegEmpArma;
import javax.persistence.TemporalType;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class AmaRegEmpArmaFacade extends AbstractFacade<AmaRegEmpArma> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaRegEmpArmaFacade() {
        super(AmaRegEmpArma.class);
    }
    
    public List<AmaRegEmpArma> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaRegEmpArma a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<AmaRegEmpArma> obtenerListadoArmasRegularizacion(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.estadoVerfica in (1,2,3,4) "+ // and a.expediente is null
                                  " and a.personaId.ruc = :ruc "+
                                  " order by a.id asc" );
        q.setParameter("ruc", s);
        return q.getResultList();
    }
    
    public List<AmaRegEmpArma> obtenerListadoArmasRegularizacionFinalizado(String s, boolean esInpe) {
        if (s == null) {
            s = "";
        }
        String jpql = "select a from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.estadoVerfica in (1,2,3,4) and a.expedienteMaster is not null "+
                      " and a.personaId.ruc = :ruc ";
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        Query q = em.createQuery( jpql );
        q.setParameter("ruc", s);
        return q.getResultList();
    }
    
    public AmaRegEmpArma obtenerArmaVerificada(Long id) {
        Query q = em.createQuery("select a from AmaRegEmpArma a where a.id = :id");
        q.setParameter("id", id);
        return (AmaRegEmpArma) q.getSingleResult();
    }
    
    public AmaRegEmpArma obtenerArmaVerificadaXLicencia(Long licencia) {
        Query q = em.createQuery("select a from AmaRegEmpArma a where a.licDiscaId = :licencia");
        q.setParameter("licencia", licencia);
        
        if(q.getResultList().isEmpty()){
            return null;
        }
        return (AmaRegEmpArma) q.getSingleResult();
    }
    
    public int contarRegistrosXEmitir(String ruc, String expedienteMaster) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' " +
                      " and ( (a.estadoVerfica = 3) OR ( a.estadoVerfica = 1 and a.situacionArma.codProg != 'TP_SITU_VEN')  ) "+
                      " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarArmasPosesion(String ruc, String expedienteMaster) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.estadoVerfica != 4 "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' " +
                      " and a.situacionArma.codProg = 'TP_SITU_POS' " +
                      " and a.personaId.ruc = :ruc " ;
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarArmasMenosLasVendidas(String ruc, String expedienteMaster) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.estadoVerfica not in (2,4) "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' " +
                      " and a.situacionArma.codProg not in ('TP_SITU_VEN','TP_SITU_POS') " +
                      " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarRegistrosNoMePertenecen(String ruc, String expedienteMaster) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.estadoVerfica != 4 "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' " +
                      " and (a.situacionArma.codProg = 'TP_SITU_VEN' OR a.estadoVerfica = 2) " +
                      " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarRegistrosEmitidas(String ruc) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.estadoVerfica = 4 "+
                                  " and a.personaId.ruc = :ruc " );
        q.setParameter("ruc", ruc);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public List<Map> obtenerArmasAgrupadas(String ruc) {
        int cont = 0;
        Query q = em.createQuery("select a.situacionArma.codProg as codProg,count(a) as total from AmaRegEmpArma a where a.activo = 1 and a.estadoVerfica != 4  "+                                  
                                  " and a.personaId.ruc = :ruc " +
                                  " group by a.situacionArma.codProg ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("ruc", ruc);        
        return q.getResultList();
    }
 
    public List<Map> buscarObservacionesRegularizacionPJ(HashMap mMap){
            String sql1 = "SELECT A.ID, A.ESTADO_VERFICA, A.LIC_DISCA_ID, E.NUMERO, E.FECHA_CREACION, P.NOMBRE,"+
                        " PER.RUC, PER.RZN_SOCIAL, TE.COD_PROG ESTADO_CODPROG, TE.nombre ESTADO, " +
                        " EV.nombre EVALUACION, EV.COD_PROG EVALUACION_CODPROG, " +
                        " A.ASUNTO_OBS_SIS, A.DESC_OBS_SIS, A.FECHA_OBS_SIS, " +
                        " ADOC.FECHA_FIRMA AS FECHA_OFICIO, A.FECHA_SUB " +
                        " FROM BDINTEGRADO.AMA_REG_EMP_ARMA A " +
                        " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = A.TIPO_REGU_ID \n" +
                        " INNER JOIN TRAMDOC.EXPEDIENTE E ON A.EXPEDIENTE = E.NUMERO " +
                        " INNER JOIN TRAMDOC.PROCESO P ON P.ID_PROCESO = E.ID_PROCESO " +
                        " INNER JOIN BDINTEGRADO.SB_PERSONA PER ON PER.ID = A.PERSONA_ID " +
                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TE ON TE.ID = A.TIPO_ESTADO_ID "+
                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC EV ON EV.ID = A.TIPO_EVALUACION_ID "+
                        " LEFT JOIN BDINTEGRADO.AMA_DOCUMENTO_REG DOC ON DOC.AMA_REG_EMP_ARMA_ID = A.ID "+
                        " LEFT JOIN BDINTEGRADO.AMA_DOCUMENTO ADOC ON ADOC.ID = DOC.DOCUMENTO_ID " +
                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TTDOC ON TTDOC.ID = ADOC.EVENTO_TRAZA_DOC_ID "+
                        " WHERE A.ACTIVO = 1 AND A.ESTADO_VERFICA IN (1,2,3) AND E.ID_PROCESO IN (712) "+
                        " AND ( EV.COD_PROG IN ('TP_EVA_SIS','TP_EVA_S48H','TP_EVA_S10D') OR (EV.COD_PROG = 'TP_EVA_OFI' AND TTDOC.COD_PROG = 'TP_EVETR_FIR') )" +
                        " AND PER.RUC = ?3 " +
                        " AND TREG.COD_PROG = 'TP_PR_RAESS_SEG' ";

        if (mMap.get("filtro") != null) {
            sql1 = sql1 + " AND E.NUMERO = ?1 ";
        }
        String jpql = sql1 +
                      " ORDER BY 1";
        
        Query q = em.createNativeQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(3, mMap.get("usuario").toString() );
        
        if (mMap.get("filtro") != null) {
            q.setParameter(1, mMap.get("filtro").toString() );
        }
         
        return q.getResultList();
    }
    
    public int contarLicenciaNullRegistros(String ruc) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.estadoVerfica in (1,2) and a.expediente is null "+
                                  " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' " +
                                  " and a.licDiscaId is null and a.personaId.ruc = :ruc " );
        q.setParameter("ruc", ruc);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }    
    
    public int contArmasRegistradasConExpedienteyEmitidas(String s) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and ((a.estadoVerfica in (1,2,3,4) and a.expedienteMaster is not null))"+
                                  " and a.personaId.ruc = :ruc " );
        q.setParameter("ruc", s);
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public String obtenerExpedienteMaster(String s) {
        String expediente = null;
        Query q = em.createQuery("select a.expedienteMaster from AmaRegEmpArma a where a.activo = 1 "+                                  
                                  " and a.personaId.ruc = :ruc and a.expedienteMaster is not null " );
        q.setParameter("ruc", s);
        q.setMaxResults(1);
        if(!q.getResultList().isEmpty()){
            expediente = (String) q.getResultList().get(0);        
        }
        
        return expediente;
    }

    
    public int contArmasTotalesByRuc(String s) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.estadoVerfica in (1,2,3,4) "+
                                  " and a.personaId.ruc = :ruc " );
        q.setParameter("ruc", s);
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contSiTieneExpedienteMasterById(Long id) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.id = :id "+
                                  " and a.expedienteMaster is not null " );
        q.setParameter("id", id);
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public List<AmaRegEmpArma> buscarArmasProcesarDuplicadosAsc(String ids) {
        Query q = em.createQuery("select a from AmaRegEmpArma a where a.id in ("+ids+") order by a.id asc");
        return q.getResultList();
    }
    
    public List<AmaRegEmpArma> buscarArmasProcesarDuplicadosDesc(String ids) {
        Query q = em.createQuery("select a from AmaRegEmpArma a where a.id in ("+ids+") order by a.estadoVerfica desc, a.id asc");
        return q.getResultList();
    }
    
    public List<AmaRegEmpArma> buscarArmasProcesarDuplicadosAsc2(String ids) {
        Query q = em.createQuery("select a from AmaRegEmpArma a where a.id in ("+ids+") order by a.estadoVerfica asc, a.id asc");
        return q.getResultList();
    }
    
    public int contArmasSinExpediente(String s) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.estadoVerfica in (1,2,3,4) "+
                                  " and a.personaId.ruc = :ruc "+
                                  " and a.expedienteMaster is null" );
        q.setParameter("ruc", s);
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contArmasBySerieByLicencia(String ruc, String serie, Long licencia, Long id, boolean esInpe) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+                                  
                                  " and a.personaId.ruc = :ruc "+
                                  " and a.serie = :serie and a.licDiscaId = :licencia ";
        if(id != null){
            jpql = jpql + " and a.id != :id ";
        }
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        q.setParameter("serie", serie);
        q.setParameter("licencia", licencia);
        
        if(id != null){
            q.setParameter("id", id);
        }        
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contArmasBySerieByModelo(String ruc, String serie, Long modelo, Long id) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.personaId.ruc = :ruc "+
                                  " and a.estadoVerfica in (1,2,3,4) " +                                  
                                  " and a.serie = :serie and a.modeloId = :modelo ";
        
        if(id != null){
            jpql = jpql + " and a.id != :id ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        q.setParameter("serie", serie.trim());
        q.setParameter("modelo", modelo);
        if(id != null){
            q.setParameter("id", id);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
     
    public int actualizaExpedienteArmaNativo(Long id, String expediente) {
        
        Query q = em.createNativeQuery("UPDATE BDINTEGRADO.AMA_REG_EMP_ARMA a SET EXPEDIENTE = ?1 "+
                                  " WHERE a.id = ?2 AND EXPEDIENTE IS NULL " );
        q.setParameter(1, expediente);
        q.setParameter(2, id);
        return q.executeUpdate();
    }
    
    public int actualizaExpedienteMasterArmaNativo(Long id, String expediente) {
        
        Query q = em.createNativeQuery("UPDATE BDINTEGRADO.AMA_REG_EMP_ARMA a SET EXPEDIENTE_MASTER = ?1 "+
                                  " WHERE a.id = ?2 AND EXPEDIENTE_MASTER IS NULL " );
        q.setParameter(1, expediente);
        q.setParameter(2, id);
        return q.executeUpdate();
    }
    
    /////// FUNCIONES PARA INPE ///////
    public int contarRegistrosEmitidasRaess(String ruc, String expedienteMaster, Long regionId, boolean esInpe) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a "+
                                  " where a.activo = 1 "+
                                  " and a.estadoVerfica = 4 "+
                                  " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        if(regionId != null){
            jpql += " and a.regionId.id = :regionId";
        }
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        if(regionId != null){
            q.setParameter("regionId", regionId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }

    public int contarRegistrosNoMePertenecenInpe(String ruc, String expedienteMaster, Long regionId) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a "+
                      " where a.activo = 1 "+
                      " and a.estadoVerfica != 4 "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' " +
                      " and (a.situacionArma.codProg = 'TP_SITU_VEN' OR a.estadoVerfica = 2) " +
                      " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        if(regionId != null){
            jpql += " and a.regionId.id = :regionId ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        if(regionId != null){
            q.setParameter("regionId", regionId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarRegistrosXEmitirInpe(String ruc, String expedienteMaster, Long regionId) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a "+
                      " where a.activo = 1 "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' " +
                      " and ( (a.estadoVerfica = 3) OR ( a.estadoVerfica = 1 and a.situacionArma.codProg != 'TP_SITU_VEN')  ) "+
                      " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }        
        if(regionId != null){
            jpql += " and a.regionId.id = :regionId ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        if(regionId != null){
            q.setParameter("regionId", regionId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarArmasPosesionInpe(String ruc, String expedienteMaster, Long regionId) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a "+
                      " where a.activo = 1 "+
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' " +
                      " and a.situacionArma.codProg = 'TP_SITU_POS' " +
                      " and a.estadoVerfica != 4 " +
                      " and a.personaId.ruc = :ruc ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        if(regionId != null){
            jpql += " and a.regionId.id = :regionId ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        if(regionId != null){
            q.setParameter("regionId", regionId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarArmasMenosLasVendidasInpe(String ruc, String expedienteMaster, Long regionId) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a "+
                      " where a.activo = 1 "+
                      " and a.estadoVerfica not in (2,4) "+
                      " and a.situacionArma.codProg not in ('TP_SITU_VEN','TP_SITU_POS') " +
                      " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' " +
                      " and a.personaId.ruc = :ruc ";
                      //" and a.expedienteMaster is null ";
        if(expedienteMaster != null){
            jpql += " and a.expedienteMaster = :expedienteMaster ";
        }else{
            jpql += " and a.expedienteMaster is null ";
        }
        if(regionId != null){
            jpql += " and a.regionId.id = :regionId ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        if(expedienteMaster != null){
            q.setParameter("expedienteMaster", expedienteMaster);
        }
        if(regionId != null){
            q.setParameter("regionId", regionId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public List<AmaRegEmpArma> obtenerListadoArmasRegularizacionRaess(String s, Long regionId, boolean esInpe) {
        String jpql = "";
        if (s == null) {
            s = "";
        }
        jpql = "select a from AmaRegEmpArma a "+
              " left join a.amaRegEmpProcesoList l " +
              " where a.activo = 1 "+
              " and a.estadoVerfica in (1,2,3,4) "+
              " and a.personaId.ruc = :ruc "+                                  
              " and (l.estado in (1, 2) or l.estado is null) ";
        if(regionId != null){
            jpql += " and a.regionId.id = :regionId ";
        }
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        jpql += " order by a.id asc";
        Query q = em.createQuery( jpql );
        q.setParameter("ruc", s);
        if(regionId != null){
            q.setParameter("regionId", regionId);
        }
        return q.getResultList();
    }
    
    
    public List<Map> obtenerExpedienteMasterInpe(String s) {
        Query q = em.createQuery("select distinct a.expedienteMaster as EXPEDIENTE, (case when (l.estado = 3) then l.estado else 0 end) as ESTADO, "+
                                  " l.fechaIni AS FECHA, a.regionId.id AS REGION_ID " +
                                  " from AmaRegEmpArma a "+
                                  " left join a.amaRegEmpProcesoList l " +
                                  " where a.activo = 1 "+                                  
                                  " and a.personaId.ruc = :ruc and a.expedienteMaster is not null "+
                                  " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' " +
                                  " order by l.fechaIni desc " );
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("ruc", s);
        if(!q.getResultList().isEmpty()){
            return q.getResultList();
        }
        
        return null;
    }  
    
    
    public int contarLicenciaNullRegistrosInpe(String ruc) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a "+
                                  " where a.activo = 1 "+
                                  " and a.estadoVerfica in (1,2) "+
                                  " and a.expediente is null" +
                                  " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' " +
                                  " and a.licDiscaId is null and a.personaId.ruc = :ruc " );
        q.setParameter("ruc", ruc);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarRegistrosPendientesInpe(String s) {
        int cont = 0;
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a "+
                                  " left join a.amaRegEmpProcesoList l " +
                                  " where a.activo = 1 "+
                                  " and a.estadoVerfica in (1,2,3,4) "+
                                  " and a.personaId.ruc = :ruc "+
                                  " and (l.estado in (1, 2) or l.estado is null) "+
                                  " and a.expedienteMaster is not null ");
        q.setParameter("ruc", s);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
    
    public AmaRegEmpArma obtenerAmaRegByExpedienteMaster(String nroExpediente, String ruc, boolean esInpe) {
        String jpql = "select a from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.expedienteMaster = :nroExpediente "+
                      " and a.personaId.ruc = :ruc ";
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        Query q = em.createQuery( jpql );
        q.setParameter("nroExpediente", nroExpediente.trim() );
        q.setParameter("ruc", ruc.trim() );
        q.setMaxResults(1);
        
        if(!q.getResultList().isEmpty()){
            return (AmaRegEmpArma) q.getSingleResult();
        }
        
        return null;
    }
    
    public List<AmaRegEmpArma> obtenerListadoArmasRegCompletarProceso(String nroExpediente, String ruc, boolean esInpe) {
        String jpql = "select a from AmaRegEmpArma a where a.activo = 1 "+
                      " and a.expedienteMaster = :nroExpediente "+
                      " and a.personaId.ruc = :ruc " ;
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("nroExpediente", nroExpediente.trim() );
        q.setParameter("ruc", ruc.trim() );
        return q.getResultList();
    }
    
    public int contarTotalArmasRegularizadasByRuc(String ruc) {
        int cont = 0;
        Query q = em.createQuery("select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.personaId.ruc = :ruc " );
        q.setParameter("ruc", ruc);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public String obtenerUsuarioTrazaActualExpediente(String nroExpediente) {
        String usuario = null;
        Query q = em.createNativeQuery("select u.usuario from tramdoc.traza t " +
                                        " inner join tramdoc.expediente ex on ex.id_expediente = t.id_expediente " +
                                        " inner join tramdoc.usuario_por_traza ut on ut.traza = t.id_traza " +
                                        " inner join tramdoc.usuario u on u.id_usuario = ut.usuario " +
                                        " where ex.numero = ?1 and t.actual = 1 " +
                                        " ORDER BY t.id_traza asc " );
        q.setParameter(1, nroExpediente);
        if(!q.getResultList().isEmpty()){
            usuario = (String) q.getResultList().get(0);        
        }
        
        return usuario;
    }
    
    public int contArmasBySerie(String ruc, String serie, Long id, boolean esInpe) {
        int cont = 0;
        String jpql = "select count(a) from AmaRegEmpArma a where a.activo = 1 "+
                                  " and a.personaId.ruc = :ruc "+
                                  " and a.estadoVerfica in (1,2,3,4) " +                      
                                  " and a.serie = :serie ";
        
        if(id != null){
            jpql = jpql + " and a.id != :id ";
        }
        if(esInpe){
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_INP' ";
        }else{
            jpql += " and a.tipoReguId.codProg = 'TP_PR_RAESS_SEG' ";
        }
        
        Query q = em.createQuery(jpql);
        q.setParameter("ruc", ruc);
        q.setParameter("serie", serie.trim());
        if(id != null){
            q.setParameter("id", id);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    ////////////////////////////
    
    
    public List<Map> obtenerExpedienteMasterJuridicas(String s) {
        String jpql = "SELECT DISTINCT A.EXPEDIENTE_MASTER AS EXPEDIENTE, \n" +
                        "  (CASE WHEN AP.ESTADO = 3 THEN AP.ESTADO ELSE 0 END) AS ESTADO, \n" +
                        "  AP.FECHA_INI \n" +
                        " FROM BDINTEGRADO.AMA_REG_EMP_ARMA A \n" +
                        " INNER JOIN BDINTEGRADO.AMA_REG_PRO_ARMA APA ON APA.REG_ARMA_ID = A.ID \n" +
                        " INNER JOIN BDINTEGRADO.AMA_REG_EMP_PROCESO AP ON AP.ID = APA.REG_PROCESO_ID \n" +
                        " INNER JOIN BDINTEGRADO.SB_PERSONA PER ON PER.ID = A.PERSONA_ID \n" +
                        " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = A.TIPO_REGU_ID \n" +
                        " WHERE A.ACTIVO = 1 AND PER.RUC = ?1 \n" +
                        "  AND EXTRACT(YEAR FROM AP.FECHA_INI) >= 2019 " +
                        "  AND A.EXPEDIENTE_MASTER IS NOT NULL \n" +
                        "  AND TREG.COD_PROG = 'TP_PR_RAESS_SEG' \n" +
                        " ORDER BY AP.FECHA_INI DESC ";
        
        Query q = em.createNativeQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, s);
        
        if(!q.getResultList().isEmpty()){
            return q.getResultList();
        }
        
        return null;
    }
    
    public List<Map> buscarObservacionesRegularizacionInpe(HashMap mMap){
             String sql1 = "SELECT A.ID, A.ESTADO_VERFICA, A.LIC_DISCA_ID, E.NUMERO, E.FECHA_CREACION, P.NOMBRE,"+
                        " PER.RUC, PER.RZN_SOCIAL, TE.COD_PROG ESTADO_CODPROG, TE.nombre ESTADO, " +
                        " EV.nombre EVALUACION, EV.COD_PROG EVALUACION_CODPROG, " +
                        " A.ASUNTO_OBS_SIS, A.DESC_OBS_SIS, A.FECHA_OBS_SIS, " +
                        " TRUNC(EN.FECHA) AS FECHA_OFICIO, A.FECHA_SUB " +
                        " FROM BDINTEGRADO.AMA_REG_EMP_ARMA A " +
                        " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = A.TIPO_REGU_ID \n" +
                        " INNER JOIN TRAMDOC.EXPEDIENTE E ON A.EXPEDIENTE = E.NUMERO " +
                        " INNER JOIN TRAMDOC.PROCESO P ON P.ID_PROCESO = E.ID_PROCESO " +
                        " INNER JOIN BDINTEGRADO.SB_PERSONA PER ON PER.ID = A.PERSONA_ID  " +
                        " INNER JOIN BDINTEGRADO.AMA_REG_EMP_NOTIFICA RN ON RN.REG_EMP_ARMA_ID = A.ID AND RN.ACTIVO = 1" +
                        " INNER JOIN BDINTEGRADO.NE_DOCUMENTO N ON N.ID = RN.NOTIFICACION_ID AND N.ACTIVO = 1 " +
                        " INNER JOIN BDINTEGRADO.NE_EVENTO EN ON EN.DOCUMENTO_ID = N.ID AND EN.ACTIVO = 1 " +
                        " INNER JOIN BDINTEGRADO.TIPO_BASE TEN ON TEN.ID = EN.ESTADO_ID AND TEN.COD_PROG = 'TP_EV_ENV' " +
                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TE ON TE.ID = A.TIPO_ESTADO_ID " +
                        " LEFT JOIN BDINTEGRADO.TIPO_GAMAC EV ON EV.ID = A.TIPO_EVALUACION_ID "+
                        " WHERE A.ACTIVO = 1 AND A.ESTADO_VERFICA IN (1,2,3) "+
                        " AND EV.COD_PROG IN ('TP_EVA_OFI') " +
                        " AND PER.RUC = ?3 " +
                        " AND TREG.COD_PROG = 'TP_PR_RAESS_INP' ";

        if (mMap.get("filtro") != null) {
            sql1 = sql1 + " AND E.NUMERO = ?1 ";
        }
        String jpql = sql1 +
                      " ORDER BY 1";
        
        Query q = em.createNativeQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(3, mMap.get("usuario").toString() );
        
        if (mMap.get("filtro") != null) {
            q.setParameter(1, mMap.get("filtro").toString() );
        }
         
        return q.getResultList();
    }
    
    public Date calcularFechaLimiteSubsanacion(Date fecha, int dias) {
        Date fechaCalculada = null;
        String jpql= "SELECT BDINTEGRADO.FNC_FECHA_CALCULADA(?1, ?2) FROM DUAL ";
        javax.persistence.Query q = em.createNativeQuery(jpql);
        q.setParameter(1, fecha, TemporalType.DATE);
        q.setParameter(2, dias);
        
        List<Date> results = q.getResultList();
        for (Date _values : results) {
            fechaCalculada = _values;
            break;
        }

        return fechaCalculada;
    }
      
      
    public int actualizarProcesoTituloExpediente(String nroExp,Integer idProceso, String titulo) {
        try {        
            Query q = em.createNativeQuery("UPDATE TRAMDOC.EXPEDIENTE "
                    + "SET ID_PROCESO = ?2, TITULO = ?3 "
                    + "WHERE NUMERO = ?1 " );
            q.setParameter(1, nroExp);
            q.setParameter(2, idProceso);
            q.setParameter(3, titulo);
            return q.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public List<Map> buscarArmasRegularizacionInpe(Map mMap, boolean isFinalizado) {
        try{
            String jpql = "";            
            //String sqlMigra = "SELECT * FROM (" + ejbAmaMaestroArmasFacade.SQL_DISCA + ") DISCA ";
            
            // Sin verificar
            String jpql_sinverificar = "SELECT DISTINCT 5 AS TIPO, 5 AS ESTADO_VERFICA, NULL AS ID,NULL AS NROEXP, NULL AS FECEXP, A.NRO_LIC, A.NRO_SERIE, A.TIP_ARM, ART.DES_ARM, A.COD_MARCA, "+
                            " M.DES_MARCA, A.COD_MODELO, M.DES_MODELO, A.CALIBRE,  NULL AS FEC_VEN, A.SIT_ARM AS COD_ESTADO, SI.DES_SIT ESTADO, NULL AS PROCESO_ID, NULL AS SERIE_PROP " +
                            " FROM RMA1369.AM_ARMA@SUCAMEC A " +
                            " INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC " +
                            " INNER JOIN RMA1369.SIT_ARMA@SUCAMEC SI ON SI.SIT_ARMA = A.SIT_ARM " +
                            " LEFT JOIN RMA1369.ARTICULO@SUCAMEC ART ON ART.TIP_ART = A.TIP_ART AND ART.TIP_ARM = A.TIP_ARM " +
                            " LEFT JOIN RMA1369.AM_MARCA@SUCAMEC M ON M.TIP_ART = A.TIP_ART AND M.TIP_ARM = A.TIP_ARM AND M.COD_MARCA = A.COD_MARCA AND M.COD_MODELO = A.COD_MODELO " +
                            " WHERE L.COD_USR = ?1 AND L.TIP_USR = 1 AND A.SIT_ARM != 16 AND A.NRO_LIC IN ( " +
                            "      SELECT A.NRO_LIC" +
                            "       FROM RMA1369.AM_ARMA@SUCAMEC A" +
                            "       INNER JOIN RMA1369.AM_LICENCIA@SUCAMEC L ON L.NRO_LIC = A.NRO_LIC" +
                            "       WHERE L.COD_USR = ?1 AND L.TIP_USR = 1" +
                            "      MINUS" +
                            "       SELECT A.LIC_DISCA_ID FROM BDINTEGRADO.AMA_REG_EMP_ARMA A WHERE A.ACTIVO = 1" +
                            "      MINUS" +
                            "       SELECT A.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA A WHERE A.ACTIVO = 1 "+
                            "  )";
            
           // Nuevo
            String jpql_generico = "SELECT ESTADO_VERFICA AS TIPO, ESTADO_VERFICA, A.ID,EXPEDIENTE AS NROEXP, EX.FECHA_CREACION AS FECEXP, LIC_DISCA_ID AS NRO_LIC," +
                                    " SERIE AS NRO_SERIE,T.ID AS TIP_ARM,  T.NOMBRE AS DES_ARM, MA.ID AS COD_MARCA, " +
                                    " MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO,M.MODELO AS DES_MODELO, LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, "+
                                    " NULL AS FEC_VEN, A.SITUACION_ARMA AS COD_ESTADO, TG.NOMBRE AS ESTADO, NULL AS PROCESO_ID, A.SERIE_PROPUESTA AS SERIE_PROP " +
                                    " FROM BDINTEGRADO.AMA_REG_EMP_ARMA A" +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = A.TIPO_REGU_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.SITUACION_ARMA " +
                                    " LEFT JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = A.EXPEDIENTE" +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO T ON M.TIPO_ARMA_ID = T.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID " +
                                    " WHERE A.ACTIVO = 1 AND A.PERSONA_ID = ?3 AND A.ESTADO_VERFICA != 4  "+
                                    " AND (A.MODELO_ID, A.SERIE, A.LIC_DISCA_ID) NOT IN (SELECT AR.MODELO_ID, AR.SERIE, AR.LICENCIA_DISCA FROM BDINTEGRADO.AMA_ARMA AR " +
                                    "       INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = AR.ID " +
                                    "       WHERE AR.ACTIVO = 1 AND T.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3) AND TREG.COD_PROG = 'TP_PR_RAESS_INP' ";
            
            // Emitidos
            String jpql_emitidos = "SELECT DISTINCT 4 AS TIPO, REG.ESTADO_VERFICA, REG.ID AS ID, EX.NUMERO AS NROEXP, EX.FECHA_CREACION AS FECEXP, A.LICENCIA_DISCA AS NRO_LIC, A.SERIE AS NRO_SERIE," +
                                    "  TA.ID AS TIP_ARM, TA.NOMBRE AS DES_ARM,MA.ID AS COD_MARCA, MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO, M.MODELO AS DES_MODELO," +
                                    "  LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, NULL AS FEC_VEN, A.ESTADO_ID AS COD_ESTADO, TG.NOMBRE AS ESTADO, PROC.ID AS PROCESO_ID, NULL AS SERIE_PROP " +
                                    " FROM BDINTEGRADO.AMA_ARMA A" +
                                    "  INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = A.ID" +
                                    "  INNER JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = T.NRO_EXPEDIENTE" +
                                    "  INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.ESTADO_ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_REG_EMP_ARMA REG ON REG.LIC_DISCA_ID = A.LICENCIA_DISCA AND T.PERSONA_COMPRADOR_ID = REG.PERSONA_ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_REG_PRO_ARMA RPRO ON RPRO.REG_ARMA_ID = REG.ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_REG_EMP_PROCESO PROC ON PROC.ID = RPRO.REG_PROCESO_ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID " +
                                    "  LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID" +
                                    "  LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID" +
                                    "  LEFT JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID" +
                                    "  LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID" +
                                    " WHERE T.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3 AND A.ACTIVO = 1 AND A.LICENCIA_DISCA IS NOT NULL AND REG.ESTADO_VERFICA IS NULL";

            // Procesados (con tarjeta de propiedad)
            String jpql_procesados = "SELECT DISTINCT 4 AS TIPO, REG.ESTADO_VERFICA, REG.ID AS ID, EX.NUMERO AS NROEXP, EX.FECHA_CREACION AS FECEXP, A.LICENCIA_DISCA AS NRO_LIC, A.SERIE AS NRO_SERIE, "+
                                    " TA.ID AS TIP_ARM, TA.NOMBRE AS DES_ARM, MA.ID AS COD_MARCA, MA.NOMBRE AS DES_MARCA, M.ID AS COD_MODELO, M.MODELO AS DES_MODELO, " +
                                    " LISTAGG(CA.NOMBRE, '/') WITHIN GROUP (ORDER BY M.ID) AS CALIBRE, NULL AS FEC_VEN, A.ESTADO_ID AS COD_ESTADO, TG.NOMBRE AS ESTADO, PROC.ID AS PROCESO_ID, NULL AS SERIE_PROP " +
                                    " FROM BDINTEGRADO.AMA_ARMA A " +
                                    " INNER JOIN BDINTEGRADO.AMA_TARJETA_PROPIEDAD T ON T.ARMA_ID = A.ID  " +
                                    " INNER JOIN TRAMDOC.EXPEDIENTE EX ON EX.NUMERO = T.NRO_EXPEDIENTE " +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TG ON TG.ID = A.SITUACION_ID " +
                                    " INNER JOIN BDINTEGRADO.AMA_REG_EMP_ARMA REG ON REG.PERSONA_ID = T.PERSONA_COMPRADOR_ID AND REG.SERIE = A.SERIE AND (A.MODELO_ID = REG.MODELO_ID OR A.LICENCIA_DISCA = REG.LIC_DISCA_ID) " +
                                    " INNER JOIN BDINTEGRADO.TIPO_GAMAC TREG ON TREG.ID = REG.TIPO_REGU_ID \n" +
                                    " LEFT JOIN BDINTEGRADO.AMA_REG_PRO_ARMA RPRO ON RPRO.REG_ARMA_ID = REG.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_REG_EMP_PROCESO PROC ON PROC.ID = RPRO.REG_PROCESO_ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELOS M ON A.MODELO_ID = M.ID  " +
                                    " LEFT JOIN BDINTEGRADO.AMA_MODELO_CALIBRE MC ON MC.MODELO_ID = M.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO CA ON MC.CATALOGO_ID = CA.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO TA ON M.TIPO_ARMA_ID = TA.ID " +
                                    " LEFT JOIN BDINTEGRADO.AMA_CATALOGO MA ON M.MARCA_ID = MA.ID " +
                                    " WHERE T.ACTIVO = 1 AND REG.ACTIVO = 1 AND T.PERSONA_COMPRADOR_ID = ?3 AND A.ACTIVO = 1 AND TREG.COD_PROG = 'TP_PR_RAESS_INP'  ";

            if(mMap.get("tipoBusqueda") != null){
                switch(mMap.get("tipoBusqueda").toString()){
                    case "1": 
                            jpql_sinverificar = jpql_sinverificar + " AND A.NRO_LIC = ?2 ";
                            jpql_generico = jpql_generico + " AND LIC_DISCA_ID = ?2 ";                            
                            jpql_emitidos = jpql_emitidos + " AND A.LICENCIA_DISCA = ?2 ";
                            jpql_procesados = jpql_procesados + " AND A.LICENCIA_DISCA = ?2 ";
                            break;
                    case "2":
                            jpql_sinverificar = jpql_sinverificar + " AND A.NRO_SERIE = ?2 ";
                            jpql_generico = jpql_generico + " AND SERIE = ?2 ";
                            jpql_emitidos = jpql_emitidos + " AND A.SERIE = ?2 ";
                            jpql_procesados = jpql_procesados + " AND A.SERIE = ?2 ";
                            break;
                }
            }
            if(mMap.get("regionInpeSelected") != null){                
                jpql_generico = jpql_generico + " AND A.REGION_ID = ?7 ";
                jpql_emitidos = jpql_emitidos + " AND ( REG.ID IS NULL OR (REG.ID IS NOT NULL AND REG.REGION_ID = ?7) ) ";
                jpql_procesados = jpql_procesados + " AND REG.REGION_ID = ?7 ";
            }
            if(mMap.get("tipoEstado") != null){
                switch(mMap.get("tipoEstado").toString()){
                    case "sinverificar":
                                        jpql = jpql_sinverificar;
                                        if(mMap.get("regionInpeSelected") != null){
                                            return null;
                                        }
                                        if(isFinalizado){
                                            return null;
                                        }
                                        break;
                    case "verificado":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 1 "+                                        
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                                        break;
                    case "nopertenece":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 2 "+
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                                        break;
                    case "nuevo":
                                        jpql = jpql_generico + " AND A.ESTADO_VERFICA = 3 "+
                                                " GROUP BY A.ID,ESTADO_VERFICA,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                                        break;
                    case "emitidos":
                                        jpql = jpql_emitidos+
                                                " GROUP BY ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID" +
                                                " UNION " +
                                                jpql_procesados +
                                                " GROUP BY REG.ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID ";
                                        
                                        break;
                }
            }else{
                jpql_generico = jpql_generico + " GROUP BY ESTADO_VERFICA,A.ID,EXPEDIENTE,EX.FECHA_CREACION,LIC_DISCA_ID,SERIE,T.ID,T.NOMBRE,MA.ID,MA.NOMBRE, M.ID,M.MODELO,A.SITUACION_ARMA,TG.NOMBRE,A.SERIE_PROPUESTA";
                jpql_emitidos = jpql_emitidos + " GROUP BY ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID";
                jpql_procesados = jpql_procesados + " GROUP BY REG.ESTADO_VERFICA,REG.ID, EX.NUMERO, EX.FECHA_CREACION, A.LICENCIA_DISCA, A.SERIE, TA.ID, TA.NOMBRE,MA.ID, MA.NOMBRE, M.ID, M.MODELO,A.ESTADO_ID, TG.NOMBRE, PROC.ID ";
                        
                jpql = ((isFinalizado || mMap.get("regionInpeSelected") != null)?"":jpql_sinverificar + " UNION ") + jpql_generico + " UNION "+ jpql_emitidos + " UNION "+ jpql_procesados+ " ORDER BY ID ASC";
            }
            
            Query q = em.createNativeQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            
            if(mMap.get("tipoBusqueda") != null){
                switch(mMap.get("tipoBusqueda").toString()){
                    case "1":
                    case "2":
                            q.setParameter(2, mMap.get("filtro"));
                            break;
                    case "3":
                            break;
                }
            }
            if(mMap.get("tipoEstado") != null){
                switch(mMap.get("tipoEstado").toString()){
                    case "sinverificar":
                            q.setParameter(1, mMap.get("ruc").toString());
                            break;
                    default:
                            q.setParameter(3, (Long) mMap.get("idEmpresa"));
                            if(mMap.get("regionInpeSelected") != null){
                                q.setParameter(7, (Long) mMap.get("regionInpeSelected"));    
                            }
                            break;
                }
            }else{
                q.setParameter(3, (Long) mMap.get("idEmpresa"));
                if(!isFinalizado){
                    q.setParameter(1, mMap.get("ruc").toString());
                }
                if(mMap.get("regionInpeSelected") != null){
                    q.setParameter(7, (Long) mMap.get("regionInpeSelected"));    
                }
            }
            return (List<Map>) q.getResultList();
        }catch(Exception ex){
            return null;
        }
    }
}
