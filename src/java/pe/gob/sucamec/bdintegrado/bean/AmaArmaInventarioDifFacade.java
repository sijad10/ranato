/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.AmaArmaInventarioDif;
import static pe.gob.sucamec.sistemabase.beans.AbstractFacade.MAX_RES;

/**
 *
 * @author gchavez
 */
@Stateless
public class AmaArmaInventarioDifFacade extends AbstractFacade<AmaArmaInventarioDif> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmaArmaInventarioDifFacade() {
        super(AmaArmaInventarioDif.class);
    }

    public List<AmaArmaInventarioDif> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from AmaArmaInventarioDif a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }

    public List<AmaArmaInventarioDif> buscarArmasDiferenciaInventario(HashMap mMap) {
        try {
            String jpql = "select a from AmaArmaInventarioDif a where a.activo = 1 and a.inventarioArmaId is not null and a.armaId is not null ";
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1: //MARCA
                        jpql = jpql + " and a.armaId.modeloId.marcaId.nombre like :filtro ";
                        break;
                    case 2: //MODELO
                        jpql = jpql + " and a.armaId.modeloId.modelo like :filtro ";
                        break;
                    case 3: // SERIE
                        jpql = jpql + " and a.inventarioArmaId.serie like :filtro ";
                        break;
                    case 4: // ANAQUEL
                        jpql = jpql + " and a.inventarioArmaId.anaquel like :filtro ";
                        break;
                    case 5: // NRO. RUA
                        jpql = jpql + " and a.inventarioArmaId.nroRua like :filtro ";
                        break;

                }
            }
            jpql = jpql + " order by a.id desc";

            Query q = em.createQuery(jpql);
            if (mMap.get("buscarPor") != null) {
                q.setParameter("filtro", "%" + mMap.get("filtro").toString() + "%");
            }
            return q.setMaxResults(200).getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public AmaArmaInventarioDif obtenerPorIdArma(Long id) {
        AmaArmaInventarioDif res = null;
        Query q = em.createQuery("select a from AmaArmaInventarioDif a where a.armaId.id like :id and a.activo = 1 ");
        q.setParameter("id", id);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (AmaArmaInventarioDif) q.getResultList().get(0);
        }
        return res;
    }

    public AmaArmaInventarioDif obtenerPorIdInventarioArma(Long id) {
        AmaArmaInventarioDif res = null;
        Query q = em.createQuery("select a from AmaArmaInventarioDif a where a.inventarioArmaId.id like :id and a.activo = 1 ");
        q.setParameter("id", id);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (AmaArmaInventarioDif) q.getResultList().get(0);
        }
        return res;

    }
}
