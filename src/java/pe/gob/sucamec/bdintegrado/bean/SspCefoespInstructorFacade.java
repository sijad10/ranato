/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.sucamec.bdintegrado.data.SspCefoespInstructor;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author locador845.ogtic
 */
@Stateless
public class SspCefoespInstructorFacade extends AbstractFacade<SspCefoespInstructor>{
    
    @PersistenceContext(unitName = "SELPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public SspCefoespInstructorFacade() {
        super(SspCefoespInstructor.class);
    }
    
        public int anualarInstructores(Long id) {
        
        javax.persistence.Query q = em.createNativeQuery("UPDATE BDINTEGRADO.SSP_CEFOESP_INSTRUCTOR  a SET ACTIVO = 0 "+
                                  " WHERE a.registro_Id = ?1" );
        q.setParameter(1, id);
        return q.executeUpdate();
    }
    
    public List<SspCefoespInstructor> buscarInstructoresPorIdRegistroModalidad(Long registroId, String mod){
        
        String jpql = "select ci "                      
                      + " from SspCefoespInstructor ci, SspRegistro s "
                      + " where ci.registroId.id = s.id and s.id =:registroId and s.tipoProId.codProg =:mod "
                      + " and ci.activo = 1";        //r.tipoProId.codProg = 'TP_GSSP_CCRN'
        //and =:registroId ci.carneInstructorId.id = s.id  and
        //select ci from SspCefoespInstructor ci ,SspRegistro s where ci.registroId.id = s.id and s.tipoProId.codProg ='TP_GSSP_MOD_CEF' and ci.activo = '1'
        javax.persistence.Query q = em.createQuery(jpql);
        q.setParameter("registroId", registroId );        
        q.setParameter("mod", mod ); 
        if(!q.getResultList().isEmpty()){
            return q.getResultList();
        }
        return null;
                
    }   
    
    
    public List<Map> verificacionInstructor_ByTipoDocDISCA(Long idTipoDoc, String numDoc){
        List<Map> listPersona = new ArrayList<>();
        String sentencia = "SELECT \n" +
                                    " P.ID,\n" +
                                    " P.APE_PAT,\n" +
                                    " P.APE_MAT,\n" +
                                    " P.NOMBRES,\n" +
                                    " P.TIPO_ID,\n" +
                                    " P.NUM_DOC,\n" +
                                    " (CASE WHEN(DISCA.DNI IS NOT NULL) THEN 'SI' ELSE 'NO' END) AS AUTORIZADO\n" +
                                    " FROM BDINTEGRADO.SB_PERSONA P\n" +
                                    " LEFT JOIN ( SELECT LPAD(VS.COD_USR,8,'0') AS DNI FROM rma1369.ss_INSTRUCTOR@sucamec VS WHERE TRUNC(VS.FEC_VEN)>=TRUNC(current_date) GROUP BY VS.COD_USR ) DISCA ON (DISCA.DNI = P.NUM_DOC)\n" +
                                    " WHERE P.ACTIVO = 1 \n" +
                                    " AND P.TIPO_DOC = ?1 \n" +
                                    " AND P.NUM_DOC = ?2 ";
        javax.persistence.Query query = em.createNativeQuery(sentencia);
        query.setHint("eclipselink.result-type", "Map");
        query.setParameter(1, idTipoDoc);
        query.setParameter(2, numDoc);
        
        if(query.getResultList().isEmpty()){
            listPersona = null;
        }else{
            listPersona = query.getResultList();
        }
        return listPersona;
        
    }
    
    
    
}
