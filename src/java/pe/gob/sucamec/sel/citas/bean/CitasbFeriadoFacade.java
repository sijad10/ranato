/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pe.gob.sucamec.sel.citas.data.CitaSbFeriado;
import pe.gob.sucamec.sel.citas.data.CitaTipoGamac;

/**
 *
 * @author rarevalo
 */
@Stateless
public class CitasbFeriadoFacade extends AbstractFacade<CitaSbFeriado> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CitasbFeriadoFacade() {
        super(CitaSbFeriado.class);
    }

    public List<CitaSbFeriado> listaFeriados() {
        List<CitaSbFeriado> respuesta = new ArrayList();
        String sentencia = "select t from CitaSbFeriado t where t.activo =1";
        Query query = getEntityManager().createQuery(sentencia);
        respuesta = query.getResultList();
        return respuesta;
    }

    public List<Date> listFeriados(Date fecIni, Date fecFin) {
        Query q = em.createQuery("SELECT p.fechaFeriado FROM CitaSbFeriado p where FUNC('trunc',p.fechaFeriado) between FUNC('trunc',:ffini) and FUNC('trunc',:ffin)");
        q.setParameter("ffini", fecIni, TemporalType.DATE);
        q.setParameter("ffin", fecFin, TemporalType.DATE);
        return q.getResultList();
    }
    
    public List<CitaSbFeriado> listFeriadosRango(Date fecIni, Date fecFin) {
        Query q = em.createQuery("SELECT p.fechaFeriado FROM CitaSbFeriado p where FUNC('trunc',p.fechaFeriado) between FUNC('trunc',:ffini) and FUNC('trunc',:ffin)");
        q.setParameter("ffini", fecIni, TemporalType.DATE);
        q.setParameter("ffin", fecFin, TemporalType.DATE);
        return q.getResultList();
    }
    
    
}
