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
import javax.persistence.TemporalType;
import pe.gob.sucamec.bdintegrado.data.AmaInventarioAccesorios;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaInventarioAccesoriosFacade extends AbstractFacade<AmaInventarioAccesorios> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaInventarioAccesoriosFacade() {
        super(AmaInventarioAccesorios.class);
    }

    public List<Map> listAccesAllArmas(String cadena, short estado) {
        List<Map> listRes = null;
        Query q = em.createQuery("select acc.articuloConexoId.id, "
                + "CONCAT(acc.inventarioArmaId.modeloId.tipoArmaId.nombre, ' ',acc.inventarioArmaId.modeloId.marcaId.nombre,' ',acc.inventarioArmaId.modeloId.modelo) as modelo, "
                + "acc.articuloConexoId.nombre as articulo, "
                + "acc.cantidad "
                + "from AmaInventarioAccesorios acc where acc.activo = 1 and acc.inventarioArmaId.id in (" + cadena + ") and acc.estadoDeposito = :estado ");
        q.setParameter("estado", estado);
        q.setHint("eclipselink.result-type", "Map");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }

    public List<Map> listAccesAllArmas(AmaGuiaTransito amaGuia, short estado) {
        List<Map> listRes = null;
        Query q = em.createQuery("select acc.articuloConexoId.id, "
                + "CONCAT(ia.modeloId.tipoArmaId.nombre, ' ',ia.modeloId.marcaId.nombre,' ',ia.modeloId.modelo) as modelo, "
                + "acc.articuloConexoId.nombre as articulo, "
                + "acc.cantidad "
                + "from AmaGuiaTransito gt "
                + "left join gt.amaInventarioArmaList ia "
                + "inner join ia.amaInventarioAccesoriosList acc on (ia.id = acc.inventarioArmaId.id) "
                + "where gt.id = :idGuia "
                + "and gt.activo = 1 and ia.activo = 1 and ia.actual = 1 and acc.activo = 1 "
                + "and ia.condicionAlmacen = :estado and acc.estadoDeposito = :estado ");
        q.setParameter("estado", estado);
        q.setParameter("idGuia", amaGuia == null ? 0L : amaGuia.getId());
        q.setHint("eclipselink.result-type", "Map");
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            listRes = q.getResultList();
        }
        return listRes;
    }
    
}
