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
 * @author locador192.ogtic
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_DATO_ADICIONAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaDatoAdicional.findAll", query = "SELECT a FROM AmaDatoAdicional a"),
    @NamedQuery(name = "AmaDatoAdicional.findById", query = "SELECT a FROM AmaDatoAdicional a WHERE a.id = :id"),
    @NamedQuery(name = "AmaDatoAdicional.findByNombre", query = "SELECT a FROM AmaDatoAdicional a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "AmaDatoAdicional.findByAbreviatura", query = "SELECT a FROM AmaDatoAdicional a WHERE a.abreviatura = :abreviatura"),
    @NamedQuery(name = "AmaDatoAdicional.findByDescripcion", query = "SELECT a FROM AmaDatoAdicional a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AmaDatoAdicional.findByActivo", query = "SELECT a FROM AmaDatoAdicional a WHERE a.activo = :activo order by a.nombre"),
    @NamedQuery(name = "AmaDatoAdicional.findByAudLogin", query = "SELECT a FROM AmaDatoAdicional a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaDatoAdicional.findByAudNumIp", query = "SELECT a FROM AmaDatoAdicional a WHERE a.audNumIp = :audNumIp")})
public class AmaDatoAdicional implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_DATO_ADICIONAL")
    @SequenceGenerator(name = "SEQ_AMA_DATO_ADICIONAL", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_DATO_ADICIONAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 200)
    @Column(name = "ABREVIATURA")
    private String abreviatura;
    @Size(max = 200)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAdicionalId")
    private List<AmaIngsaltdMrAdic> amaIngsaltdMrAdicList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAdicionalId")
    private List<AmaIngsaltdMuniAdic> amaIngsaltdMuniAdicList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAdicionalId")
    private List<AmaIngsaltdArmaAdic> amaIngsaltdArmaAdicList;
    @JoinColumn(name = "TIPO_ARTICULO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoArticulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAdicionalId")
    private List<AmaDetalleResoArmaAdic> amaDetalleResoArmaAdicList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAdicionalId")
    private List<AmaDetalleResoMuniAdic> amaDetalleResoMuniAdicList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "datoAdicionalId")
    private List<AmaDetalleResoMrAdic> amaDetalleResoMrAdicList;

    public AmaDatoAdicional() {
    }

    public AmaDatoAdicional(Long id) {
        this.id = id;
    }

    public AmaDatoAdicional(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public List<AmaDetalleResoArmaAdic> getAmaDetalleResoArmaAdicList() {
        return amaDetalleResoArmaAdicList;
    }

    public void setAmaDetalleResoArmaAdicList(List<AmaDetalleResoArmaAdic> amaDetalleResoArmaAdicList) {
        this.amaDetalleResoArmaAdicList = amaDetalleResoArmaAdicList;
    }

    @XmlTransient
    public List<AmaDetalleResoMuniAdic> getAmaDetalleResoMuniAdicList() {
        return amaDetalleResoMuniAdicList;
    }

    public void setAmaDetalleResoMuniAdicList(List<AmaDetalleResoMuniAdic> amaDetalleResoMuniAdicList) {
        this.amaDetalleResoMuniAdicList = amaDetalleResoMuniAdicList;
    }

    @XmlTransient
    public List<AmaDetalleResoMrAdic> getAmaDetalleResoMrAdicList() {
        return amaDetalleResoMrAdicList;
    }

    public void setAmaDetalleResoMrAdicList(List<AmaDetalleResoMrAdic> amaDetalleResoMrAdicList) {
        this.amaDetalleResoMrAdicList = amaDetalleResoMrAdicList;
    }

    @XmlTransient
    public List<AmaIngsaltdMrAdic> getAmaIngsaltdMrAdicList() {
        return amaIngsaltdMrAdicList;
    }

    public void setAmaIngsaltdMrAdicList(List<AmaIngsaltdMrAdic> amaIngsaltdMrAdicList) {
        this.amaIngsaltdMrAdicList = amaIngsaltdMrAdicList;
    }

    @XmlTransient
    public List<AmaIngsaltdMuniAdic> getAmaIngsaltdMuniAdicList() {
        return amaIngsaltdMuniAdicList;
    }

    public void setAmaIngsaltdMuniAdicList(List<AmaIngsaltdMuniAdic> amaIngsaltdMuniAdicList) {
        this.amaIngsaltdMuniAdicList = amaIngsaltdMuniAdicList;
    }

    @XmlTransient
    public List<AmaIngsaltdArmaAdic> getAmaIngsaltdArmaAdicList() {
        return amaIngsaltdArmaAdicList;
    }

    public void setAmaIngsaltdArmaAdicList(List<AmaIngsaltdArmaAdic> amaIngsaltdArmaAdicList) {
        this.amaIngsaltdArmaAdicList = amaIngsaltdArmaAdicList;
    }

    public TipoGamac getTipoArticulo() {
        return tipoArticulo;
    }

    public void setTipoArticulo(TipoGamac tipoArticulo) {
        this.tipoArticulo = tipoArticulo;
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
        if (!(object instanceof AmaDatoAdicional)) {
            return false;
        }
        AmaDatoAdicional other = (AmaDatoAdicional) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaDatoAdicional[ id=" + id + " ]";
    }

}
