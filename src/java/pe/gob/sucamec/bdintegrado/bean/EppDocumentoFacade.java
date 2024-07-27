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
import pe.gob.sucamec.bdintegrado.data.EppDocumento;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author mespinoza
 */
@Stateless
public class EppDocumentoFacade extends AbstractFacade<EppDocumento> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EppDocumentoFacade() {
        super(EppDocumento.class);
    }

    public List<EppDocumento> findDocumento(String tipoId, String numero) {
        List<EppDocumento> lis = new ArrayList();
        try {
            Query q = getEntityManager().createQuery(
                    "select u from EppDocumento u where trim(u.tipoId.id) like :tipoId and u.numero like :numero and u.activo = 1");
            q.setParameter("tipoId", "%" + tipoId + "%");
            q.setParameter("numero", "%" + numero + "%");
            lis = q.getResultList();
            return lis;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<EppDocumento> buscarDocumentoGuiaTransitoExterna(String tipoId, String numero, SbPersonaGt propietario) {
        List<EppDocumento> list = new ArrayList<>();
        try {
            Query q = getEntityManager().createQuery(
                    "select u from EppDocumento u "
                    + "where "
                    + "(u.tipoId.codProg = :tipoId) and "
                    + "(u.empresaId.id = :propietario) and "
                    + "(u.numero like :numero or :numero is null) and "
                    + "u.activo = 1");
            q.setParameter("tipoId", tipoId);
            q.setParameter("numero", "%" + numero + "%");
            q.setParameter("propietario", propietario.getId());
            list = q.getResultList();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
