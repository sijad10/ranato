/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.renagi.data.ConsultaSbPersona;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;

/**
 *
 * @author rarevalo
 */
@Stateless
public class ConsultaSbPersonaFacade extends AbstractFacade<ConsultaSbPersona> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaSbPersonaFacade() {
        super(ConsultaSbPersona.class);
    }

    public List<ConsultaSbPersona> listxRepresentante(String filtro) {
        if (filtro != null) {
            Query q = em.createQuery(
                    "select p from ConsultaSbPersona p"
                            + " inner join GamacSbRelacionPersona rp ON rp.personaDestId.id = p.id and rp.personaOriId.id = :idOrigen "                            
                            + " where"
                            + " p.activo = 1 and p.numDoc is not null"
                            + " order by p.apePat, p.apeMat, p.nombres");
            
            q.setParameter("idOrigen", Long.valueOf(filtro)); 
            return q.getResultList();

        }else{
            return null;
        }

    }
    
    public List<ConsultaSbPersona> selectLikePersonaNatural() {
        Query q = em.createQuery(
                "select p from ConsultaSbPersona p where p.activo = 1 and p.numDoc is not null order by p.apePat, p.apeMat, p.nombres");
        return q.getResultList();

    }

    public ConsultaSbPersona findByNumDoc(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("ConsultaSbPersona.findByNumDoc", ConsultaSbPersona.class).setParameter("numDoc", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<ConsultaSbPersona> list = q.getResultList();
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
    
    public ConsultaSbPersona findByNumDoc(String filtro, String tipoDoc) {
        if (filtro != null) {
            try {
                switch(tipoDoc){
                    case "DNI":
                        filtro = JsfUtil.leftpadString(filtro, 8, "0");
                        break;
                    case "CE":
                        filtro = JsfUtil.leftpadString(filtro, 9, "0");
                        break;
                }
                Query q = em.createNamedQuery("ConsultaSbPersona.findByNumDoc", ConsultaSbPersona.class).setParameter("numDoc", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<ConsultaSbPersona> list = q.getResultList();
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

    public ConsultaSbPersona findByNroCip(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("ConsultaSbPersona.findByNroCip", ConsultaSbPersona.class).setParameter("nroCip", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<ConsultaSbPersona> list = q.getResultList();
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

    public ConsultaSbPersona findByRuc(String filtro) {
        if (filtro != null) {
            try {
                Query q = em.createNamedQuery("ConsultaSbPersona.findByRuc", ConsultaSbPersona.class).setParameter("ruc", filtro.trim());
                q.setHint("eclipselink.batch.type", "IN");
                List<ConsultaSbPersona> list = q.getResultList();
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
    
    public List<ArrayRecord> listarNoPropietariosxFiltro(String tipo, String filtro) {
        String where = "";
        String campos = "";
        if (tipo.equals("dni")) {
            campos = " 'DNI' TIPO_DOC, 'PERS. NATURAL' TIPO_PROPIETARIO, NUM_DOC DOC_PROPIETARIO, TRIM(APE_PAT) || ' ' || TRIM(APE_MAT) || ' ' || TRIM(NOMBRES) AS PROPIETARIO ";
            where = "NUM_DOC=?1 AND TIPO_ID=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_PER_NAT') "
                    + "AND TIPO_DOC=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_DOCID_DNI')";
        }
        if (tipo.equals("cip")) {
            campos = " 'CIP' TIPO_DOC, 'POLICIA' TIPO_PROPIETARIO, NRO_CIP DOC_PROPIETARIO, TRIM(APE_PAT) || ' ' || TRIM(APE_MAT) || ' ' || TRIM(NOMBRES) AS PROPIETARIO ";
            where = "NRO_CIP=?1 AND TIPO_ID=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_PER_NAT') "
                    + "AND TIPO_DOC=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_DOCID_DNI')";
        }
        if (tipo.equals("ruc")) {
            campos = " 'RUC' TIPO_DOC, 'PERS. JURIDICA' TIPO_PROPIETARIO, RUC DOC_PROPIETARIO, TRIM(RZN_SOCIAL) AS PROPIETARIO ";
            where = "RUC=?1 AND TIPO_ID=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_PER_JUR') "
                    + "AND TIPO_DOC=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_DOCID_RUC')";
        }
        if (tipo.equals("ce")) {
            campos = " 'CE' TIPO_DOC, 'EXTRANJERO' TIPO_PROPIETARIO, NUM_DOC DOC_PROPIETARIO, TRIM(APE_PAT) || ' ' || TRIM(APE_MAT) || ' ' || TRIM(NOMBRES) AS PROPIETARIO ";
            where = "NUM_DOC=?1 AND TIPO_ID=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_PER_NAT') "
                    + "AND TIPO_DOC=(SELECT ID FROM BDINTEGRADO.TIPO_BASE WHERE COD_PROG='TP_DOCID_CE')";
        }
        String sql = "SELECT " + campos + " FROM BDINTEGRADO.SB_PERSONA WHERE "+ where;
        try {
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, filtro.trim().toUpperCase());
            q.setHint("eclipselink.result-type", "Map");
            List<ArrayRecord> list = q.setMaxResults(MAX_RES).getResultList(); 
            
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
