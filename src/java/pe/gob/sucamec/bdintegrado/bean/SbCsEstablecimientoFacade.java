/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.SbCsEstablecimiento;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbCsEstablecimientoFacade extends AbstractFacade<SbCsEstablecimiento> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbCsEstablecimientoFacade() {
        super(SbCsEstablecimiento.class);
    }

    public List<SbCsEstablecimiento> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbCsEstablecimiento s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<SbCsEstablecimiento> buscarEstablecimientos(HashMap mMap) {
        try {
            String jpql = "select a from SbCsEstablecimiento a where a.activo = 1";

            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1: // NOMBRE ESTABLECIMIENTO
                        jpql = jpql + " and a.nombre like :filtro ";
                        break;
                    case 2: // RUC PROPIETARIO
                        jpql = jpql + " and a.propietarioId.ruc like :filtro ";
                        break;
                    case 3: // NOMBRE PROPIETARIO
                        jpql = jpql + " and ( (a.propietarioId.rznSocial like :filtro) or (concat(a.propietarioId.apePat, ' ', a.propietarioId.apeMat, ' ', a.propietarioId.nombres) like :filtro) ) ";
                        break;
                    case 4: // ESTADO
                        jpql = jpql + " and a.habilitado = :estadoCentroSalud ";
                        break;
                    case 5: // RESOLUCION
                        jpql = jpql + " and a.nroResFuncionamiento like :filtro ";
                        break;
                }
            }
            jpql = jpql + " order by a.id desc";

            Query q = em.createQuery(jpql);
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 4:
                        q.setParameter("estadoCentroSalud", (Integer) mMap.get("estadoCentroSalud"));
                        break;
                    default:
                        q.setParameter("filtro", "%" + mMap.get("filtro").toString() + "%");
                        break;
                }

            }
            return q.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<SbCsEstablecimiento> listarEstablecimientos(String ruc, boolean activo) {
        try {
            String sql = "select s from SbCsEstablecimiento s where s.activo = 1 and s.habilitado = 1 ";
            if (ruc != null) {
                sql += " and s.propietarioId.ruc = :ruc ";
            }
            if (activo) {
                sql += " and FUNC('trunc',s.fechaFin) >= FUNC('trunc',current_date)  "
                     + " and s.nroResMinsa is not null and s.fechaIniRgMinsa is not null and s.fechaFinRgMinsa is not null "
                     + " and FUNC('trunc',s.fechaFinRgMinsa) >= FUNC('trunc',current_date) ";
            }
            Query q = em.createQuery(sql);
            q.setParameter("ruc", ruc.trim());
            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int contarEstablecimientosHabilitadosByRuc(String ruc) {
        int cont = 0;
        String sql = " select count(s) from SbCsEstablecimiento s where s.activo = 1 and s.habilitado = 1 "
                + " and FUNC('trunc',s.fechaFin) >= FUNC('trunc',current_date) "
                + " and s.nroResMinsa is not null and s.fechaIniRgMinsa is not null and s.fechaFinRgMinsa is not null "
                + " and FUNC('trunc',s.fechaFinRgMinsa) >= FUNC('trunc',current_date) "
                + " and s.propietarioId.ruc = :ruc ";

        Query q = em.createQuery(sql);
        q.setParameter("ruc", ruc.trim());

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }
    
    public int contarEstablecimientosHabilitadosSinMinsaByRuc(String ruc) {
        int cont = 0;
        String sql = " select count(s) from SbCsEstablecimiento s where s.activo = 1 and s.habilitado = 1 "
                + " and FUNC('trunc',s.fechaFin) >= FUNC('trunc',current_date) "
                + " and ( s.fechaFinRgMinsa is null or FUNC('trunc',s.fechaFinRgMinsa) < FUNC('trunc',current_date) )"
                + " and s.propietarioId.ruc = :ruc ";

        Query q = em.createQuery(sql);
        q.setParameter("ruc", ruc.trim());

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }

    public int contarEstablecimientosInhabilitadosByRuc(String ruc) {
        int cont = 0;
        String sql = " select count(s) from SbCsEstablecimiento s where s.activo = 1 and s.habilitado = 0 "
                + " and s.propietarioId.ruc = :ruc ";

        Query q = em.createQuery(sql);
        q.setParameter("ruc", ruc.trim());

        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }

        return cont;
    }

    public List<SbCsEstablecimiento> listarEstablecimientos(String valor) {
        String sql = "select distinct s from SbCsEstablecimiento s left join s.propietarioId.sbUsuarioList usu left join usu.sbPerfilList per "
                + "where s.activo = 1 "
                + "and s.habilitado = 1 "
                + "and usu.activo = 1 "
                + "and s.propietarioId.activo = 1 "
                + "and usu.id is not null "
                + "and per.codProg = 'AMA_CSALUD' "
                + "and FUNC('trunc',:fechaHoy) <= FUNC('trunc',s.fechaFin) ";
        if (valor != null && !"".equals(valor)) {
            sql = sql + " and (s.propietarioId.ruc like :valor or s.nombre like :valor) ";
        }
        sql = sql + " order by s.nombre asc ";
        Query q = em.createQuery(sql);
        q.setParameter("fechaHoy", (new Date()), TemporalType.TIMESTAMP);
        if (valor != null && !"".equals(valor)) {
            q.setParameter("valor", "%" + valor.trim() + "%");
        }
        return q.getResultList();
    }
    
    public List<SbCsEstablecimiento> buscarEstablecimientosByPropietario(HashMap mMap) {
        try {
            String jpql = "select a from SbCsEstablecimiento a where a.activo = 1" +
                          " and a.propietarioId.ruc = :ruc ";

            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1: // NOMBRE ESTABLECIMIENTO
                        jpql = jpql + " and a.nombre like :filtro ";
                        break;
                    case 4: // ESTADO
                        jpql = jpql + " and a.habilitado = :estadoCentroSalud ";
                        break;
                    case 5: // RESOLUCION
                        jpql = jpql + " and a.nroResFuncionamiento like :filtro ";
                        break;
                }
            }
            jpql = jpql + " order by a.id desc";

            Query q = em.createQuery(jpql);
            q.setParameter("ruc", mMap.get("ruc").toString());
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 4:
                        q.setParameter("estadoCentroSalud", (Integer) mMap.get("estadoCentroSalud"));
                        break;
                    default:
                        q.setParameter("filtro", "%" + mMap.get("filtro").toString() + "%");
                        break;
                }

            }
            return q.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }
    
    public List<SbCsEstablecimiento> listarEstablecimientosHabilitadosByRuc(String ruc) {
        try {
            String sql = " select distinct s from SbCsEstablecimiento s where s.activo = 1 and s.habilitado = 1 "
                    + " and FUNC('trunc',s.fechaFin) >= FUNC('trunc',current_date) "
                    + " and s.nroResMinsa is not null and s.fechaIniRgMinsa is not null and s.fechaFinRgMinsa is not null "
                    + " and FUNC('trunc',s.fechaFinRgMinsa) >= FUNC('trunc',current_date) "
                    + " and s.propietarioId.ruc = :ruc ";

            Query q = em.createQuery(sql);
            q.setParameter("ruc", ruc.trim());

            return q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
