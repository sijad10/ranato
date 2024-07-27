/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.SbExpVirtualSolicitud;

/**
 *
 * @author msalinas
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_TURNO_EXPEDIENTE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaSbTurnoExpediente.findAll", query = "SELECT s FROM CitaSbTurnoExpediente s"),
    @NamedQuery(name = "CitaSbTurnoExpediente.findById", query = "SELECT s FROM CitaSbTurnoExpediente s WHERE s.id = :id"),
    @NamedQuery(name = "CitaSbTurnoExpediente.findByNroExpediente", query = "SELECT s FROM CitaSbTurnoExpediente s WHERE s.nroExpediente = :nroExpediente"),
    @NamedQuery(name = "CitaSbTurnoExpediente.findByActivo", query = "SELECT s FROM CitaSbTurnoExpediente s WHERE s.activo = :activo"),
    @NamedQuery(name = "CitaSbTurnoExpediente.findByAudLogin", query = "SELECT s FROM CitaSbTurnoExpediente s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "CitaSbTurnoExpediente.findByAudNumIp", query = "SELECT s FROM CitaSbTurnoExpediente s WHERE s.audNumIp = :audNumIp")})
public class CitaSbTurnoExpediente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_TURNO_EXPEDIENTE")
    @SequenceGenerator(name = "SEQ_SB_TURNO_EXPEDIENTE", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_TURNO_EXPEDIENTE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NRO_EXPEDIENTE")
    private String nroExpediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurTurno turnoId;
    @JoinColumn(name = "EXP_VIRTUAL_SOLICITUD_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbExpVirtualSolicitud expVirtualSolicitudId;

    public CitaSbTurnoExpediente() {
    }

    public CitaSbTurnoExpediente(Long id) {
        this.id = id;
    }

    public CitaSbTurnoExpediente(Long id, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getAudLogin() {
        return audLogin;
    }

    public void setAudLogin(String audLogin) {
        this.audLogin = audLogin;
    }

    public String getAudNumIp() {
        return audNumIp;
    }

    public void setAudNumIp(String audNumIp) {
        this.audNumIp = audNumIp;
    }

    public CitaTurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(CitaTurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public SbExpVirtualSolicitud getExpVirtualSolicitudId() {
        return expVirtualSolicitudId;
    }

    public void setExpVirtualSolicitudId(SbExpVirtualSolicitud expVirtualSolicitudId) {
        this.expVirtualSolicitudId = expVirtualSolicitudId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitaSbTurnoExpediente)) {
            return false;
        }
        CitaSbTurnoExpediente other = (CitaSbTurnoExpediente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.SbTurnoExpediente[ id=" + id + " ]";
    }
    
}
