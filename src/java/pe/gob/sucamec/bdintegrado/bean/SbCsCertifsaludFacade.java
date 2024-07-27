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
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SbCsCertifsalud;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbCsCertifsaludFacade extends AbstractFacade<SbCsCertifsalud> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbCsCertifsaludFacade() {
        super(SbCsCertifsalud.class);
    }
    public List<SbCsCertifsalud> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbCsCertifsalud s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SbCsCertifsalud> buscarCertificadosMedicos(HashMap mMap){
        try {
            String jpql = "select distinct(a) from SbCsCertifsalud a "+
                            " left join a.sbCsCertifMedicoList l " +
                            " where a.activo = 1 ";
            
            if(! (boolean) mMap.get("busquedaCertifExternos")){
                jpql += " and a.establecimientoId.propietarioId.ruc = :ruc ";
            }
            
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1: // DNI SOLICITANTE
                        jpql = jpql + " and a.solicitanteId.numDoc like :filtro ";
                        break;
                    case 2: // MEDICO QUE AUTORIZÓ
                        jpql = jpql + " and ( (l.medicoId.nroCmp like :filtro) OR (concat(l.medicoId.personaId.apePat, ' ', l.medicoId.personaId.apeMat, ' ', l.medicoId.personaId.nombres) like :filtro) OR (l.medicoId.personaId.numDoc like :filtro) )";
                        break;
                    case 3: // NRO. DE CERTIFICADO
                        jpql = jpql + " and a.numero like :filtro ";
                        break;
                    case 4: // FECHA DE EMISIÓN
                        jpql = jpql + " and FUNC('TRUNC',a.fechaEmision) = FUNC('TRUNC',:fechaSolicitante) ";
                        break;
                    case 5: //  ESTADO
                        jpql = jpql + " and a.estadoId.id = :estadoCertif ";
                        break;
                }
            }
            if(mMap.get("establecimiento") != null){
                jpql = jpql + " and a.establecimientoId.id = :establecimiento ";
            }
            jpql = jpql + " order by a.id desc";

            Query q = em.createQuery(jpql);
            if(! (boolean) mMap.get("busquedaCertifExternos")){
                q.setParameter("ruc", mMap.get("ruc"));
            }
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1:
                    case 2:
                    case 3:
                        q.setParameter("filtro", "%" + mMap.get("filtro").toString() + "%");
                        break;
                    case 4:
                        q.setParameter("fechaSolicitante", (Date) mMap.get("fechaSolicitante"), TemporalType.DATE );
                        break;
                    case 5:
                        q.setParameter("estadoCertif", (Long) mMap.get("estadoCertif"));
                        break;
                }
            }
            if(mMap.get("establecimiento") != null){
                q.setParameter("establecimiento", (Long) mMap.get("establecimiento") );
            }
            return q.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<SbCsCertifsalud> obtenerCertificadosCreadorXEstablecimiento(String ruc, String login) {
        Query q = em.createQuery("select s from SbCsCertifsalud s where s.activo = 1 and s.estadoId.codProg = 'TP_ECS_CRE' "+
                                 " and s.establecimientoId.habilitado = 1 "+
                                 " and s.establecimientoId.propietarioId.ruc = :ruc and s.usuarioId.login = :login " +
                                 " order by s.id desc ");
        q.setParameter("ruc", ruc.trim() );
        q.setParameter("login", login.trim() );
        return q.getResultList();
    }
    
    public int contarCertificadosCreadorXEstablecimiento(String ruc, String login) {
        int cont = 0;
        Query q = em.createQuery("select count(s) from SbCsCertifsalud s where s.activo = 1 and s.estadoId.codProg = 'TP_ECS_CRE' "+
                                 " and s.establecimientoId.habilitado = 1 "+
                                 " and s.establecimientoId.propietarioId.ruc = :ruc and s.usuarioId.login = :login " );
        q.setParameter("ruc", ruc.trim() );
        q.setParameter("login", login.trim() );
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarNroCertificado(String nroCertificado, Long establecimientoId, Long registroId) {
        int cont = 0;
        String plsql = "select count(a) from SbCsCertifsalud a where a.activo = 1 "+
                        " and a.numero = :nroCertificado and a.establecimientoId.id = :establecimientoId ";
        if(registroId != null){
            plsql = plsql + " and a.id != :registroId ";
        }
        
        Query q = em.createQuery(plsql);
        q.setParameter("nroCertificado", nroCertificado);
        q.setParameter("establecimientoId", establecimientoId);
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarCertificadosXSolicitanteXEstado(Long idCertif, Long solicitanteId, String estadoCodProg) {
        int cont = 0;
        String jpql = "select count(s) from SbCsCertifsalud s where s.activo = 1 "+
                                 " and s.fechaVencimiento >= current_date " +
                                 " and s.solicitanteId.id = :solicitanteId ";
        if(idCertif != null){
            jpql += " and s.id != :idCertif ";
        }
        if(estadoCodProg != null){
            jpql += " and s.estadoId.codProg = :estadoCodProg ";
        }
        Query q = em.createQuery(jpql );
        q.setParameter("solicitanteId", solicitanteId );
        if(idCertif != null){
            q.setParameter("idCertif", idCertif );
        }
        if(estadoCodProg != null){
            q.setParameter("estadoCodProg", estadoCodProg );
        }
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public int contarCertificadosCreadosXSolicitanteXEmpresa(Long certifId, Long solicitanteId, String ruc) {
        int cont = 0;
        String jpql = "select count(s) from SbCsCertifsalud s where s.activo = 1 "+
                                 " and s.fechaVencimiento >= current_date " +
                                 " and s.solicitanteId.id = :solicitanteId " + 
                                 " and s.establecimientoId.propietarioId.ruc = :ruc " +
                                 " and s.estadoId.codProg = 'TP_ECS_CRE' ";
        if(certifId != null){
            jpql += " and s.id != :certifId ";
        }
        Query q = em.createQuery(jpql );
        q.setParameter("solicitanteId", solicitanteId );
        q.setParameter("ruc", ruc );
        if(certifId != null){
            q.setParameter("certifId", certifId );
        }

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public SbCsCertifsalud obtenerEstadoCertificadosXSolicitanteXEstado(Long idCertif, Long solicitanteId) {
        String jpql = "select s from SbCsCertifsalud s where s.activo = 1 "+
                                 " and s.fechaVencimiento >= current_date " +
                                 " and s.solicitanteId.id = :solicitanteId ";
        if(idCertif != null){
            jpql += " and s.id != :idCertif ";
        }
        jpql += " order by s.id desc ";
        Query q = em.createQuery(jpql );
        q.setParameter("solicitanteId", solicitanteId );
        if(idCertif != null){
            q.setParameter("idCertif", idCertif );
        }
        if(!q.getResultList().isEmpty()){
            return (SbCsCertifsalud) q.getResultList().get(0);
        }
        
        return null;
    }
    
    public List<Map> buscarCertificadosReporte(String ruc, Integer anio) {
        Query q = em.createQuery("select " +
                                " e.establecimientoId.nombre as ESTABLECIMIENTO, " +
                                " sum(case when e.estadoId.codProg = 'TP_ECS_CRE' and e.condicionObtenidaId.codProg = 'TP_COB_APT' then 1 else 0 end) as CREADO_APTO, " +
                                " sum(case when e.estadoId.codProg = 'TP_ECS_CRE' and e.condicionObtenidaId.codProg = 'TP_COB_NAP' then 1 else 0 end) as CREADO_NOAPTO, " +
                                " sum(case when e.estadoId.codProg = 'TP_ECS_TRA' and e.condicionObtenidaId.codProg = 'TP_COB_APT' then 1 else 0 end) as TRANSMITIDO_APTO, " +
                                " sum(case when e.estadoId.codProg = 'TP_ECS_TRA' and e.condicionObtenidaId.codProg = 'TP_COB_NAP' then 1 else 0 end) as TRANSMITIDO_NOAPTO " +
                                " from SbCsCertifsalud e " +
                                " where e.activo = 1 and (extract(year from e.fechaRegistro)= :anio or extract(year from e.fechaPresentacion)= :anio) " +
                                " and e.establecimientoId.propietarioId.ruc = :ruc " +
                                " group by e.establecimientoId.nombre ");
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("anio", anio);
        q.setParameter("ruc", ruc);
        return q.getResultList();
    }

    public SbCsCertifsalud obtenerCertificadosXSolicitante(String solicitanteDoc, Date fecha) {
        String jpql = "select s from SbCsCertifsalud s where s.activo = 1 "+
                                 //" and s.fechaVencimiento >= current_date " +
                                 " and FUNC('TRUNC',s.fechaVencimiento) >= FUNC('TRUNC',:fechaVencimiento) " +
                                 " and s.solicitanteId.numDoc = :solicitanteDoc " +
                                 " and s.estadoId.codProg = 'TP_ECS_TRA' ";
        /*if(idCertif != null){obtenerCertificadosXSolicitanteXEstado
            jpql += " and s.id != :idCertif ";
        }*/
        jpql += " order by s.id desc ";
        Query q = em.createQuery(jpql );
        q.setParameter("solicitanteDoc", solicitanteDoc );
        q.setParameter("fechaVencimiento", fecha, TemporalType.DATE);
        /*if(idCertif != null){
            q.setParameter("idCertif", idCertif );
        }*/
        if(!q.getResultList().isEmpty()){
            return (SbCsCertifsalud) q.getResultList().get(0);
        }
        
        return null;
    }

    public SbCsCertifsalud obtenerCertificadosXSolicitanteGeneral(String solicitanteDoc) {
        String jpql = "select s from SbCsCertifsalud s where s.activo = 1 "+
                        " and s.solicitanteId.numDoc = :solicitanteDoc " +
                        " and s.estadoId.codProg = 'TP_ECS_TRA' order by s.fechaVencimiento desc";
        Query q = em.createQuery(jpql );
        q.setParameter("solicitanteDoc", solicitanteDoc );
        if(!q.getResultList().isEmpty()){
            return (SbCsCertifsalud) q.getResultList().get(0);
        }
        return null;
    }
    
}
