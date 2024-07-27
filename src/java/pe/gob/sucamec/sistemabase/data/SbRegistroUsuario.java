/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

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

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_REGISTRO_USUARIO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbRegistroUsuario.findAll", query = "SELECT s FROM SbRegistroUsuario s"),
    @NamedQuery(name = "SbRegistroUsuario.findById", query = "SELECT s FROM SbRegistroUsuario s WHERE s.id = :id"),
    @NamedQuery(name = "SbRegistroUsuario.findByAdministradoNombres", query = "SELECT s FROM SbRegistroUsuario s WHERE s.administradoNombres = :administradoNombres"),
    @NamedQuery(name = "SbRegistroUsuario.findByAdministradoTipoDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.administradoTipoDoc = :administradoTipoDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findByAdministradoNroDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.administradoNroDoc = :administradoNroDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findByRepLegalNombres", query = "SELECT s FROM SbRegistroUsuario s WHERE s.repLegalNombres = :repLegalNombres"),
    @NamedQuery(name = "SbRegistroUsuario.findByRepLegalTipoDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.repLegalTipoDoc = :repLegalTipoDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findByRepLegalNroDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.repLegalNroDoc = :repLegalNroDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findByEmailAcceso", query = "SELECT s FROM SbRegistroUsuario s WHERE s.emailAcceso = :emailAcceso"),
    @NamedQuery(name = "SbRegistroUsuario.findByUsuario", query = "SELECT s FROM SbRegistroUsuario s WHERE s.usuario = :usuario"),
    @NamedQuery(name = "SbRegistroUsuario.findByTipoSolicitante", query = "SELECT s FROM SbRegistroUsuario s WHERE s.tipoSolicitante = :tipoSolicitante"),
    @NamedQuery(name = "SbRegistroUsuario.findBySolicitanteNombres", query = "SELECT s FROM SbRegistroUsuario s WHERE s.solicitanteNombres = :solicitanteNombres"),
    @NamedQuery(name = "SbRegistroUsuario.findBySolicitanteTipoDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.solicitanteTipoDoc = :solicitanteTipoDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findBySolicitanteNroDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.solicitanteNroDoc = :solicitanteNroDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findByFechaDoc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.fechaDoc = :fechaDoc"),
    @NamedQuery(name = "SbRegistroUsuario.findByFechaPrc", query = "SELECT s FROM SbRegistroUsuario s WHERE s.fechaPrc = :fechaPrc"),
    @NamedQuery(name = "SbRegistroUsuario.findByObservacion", query = "SELECT s FROM SbRegistroUsuario s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SbRegistroUsuario.findByActivo", query = "SELECT s FROM SbRegistroUsuario s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbRegistroUsuario.findByAudLogin", query = "SELECT s FROM SbRegistroUsuario s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbRegistroUsuario.findByAudNumIp", query = "SELECT s FROM SbRegistroUsuario s WHERE s.audNumIp = :audNumIp")})
public class SbRegistroUsuario implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_REGISTRO_USUARIO")
    @SequenceGenerator(name = "SEQ_SB_REGISTRO_USUARIO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_REGISTRO_USUARIO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "ADMINISTRADO_NOMBRES")
    private String administradoNombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ADMINISTRADO_TIPO_DOC")
    private String administradoTipoDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ADMINISTRADO_NRO_DOC")
    private String administradoNroDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "REP_LEGAL_NOMBRES")
    private String repLegalNombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "REP_LEGAL_TIPO_DOC")
    private String repLegalTipoDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "REP_LEGAL_NRO_DOC")
    private String repLegalNroDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "EMAIL_ACCESO")
    private String emailAcceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "USUARIO")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "TIPO_SOLICITANTE")
    private String tipoSolicitante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "SOLICITANTE_NOMBRES")
    private String solicitanteNombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SOLICITANTE_TIPO_DOC")
    private String solicitanteTipoDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SOLICITANTE_NRO_DOC")
    private String solicitanteNroDoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DOC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_PRC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrc;
    @Size(max = 200)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioId;

    public SbRegistroUsuario() {
    }

    public SbRegistroUsuario(Long id) {
        this.id = id;
    }

    public SbRegistroUsuario(Long id, String administradoNombres, String administradoTipoDoc, String administradoNroDoc, String repLegalNombres, String repLegalTipoDoc, String repLegalNroDoc, String emailAcceso, String usuario, String tipoSolicitante, String solicitanteNombres, String solicitanteTipoDoc, String solicitanteNroDoc, Date fechaDoc, Date fechaPrc, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.administradoNombres = administradoNombres;
        this.administradoTipoDoc = administradoTipoDoc;
        this.administradoNroDoc = administradoNroDoc;
        this.repLegalNombres = repLegalNombres;
        this.repLegalTipoDoc = repLegalTipoDoc;
        this.repLegalNroDoc = repLegalNroDoc;
        this.emailAcceso = emailAcceso;
        this.usuario = usuario;
        this.tipoSolicitante = tipoSolicitante;
        this.solicitanteNombres = solicitanteNombres;
        this.solicitanteTipoDoc = solicitanteTipoDoc;
        this.solicitanteNroDoc = solicitanteNroDoc;
        this.fechaDoc = fechaDoc;
        this.fechaPrc = fechaPrc;
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

    public String getAdministradoNombres() {
        return administradoNombres;
    }

    public void setAdministradoNombres(String administradoNombres) {
        this.administradoNombres = administradoNombres;
    }

    public String getAdministradoTipoDoc() {
        return administradoTipoDoc;
    }

    public void setAdministradoTipoDoc(String administradoTipoDoc) {
        this.administradoTipoDoc = administradoTipoDoc;
    }

    public String getAdministradoNroDoc() {
        return administradoNroDoc;
    }

    public void setAdministradoNroDoc(String administradoNroDoc) {
        this.administradoNroDoc = administradoNroDoc;
    }

    public String getRepLegalNombres() {
        return repLegalNombres;
    }

    public void setRepLegalNombres(String repLegalNombres) {
        this.repLegalNombres = repLegalNombres;
    }

    public String getRepLegalTipoDoc() {
        return repLegalTipoDoc;
    }

    public void setRepLegalTipoDoc(String repLegalTipoDoc) {
        this.repLegalTipoDoc = repLegalTipoDoc;
    }

    public String getRepLegalNroDoc() {
        return repLegalNroDoc;
    }

    public void setRepLegalNroDoc(String repLegalNroDoc) {
        this.repLegalNroDoc = repLegalNroDoc;
    }

    public String getEmailAcceso() {
        return emailAcceso;
    }

    public void setEmailAcceso(String emailAcceso) {
        this.emailAcceso = emailAcceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipoSolicitante() {
        return tipoSolicitante;
    }

    public void setTipoSolicitante(String tipoSolicitante) {
        this.tipoSolicitante = tipoSolicitante;
    }

    public String getSolicitanteNombres() {
        return solicitanteNombres;
    }

    public void setSolicitanteNombres(String solicitanteNombres) {
        this.solicitanteNombres = solicitanteNombres;
    }

    public String getSolicitanteTipoDoc() {
        return solicitanteTipoDoc;
    }

    public void setSolicitanteTipoDoc(String solicitanteTipoDoc) {
        this.solicitanteTipoDoc = solicitanteTipoDoc;
    }

    public String getSolicitanteNroDoc() {
        return solicitanteNroDoc;
    }

    public void setSolicitanteNroDoc(String solicitanteNroDoc) {
        this.solicitanteNroDoc = solicitanteNroDoc;
    }

    public Date getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(Date fechaDoc) {
        this.fechaDoc = fechaDoc;
    }

    public Date getFechaPrc() {
        return fechaPrc;
    }

    public void setFechaPrc(Date fechaPrc) {
        this.fechaPrc = fechaPrc;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
        if (!(object instanceof SbRegistroUsuario)) {
            return false;
        }
        SbRegistroUsuario other = (SbRegistroUsuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbRegistroUsuario[ id=" + id + " ]";
    }
    
}
