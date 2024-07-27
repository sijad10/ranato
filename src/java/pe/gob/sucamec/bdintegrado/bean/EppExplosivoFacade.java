/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.EppExplosivo;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppExplosivoFacade extends AbstractFacade<EppExplosivo> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppExplosivoFacade() {
        super(EppExplosivo.class);
    }

    public List<EppExplosivo> lstExplosivosActivos() {
        String sentencia = "select e from EppExplosivo e where e.activo = 1 order by e.nombre";
        Query query = getEntityManager().createQuery(sentencia);
        return query.getResultList();
    }

    public List<EppExplosivo> lstExplosivosActivosGtPlantaPlanta(SbPersona paramEmpresa) {
        List<EppExplosivo> lst = new ArrayList();
        String sql = "select \n"
                + "DISTINCT(e.id) \n"
                + "from bdexplosivo.epp_registro r \n"
                + "inner join bdexplosivo.tipo_base t on r.tipo_pro_id = t.id \n"
                + "inner join bdexplosivo.epp_detalle_internamiento_sal dis on r.id = DIS.REGISTRO_ID \n"
                + "inner join BDEXPLOSIVO.epp_explosivo e on dis.explosivo_id = e.id\n"
                + "where \n"
                + "t.cod_prog in ('TP_PRTUP_INT') and \n"
                + "r.activo = 1 and \n"
                + "dis.activo = 1 and \n"
                + "r.empresa_id = ?1\n"
                + "union all \n"
                + "select \n"
                + "DISTINCT(e.id) \n"
                + "from bdexplosivo.epp_registro r \n"
                + "inner join bdexplosivo.epp_registro_autorizacion ra1 on ra1.registro_id = r.id\n"
                + "inner join bdexplosivo.tipo_base t on r.tipo_pro_id = t.id \n"
                + "inner join BDEXPLOSIVO.EPP_AUTORIZACION_EXPLOSIVO ae1 on ra1.id = ae1.AUTORIZACION_ID\n"
                + "inner join BDEXPLOSIVO.epp_explosivo e on ae1.explosivo_id = e.id \n"
                + "where \n"
                + "t.cod_prog in ('TP_PRTUP_PLA') and \n"
                + "r.activo = 1 and \n"
                + "r.empresa_id = ?1\n"
                + "union all \n"
                + "select \n"
                + "DISTINCT(e.id) \n"
                + "from bdexplosivo.epp_registro r \n"
                + "inner join bdexplosivo.epp_registro_autorizacion ra2 on ra2.registro_id = r.id\n"
                + "inner join bdexplosivo.tipo_base t on r.tipo_pro_id = t.id \n"
                + "inner join BDEXPLOSIVO.EPP_AUTORIZACION_EXPLOSIVO ae2 on ra2.id = ae2.AUTORIZACION_ID\n"
                + "inner join BDEXPLOSIVO.epp_explosivo e on ae2.explosivo_id = e.id \n"
                + "where \n"
                + "t.cod_prog in ('TP_PRTUP_COM') and \n"
                + "r.activo = 1 and \n"
                + "r.empresa_id = ?1";
        Query q = em.createNativeQuery(sql);
        List<Map> res = new ArrayList();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, paramEmpresa.getId());
        res = q.getResultList();

        if (!res.isEmpty()) {
            for (Map re : res) {
                Query query = getEntityManager().createQuery("select e from EppExplosivo e where e.id = :id");
                query.setParameter("id", Long.valueOf(String.valueOf(re.get("ID"))));
                lst.add((EppExplosivo) query.getSingleResult());
            }
        }

        return lst;
    }

    public List<EppExplosivo> buscarExplosivosXNombre(String nombre) {
        Query query = getEntityManager().createQuery("select e from EppExplosivo e where e.activo = 1 and e.nombre like :nombre order by e.nombre");
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    public List<EppExplosivo> buscarExplosivos() {
        Query query = getEntityManager().createQuery("select e from EppExplosivo e where e.activo = 1 and e.tipoExplosivoId = 517 order by e.nombre");
        return query.getResultList();
    }

    public List<EppExplosivo> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        javax.persistence.Query q = em.createQuery(
                "select p from EppExplosivo p where p.nombre like :nombre and p.activo = 1 order by p.id DESC ");
        q.setParameter("nombre", "%" + s.toUpperCase() + "%");
        return q.getResultList();

    }

    /**
     * libros
     *
     * @param empresa
     * @return
     */
    public List<EppExplosivo> listExploInterSalidaXempresa(SbPersonaGt empresa) {
        Query q = em.createQuery("SELECT DISTINCT(p.explosivoId) FROM EppDetalleIntSal p where p.registroId.registroId.empresaId = :empresa and p.activo = 1");
        q.setParameter("empresa", empresa);
        return q.getResultList();
    }

    public List<EppExplosivo> lstExplosivosActivosGtPlantaPlantaGTE(SbPersonaGt paramEmpresa) {
        List<EppExplosivo> lst = new ArrayList();
        String sql = ""
                + "select \n"
                + "DISTINCT(e.id), e.nombre \"n1\" \n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.epp_registro_int_sal ris on ris.registro_id = r.id \n"
                + "inner join bdintegrado.epp_detalle_int_sal dis on ris.id = dis.registro_id \n"
                + "inner join bdintegrado.epp_explosivo e on dis.explosivo_id = e.id\n"
                + "inner join bdintegrado.tipo_base t on r.tipo_pro_id = t.id\n"
                + "where \n"
                + "t.cod_prog in ('TP_PRTUP_AINT','TP_PRTUP_INT') and \n"
                + "r.activo = 1 and \n"
                + "dis.activo = 1 and \n"   // and e.activo = 1
                + "r.empresa_id = ?1 \n"
                + "\n"
                + "union all \n"
                + "\n"
                + "select \n"
                + "DISTINCT(e.id), e.nombre \"n1\"\n"
                + "from bdintegrado.epp_registro r \n"
                + "inner join bdintegrado.tipo_base t on r.tipo_pro_id = t.id \n"
                + "inner join bdintegrado.epp_planta_explosivo ae2 on r.id = ae2.registro_id\n"
                + "inner join bdintegrado.epp_explosivo e on ae2.explosivo_id = e.id \n"
                + "where \n"
                + "t.cod_prog in ('TP_PRTUP_COM','TP_PRTUP_ACOM','TP_PRTUP_PLA','TP_PRTUP_AFAB') and \n"
                + "r.activo = 1 and \n" // and e.activo = 1
                + "r.empresa_id = ?1 \n"
                + "\n"
                + "order by \"n1\"\n"
                + "";
        Query q = em.createNativeQuery(sql);
        List<Map> res = new ArrayList();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, paramEmpresa.getId());
        res = q.getResultList();

        if (!res.isEmpty()) {
            EppExplosivo expTemp = null;
            for (Map re : res) {
                Query query = getEntityManager().createQuery("select e from EppExplosivo e where e.id = :id");
                query.setParameter("id", Long.valueOf(String.valueOf(re.get("ID"))));
                expTemp = (EppExplosivo) query.getSingleResult();
                if(!lst.contains(expTemp)){
                    lst.add(expTemp);
                }
            }
        }

        return lst;
    }
}
