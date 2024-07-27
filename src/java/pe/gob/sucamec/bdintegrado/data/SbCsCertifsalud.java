/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;

/**
 *
 * @author rfernandezv
 */
@Entity
@Table(name = "SB_CS_CERTIFSALUD", catalog = "", schema = "BDINTEGRADO")
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsCertifsalud.findAll", query = "SELECT s FROM SbCsCertifsalud s"),
    @NamedQuery(name = "SbCsCertifsalud.findById", query = "SELECT s FROM SbCsCertifsalud s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsCertifsalud.findByNumero", query = "SELECT s FROM SbCsCertifsalud s WHERE s.numero = :numero"),
    @NamedQuery(name = "SbCsCertifsalud.findByFechaEmision", query = "SELECT s FROM SbCsCertifsalud s WHERE s.fechaEmision = :fechaEmision"),
    @NamedQuery(name = "SbCsCertifsalud.findByFechaVencimiento", query = "SELECT s FROM SbCsCertifsalud s WHERE s.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "SbCsCertifsalud.findByNroHistClinica", query = "SELECT s FROM SbCsCertifsalud s WHERE s.nroHistClinica = :nroHistClinica"),
    @NamedQuery(name = "SbCsCertifsalud.findByActivo", query = "SELECT s FROM SbCsCertifsalud s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsCertifsalud.findByAudLogin", query = "SELECT s FROM SbCsCertifsalud s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsCertifsalud.findByAudNumIp", query = "SELECT s FROM SbCsCertifsalud s WHERE s.audNumIp = :audNumIp")})
public class SbCsCertifsalud implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_CERTIFSALUD")
    @SequenceGenerator(name = "SEQ_SB_CS_CERTIFSALUD", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_CERTIFSALUD", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NUMERO")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_EMISION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Size(max = 10)
    @Column(name = "NRO_HIST_CLINICA")
    private String nroHistClinica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "FECHA_PRESENTACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
    @Size(max = 50)
    @Column(name = "RUTA_DOCUMENTO")
    private String rutaDocumento;
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
    @JoinColumn(name = "ESTADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt estadoId;
    @JoinColumn(name = "TIPO_CERTIFICADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoCertificadoId;
    @JoinColumn(name = "CONDICION_OBTENIDA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt condicionObtenidaId;
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioId;
    @JoinColumn(name = "SOLICITANTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt solicitanteId;
    @JoinColumn(name = "ESTABLECIMIENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsEstablecimiento establecimientoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certifmedicoId")
    private List<SbCsCertifMedico> sbCsCertifMedicoList;
    @JoinColumn(name = "CERTIFICADO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbCsCertifsalud certificadoId;
    @Size(max = 100)
    @Column(name = "ESPECIE_VALORADA")
    private String especieValorada;
    

    public SbCsCertifsalud() {
    }

    public SbCsCertifsalud(Long id) {
        this.id = id;
    }

    public SbCsCertifsalud(Long id, String numero, Date fechaEmision, Date fechaVencimiento, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNroHistClinica() {
        return nroHistClinica;
    }

    public void setNroHistClinica(String nroHistClinica) {
        this.nroHistClinica = nroHistClinica;
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

    public TipoBaseGt getTipoCertificadoId() {
        return tipoCertificadoId;
    }

    public void setTipoCertificadoId(TipoBaseGt tipoCertificadoId) {
        this.tipoCertificadoId = tipoCertificadoId;
    }

    public TipoBaseGt getCondicionObtenidaId() {
        return condicionObtenidaId;
    }

    public void setCondicionObtenidaId(TipoBaseGt condicionObtenidaId) {
        this.condicionObtenidaId = condicionObtenidaId;
    }

    public SbUsuarioGt getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(SbUsuarioGt usuarioId) {
        this.usuarioId = usuarioId;
    }

    public SbPersonaGt getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(SbPersonaGt solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public SbCsEstablecimiento getEstablecimientoId() {
        return establecimientoId;
    }

    public void setEstablecimientoId(SbCsEstablecimiento establecimientoId) {
        this.establecimientoId = establecimientoId;
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
        if (!(object instanceof SbCsCertifsalud)) {
            return false;
        }
        SbCsCertifsalud other = (SbCsCertifsalud) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsCertifsalud[ id=" + id + " ]";
    }
    
    public TipoBaseGt getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(TipoBaseGt estadoId) {
        this.estadoId = estadoId;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }    
    
    @XmlTransient
    public List<SbCsCertifMedico> getSbCsCertifMedicoList() {
        return sbCsCertifMedicoList;
    }

    public void setSbCsCertifMedicoList(List<SbCsCertifMedico> sbCsCertifMedicoList) {
        this.sbCsCertifMedicoList = sbCsCertifMedicoList;
    }
    
    public SbCsCertifsalud getCertificadoId() {
        return certificadoId;
    }

    public void setCertificadoId(SbCsCertifsalud certificadoId) {
        this.certificadoId = certificadoId;
    }
    
    public String getEspecieValorada() {
        return especieValorada;
    }

    public void setEspecieValorada(String especieValorada) {
        this.especieValorada = especieValorada;
    }
}
