/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_REGISTRO_REG_DISCA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRegistroRegDisca.findAll", query = "SELECT s FROM SspRegistroRegDisca s"),
    @NamedQuery(name = "SspRegistroRegDisca.findById", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.id = :id"),
    @NamedQuery(name = "SspRegistroRegDisca.findByModEmp", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.modEmp = :modEmp"),
    @NamedQuery(name = "SspRegistroRegDisca.findByNroRd", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.nroRd = :nroRd"),
    @NamedQuery(name = "SspRegistroRegDisca.findByAnoRd", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.anoRd = :anoRd"),
    @NamedQuery(name = "SspRegistroRegDisca.findByFecha", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspRegistroRegDisca.findByActivo", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspRegistroRegDisca.findByAudLogin", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspRegistroRegDisca.findByAudNumIp", query = "SELECT s FROM SspRegistroRegDisca s WHERE s.audNumIp = :audNumIp")})
public class SspRegistroRegDisca implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REGISTRO_REG_DISCA")
    @SequenceGenerator(name = "SEQ_SSP_REGISTRO_REG_DISCA", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REGISTRO_REG_DISCA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @Column(name = "MOD_EMP")
    private Short modEmp;
    @Column(name = "NRO_RD")
    private Integer nroRd;
    @Column(name = "ANO_RD")
    private Short anoRd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
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
    @Size(max = 40)
    @Column(name = "ID_RESOLUCION_DISCA")
    private String idResolucionDISCA;

    public SspRegistroRegDisca() {
    }

    public SspRegistroRegDisca(Long id) {
        this.id = id;
    }

    public SspRegistroRegDisca(Long id, Date fecha, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fecha = fecha;
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

    public Short getModEmp() {
        return modEmp;
    }

    public void setModEmp(Short modEmp) {
        this.modEmp = modEmp;
    }

    public Integer getNroRd() {
        return nroRd;
    }

    public void setNroRd(Integer nroRd) {
        this.nroRd = nroRd;
    }

    public Short getAnoRd() {
        return anoRd;
    }

    public void setAnoRd(Short anoRd) {
        this.anoRd = anoRd;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
    
    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public String getIdResolucionDISCA() {
        return idResolucionDISCA;
    }

    public void setIdResolucionDISCA(String idResolucionDISCA) {
        this.idResolucionDISCA = idResolucionDISCA;
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
        if (!(object instanceof SspRegistroRegDisca)) {
            return false;
        }
        SspRegistroRegDisca other = (SspRegistroRegDisca) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspRegistroRegDisca[ id=" + id + " ]";
    }
    
}
