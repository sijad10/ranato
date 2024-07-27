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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_NOMBRECOMERCIAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppNombrecomercial.findAll", query = "SELECT e FROM EppNombrecomercial e"),
    @NamedQuery(name = "EppNombrecomercial.findById", query = "SELECT e FROM EppNombrecomercial e WHERE e.id = :id"),
    @NamedQuery(name = "EppNombrecomercial.findByNombre", query = "SELECT e FROM EppNombrecomercial e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppNombrecomercial.findByActivo", query = "SELECT e FROM EppNombrecomercial e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppNombrecomercial.findByAudLogin", query = "SELECT e FROM EppNombrecomercial e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppNombrecomercial.findByAudNumIp", query = "SELECT e FROM EppNombrecomercial e WHERE e.audNumIp = :audNumIp")})
public class EppNombrecomercial implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_NOMBRECOMERCIAL")
    @SequenceGenerator(name = "SEQ_EPP_NOMBRECOMERCIAL", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_NOMBRECOMERCIAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @ManyToMany(mappedBy = "eppNombrecomercialList")
    private List<EppComercializacion> eppComercializacionList;
    @JoinTable(schema="BDINTEGRADO", name = "EPP_NOMBRECOMERCIAL_UM", joinColumns = {
        @JoinColumn(name = "NOMBRE_COMERCIAL_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "UNIDAD_MEDIDA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<UnidadMedida> unidadMedidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pironomcomId")
    private List<EppDetalleGtransPiro> eppDetalleGtransPiroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pironomcomId")
    private List<EppImpExpPirotecnico> eppImpExpPirotecnicoList;
    @JoinColumn(name = "PIROTECNICO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppPirotecnico pirotecnicoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pironomcomId")
    private List<EppIntsalPirotecnico> eppIntsalPirotecnicoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pironomcomId")
    private List<EppEspectaculoPirotec> eppEspectaculoPirotecList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pironomcomId")
    private List<EppPiroproyectado> eppPiroproyectadoList;

    public EppNombrecomercial() {
    }

    public EppNombrecomercial(Long id) {
        this.id = id;
    }

    public EppNombrecomercial(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    public List<EppComercializacion> getEppComercializacionList() {
        return eppComercializacionList;
    }

    public void setEppComercializacionList(List<EppComercializacion> eppComercializacionList) {
        this.eppComercializacionList = eppComercializacionList;
    }

    @XmlTransient
    public List<UnidadMedida> getUnidadMedidaList() {
        return unidadMedidaList;
    }

    public void setUnidadMedidaList(List<UnidadMedida> unidadMedidaList) {
        this.unidadMedidaList = unidadMedidaList;
    }

    @XmlTransient
    public List<EppDetalleGtransPiro> getEppDetalleGtransPiroList() {
        return eppDetalleGtransPiroList;
    }

    public void setEppDetalleGtransPiroList(List<EppDetalleGtransPiro> eppDetalleGtransPiroList) {
        this.eppDetalleGtransPiroList = eppDetalleGtransPiroList;
    }

    @XmlTransient
    public List<EppImpExpPirotecnico> getEppImpExpPirotecnicoList() {
        return eppImpExpPirotecnicoList;
    }

    public void setEppImpExpPirotecnicoList(List<EppImpExpPirotecnico> eppImpExpPirotecnicoList) {
        this.eppImpExpPirotecnicoList = eppImpExpPirotecnicoList;
    }

    public EppPirotecnico getPirotecnicoId() {
        return pirotecnicoId;
    }

    public void setPirotecnicoId(EppPirotecnico pirotecnicoId) {
        this.pirotecnicoId = pirotecnicoId;
    }

    @XmlTransient
    public List<EppIntsalPirotecnico> getEppIntsalPirotecnicoList() {
        return eppIntsalPirotecnicoList;
    }

    public void setEppIntsalPirotecnicoList(List<EppIntsalPirotecnico> eppIntsalPirotecnicoList) {
        this.eppIntsalPirotecnicoList = eppIntsalPirotecnicoList;
    }

    @XmlTransient
    public List<EppEspectaculoPirotec> getEppEspectaculoPirotecList() {
        return eppEspectaculoPirotecList;
    }

    public void setEppEspectaculoPirotecList(List<EppEspectaculoPirotec> eppEspectaculoPirotecList) {
        this.eppEspectaculoPirotecList = eppEspectaculoPirotecList;
    }

    @XmlTransient
    public List<EppPiroproyectado> getEppPiroproyectadoList() {
        return eppPiroproyectadoList;
    }

    public void setEppPiroproyectadoList(List<EppPiroproyectado> eppPiroproyectadoList) {
        this.eppPiroproyectadoList = eppPiroproyectadoList;
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
        if (!(object instanceof EppNombrecomercial)) {
            return false;
        }
        EppNombrecomercial other = (EppNombrecomercial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppNombrecomercial[ id=" + id + " ]";
    }
    
}
