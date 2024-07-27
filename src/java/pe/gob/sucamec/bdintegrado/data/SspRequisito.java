/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_REQUISITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRequisito.findAll", query = "SELECT s FROM SspRequisito s"),
    @NamedQuery(name = "SspRequisito.findById", query = "SELECT s FROM SspRequisito s WHERE s.id = :id"),
    @NamedQuery(name = "SspRequisito.findByTipoRequisitoId", query = "SELECT s FROM SspRequisito s WHERE s.tipoRequisitoId = :tipoRequisitoId"),
    @NamedQuery(name = "SspRequisito.findByValorRequisito", query = "SELECT s FROM SspRequisito s WHERE s.valorRequisito = :valorRequisito"),
    @NamedQuery(name = "SspRequisito.findByFecha", query = "SELECT s FROM SspRequisito s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspRequisito.findByVerificacion", query = "SELECT s FROM SspRequisito s WHERE s.verificacion = :verificacion"),
    @NamedQuery(name = "SspRequisito.findByActivo", query = "SELECT s FROM SspRequisito s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspRequisito.findByAudLogin", query = "SELECT s FROM SspRequisito s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspRequisito.findByAudNumIp", query = "SELECT s FROM SspRequisito s WHERE s.audNumIp = :audNumIp")})
public class SspRequisito implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REQUISITO")
    @SequenceGenerator(name = "SEQ_SSP_REQUISITO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REQUISITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "VALOR_REQUISITO")
    private String valorRequisito;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "VERIFICACION")
    private Short verificacion;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requisitoId")
    private List<SspArchivo> sspArchivoList;
    @JoinColumn(name = "TIPO_REQUISITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoRequisitoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;

    public SspRequisito() {
    }

    public SspRequisito(Long id) {
        this.id = id;
    }

    public SspRequisito(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getValorRequisito() {
        return valorRequisito;
    }

    public void setValorRequisito(String valorRequisito) {
        this.valorRequisito = valorRequisito;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Short getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(Short verificacion) {
        this.verificacion = verificacion;
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

    @XmlTransient
    public List<SspArchivo> getSspArchivoList() {
        return sspArchivoList;
    }

    public void setSspArchivoList(List<SspArchivo> sspArchivoList) {
        this.sspArchivoList = sspArchivoList;
    }

    public TipoSeguridad getTipoRequisitoId() {
        return tipoRequisitoId;
    }

    public void setTipoRequisitoId(TipoSeguridad tipoRequisitoId) {
        this.tipoRequisitoId = tipoRequisitoId;
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof SspRequisito)) {
            return false;
        }
        SspRequisito other = (SspRequisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspRequisito[ id=" + id + " ]";
    }
    
}
