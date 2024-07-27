/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.ws;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import pe.gob.sucamec.sel.citas.data.CitaTurPersona;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;
import wspide.BeanDdp;
import wspide.Consulta;
//cambio poder judicial
//import wspide.ConsultaPJ;
//import wspide.ConsultaPJ_Service;
import wspide.Consulta_Service;
import wspide.Persona;

/**
 *
 * @author rarevalo
 */
@Named("citaWsPide")
@SessionScoped
public class CitaWsPide implements Serializable {

    @EJB
    private pe.gob.sucamec.sel.citas.bean.CitaTipoBaseFacade ejbTipoBaseFacade;

    public CitaTurPersona buscarSunat(String ruc) {
        try {
            CitaTurPersona regPersona = new CitaTurPersona();
            regPersona.setRuc(ruc);
//            dniRucOk = false;
            if (//!ruc.startsWith("20")|| 
                    !JsfUtil.ValidarDniRuc(ruc, "RUC")) {
                JsfUtil.addErrorMessage("El RUC no es válido.");
            }
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();
            BeanDdp b = port.consultaRuc(ruc);
            if ((b == null) || !b.isEsActivo()) {
                JsfUtil.addErrorMessage("El RUC no existe o no esta vigente");
                JsfUtil.invalidar("rucJur");
            } else {
                regPersona.setRznSocial(b.getDdpNombre());
                regPersona.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                regPersona.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));
                regPersona.setActivo((short) 1);
                return regPersona;
//                dniRucOk = true;
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/BundleCitaInterna").getString("ErrorConsultaSunat"));
        }
        return null;
    }

    public CitaTurPersona buscarReniec(CitaTurPersona persona) {
        try {
            if (!JsfUtil.ValidarDniRuc(persona.getNumDoc() + persona.getNumDocVal(), "DNI")) {
                JsfUtil.addErrorMessage("El DNI no es válido.");
            } else {
                CitaTurPersona regPersona = new CitaTurPersona();
                regPersona.setNumDoc(persona.getNumDoc());
                regPersona.setNumDocVal(persona.getNumDocVal());
//            dniRucOk = false;
                Consulta_Service serR = new Consulta_Service();
                Consulta port = serR.getConsultaPort();
                wspide.Persona p = port.consultaDNI(regPersona.getNumDoc());
                regPersona.setApePat(null);
                regPersona.setApeMat(null);
                regPersona.setNombres(null);
                if (p == null) {
                    regPersona.setApePat(persona.getApePat());
                    regPersona.setApeMat(persona.getApeMat());
                    regPersona.setNombres(persona.getNombres());
                    regPersona.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
                    regPersona.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                    regPersona.setActivo((short) 1);
                    return regPersona;
//                    JsfUtil.addErrorMessage("No se encontró el DNI");
//                    JsfUtil.invalidar("numDoc");
                } else {
                    regPersona.setApePat(p.getAPPAT());
                    regPersona.setApeMat(p.getAPMAT());
                    regPersona.setNombres(p.getNOMBRES());
                    regPersona.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
                    regPersona.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
                    regPersona.setActivo((short) 1);
                    return regPersona;
//                dniRucOk = true;
                }
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/BundleCitaInterna").getString("ErrorConsultaReniec"));
        }
        return null;
    }

    public CitaTurPersona buscarReniec(String numDoc) {
        wspide.Persona p = new Persona();
        CitaTurPersona persona = new CitaTurPersona();
        try {
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();
            p = port.consultaDNI(numDoc);
        } catch (Exception ex) {
            //ex.printStackTrace();
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("ErrorConsultaReniec"));
            //persona.setNombres(null);
            p = null;
        }
            
        if (p == null) {
            persona.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
            persona.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
            persona.setActivo((short) 1);

            return persona;
        } else {
            persona.setNumDoc(numDoc);
            persona.setApePat(p.getAPPAT());
            persona.setApeMat(p.getAPMAT());
            persona.setNombres(p.getNOMBRES());
            persona.setTipoDoc(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_DOCID_DNI"));
            persona.setTipoId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_PER_NAT"));
            persona.setActivo((short) 1);
            if(p.getSEXO() != null){
                if(p.getSEXO().trim().toUpperCase().equals("M")){
                    persona.setGeneroId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_MAS"));
                }else{
                    if(p.getSEXO().trim().toUpperCase().equals("F")){
                        persona.setGeneroId(ejbTipoBaseFacade.buscarTipoBaseXCodProg("TP_GEN_FEM"));
                    }
                }
            }
            if(p.getFENAC() != null){
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    persona.setFechaNac(formatter.parse(p.getFENAC()));
                } catch (Exception e) {
                }
            }
            return persona;
        }
        //}
        //return persona;
    }
    
    
    //cambio poder judicial
//    public boolean buscarAntecedentes(String login, String nroDoc, String nroExp) {
//        boolean flagValido = true;
//
//        ConsultaPJ_Service serR = new ConsultaPJ_Service();
//        ConsultaPJ port = serR.getConsultaPJPort();
//
////        String login = "DDIAZ";
////        String dni = "15904616";
////        String nroExp = "20165540540";
//
//        String respuesta = port.consultaPJxDNI(login, nroDoc, nroExp);
//        
//        if(Objects.equals(respuesta, "SINHOMONIMIA/SINOFICIO")){
//            flagValido = false;
//        }
//
//        return flagValido;
//    }
    
}
