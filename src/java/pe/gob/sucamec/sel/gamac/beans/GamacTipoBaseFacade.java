/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gamac.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.sel.gamac.data.GamacTipoBase;

/**
 *
 * @author mespinoza
 */
@Stateless
public class GamacTipoBaseFacade extends AbstractFacade<GamacTipoBase> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GamacTipoBaseFacade() {
        super(GamacTipoBase.class);
    }

    public List<GamacTipoBase> findByCodProg(String s) {
        Query q = em.createNamedQuery("GamacTipoBase.findByCodProg");
        q.setParameter("codProg", s != null ? s : "");
        return q.getResultList();
    }

    public GamacTipoBase tipoBaseXCodProg(String codprog) {
        GamacTipoBase tipo = null;
        String sentencia = "select t from GamacTipoBase t where t.codProg = :parametro";
        Query query = getEntityManager().createQuery(sentencia);
        query.setParameter("parametro", codprog != null ? codprog : "");
        tipo = (GamacTipoBase) query.getSingleResult();
        return tipo;
    }
    
    public List<GamacTipoBase> lstTipoBase(String s) {
        Query query = getEntityManager().createQuery("select t from GamacTipoBase t where t.activo =1 and t.tipoId.codProg = :codProg order by t.orden, t.nombre");
        query.setParameter("codProg", s != null ? s : "");
        return query.getResultList();
    }
    
    public GamacTipoBase tipoBaseXNombre(String nom) {
        GamacTipoBase res=null;
        Query query = em.createQuery("select t from GamacTipoBase t where t.activo =1 and t.nombre = :nombre ");
        query.setParameter("nombre", nom != null ? nom : "");
        if(query.getResultList()!=null&&!query.getResultList().isEmpty()){
            res=(GamacTipoBase) query.getResultList().get(0);
        }
        return res;
    }
    
    public List<GamacTipoBase> lstTiposDocumentosPersona() {
        Query query = getEntityManager().createQuery("select t from  GamacTipoBase t where t.activo =1 and t.codProg in ('TP_DOCID_DNI','TP_DOCID_CE') order by t.orden, t.nombre");
        return query.getResultList();
    }

}
