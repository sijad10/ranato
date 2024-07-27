/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.notificacion.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbTipo;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "NE_EVENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NeEvento.findAll", query = "SELECT n FROM NeEvento n"),
    @NamedQuery(name = "NeEvento.findById", query = "SELECT n FROM NeEvento n WHERE n.id = :id"),
    @NamedQuery(name = "NeEvento.findByFecha", query = "SELECT n FROM NeEvento n WHERE n.fecha = :fecha"),
    @NamedQuery(name = "NeEvento.findByActivo", query = "SELECT n FROM NeEvento n WHERE n.activo = :activo"),
    @NamedQuery(name = "NeEvento.findByAudLogin", query = "SELECT n FROM NeEvento n WHERE n.audLogin = :audLogin"),
    @NamedQuery(name = "NeEvento.findByAudNumIp", query = "SELECT n FROM NeEvento n WHERE n.audNumIp = :audNumIp")})
public class NeEvento implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NE_EVENTO")
    @SequenceGenerator(name = "SEQ_NE_EVENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_NE_EVENTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "ACTIVO")
    private Short activo;
    @Size(max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "DOCUMENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NeDocumento documentoId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo estadoId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;

    public NeEvento(Date fecha, Short activo, NeDocumento documentoId, SbTipo tipoId, SbTipo estadoId, SbUsuario usuarioId) {
        this.fecha = fecha;
        this.activo = activo;
        this.documentoId = documentoId;
        this.tipoId = tipoId;
        this.estadoId = estadoId;
        this.usuarioId = usuarioId;
    }
    
     public NeEvento(Date fecha, Short activo, NeDocumento documentoId, SbTipo tipoId, SbTipo estadoId, SbUsuario usuarioId, String audLogin, String audNumIp) {
        this.id = null;
        this.fecha = fecha;
        this.activo = activo;
        this.documentoId = documentoId;
        this.tipoId = tipoId;
        this.estadoId = estadoId;
        this.usuarioId = usuarioId;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }
    
    public NeEvento() {
    }

    public NeEvento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
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

    public NeDocumento getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(NeDocumento documentoId) {
        this.documentoId = documentoId;
    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
    }

    public SbTipo getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(SbTipo estadoId) {
        this.estadoId = estadoId;
    }

    public SbUsuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuario usuarioId) {
        this.usuarioId = usuarioId;
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
        if (!(object instanceof NeEvento)) {
            return false;
        }
        NeEvento other = (NeEvento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.notificacion.data.NeEvento[ id=" + id + " ]";
    }
    
}
