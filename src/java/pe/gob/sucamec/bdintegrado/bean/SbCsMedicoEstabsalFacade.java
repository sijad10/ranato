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
import pe.gob.sucamec.bdintegrado.data.SbCsMedico;
import pe.gob.sucamec.bdintegrado.data.SbCsMedicoEstabsal;

/**
 *
 * @author rfernandezv
 */
@Stateless
public class SbCsMedicoEstabsalFacade extends AbstractFacade<SbCsMedicoEstabsal> {

    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SbCsMedicoEstabsalFacade() {
        super(SbCsMedicoEstabsal.class);
    }
    public List<SbCsMedicoEstabsal> selectLike(String s) {
        if (s == null) {
            s = "";
        }
        Query q = em.createQuery("select s from SbCsMedicoEstabsal s where trim(s.id) like :id");
        q.setParameter("id", "%" + s + "%");
        q.setHint("eclipselink.batch.type", "IN");
        return q.getResultList();
    }
    
    public List<SbCsMedicoEstabsal> buscarMedicosCS(HashMap mMap){
        try {
            String jpql = "select a from SbCsMedicoEstabsal a "+
                           " where a.activo = 1";
            
            if (mMap.get("ruc") != null) {
                jpql = jpql + " and a.establecimientoId.propietarioId.ruc = :ruc";
            }
            
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1: // DNI MEDICO
                        jpql = jpql + " and a.medicoId.personaId.numDoc = :filtro ";
                        break;
                    case 2: // NOMBRE MEDICO
                        jpql = jpql + " and (concat(a.medicoId.personaId.apePat, ' ', a.medicoId.personaId.apeMat, ' ', a.medicoId.personaId.nombres) like :filtro) ";
                        break;
                    case 3: // ESTABLECIMIENTO
                        jpql = jpql + " and a.establecimientoId.id = :establecimientoCS ";
                        break;
                }
            }
            jpql = jpql + " order by a.id desc";

            Query q = em.createQuery(jpql);
            if (mMap.get("buscarPor") != null) {
                switch (Integer.parseInt(mMap.get("buscarPor").toString())) {
                    case 1:
                        q.setParameter("filtro", mMap.get("filtro").toString());
                        break;
                    case 2:
                        q.setParameter("filtro", "%" + mMap.get("filtro").toString() + "%");
                        break;
                    case 3:
                        q.setParameter("establecimientoCS", (Long) mMap.get("establecimientoCS") );
                        break;
                }
            }
            if (mMap.get("ruc") != null) {
                q.setParameter("ruc", mMap.get("ruc").toString());
            }
            return q.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }
    
        
    public int contarDNIPersonMedicoEstablecimiento(String numDoc, Long establecimientoId, Long cargoId, Long registroId) {
        int cont = 0;
        String plsql = "select count(a) from SbCsMedicoEstabsal a where a.activo = 1 "+
                        " and a.medicoId.personaId.numDoc = :numDoc and a.establecimientoId.id = :establecimientoId "+
                        " and a.cargoId.id = :cargoId ";
        if(registroId != null){
            plsql = plsql + " and a.id != :registroId ";
        }
        
        Query q = em.createQuery(plsql);
        q.setParameter("numDoc", numDoc);
        q.setParameter("establecimientoId", establecimientoId);
        q.setParameter("cargoId", cargoId);
        if(registroId != null){
            q.setParameter("registroId", registroId);
        }
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
    
    public List<SbCsMedicoEstabsal> obtenerCargosMedicoXEstablecimiento(String numDoc, Long establecimientoId) {
        String plsql = "select a from SbCsMedicoEstabsal a where a.activo = 1 "+
                        " and a.medicoId.personaId.numDoc = :numDoc and a.establecimientoId.id = :establecimientoId ";
        
        Query q = em.createQuery(plsql);
        q.setParameter("numDoc", numDoc);
        q.setParameter("establecimientoId", establecimientoId);

        return q.getResultList();
    }
    
    public int contarDelegadosXEstablecimiento(Long establecimientoId) {
        int cont = 0;
        String plsql = "select count(a) from SbCsMedicoEstabsal a where a.activo = 1 "+
                        " and a.establecimientoId.id = :establecimientoId "+
                        " and a.esDelegado = 1  ";
        
        Query q = em.createQuery(plsql);        
        q.setParameter("establecimientoId", establecimientoId);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        return cont;
    }
    
    public int contarDelegadosXEstablecimientoXMedico(Long establecimientoId, Long medicoId) {
        int cont = 0;
        String plsql = "select count(a) from SbCsMedicoEstabsal a where a.activo = 1 "+
                        " and a.establecimientoId.id = :establecimientoId "+
                        " and a.cargoId.codProg = 'TP_CCS_TIT' and a.medicoId.id = :medicoId  ";
        
        Query q = em.createQuery(plsql);        
        q.setParameter("establecimientoId", establecimientoId);
        q.setParameter("medicoId", medicoId);
        
        List<Long> results = q.getResultList();
        for (Long _values : results) {
            cont = _values.intValue();
            break;
        }
        
        return cont;
    }
}
