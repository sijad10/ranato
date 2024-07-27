/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.renagi.data.ConsultaAmaTarjetaPropiedad;

/**
 *
 * @author msalinas
 */
@Stateless
public class ConsultaAmaTarjetaPropiedadFacade extends AbstractFacade<ConsultaAmaTarjetaPropiedad> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConsultaAmaTarjetaPropiedadFacade() {
        super(ConsultaAmaTarjetaPropiedad.class);
    }

    public List<ConsultaAmaTarjetaPropiedad> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select a from ConsultaAmaTarjetaPropiedad a where trim(a.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaTarjetaPropiedad> buscarTarjetaXNroExp(String s) {
        Query q = em.createQuery("select a from ConsultaAmaTarjetaPropiedad a where a.nroExpediente = :nroExp and a.activo = 1");
        q.setParameter("nroExp", s);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaTarjetaPropiedad> buscarTarjetaXNroDoc(String s) {
        Query q = em.createQuery("select a from ConsultaAmaTarjetaPropiedad a where a.personaCompradorId.numDoc = :nroDoc and a.activo = 1");
        q.setParameter("nroDoc", s);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaTarjetaPropiedad> listarTarjetasXnroDoc(String filtro) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("select a from ConsultaAmaTarjetaPropiedad a where a.activo = 1 ");
        sbQuery.append("and (a.personaCompradorId.numDoc like :dni or a.personaCompradorId.ruc like :ruc ");
        sbQuery.append("or a.armaId.serie like :serie ");
        sbQuery.append("or a.armaId.nroRua like :nrorua)");

        Query q = em.createQuery(sbQuery.toString());
        q.setParameter("dni", filtro);
        q.setParameter("ruc", filtro);
        q.setParameter("serie", filtro.toUpperCase());
        q.setParameter("nrorua", filtro.toUpperCase());
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaTarjetaPropiedad> listarTarjetasXPerComp(Long id) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("select a from ConsultaAmaTarjetaPropiedad a where a.activo = 1 ");
        sbQuery.append("and a.personaCompradorId.id = :personaCompradorId ");

        Query q = em.createQuery(sbQuery.toString());
        q.setParameter("personaCompradorId", id);
        //q.setMaxResults(MAX_RES);
        return q.getResultList();
    }
    
    public List<Map> listarTarjetasDisca(String filtro) {
        String sql = "SELECT distinct l.doc_propietario||' - '||l.propietario \"personaComprador\", "
                + "l.nro_serie \"serie\", l.tipo_arma \"tipoArma\", l.calibre \"calibre\", l.marca \"marca\", "
                + "l.modelo \"modelo\", l.situacion \"situacion\", l.nro_lic \"nroLic\" FROM rma1369.ws_licencias@SUCAMEC l where l.doc_propietario like ?1 or l.nro_serie like ?2";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        query.setParameter(2, filtro);
        res = query.getResultList();
        return res;
    }

    public List<Map> listarTarjetasCasaComDisca(String filtro) {
        String sql = "SELECT B.RZN_SOC \"personaComprador\", C.DES_ARM \"tipoArma\", D.DES_MARCA \"marca\", D.DES_MODELO \"modelo\", A.NRO_SERIE \"serie\", A.CALIBRE \"calibre\", E.DES_SIT \"situacion\", A.NRO_LIC \"nroLic\"\n"
                + "FROM RMA1369.AM_ALMACEN@SUCAMEC A "
                + "LEFT JOIN RMA1369.EMPRESA@SUCAMEC B ON NVL(A.COD_COM, A.COD_USR) = B.RUC\n"
                + "LEFT JOIN RMA1369.ARTICULO@SUCAMEC C ON A.TIP_ART = C.TIP_ART AND A.TIP_ARM = C.TIP_ARM \n"
                + "LEFT JOIN RMA1369.AM_MARCA@SUCAMEC D ON A.TIP_ART = D.TIP_ART AND A.TIP_ARM = D.TIP_ARM AND A.COD_MARCA = D.COD_MARCA AND A.COD_MODELO = D.COD_MODELO\n"
                + "LEFT JOIN RMA1369.SIT_ARMA@SUCAMEC E ON A.SIT_ARM = E.SIT_ARMA\n"
                + "WHERE A.NRO_LIC IS NULL AND TRIM(NVL(A.COD_COM, A.COD_USR)) like ?1 OR A.NRO_SERIE like ?2 ";
        Query query = em.createNativeQuery(sql);
        @SuppressWarnings("UnusedAssignment")
        List<Map> res = new ArrayList<>();
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, filtro);
        query.setParameter(2, filtro);
        res = query.getResultList();
        return res;
    }

    public List<ConsultaAmaTarjetaPropiedad> listarAmaTarjetaXExpediente(String nroExp) {
        Query q = em.createQuery("select a from ConsultaAmaTarjetaPropiedad a where a.nroExpediente = :nroExp and a.activo = 1");
        q.setParameter("nroExp", nroExp);
        q.setMaxResults(MAX_RES);
        return q.getResultList();
    }

    public List<ConsultaAmaTarjetaPropiedad> buscarTarXNroRua(String nroRua) {
        Query q = em.createQuery("select a from ConsultaAmaTarjetaPropiedad a where a.armaId.nroRua = :nroRua order by 1 asc");
        q.setParameter("nroRua", nroRua);
        return q.getResultList();
    }

}
