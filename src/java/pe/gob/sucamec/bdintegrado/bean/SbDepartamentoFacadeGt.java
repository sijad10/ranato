/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.SbDepartamentoGt;
import pe.gob.sucamec.bdintegrado.data.SbProvinciaGt;


/**
 *
 * @author mespinoza
 */
@Stateless
public class SbDepartamentoFacadeGt extends AbstractFacade<SbDepartamentoGt> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbDepartamentoFacadeGt() {
        super(SbDepartamentoGt.class);
    }
    
    public List<SbDepartamentoGt> lstDepartamentos() {
        List<SbDepartamentoGt> respuesta = new ArrayList();
        String sentencia = "select d from SbDepartamentoGt d";
        Query query = getEntityManager().createQuery(sentencia);
        //query.setParameter("parametro", "%" + parametro + "%");
        respuesta = query.getResultList();
        respuesta.remove(0);
        return respuesta;
    }

    public List<SbDepartamentoGt> listarDepartamentoPorProvincia(SbProvinciaGt provincia) {
        Query q = getEntityManager().createQuery("SELECT D FROM SbDepartamentoGt D WHERE D.id = :departamentoId");
        q.setParameter("departamentoId", provincia.getDepartamentoId().getId());
        return q.getResultList();
    }

    public List<SbDepartamentoGt> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = getEntityManager().createQuery("select d from SbDepartamentoGt d where d.nombre like :nombre");
        q.setParameter("nombre", "%" + s + "%");
        return q.getResultList();

    }
    
    public SbDepartamentoGt obtenerDepartamentoByNombre(String nombre) {
        if (nombre == null) {
            nombre = "";
        }
        Query q = getEntityManager().createQuery("select d from SbDepartamentoGt d where d.nombre = :nombre");
        q.setParameter("nombre", nombre);
        if(!q.getResultList().isEmpty()){
            return (SbDepartamentoGt) q.getResultList().get(0);
        }
        return null;
    }

}
