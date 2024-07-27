/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.util.List;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.sessions.factories.DescriptorCustomizer;
import pe.gob.sucamec.sel.citas.jsf.util.JsfUtil;

/**
 *
 * @author Renato
 */
public class AuditoriaEntidad extends DescriptorEventAdapter implements DescriptorCustomizer {

    String tablas[][] = { /*
     {"Areas", "INTEGRADO.AREAS_HIST"},
     {"Documentos", "INTEGRADO.DOCUMENTOS_HIST"},
     {"Roles", "INTEGRADO.ROLES_HIST"},
     {"Tickets", "INTEGRADO.TICKETS_HIST"},
     {"TicketObs", "INTEGRADO.TICKET_OBS_HIST"},
     {"TicketRutas", "INTEGRADO.TICKET_RUTAS_HIST"},
     {"Tipos", "INTEGRADO.TIPOS_HIST"},
     {"Usuarios", "INTEGRADO.USUARIOS_HIST"},
     */};

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
        for (String table : (List<String>) event.getDescriptor().getTableNames()) {
            event.getRecord().put(table + ".AUD_LOGIN", "WEB");
            event.getRecord().put(table + ".AUD_NUM_IP", JsfUtil.obtenertNumIP());
        }
    }

    @Override
    public void aboutToUpdate(DescriptorEvent event) {
        for (String table : (List<String>) event.getDescriptor().getTableNames()) {
            event.getRecord().put(table + ".AUD_LOGIN", "WEB");
            event.getRecord().put(table + ".AUD_NUM_IP", JsfUtil.obtenertNumIP());
        }
    }
}
