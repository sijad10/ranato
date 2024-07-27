/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.ws;

import com.sun.xml.ws.client.BindingProviderProperties;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.ws.BindingProvider;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;
import pe.gob.sucamec.bdintegrado.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import wspide.BeanDdp;
import wspide.Consulta;
import wspide.Consulta_Service;
import wspide.Persona;
import wspide.PideMigraciones;
import wspide.PideMigraciones_Service;
import wspide.ResPideMigra;
import wsreniecmq.ws.ReniecMq;
import wsreniecmq.ws.ReniecMq_Service;
import wsreniecmq.ws.RespuestaMq;

/**
 *
 * @author msalinas
 */
@Named("wsPide")
@SessionScoped
public class WsPide implements Serializable {

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.TipoBaseFacade ejbTipoBaseFacade;
    @EJB
    private pe.gob.sucamec.sistemabase.beans.SbTipoFacade ejbTipoFacade;

    public SbPersonaGt buscarReniec(SbPersonaGt persona) {
        try {
            wspide.Consulta_Service serR = new wspide.Consulta_Service();
            wspide.Consulta port = serR.getConsultaPort();
            
            wspide.Persona p = port.consultaDNI(persona.getNumDoc());
            persona.setApeMat(null);
            persona.setApePat(null);
            persona.setNombres(null);
            persona.setFechaNac(null);
            persona.setGeneroId(null);
            
            if (p == null) {
                persona.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                persona.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                persona.setActivo((short) 1);
                return persona;
            } else {
                persona.setApePat(p.getAPPAT());
                persona.setApeMat(p.getAPMAT());
                persona.setNombres(p.getNOMBRES());
                persona.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                persona.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                persona.setActivo((short) 1);
                if(p.getSEXO() != null){
                    if(p.getSEXO().trim().toUpperCase().equals("M")){
                        persona.setGeneroId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_MAS"));
                    }else{
                        if(p.getSEXO().trim().toUpperCase().equals("F")){
                            persona.setGeneroId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_GEN_FEM"));
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
        } catch (Exception ex) {
            System.err.printf("DNI error: " + ((persona.getNumDoc() != null)?persona.getNumDoc():"-") );
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("ErrorConsultaReniec"));            
            return null;
        }        
    }
    
    public SbPersonaGt buscarReniec_capacitacion(SbPersonaGt persona) {
        try {
            if (!JsfUtil.ValidarDniRuc(persona.getNumDoc() + persona.getNumDocVal(), JsfUtil.TipoDoc.DNI)) {
                persona.setApeMat(null);
                persona.setApePat(null);
                persona.setNombres(null);
                JsfUtil.mensajeAdvertencia("El DNI no es válido.");
            } else {

                wspide.Consulta_Service serR = new wspide.Consulta_Service();
                wspide.Consulta port = serR.getConsultaPort();

                wspide.Persona p = port.consultaDNI(persona.getNumDoc());
                persona.setApeMat(null);
                persona.setApePat(null);
                persona.setNombres(null);

                if (p == null) {
                    persona.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                    persona.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                    persona.setActivo((short) 1);
                    JsfUtil.mensajeAdvertencia("No se encontró el documento ingresado.");
                } else {
                    persona.setApePat(p.getAPPAT());
                    persona.setApeMat(p.getAPMAT());
                    persona.setNombres(p.getNOMBRES());
                    persona.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_DNI"));
                    persona.setTipoId(ejbTipoBaseFacade.tipoBaseXCodProg("TP_PER_NAT"));
                    persona.setActivo((short) 1);
                }
            }
            return persona;
        } catch (Exception ex) {
            System.err.printf("DNI error: " + ((persona.getNumDoc() != null)?persona.getNumDoc():"-") );
            JsfUtil.mensajeAdvertencia(JsfUtil.bundle("ErrorConsultaReniec"));
            return null;
        }
    }
    
    public Persona ReniecDni(String dni) {
        try {
            if (dni.length() != 8) {
                JsfUtil.mensajeError("El DNI no es válido.");
            } else {
                Consulta_Service serR = new Consulta_Service();
                Consulta port = serR.getConsultaPort();

                BindingProvider bindingProvider = (BindingProvider) port;
                Map<String, Object> context = bindingProvider.getRequestContext();
                context.put(BindingProviderProperties.CONNECT_TIMEOUT, 6 * 1000);
                context.put(BindingProviderProperties.REQUEST_TIMEOUT, 6 * 1000);

                Persona p = port.consultaDNI(dni);
                if (p == null) {
                    JsfUtil.mensajeError("No se encontró el DNI");
                    JsfUtil.invalidar("numDoc");
                } else {
                    return p;
                }
            }
        } catch (Exception ex) {
            //Syso(ex.getMessage());
            JsfUtil.mensajeError(ResourceBundle.getBundle("/BundleGamac").getString("ErrorConsultaReniec"));
        }
        return null;        
    }
    
    public boolean buscarSunat(String ruc) {
        try {
            if (!JsfUtil.ValidarDniRuc(ruc, JsfUtil.TipoDoc.RUC)) {
                JsfUtil.mensajeError("El RUC no es válido.");
                return false;
            }
            wspide.Consulta_Service serR = new wspide.Consulta_Service();
            wspide.Consulta port = serR.getConsultaPort();
            wspide.BeanDdp b = port.consultaRuc(ruc);
            if (b == null) {
                JsfUtil.mensajeAdvertencia("No se encontro el ruc");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.mensajeError("SUNAT no se encuentra disponible");
            return false;
        }
        return true;
    }
    
    public SbPersona buscarPerSunat(String ruc) {
        try {
            SbPersona regPersona = new SbPersona();
            regPersona.setRuc(ruc);
//            dniRucOk = false;
            if (//!ruc.startsWith("20")|| 
                    !JsfUtil.ValidarDniRuc(ruc, JsfUtil.TipoDoc.RUC)) {
                JsfUtil.mensajeError("El RUC no es válido.");
            }
            Consulta_Service serR = new Consulta_Service();
            Consulta port = serR.getConsultaPort();

            BindingProvider bindingProvider = (BindingProvider) port;
            Map<String, Object> context = bindingProvider.getRequestContext();
            context.put(BindingProviderProperties.CONNECT_TIMEOUT, 6 * 1000);
            context.put(BindingProviderProperties.REQUEST_TIMEOUT, 6 * 1000);

            BeanDdp b = port.consultaRuc(ruc);
            if ((b == null) || !b.isEsActivo()) {
                JsfUtil.mensajeError("El RUC no existe o no esta vigente");
                JsfUtil.invalidar("rucJur");
            } else {
                regPersona.setRznSocial(b.getDdpNombre());
                regPersona.setTipoDoc(ejbTipoFacade.buscarTipoBaseXCodProg("TP_DOCID_RUC"));
                regPersona.setTipoId(ejbTipoFacade.buscarTipoBaseXCodProg("TP_PER_JUR"));
                regPersona.setActivo((short) 1);
                regPersona.setAudLogin("ADMIN");
                regPersona.setAudNumIp("0.0.0.0");
                return regPersona;
//                dniRucOk = true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JsfUtil.mensajeError(ResourceBundle.getBundle("/BundleGamac").getString("ErrorConsultaSunat"));
        }
        return null;
    }
    
    public SbPersonaGt buscarSunat(SbPersonaGt persona) {
        try {
            if (!JsfUtil.ValidarDniRuc(persona.getRuc(), JsfUtil.TipoDoc.RUC)) {
                persona.setRznSocial(null);
                JsfUtil.mensajeError("El RUC no es válido.");
            }else{
                wspide.Consulta_Service serR = new wspide.Consulta_Service();
                wspide.Consulta port = serR.getConsultaPort();
                wspide.BeanDdp p = port.consultaRuc(persona.getRuc());

                persona.setRznSocial(null);
                persona.setApeMat(null);
                persona.setApePat(null);
                persona.setNombres(null);
                persona.setFechaNac(null);
                persona.setGeneroId(null);
                persona.setTipoDoc(ejbTipoBaseFacade.tipoBaseXCodProg("TP_DOCID_RUC"));
                
                if (p == null) {                    
                    persona.setActivo((short) 1);
                }else{
                    persona.setRznSocial(p.getDdpNombre());
                    persona.setActivo((short) 1);
                }
            }
            return persona;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.mensajeAdvertencia(JsfUtil.bundleBDIntegrado("ErrorConsultaSunat"));
            return null;
        }
    }
    
    public String buscarAntecedentes(String login, String nroDoc, String nroExp) {
        wspide.ConsultaPJ_Service serR = new wspide.ConsultaPJ_Service();
        wspide.ConsultaPJ port = serR.getConsultaPJPort();

        String respuesta;
        respuesta = port.consultaPJxDNI(login, nroDoc, nroExp);

        return respuesta;
    }
        
    public ResPideMigra buscarCarnetExtranjeria(String login, String ce) {
        try {
            PideMigraciones_Service serR = new PideMigraciones_Service();
            PideMigraciones port = serR.getPideMigracionesPort();

            return port.consultarDocExt(login, ce, "CE");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.printf("CE error: " + ((ce != null)?ce:"-") );
            JsfUtil.mensajeError("Hubo un error al consultar a Migraciones");
        }
        return null;
    }
    
    public RespuestaMq buscarReniecMQ(String login, String dni){
        try {
            ReniecMq_Service servicio = new ReniecMq_Service();
            ReniecMq port = servicio.getReniecMqPort();

            RespuestaMq respuestaMq = port.consultaDNI(login, dni, "123", "1");
            return respuestaMq;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
