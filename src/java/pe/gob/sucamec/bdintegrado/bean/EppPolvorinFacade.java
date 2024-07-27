/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppPolvorin;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.SbDistritoGt;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppPolvorinFacade extends AbstractFacade<EppPolvorin> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppPolvorinFacade() {
        super(EppPolvorin.class);
    }

    public List<EppPolvorin> buscarPolvorinGt(String razSocEmp, SbPersonaGt paramInEmpresa, SbDistritoGt ubigeo) {
        String jpql = "select p from EppPolvorin p "
                + "where "
                + "p.direccion is not null and "
                + "(p.propietarioId.id in (select distinct(r.empresaId.id) from Registro r where r.tipoProId.codProg in ('TP_PRTUP_COM','TP_PRTUP_PLA') and r.activo = 1)) and "
                + "(p.propietarioId.id = :empalq or :empalq is null) and "
                + "p.activo = 1 and "
                + "(p.propietarioId.rznSocial like :razSocEmp or "
                + "p.descripcion like :razSocEmp or "
                + "p.registroId.eppResolucion.numero like :razSocEmp or :razSocEmp is null) and "
                + "(p.distritoId.id = :ubi or :ubi is null) and "
                + "(p.registroId.eppResolucion.fechaFin >= current_date) "
                + "union "
                + "select p.polvorinId from EppContratoAlqPolv p "
                + "where "
                + "p.polvorinId.direccion is not null and "
                + "(p.arrendatariaId.id in (select distinct(r.empresaId.id) from Registro r where r.tipoProId.codProg in ('TP_PRTUP_COM','TP_PRTUP_PLA') and r.activo = 1)) and "
                + "(p.arrendatariaId.id = :empalq or :empalq is null) and "
                + "(p.arrendatariaId.rznSocial like :razSocEmp or "
                + "p.polvorinId.descripcion like :razSocEmp or "
                + "p.polvorinId.registroId.eppResolucion.numero like :razSocEmp or :razSocEmp is null) and "
                + "(p.polvorinId.distritoId.id = :ubi or :ubi is null) and "
                + "p.fecfinAlq >= current_date and "
                + "(p.polvorinId.registroId.eppResolucion.fechaFin >= current_date)";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("empalq", paramInEmpresa == null ? null : paramInEmpresa.getId());
        q.setParameter("razSocEmp", razSocEmp == null ? null : ("%" + razSocEmp + "%"));
        q.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());
        return q.getResultList();
    }

    public List<EppPolvorin> buscarPolvorinGtDestino(String filtro, EppRegistro r, SbDistritoGt ubigeo) {
        String jpql = "select l from EppRegistro r "
                + "inner join r.eppPolvorinList l "
                + "where "
                + "r.id = :rid and "
                + "l.activo = 1 and r.activo = 1 and "
                + "l.registroId.eppResolucion.fechaFin >= current_date and "
                + "(l.descripcion like :filtro or "
                + "l.propietarioId.rznSocial like :filtro or "
                + "l.registroId.eppResolucion.numero like :filtro or :filtro is null) and "
                + "(l.distritoId.id = :ubi or :ubi is null)";
        Query q = getEntityManager().createQuery(jpql);
        q.setParameter("rid", r.getId());
        q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));
        q.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());
        return q.getResultList();
    }
    
    public List<EppPolvorin> lstPovorinXEmpresa(SbPersonaGt persona) {
        try {
            javax.persistence.Query q = em.createQuery(
                    "SELECT p FROM EppPolvorin p WHERE p.propietarioId.id = :parametro AND p.activo = 1 and p.registroId.activo = 1 and p.registroId.eppResolucion.fechaFin >= current_date "
            );
            q.setParameter("parametro", persona.getId());
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param empresa
     * @param filtro
     * @param ubigeo
     * @return
     */
    public List<EppPolvorin> buscarPolvorinGtTipo5(String filtro,SbPersonaGt empresa, SbDistritoGt ubigeo) {
        try {
            String jpql = "SELECT p FROM EppPolvorin p \n"
                    + "inner join p.registroId.eppResolucion res \n"
                    + "WHERE \n"
                    + "p.activo = 1 and \n"
                    + "(p.propietarioId.id = :empresa or :empresa is null) and \n"
                    + "((p.direccion LIKE :filtro or :filtro is null) or \n"
                    + "(res.numero like :filtro or :filtro is null) or \n"
                    + "((p.propietarioId.rznSocial like :filtro or :filtro is null) or \n"
                    + "(CONCAT(p.propietarioId.nombres ,' ', p.propietarioId.apePat ,' ', p.propietarioId.apeMat) like :filtro or :filtro is null))) and "
                    + "(p.distritoId.id = :ubi or :ubi is null) "
                    + " and res.fechaFin >= current_date ";

            //INCLUYENDO POLVORINES ALQUILADOS
            jpql = jpql + " union "
                    + "select cap.polvorinId from EppContratoAlqPolv cap \n"
                    + "inner join cap.polvorinId.registroId.eppResolucion resalq \n"
                    + "where \n"
                    + "cap.polvorinId.activo = 1 and \n"
                    + "(cap.arrendatariaId.id = :empresa or :empresa is null) and \n"
                    + "((cap.polvorinId.direccion LIKE :filtro or :filtro is null) or \n"
                    + "(resalq.numero like :filtro or :filtro is null) or \n"
                    + "((cap.arrendatariaId.rznSocial like :filtro or :filtro is null) or \n"
                    + "(CONCAT(cap.arrendatariaId.nombres ,' ', cap.arrendatariaId.apePat ,' ', cap.arrendatariaId.apeMat) like :filtro or :filtro is null))) and "
                    + "(cap.polvorinId.distritoId.id = :ubi or :ubi is null) "
                    + " and cap.fecfinAlq >= current_date ";

            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("empresa", empresa == null ? null : empresa.getId());
            q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));
            q.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());

            return q.setMaxResults(80).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<EppPolvorin> buscarPolvorinGtTipo2(EppRegistro reg, String filtro) {
        try {
            String jpql = "select p from EppRegistro r "
                    + " join r.eppPolvorinList p "
                    + " where "
                    + " r.id = :reg and "
                    + " p.activo = 1 and r.activo = 1 and "
                    + " (p.descripcion like :filtro  or "
                    + " p.propietarioId.rznSocial like :filtro or"
                    + " p.registroId.eppResolucion.numero like :filtro) and "
                    + " p.registroId.eppResolucion.fechaFin >= current_date ";

            javax.persistence.Query q = em.createQuery(jpql);
            q.setParameter("reg", reg.getId());
            q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));

            return q.setMaxResults(80).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Map> buscarMapPolvorinGt(String razSocEmp, SbPersonaGt paramInEmpresa, SbDistritoGt ubigeo) {
        String idsComercio = "";
        String jpqlComer = "select distinct(r.empresaId.id) from Registro r where r.tipoProId.codProg in ('TP_PRTUP_COM','TP_PRTUP_PLA') and r.activo = 1 ";
        Query qCom = getEntityManager().createQuery(jpqlComer);
        List<Long> lstIdComercio = qCom.getResultList();
        for (Long id : lstIdComercio) {
            if (idsComercio.isEmpty()) {
                idsComercio = "" + id;
            } else {
                idsComercio = idsComercio + "," + id;
            }
        }
         if (idsComercio.isEmpty()) {
                return null;
            }
        
        String jpql = "select distinct 1 AS TIPO, p.id, p.descripcion, p.activo, p.registroId.eppResolucion.numero, p.direccion, "
                + " p.propietarioId.rznSocial, CONCAT(p.distritoId.nombre, ' / ', p.distritoId.provinciaId.nombre, ' / ', p.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, "
                + " 0 as contratoId, p.registroId.eppResolucion.fechaFin, current_date as fechaFinContrato "
                + " from EppPolvorin p "
                + "where "
                + " p.direccion is not null and "
                + "(p.propietarioId.id in ("+idsComercio+")) and "
                + " (p.propietarioId.id = :empalq or :empalq is null) and "
                + " p.activo = 1 and p.registroId.eppResolucion.activo = 1 and "
                + " p.registroId.estado.codProg = 'TP_REGEV_FIN' and "
                + " p.registroId.tipoOpeId.codProg != 'TP_OPE_CAN' and "
                + " (p.propietarioId.rznSocial like :razSocEmp or "
                + " p.descripcion like :razSocEmp or "
                + " p.registroId.eppResolucion.numero like :razSocEmp or :razSocEmp is null) and "
                + " (p.distritoId.id = :ubi or :ubi is null) and "
                + " (p.registroId.eppResolucion.fechaFin >= current_date) "
                + " UNION "
                + "select distinct 2 AS TIPO, p.polvorinId.id, p.polvorinId.descripcion, p.polvorinId.activo, p.polvorinId.registroId.eppResolucion.numero, p.polvorinId.direccion, "
                + " p.arrendatariaId.rznSocial, CONCAT(p.polvorinId.distritoId.nombre, ' / ', p.polvorinId.distritoId.provinciaId.nombre, ' / ', p.polvorinId.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, "
                + " p.id as contratoId, p.polvorinId.registroId.eppResolucion.fechaFin, p.fecfinAlq as fechaFinContrato "
                + " from EppContratoAlqPolv p "
                + "where "
                + "p.activo = 1 and p.polvorinId.registroId.activo = 1 and p.polvorinId.registroId.eppResolucion.activo = 1 and "
                + "p.polvorinId.direccion is not null and "
                + " p.polvorinId.registroId.estado.codProg = 'TP_REGEV_FIN' and "
                + "(p.arrendatariaId.id in ("+idsComercio+")) and "
                + "(p.arrendatariaId.id = :empalq or :empalq is null) and "
                + "(p.arrendatariaId.rznSocial like :razSocEmp or "
                + "p.polvorinId.descripcion like :razSocEmp or "
                + "p.polvorinId.registroId.eppResolucion.numero like :razSocEmp or :razSocEmp is null) and "                
                + "(p.polvorinId.distritoId.id = :ubi or :ubi is null) and "
                + "p.fecfinAlq >= current_date and "
                + "(p.polvorinId.registroId.eppResolucion.fechaFin >= current_date)";
        
        Query q = getEntityManager().createQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("empalq", paramInEmpresa == null ? null : paramInEmpresa.getId());
        q.setParameter("razSocEmp", razSocEmp == null ? null : ("%" + razSocEmp + "%"));
        q.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());
        return q.getResultList();
    }
    
    public List<Map> buscarMapPolvorinGtTipo2(EppRegistro reg, String filtro) {
        try {
            String jpql = "select distinct 1 AS TIPO, p.id, p.descripcion, p.activo, p.registroId.eppResolucion.numero, p.direccion, "
                    + " p.propietarioId.rznSocial, CONCAT(p.distritoId.nombre, ' / ', p.distritoId.provinciaId.nombre, ' / ', p.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, "
                    + " 0 as contratoId, p.registroId.eppResolucion.fechaFin, current_date as fechaFinContrato "
                    + " from EppRegistro r "
                    + " join r.eppPolvorinList p "
                    + " where "
                    + " r.id = :reg and "
                    + " p.activo = 1 and r.activo = 1 and p.registroId.eppResolucion.activo = 1 and p.registroId.estado.codProg = 'TP_REGEV_FIN' and "
                    + " (p.descripcion like :filtro  or "
                    + " p.propietarioId.rznSocial like :filtro or"
                    + " p.registroId.eppResolucion.numero like :filtro) and "
                    + " p.registroId.eppResolucion.fechaFin >= current_date and "
                    + " p.registroId.tipoOpeId.codProg != 'TP_OPE_CAN' ";

            javax.persistence.Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("reg", reg.getId());
            q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));

            return q.setMaxResults(80).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Map> buscarMapPolvorinGtDestino(String filtro, EppRegistro r, SbDistritoGt ubigeo) {
        try {
            String jpql = "select distinct 1 AS TIPO, l.id, l.descripcion, l.activo, l.registroId.eppResolucion.numero, l.direccion, "
                    + " l.propietarioId.rznSocial, CONCAT(l.distritoId.nombre, ' / ', l.distritoId.provinciaId.nombre, ' / ', l.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, "
                    + " 0 as contratoId, l.registroId.eppResolucion.fechaFin, current_date as fechaFinContrato "
                    + " from EppRegistro r "
                    + " inner join r.eppPolvorinList l "
                    + " where "
                    + " r.id = :rid and "
                    + " l.activo = 1 and r.activo = 1 and l.registroId.eppResolucion.activo = 1 and "
                    + " l.registroId.estado.codProg = 'TP_REGEV_FIN' and "
                    + " l.registroId.eppResolucion.fechaFin >= current_date and "
                    + " l.registroId.tipoOpeId.codProg != 'TP_OPE_CAN' and  "
                    + " (l.descripcion like :filtro or "
                    + " l.propietarioId.rznSocial like :filtro or "
                    + " l.registroId.eppResolucion.numero like :filtro or :filtro is null) and "
                    + " (l.distritoId.id = :ubi or :ubi is null)";
            Query q = getEntityManager().createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("rid", r.getId());
            q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));
            q.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Map> buscarMapPolvorinGtTipo5(String filtro,SbPersonaGt empresa, SbDistritoGt ubigeo) {
        try {
            String jpql = "SELECT distinct 1 AS TIPO, p.id, p.descripcion, p.activo, p.registroId.eppResolucion.numero, p.direccion, "
                    + " p.propietarioId.rznSocial, CONCAT(p.distritoId.nombre, ' / ', p.distritoId.provinciaId.nombre, ' / ', p.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, "
                    + " 0 as contratoId, p.registroId.eppResolucion.fechaFin, current_date as fechaFinContrato "
                    + " FROM EppPolvorin p \n"
                    + " inner join p.registroId.eppResolucion res \n"
                    + " WHERE \n"
                    + " p.activo = 1 and res.activo = 1 and  \n"
                    + " p.registroId.tipoOpeId.codProg != 'TP_OPE_CAN' and \n"
                    + " p.registroId.estado.codProg = 'TP_REGEV_FIN' and "
                    + " (p.propietarioId.id = :empresa or :empresa is null) and \n"
                    + " ((p.direccion LIKE :filtro or :filtro is null) or \n"
                    + " (res.numero like :filtro or :filtro is null) or \n"
                    + " ((p.propietarioId.rznSocial like :filtro or :filtro is null) or \n"
                    + " (CONCAT(p.propietarioId.nombres ,' ', p.propietarioId.apePat ,' ', p.propietarioId.apeMat) like :filtro or :filtro is null))) and "
                    + " (p.distritoId.id = :ubi or :ubi is null) "
                    + " and res.fechaFin >= current_date ";

            //INCLUYENDO POLVORINES ALQUILADOS
            jpql = jpql + " UNION "
                    + "select distinct 2 AS TIPO, cap.polvorinId.id, cap.polvorinId.descripcion, cap.polvorinId.activo, cap.polvorinId.registroId.eppResolucion.numero, cap.polvorinId.direccion, "
                    + " cap.arrendatariaId.rznSocial, CONCAT(cap.polvorinId.distritoId.nombre, ' / ', cap.polvorinId.distritoId.provinciaId.nombre, ' / ', cap.polvorinId.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, "
                    + " cap.id as contratoId, cap.polvorinId.registroId.eppResolucion.fechaFin, cap.fecfinAlq as fechaFinContrato "
                    + " from EppContratoAlqPolv cap \n"
                    + " inner join cap.polvorinId.registroId.eppResolucion resalq \n"
                    + " where \n"
                    + " cap.polvorinId.activo = 1 and cap.activo = 1 and \n"
                    + " (cap.arrendatariaId.id = :empresa or :empresa is null) and \n"
                    + " ((cap.polvorinId.direccion LIKE :filtro or :filtro is null) or \n"
                    + " (resalq.numero like :filtro or :filtro is null) or \n"
                    + " ((cap.arrendatariaId.rznSocial like :filtro or :filtro is null) or \n"
                    + " (CONCAT(cap.arrendatariaId.nombres ,' ', cap.arrendatariaId.apePat ,' ', cap.arrendatariaId.apeMat) like :filtro or :filtro is null))) and "
                    + " (cap.polvorinId.distritoId.id = :ubi or :ubi is null) "
                    + " and cap.fecfinAlq >= current_date ";

            javax.persistence.Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("empresa", empresa == null ? null : empresa.getId());
            q.setParameter("filtro", filtro == null ? null : ("%" + filtro + "%"));
            q.setParameter("ubi", ubigeo == null ? null : ubigeo.getId());

            return q.setMaxResults(80).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<Map> lstMapPovorinXEmpresa(SbPersonaGt persona) {
        try {
            String jpql = " SELECT distinct 1 AS TIPO, p.id, p.descripcion, p.activo, p.registroId.eppResolucion.numero, p.direccion, " +
                          " p.propietarioId.rznSocial, CONCAT(p.distritoId.nombre, ' / ', p.distritoId.provinciaId.nombre, ' / ', p.distritoId.provinciaId.departamentoId.nombre) AS ubigeo, " +
                          " 0 as contratoId, p.registroId.eppResolucion.fechaFin " +
                          " FROM EppPolvorin p " +
                          " WHERE p.propietarioId.id = :parametro AND p.activo = 1 and p.registroId.eppResolucion.activo = 1 " +
                          " and p.registroId.estado.codProg = 'TP_REGEV_FIN' " +
                          " and p.registroId.activo = 1 and p.registroId.eppResolucion.fechaFin >= current_date " +
                          " and p.registroId.tipoOpeId.codProg != 'TP_OPE_CAN' ";
            javax.persistence.Query q = em.createQuery(jpql);
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter("parametro", persona.getId());
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
