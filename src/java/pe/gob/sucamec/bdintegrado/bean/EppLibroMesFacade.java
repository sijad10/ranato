/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppLibro;
import pe.gob.sucamec.bdintegrado.data.EppLibroMes;
import pe.gob.sucamec.bdintegrado.data.EppLibroUsoDiario;
import pe.gob.sucamec.bdintegrado.data.EppRegistro;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;

/**
 *
 * @author rchipana
 */
@Stateless
public class EppLibroMesFacade extends AbstractFacade<EppLibroMes> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppLibroMesFacade() {
        super(EppLibroMes.class);
    }

    public List<EppLibroMes> selectDataBandejaLibros(EppLibro libro, TipoExplosivoGt tiporegistro, TipoExplosivoGt estado, String proceso, SbPersonaGt persona, TipoBaseGt mes, String anio) {
        try {
            String jpql = "SELECT r \n"
                    + "from EppLibroMes r \n"
                    + "WHERE \n"
                    + "r.activo = 1 and  \n"
                    + "r.libroId.empresaId = :empresa and \n"
                    + "r.libroId.registroId.tipoProId.codProg in " + proceso
                    + " and r.libroId = :libro";

            if (tiporegistro != null) {
                jpql = jpql + " and r.tipoRegistroId = :tipoRegistro";
            }

            if (estado != null) {
                jpql = jpql + " and r.tipoEstado = :estado";
            }

            if (anio != null) {
                jpql = jpql + " and r.libromAnio = :anio";
            }

            if (mes != null) {
                jpql = jpql + " and r.libromOrden = :mes ";
            }

            jpql = jpql + " order by r.id desc  ";

            Query query = em.createQuery(jpql);
            if (anio != null) {
                query.setParameter("anio", new Long(anio));
            }

            if (estado != null) {
                query.setParameter("estado", estado);
            }

            if (mes != null) {
                query.setParameter("mes", mes.getOrden());

            }

            if (tiporegistro != null) {
                query.setParameter("tipoRegistro", tiporegistro);
            }

            query.setParameter("empresa", persona);
            query.setParameter("libro", libro);

            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * EVALUAR SI SE SIGUE USANDO ESTE METODO
     *
     * @param libroMes
     * @param tipoRegistro
     * @return
     */
    public List<EppLibroUsoDiario> selectLibroUsoDiario(EppLibroMes libroMes, String tipoRegistro, String tipoFin) {

        try {
            String jpql = "SELECT r \n"
                    + "from EppLibroUsoDiario r \n"
                    + "WHERE \n"
                    + "r.activo = 1 and  \n"
                    + "r.tipoFinalidad.codProg = :fina and \n"
                    + "r.libroMesId = :libroMes and \n"
                    + "r.tipoRegistroId.codProg in " + tipoRegistro;

            Query query = em.createQuery(jpql);
            query.setParameter("libroMes", libroMes);
            query.setParameter("fina", tipoFin);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * NUEVO METODO BUSCA EppLibroUsoDiario X LIBRO MES
     *
     * @param libroMes
     * @return
     */
    public List<EppLibroUsoDiario> selectLibroUsoDiarioNuevo(EppLibroMes libroMes) {

        try {
            String jpql = "SELECT r \n"
                    + "from EppLibroUsoDiario r \n"
                    + "WHERE \n"
                    + "r.activo = 1 and  \n"
                    + "r.libroMesId = :libroMes"
                    + " order by r.id desc";

            Query query = em.createQuery(jpql);
            query.setParameter("libroMes", libroMes);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public List<EppLibroMes> listEpplibroMesAll() {

        try {
            String jpql = "SELECT r \n"
                    + "from EppLibroMes r \n"
                    + "WHERE \n"
                    + "r.activo = 1 \n";

            Query query = em.createQuery(jpql);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * OBS TRAZA
     *
     * @param idExpediente
     * @return
     */
    public List listObsrTraza(String idExpediente) {
        try {
            javax.persistence.Query q = em.createNativeQuery("SELECT t.OBSERVACION as obs, t.ID_EXPEDIENTE as exp , t.FECHA_CREACION as fec , t.REMITENTE AS rem  FROM TRAMDOC.TRAZA t where t.ID_EXPEDIENTE = ?  and t.ACTUAL = 1");
            q.setParameter(1, idExpediente);
            List<Map> trazaList = new ArrayList<>();
            q.setHint("eclipselink.result-type", "Map");
            trazaList = q.getResultList();
            return trazaList;
        } catch (Exception e) {
            return null;
        }

    }

    public List<String> loginUsuarioTramdoc(String idUsuario) {
        try {

            javax.persistence.Query q = em.createNativeQuery("SELECT p.USUARIO FROM  TRAMDOC.USUARIO p where p.ID_USUARIO  = ? AND P.ESTADO = 'A'");
            q.setParameter(1, idUsuario);
            return q.getResultList();

        } catch (Exception e) {
            return null;
        }

    }

    public List<EppLibroMes> listEpplibroMes(List<EppRegistro> listRe) {

        try {
            String jpql = "SELECT r \n"
                    + "from EppLibroMes r \n"
                    + "WHERE \n"
                    + "r.activo = 1 and r.libroId.registroId in :lst \n";

            Query query = em.createQuery(jpql);
            query.setParameter("lst", listRe);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public List<EppLibroUsoDiario> listEppUsoDiario(String proceso, String estado, String fina, EppLibro libro) {

        try {
            String jpql = "SELECT r \n"
                    + "from EppLibroUsoDiario r \n"
                    + "WHERE \n"
                    + "r.activo = 1 and r.libroMesId.tipoEstado.codProg = :esta \n"
                    + "and r.libroMesId.tipoRegistroId.codProg = :proc and r.tipoFinalidad.codProg = :fina and r.libroMesId.libroId = :libro  \n"
                    + " order by r.id desc \n";

            Query query = em.createQuery(jpql);
            query.setParameter("proc", proceso);
            query.setParameter("fina", fina);
            query.setParameter("esta", estado);
            query.setParameter("libro", libro);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

}
