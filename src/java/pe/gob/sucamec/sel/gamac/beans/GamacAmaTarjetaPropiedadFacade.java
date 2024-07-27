/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import pe.gob.sucamec.sel.gamac.data.GamacAmaArma;
import pe.gob.sucamec.sel.gamac.data.GamacAmaTarjetaPropiedad;
import pe.gob.sucamec.sel.gamac.data.GamacSbDireccion;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES_C;

/**
 *
 * @author rarevalo
 */
@Stateless
public class GamacAmaTarjetaPropiedadFacade extends AbstractFacade<GamacAmaTarjetaPropiedad> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacAmaTarjetaPropiedadFacade() {
        super(GamacAmaTarjetaPropiedad.class);
    }

    public List<ArrayRecord> listPerNatPnpFa(String filtro) {
        if (filtro != null) {
            String sql = "SELECT "
                    + " p.ID || ',' || tg.NOMBRE ID,"
                    + " p.ID PERSONA_ID,"
                    + " p.NUM_DOC,"
                    + " t.ESTADO_ID,"
                    + " tg.NOMBRE,"
                    + " t.ACTIVO"
                    + " FROM"
                    + " BDINTEGRADO.AMA_TARJETA_PROPIEDAD t"
                    + " INNER JOIN BDINTEGRADO.AMA_LICENCIA_DE_USO lu on lu.id = t.licencia_id"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA p ON t.PERSONA_COMPRADOR_ID=p.id AND p.TIPO_ID=92"
                    + " INNER JOIN BDINTEGRADO.TIPO_GAMAC tg ON t.ESTADO_ID = tg.ID"
                    + " INNER JOIN BDINTEGRADO.TIPO_GAMAC tg2 ON lu.ESTADO_ID = tg2.ID"
                    + " WHERE"
                    + " p.NUM_DOC LIKE ?1"
                    + " AND t.ACTIVO=1 AND lu.ACTIVO=1"
                    + " AND t.ESTADO_ID=(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE COD_PROG='TP_EST_VIG') "
                    + " AND lu.ESTADO_ID=(SELECT ID FROM BDINTEGRADO.TIPO_GAMAC WHERE COD_PROG='TP_EST_VIG') "
                    + " GROUP BY p.ID, p.NUM_DOC, t.ESTADO_ID, tg.NOMBRE, t.ACTIVO"
                    + " ORDER BY p.ID"
                    + "";
            
            //sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C);            
            return q.getResultList();
        } else {
            return null;
        }
            
    }

    /**
     * Consulta para empresas de seguridad con autorización
     * 
     * @author rarevalo
     * @param filtro
     * @return Lista de Emp de Seguridad por Filtro ingresado
     */    
    public List<ArrayRecord> listPerJur(String filtro) {
        if (filtro != null) {
            String sql = "SELECT "
                    + " p.ID || ',' || p.RUC ID,"
                    + " p.ID PERSONA_ID,"
                    + " p.RUC,"
                    + " t.ESTADO_ID,"
                    + " t.ACTIVO"
                    + " FROM"
                    + " BDINTEGRADO.AMA_TARJETA_PROPIEDAD t"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA p ON t.PERSONA_COMPRADOR_ID=p.id AND p.TIPO_ID=93"
                    + " INNER JOIN RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC rgssp ON p.RUC=rgssp.RUC"
                    + " WHERE"
                    + " p.RUC LIKE ?1"
                    + " AND t.ACTIVO=1 AND t.ESTADO_ID=121"
                    + " AND rgssp.FEC_VEN >= SYSDATE "
                    + " GROUP BY p.ID, p.RUC, t.ESTADO_ID, t.ACTIVO"
                    + " ORDER BY p.ID"
                    + "";
            
            //sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, "%" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C);            
            return q.getResultList();
        } else {
            return null;
        }
            
    }

    public List<ArrayRecord> listPerJurxNroRD(String filtro) {
        if (filtro != null) {
            String sql = "SELECT "
                    + " p.ID || ',' || p.RUC ID,"
                    + " p.ID PERSONA_ID,"
                    + " p.RUC,"
                    + " t.ESTADO_ID,"
                    + " t.ACTIVO,"
                    + " TO_CHAR(rgssp.FEC_VEN, 'dd/mm/yyyy') FEC_VEN"
                    + " FROM"
                    + " BDINTEGRADO.AMA_TARJETA_PROPIEDAD t"
                    + " INNER JOIN BDINTEGRADO.SB_PERSONA p ON t.PERSONA_COMPRADOR_ID=p.id AND p.TIPO_ID=93"
                    + " INNER JOIN RMA1369.WS_RESOLUCIONV2_GSSP@SUCAMEC rgssp ON p.RUC=rgssp.RUC"
                    + " WHERE"
                    + " rgssp.NRO_RD = ?1"
                    + " GROUP BY p.ID, p.RUC, t.ESTADO_ID, t.ACTIVO, rgssp.FEC_VEN"
                    + " ORDER BY p.ID"
                    + "";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, filtro);
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C);            
            return q.getResultList();
        } else {
            return null;
        }
            
    }

    public List<ArrayRecord> listPerJurComer(Long id, String filtro) {
        if (filtro != null) {
            String sql = "SELECT"
                    //+ " p.ID || ',' || p.RUC || ',' || rg.NRO_RD || ',' || rg.FEC_VEN ID,"
                    + " p.ID || ',' || p.RUC ID, "
                    + " p.ID PERSONA_ID, p.RUC"
                    + " FROM"
                    + " BDINTEGRADO.SB_PERSONA p"
                    //+ " INNER JOIN RMA1369.WS_RESOLUCION_GAMAC@SUCAMEC rg ON TRIM(p.RUC)=TRIM(rg.RUC)"
                    + " WHERE"
                    + " p.TIPO_ID=93"
                    + " AND p.ID<>?1"
                    + " AND p.RUC LIKE ?2"
                    //+ " GROUP BY p.ID, p.RUC, rg.NRO_RD, rg.FEC_VEN"
                    + " ORDER BY p.ID"
                    + "";
            
            //sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, id);            
            q.setParameter(2, "%" + filtro + "%");
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C);            
            return q.getResultList();
        } else {
            return null;
        }
            
    }
    
    public List<ArrayRecord> listResoComer(String ruc) {
        if (ruc != null) {
            String sql = "SELECT"
                    + " rg.NRO_RD,"
                    + " rg.FEC_VEN,"
                    + " rg.TIPO_AUTORIZACION "
                    + " FROM"
                    + " RMA1369.WS_RESOLUCION_GAMAC@SUCAMEC rg"
                    + " WHERE"
                    + " rg.RUC LIKE ?1"
                    + " ORDER BY rg.NRO_RD"
                    + "";
            
            //sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, ruc);
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C); 
            return q.getResultList();
        } else {
            return null;
        }
            
    }

    public List<ArrayRecord> listPerJurComerxNroRD(String filtro) {
        if (filtro != null) {
            String sql = "SELECT"
                    + " p.ID || ',' || p.RUC || ',' || rg.NRO_RD || ',' || rg.FEC_VEN ID,"
                    + " p.ID PERSONA_ID, p.RUC, TO_CHAR(rg.FEC_VEN, 'dd/mm/yyyy') FEC_VEN"
                    + " FROM"
                    + " BDINTEGRADO.SB_PERSONA p"
                    + " INNER JOIN RMA1369.WS_RESOLUCION_GAMAC@SUCAMEC rg ON TRIM(p.RUC)=TRIM(rg.RUC)"
                    + " WHERE"
                    + " p.TIPO_ID=93"
                    + " AND rg.NRO_RD = ?1"
                    + " GROUP BY p.ID, p.RUC, rg.NRO_RD, rg.FEC_VEN"
                    + " ORDER BY p.ID"
                    + "";
            
            //sql += " AND TRIM(TO_CHAR(a.DOC_PORTADOR, '00000000')) LIKE ?1";
            //sql += " GROUP BY a.DOC_PORTADOR";
            
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, filtro);
            q.setHint("eclipselink.result-type", "Map");
            q.setMaxResults(MAX_RES_C);            
            return q.getResultList();
        } else {
            return null;
        }
            
    }

    public List<GamacAmaTarjetaPropiedad> findByComprador(String compradorId) {
        if (compradorId != null) {
            String sql = "select a from GamacAmaTarjetaPropiedad a"
                    + " where"
                    + " a.personaCompradorId.id = :compradorId"
                    + " and a.activo=1 and a.estadoId.id=121";
            try {
                Query q = em.createQuery(sql);
                q.setParameter("compradorId", Long.valueOf(compradorId));
                q.setMaxResults(MAX_RES);
                q.setHint("eclipselink.batch.type", "IN");
                List<GamacAmaTarjetaPropiedad> list = q.getResultList();
                if (list.size() > 0) {
                    return list;
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
    
    public List<GamacAmaTarjetaPropiedad> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from GamacAmaTarjetaPropiedad a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public GamacAmaTarjetaPropiedad findById(String id) {
        if (id != null) {
            try {
                Query q = em.createNamedQuery("GamacAmaTarjetaPropiedad.findById", GamacAmaTarjetaPropiedad.class).setParameter("id", Long.valueOf(id));
                q.setMaxResults(MAX_RES);
                q.setHint("eclipselink.batch.type", "IN");
                List<GamacAmaTarjetaPropiedad> list = q.getResultList();
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
    
    public GamacAmaTarjetaPropiedad findByArma(GamacAmaArma arma) {
        if (arma != null) {
            try {
                Query q = em.createNamedQuery("GamacAmaTarjetaPropiedad.findByArma", GamacAmaTarjetaPropiedad.class).setParameter("armaId", arma);
                q.setMaxResults(MAX_RES);
                q.setHint("eclipselink.batch.type", "IN");
                List<GamacAmaTarjetaPropiedad> list = q.getResultList();
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

    public List<Map> listTarjetaArmasVendidas(GamacSbDireccion direccion, Date fecIni, Date fecFin, String tipoBus, String filtro, Long idVendedor) {
        List<Map> listRes = new ArrayList<>();
        String jpql = "select tp.id, tp.armaId.nroRua, tp.personaCompradorId.nombres, tp.personaCompradorId.apePat, tp.personaCompradorId.apeMat, tp.personaCompradorId.rznSocial, "
                + "td.nombre as tipoDoc, tp.personaCompradorId.numDoc,  tp.personaCompradorId.ruc, tp.fechaEmision, tp.armaId.modeloId.tipoArmaId.nombre as tipArma, "
                + " tp.armaId.serie, tp.armaId.calibreId.nombre as calibre, tp.armaId.modeloId.marcaId.nombre as marca, tp.armaId.modeloId.modelo, tp.armaId.id as idArma "
                + "from GamacAmaTarjetaPropiedad tp left join tp.personaCompradorId.tipoDoc td where tp.activo=1 and tp.emitido=1 ";
        if (fecIni != null && fecFin != null) {
            jpql += "and FUNC('trunc',tp.fechaEmision) between FUNC('trunc',:fecIni) and FUNC('trunc',:fecFin) ";
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "A":
                    jpql += "and (CONCAT(tp.personaCompradorId.nombres,' ',tp.personaCompradorId.apePat,' ',tp.personaCompradorId.apeMat) like :filtro or tp.personaCompradorId.rznSocial like :filtro) ";
                    break;
                case "B":
                    jpql += "and tp.personaCompradorId.ruc like :filtro ";
                    break;
                case "C":
                    jpql += "and tp.personaCompradorId.numDoc like :filtro ";
                    break;
                case "D":
                    jpql += "and tp.armaId.serie like :filtro ";
                    break;
                default:
                    break;
            }
        }
        jpql += " and tp.personaVendedorId.id = :idVend order by tp.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("idVend", idVendedor);
        q.setMaxResults(MAX_RES);
        if (fecIni != null && fecFin != null) {
            q.setParameter("fecIni", fecIni);
            q.setParameter("fecFin", fecFin);
        }
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter("filtro", filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Map> listTarjetaArmasVendidasEntrega(String tipoBus, String filtro, Long idVendedor) {
        List<Map> listRes = new ArrayList<>();
        String jpql = "select tp.id, tp.armaId.nroRua, tp.personaCompradorId.id as idPersonaC, tp.personaCompradorId.nombres, tp.personaCompradorId.apePat, tp.personaCompradorId.apeMat, tp.personaCompradorId.rznSocial, "
                + " td.nombre as tipoDoc, tp.personaCompradorId.numDoc,  tp.personaCompradorId.ruc, tp.fechaEmision, tp.armaId.modeloId.tipoArmaId.nombre as tipArma, "
                + " tp.armaId.serie, tp.armaId.calibreId.nombre as calibre, tp.armaId.modeloId.marcaId.nombre as marca, tp.armaId.modeloId.modelo, tp.armaId.id as idArma "
                + " from GamacAmaTarjetaPropiedad tp left join tp.personaCompradorId.tipoDoc td where tp.activo=1 and tp.emitido=1 ";
        if (tipoBus != null && !"".equals(tipoBus)) {
            switch (tipoBus) {
                case "B":
                    jpql += "and (CONCAT(tp.personaCompradorId.nombres,' ',tp.personaCompradorId.apePat,' ',tp.personaCompradorId.apeMat) like :filtro or tp.personaCompradorId.rznSocial like :filtro) ";
                    break;
                case "A":
                    jpql += "and (tp.personaCompradorId.numDoc like :filtro or tp.personaCompradorId.ruc like :filtro) ";
                    break;
                case "C":
                    jpql += "and tp.armaId.serie like :filtro ";
                    break;
                default:
                    break;
            }
        }
        jpql += " and tp.personaVendedorId.id = :idVend order by tp.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter("idVend", idVendedor);
        //q.setMaxResults(MAX_RES);
        if (tipoBus != null && !"".equals(tipoBus)) {
            q.setParameter("filtro", filtro == null ? "" : "%" + filtro.toUpperCase().trim() + "%");
        }
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public Boolean validarArmaEntregada(Long idTarjet) {
        Boolean r = Boolean.FALSE;
        Query q = em.createQuery("select a from GamacAmaTarjetaPropiedad a where a.id = :id and a.fechaEntregaPropietario is not null ");
        q.setParameter("id", idTarjet);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            r = Boolean.TRUE;
        }
        return r;
    }

    public Date obtenerFechaEmision(Long idTarjPro) {
        Date fecha = null;
        Query q = em.createQuery("select a.fechaEmision from GamacAmaTarjetaPropiedad a where a.id = :id and a.fechaEmision is not null and a.activo=1 ");
        q.setParameter("id", idTarjPro);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            fecha = (Date) q.getResultList().get(0);
        }
        return fecha;
    }

    public Date obtenerFechaEntregaPropietario(Long idTarjPro) {
        Date fecha = null;
        Query q = em.createQuery("select a.fechaEntregaPropietario from AmaTarjetaPropiedad a where a.id = :id and a.fechaEntregaPropietario is not null and a.activo=1 ");
        q.setParameter("id", idTarjPro);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            fecha = (Date) q.getResultList().get(0);
        }
        return fecha;
    }

    public List<GamacAmaTarjetaPropiedad> listaArmasPorSolicitudRecojo(Long idSol) {
        List<GamacAmaTarjetaPropiedad> listRes = new ArrayList<>();
        Query q = em.createQuery("select tp from AmaTarjetaPropiedad tp, AmaArma ar where tp.id in "
                + "(select distinct tp.id "
                + "from AmaSolicitudRecojo sr  LEFT JOIN sr.amaTarjetaPropiedadList tp "
                + "where  sr.id = :idSol and sr.activo=1 ) and ar.id = tp.armaId.id ");
        q.setParameter("idSol", idSol);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<GamacAmaTarjetaPropiedad> listTarjetaArmasConSolRecojo(Long idVendedor) {
        List<GamacAmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from AmaTarjetaPropiedad tp left join tp.amaSolicitudRecojoList sr where "
                + " tp.activo=1 and tp.emitido=1 and sr.activo = 1 and sr.id is not null and tp.personaVendedorId.id = :idVend "
                + "order by tp.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setParameter("idVend", idVendedor);
        //q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<GamacAmaTarjetaPropiedad> listTarjetaArmasConGuiaTransito(Long idVendedor) {
        List<GamacAmaTarjetaPropiedad> listRes = new ArrayList<>();
        String jpql = "select tp from GamacAmaTarjetaPropiedad tp, tp.armaId ar left join ar.amaGuiaTransitoList gta where "
                + " tp.activo=1 and tp.emitido=1 and gta.activo = 1 and gta.id is not null and tp.personaVendedorId.id = :idVend "
                + "order by tp.fechaEmision desc ";
        Query q = em.createQuery(jpql);
        q.setParameter("idVend", idVendedor);
        //q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

}
