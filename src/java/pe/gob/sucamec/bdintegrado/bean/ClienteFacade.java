/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.sucamec.bdintegrado.data.Cliente;

/**
 *
 * @author mespinoza
 *
 */
@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }

    /**
     * METODOS
     *
     * @param id
     * @return
     */
    public String obtenerRucClienteXId(String id) {
        try { //1:ruc, 2:dni
            String sentencia = "select c.numeroIdentificacion from Cliente c where c.idCliente = :parametro";
            Query query = getEntityManager().createQuery(sentencia);
            BigDecimal big = new BigDecimal(id);
            query.setParameter("parametro", big);//s != null ? s : "");
            return  (String) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Map> lstDocCliente(String id) {
        Query q = em.createNativeQuery(
                "SELECT DISTINCT T.ID_TIPO_CLIENTE \"TIPODOC\" ,C.NUMERO_IDENTIFICACION \"NUMDOC\" FROM \n"
                + "TRAMDOC.EXPEDIENTE E \n"
                + "INNER JOIN \n"
                + "TRAMDOC.CLIENTE C ON \n"
                + "E.ID_CLIENTE = C.ID_CLIENTE \n"
                + "INNER JOIN TRAMDOC.TIPO_CLIENTE T \n"
                + "ON C.ID_TIPO_CLIENTE = T.ID_TIPO_CLIENTE \n"
                + "WHERE \n"
                + "E.ID_CLIENTE = ?");
        List<Map> res = new ArrayList<>();
        q.setHint("eclipselink.result-type", "Map");
        q.setParameter(1, id);
        res = q.setMaxResults(80).getResultList();
        return res;
    }
    
   /*
    SELECT T.ID_TIPO_CLIENTE ,C.NUMERO_IDENTIFICACION FROM 
     TRAMDOC.EXPEDIENTE E 
     INNER JOIN 
     TRAMDOC.CLIENTE C ON 
     E.ID_CLIENTE = C.ID_CLIENTE 
     INNER JOIN TRAMDOC.TIPO_CLIENTE T 
     ON C.ID_TIPO_CLIENTE = T.ID_TIPO_CLIENTE 
     WHERE 
     E.ID_CLIENTE = 
    */
    
    public Cliente onbtenerClientePorNumDoc(String numDoc) {
        Cliente res = null;
        Query q = em.createQuery("select c from Cliente c where c.numeroIdentificacion = :numDoc ");
        q.setParameter("numDoc", numDoc);
        q.setMaxResults(MAX_RES);
        if (q.getResultList() != null && !q.getResultList().isEmpty()) {
            res = (Cliente) q.getResultList().get(0);
        }
        return res;
    }
    
}
