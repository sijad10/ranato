/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.sel.gssp.data.SspRegistroPosArma;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author rarevalo
 */
@Stateless
public class ArmasPosesionFacade extends AbstractFacade<SspRegistroPosArma> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArmasPosesionFacade() {
        super(SspRegistroPosArma.class);
    }

    public List<SspRegistroPosArma> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from SspRegistroPosArma a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public SspRegistroPosArma findBySerie(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createNamedQuery("SspRegistroPosArma.findBySerie", SspRegistroPosArma.class).setParameter("serie", s);
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        List<SspRegistroPosArma> list = q.getResultList();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Map> listarArmasXCriterios(String filtro, String criterio, Long idEmpresa) {
        StringBuilder sbQuery = new StringBuilder();
        List<Map> listRes = new ArrayList<>();
        try {
            sbQuery.append("select rpa.id,es.nombre as estado,si.nombre as situacion,rpa.numero_serie,nta.nombre as nuevo_tipo_arma,rpa.nuevo_marca,rpa.nuevo_modelo,rpa.nuevo_calibre,rpa.FLAG_SIN_CATALOGO,rpa.descripcion, ac.nombre as marca,tp.nombre as tipoarma,am.modelo, LISTAGG(ca.nombre, '/') WITHIN GROUP (ORDER BY am.ID) as calibre \n" +
"from bdintegrado.ssp_registro_pos_arma rpa \n" +
"left join bdintegrado.ama_modelos am on am.id=rpa.modelo_catalogado_id\n" +
"left join bdintegrado.ama_catalogo ac on ac.id= am.marca_id\n" +
"left join bdintegrado.ama_catalogo tp on tp.id= am.tipo_arma_id\n" +
"left join bdintegrado.ama_modelo_calibre amc on amc.modelo_id=rpa.modelo_catalogado_id\n" +
"left join bdintegrado.ama_catalogo ca on (ca.id=amc.catalogo_id and ca.activo=1)\n" +
"left join bdintegrado.tipo_seguridad es on es.id=rpa.estado_fisico_id\n" +
"left join bdintegrado.tipo_seguridad si on si.id=rpa.situacion_id\n" +
"left join bdintegrado.ama_catalogo nta on nta.id= rpa.nuevo_tipo_arma_id where rpa.activo = 1  and rpa.empresa_id=?2 ");
            if (criterio.equals("SE")) {
                sbQuery.append("and  rpa.numero_serie like ?1 ");
            }
            if (criterio.equals("MO")) {
                sbQuery.append("and (rpa.nuevo_modelo like ?1 or am.modelo like ?1 ) ");
            }
            if (criterio.equals("MA")) {
                sbQuery.append("and (rpa.nuevo_marca like ?1 or ac.nombre like ?1) ");
            }
            
             sbQuery.append(" GROUP BY rpa.id,es.nombre,si.nombre,rpa.numero_serie,nta.nombre,rpa.nuevo_marca,rpa.nuevo_modelo,rpa.nuevo_calibre,rpa.FLAG_SIN_CATALOGO,rpa.descripcion,ac.nombre,tp.nombre,am.modelo ");
            
            Query q = em.createNativeQuery(sbQuery.toString());
            q.setHint("eclipselink.result-type", "Map");
            q.setParameter(1, "%" + filtro + "%");
            q.setParameter(2, idEmpresa);
            q.setMaxResults(MAX_RES);
            if (q.getResultList() != null && !q.getResultList().isEmpty()) {
                listRes = q.getResultList();
            }
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return listRes;
    }
}
