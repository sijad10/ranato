/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.seguridad;

import java.util.List;
import javax.faces.context.FacesContext;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.sessions.factories.DescriptorCustomizer;
import pe.gob.sucamec.sistemabase.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
public class AuditoriaEntidad extends DescriptorEventAdapter implements DescriptorCustomizer {

    String tablas[][] = {//EppLibroDetalle
        {"EppLibroDetalle", "BDHISTORICO.EPP_LIBRO_DETALLE_HIS"},
        {"EppLibroUsoDiario", "BDHISTORICO.EPP_LIBRO_USO_DIARIO_HIS"},
        {"EppLibroMes", "BDHISTORICO.EPP_LIBRO_MES_HIS"},
        {"EppLibro", "BDHISTORICO.EPP_LIBRO_HIS"},
        {"Areas", "BDHISTORICO.AREAS_HIS"},
        {"SiDocumentos", "BDHISTORICO.SI_DOCUMENTOS_HIS"},
        {"Roles", "BDHISTORICO.ROLES_HIS"},
        {"Tickets", "BDHISTORICO.TICKETS_HIS"},
        {"TicketObs", "BDHISTORICO.TICKET_OBS_HIS"},
        {"TicketRutas", "BDHISTORICO.TICKET_RUTAS_HIS"},
        {"Tipos", "BDHISTORICO.TIPOS_HIS"},
        {"EppRegistro", "BDHISTORICO.EPP_REGISTRO_HIS"},
        {"EppGuiaTransitoPolicia", "BDHISTORICO.EPP_GUIA_TRANSITO_POLICIA_HIS"},
        {"EppGuiaTransitoVehiculo", "BDHISTORICO.EPP_GUIA_TRANSITO_VEHICULO_HIS"},
        {"EppRegistroGuiaTransito", "BDHISTORICO.EPP_REGISTRO_GUIA_TRANSITO_HIS"},
        {"EppResolucion", "BDHISTORICO.EPP_RESOLUCION_HIS"},
        {"EppExplosivoSolicitado", "BDHISTORICO.EPP_EXPLOSIVO_SOLICITADO_HIS"},
        {"SbRecibos", "BDHISTORICO.SB_RECIBOS_HIS"},
        {"EppGteRegistro", "BDHISTORICO.EPP_GTE_REGISTRO_HIS"},
        {"EppGteExplosivoSolicita", "BDHISTORICO.EPP_GTE_EXPLOSIVO_SOLICITA_HIS"},
        {"EppRegistro", "BDHISTORICO.EPP_REGISTRO_HIS"},
        {"EppCapacitacion", "BDHISTORICO.EPP_CAPACITACION_HIS"},
        {"EppCertificado", "BDHISTORICO.EPP_CERTIFICADO_HIS"},
        {"EppGuiaTransitoPolicia", "BDHISTORICO.EPP_GUIA_TRANSITO_POLICIA_HIS"},
        {"SspSolicitudCese", "BDHISTORICO.SSP_SOLICITUD_CESE_HIS"},
        {"SspSolicitudCeseDet", "BDHISTORICO.SSP_SOLICITUD_CESE_DET_HIS"},
        {"AmaRegEmpArma", "BDHISTORICO.AMA_REG_EMP_ARMA_HIS"},
        {"SbReciboRegistro", "BDHISTORICO.SB_RECIBO_REGISTRO_HIS"},
        {"Usuarios", "BDHISTORICO.USUARIOS_HIS"},
        {"AmaSolicitudRecojo", "BDHISTORICO.AMA_SOLICITUD_RECOJO_HIS"},
        {"AmaTarjetaPropiedad", "BDHISTORICO.AMA_TARJETA_PROPIEDAD_HIS"},
        {"AmaGuiaTransito", "BDHISTORICO.9_HIS"},
        {"SbDireccion", "BDHISTORICO.SB_DIRECCION_HIS"},
        {"SbPersona", "BDHISTORICO.SB_PERSONA_HIS"},
        {"SbDireccionGt", "BDHISTORICO.SB_DIRECCION_HIS"},
        {"SbPersonaGt", "BDHISTORICO.SB_PERSONA_HIS"},
        {"SbMedioContacto", "BDHISTORICO.SB_MEDIO_CONTACTO_HIS"},
        {"SbMedioContactoGt", "BDHISTORICO.SB_MEDIO_CONTACTO_HIS"},
        {"SbCsCertifsalud", "BDHISTORICO.SB_CS_CERTIFSALUD_HIS"},
        {"SbCsEstablecimiento", "BDHISTORICO.SB_CS_ESTABLECIMIENTO_HIS"},
        {"SbCsMedicoEstabsal", "BDHISTORICO.SB_CS_MEDICO_ESTABSAL_HIS"},
        {"SbCsMedico", "BDHISTORICO.SB_CS_MEDICO_HIS"},
        {"SbCsCertifMedico", "BDHISTORICO.SB_CS_CERTIF_MEDICO_HIS"},
        {"SspCarteraCliente", "BDHISTORICO.SSP_CARTERA_CLIENTE_HIS"},
        {"SspCarteraEvento", "BDHISTORICO.SSP_CARTERA_EVENTO_HIS"},
        {"SspCarteraArma", "BDHISTORICO.SSP_CARTERA_ARMA_HIS"},
        {"SspCarteraVehiculo", "BDHISTORICO.SSP_CARTERA_VEHICULO_HIS"},
        {"SspCarteraVigilante", "BDHISTORICO.SSP_CARTERA_VIGILANTE_HIS"},
        {"SspAlumnoCurso", "BDHISTORICO.SSP_ALUMNO_CURSO_HIS"},
        {"SspCursoEvento", "BDHISTORICO.SSP_CURSO_EVENTO_HIS"},
        {"SspInstructor", "BDHISTORICO.SSP_INSTRUCTOR_HIS"},
        {"SspLocal", "BDHISTORICO.SSP_LOCAL_HIS"},
        {"SspLocalContacto", "BDHISTORICO.SSP_LOCAL_CONTACTO_HIS"},
        {"SspModulo", "BDHISTORICO.SSP_MODULO_HIS"},
        {"SspPersonaFoto", "BDHISTORICO.SSP_PERSONA_FOTO_HIS"},
        {"SspPrograHora", "BDHISTORICO.SSP_PROGRA_HORA_HIS"},
        {"SspProgramacion", "BDHISTORICO.SSP_PROGRAMACION_HIS"},
        {"SspRegistroCurso", "BDHISTORICO.SSP_REGISTRO_CURSO_HIS"},
        {"SspPrograHistorial", "BDHISTORICO.SSP_PROGRA_HISTORIAL_HIS"},
        {"SspDocumento", "BDHISTORICO.SSP_DOCUMENTO_HIS"},
        {"SspLugarServicio", "BDHISTORICO.SSP_LUGAR_SERVICIO_HIS"},
        {"SspVehiculo", "BDHISTORICO.SSP_VEHICULO_HIS"},
        {"SspInstructorModulo", "BDHISTORICO.SSP_INSTRUCTOR_MODULO_HIS"},
        {"SspNotas", "BDHISTORICO.SSP_NOTAS_HIS"},
        {"SspRequisito", "BDHISTORICO.SSP_REQUISITO_HIS"},
        {"SspRegistro", "BDHISTORICO.SSP_REGISTRO_HIS"},
        {"SspRegistroEvento", "BDHISTORICO.SSP_REGISTRO_EVENTO_HIS"},
        {"SspCarne", "BDHISTORICO.SSP_CARNE_HIS"},
        {"SspArchivo", "BDHISTORICO.SSP_ARCHIVO_HIS"},
        {"SspRequisito", "BDHISTORICO.SSP_REQUISITO_HIS"},
        {"TipoSeguridad", "BDHISTORICO.TIPO_SEGURIDAD_HIS"},
        {"AmaRegEmpProceso", "BDHISTORICO.AMA_REG_EMP_PROCESO_HIS"},
        {"SbRegistroUsuarioGt", "BDHISTORICO.SB_REGISTRO_USUARIO_HIS"},
        {"SbValidacionWebGt", "BDHISTORICO.SB_VALIDACION_WEB_HIS"},
        {"SbValidacionWeb", "BDHISTORICO.SB_VALIDACION_WEB_HIS"},        
        {"EppGteDeposito", "BDHISTORICO.EPP_GTE_DEPOSITO_HIS"},
        {"SbUsuarioGt", "BDHISTORICO.SB_USUARIO_HIS"},
        {"SbUsuario", "BDHISTORICO.SB_USUARIO_HIS"},
        {"CitaTurTurno", "BDHISTORICO.TUR_TURNO_HIS"},
        {"CitaTurPersona", "BDHISTORICO.TUR_PERSONA_HIS"},
        {"CitaTurComprobante", "BDHISTORICO.TUR_COMPROBANTE_HIS"},
        {"CitaTurConstancia", "BDHISTORICO.TUR_CONSTANCIA_HIS"},
        {"CitaTurLicenciaReg", "BDHISTORICO.TUR_LICENCIA_REG_HIS"},
        {"AmaSolicitudTarjeta", "BDHISTORICO.AMA_SOLICITUD_TARJETA_HIS"},
        {"Tickets", "BDHISTORICO.TICKETS_HIS"},
        {"TicketRutas", "BDHISTORICO.TICKETS_RUTAS_HIS"},
        {"SbExpVirtualRequisito", "BDHISTORICO.SB_EXP_VIRTUAL_REQUISITO_HIS"},
        {"SbExpVirtualAdjunto", "BDHISTORICO.SB_EXP_VIRTUAL_ADJUNTO_HIS"},
        {"SbExpVirtualSolicitud", "BDHISTORICO.SB_EXP_VIRTUAL_SOLICITUD_HIS"},
        {"CitaSbTurnoExpediente", "BDHISTORICO.SB_TURNO_EXPEDIENTE_HIS"},
        {"GamacAmaAdmunDevolucion", "BDHISTORICO.AMA_ADMUN_DEVOLUCION_HIS"},        
        {"GamacAmaAdmunDetalleTrans", "BDHISTORICO.AMA_ADMUN_DETALLE_TRANS_HIS"},
        {"GamacAmaAdmunTransaccion", "BDHISTORICO.AMA_ADMUN_TRANSACCION_HIS"}
    };

    @Override
    public void customize(ClassDescriptor descriptor) {
        // Descriptor.Alias
        for (String t[] : tablas) {
            if (t[0].equals(descriptor.getAlias())) {
                HistoryPolicy policy = new HistoryPolicy();
                policy.addHistoryTableName(t[1]);
                policy.addStartFieldName("AUD_FECHA_INI");
                policy.addEndFieldName("AUD_FECHA_FIN");
                descriptor.setHistoryPolicy(policy);
                descriptor.getEventManager().addListener(this);
                return;
            }
        }
        descriptor.getEventManager().addListener(this);
    }

    @Override
    public void aboutToInsert(DescriptorEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(facesContext != null){
            // Parece que en estas clases no se pueden injectar los controllers //
            LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
            String login = (String) lc.getUsuario().getLogin();
            if ((login == null) || login.equals("")) {
                login = "SISTEMA";
            }
            for (String table : (List<String>) event.getDescriptor().getTableNames()) {
                event.getRecord().put(table + ".AUD_LOGIN", login);
                event.getRecord().put(table + ".AUD_NUM_IP", lc.getNumIP());
            }
        }
    }

    @Override
    public void aboutToUpdate(DescriptorEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(facesContext != null){
            // Parece que en estas clases no se pueden injectar los controllers //
            LoginController lc = (LoginController) JsfUtil.obtenerBean("loginController", LoginController.class);
            String login = (String) lc.getUsuario().getLogin();
            if ((login == null) || login.equals("")) {
                login = "SISTEMA";
            }
            for (String table : (List<String>) event.getDescriptor().getTableNames()) {
                event.getRecord().put(table + ".AUD_LOGIN", login);
                event.getRecord().put(table + ".AUD_NUM_IP", lc.getNumIP());
            }
        }
    }
}
