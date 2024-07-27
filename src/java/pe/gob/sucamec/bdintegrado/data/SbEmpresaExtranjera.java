/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbPais;
import pe.gob.sucamec.sistemabase.data.SbTipo;

/**
 *
 * @author lbartolo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_EMPRESA_EXTRANJERA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbEmpresaExtranjera.findAll", query = "SELECT s FROM SbEmpresaExtranjera s"),
    @NamedQuery(name = "SbEmpresaExtranjera.findById", query = "SELECT s FROM SbEmpresaExtranjera s WHERE s.id = :id"),
    @NamedQuery(name = "SbEmpresaExtranjera.findByNumDoc", query = "SELECT s FROM SbEmpresaExtranjera s WHERE s.numDoc = :numDoc"),
    @NamedQuery(name = "SbEmpresaExtranjera.findByDenominacion", query = "SELECT s FROM SbEmpresaExtranjera s WHERE s.denominacion = :denominacion"),
    @NamedQuery(name = "SbEmpresaExtranjera.findByActivo", query = "SELECT s FROM SbEmpresaExtranjera s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbEmpresaExtranjera.findByAudLogin", query = "SELECT s FROM SbEmpresaExtranjera s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbEmpresaExtranjera.findByAudNumIp", query = "SELECT s FROM SbEmpresaExtranjera s WHERE s.audNumIp = :audNumIp")})
public class SbEmpresaExtranjera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_EMPRESA_EXTRANJERA")
    @SequenceGenerator(name = "SEQ_SB_EMPRESA_EXTRANJERA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_EMPRESA_EXTRANJERA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "NUM_DOC")
    private String numDoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DENOMINACION")
    private String denominacion;
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
    @JoinColumn(name = "PAIS_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPaisGt paisId;
    @JoinColumn(name = "TIPO_DOC", referencedColumnName = "ID")
    @ManyToOne
    private TipoBaseGt tipoDoc;

    public SbEmpresaExtranjera() {
    }

    public SbEmpresaExtranjera(Long id) {
        this.id = id;
    }

    public SbEmpresaExtranjera(Long id, String denominacion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.denominacion = denominacion;
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

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
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

    public SbPaisGt getPaisId() {
        return paisId;
    }

    public void setPaisId(SbPaisGt paisId) {
        this.paisId = paisId;
    }

    public TipoBaseGt getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoBaseGt tipoDoc) {
        this.tipoDoc = tipoDoc;
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
        if (!(object instanceof SbEmpresaExtranjera)) {
            return false;
        }
        SbEmpresaExtranjera other = (SbEmpresaExtranjera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.bean.SbEmpresaExtranjera[ id=" + id + " ]";
    }
    
}
