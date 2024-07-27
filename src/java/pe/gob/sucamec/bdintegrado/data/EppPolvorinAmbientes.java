/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_POLVORIN_AMBIENTES", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppPolvorinAmbientes.findAll", query = "SELECT e FROM EppPolvorinAmbientes e"),
    @NamedQuery(name = "EppPolvorinAmbientes.findById", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.id = :id"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByLargo", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.largo = :largo"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByAncho", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.ancho = :ancho"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByAlto", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.alto = :alto"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByArea", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.area = :area"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByVolumen", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.volumen = :volumen"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByCapacidad", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.capacidad = :capacidad"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByDiametro", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.diametro = :diametro"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByFondoConico", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.fondoConico = :fondoConico"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByActivo", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByAudLogin", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppPolvorinAmbientes.findByAudNumIp", query = "SELECT e FROM EppPolvorinAmbientes e WHERE e.audNumIp = :audNumIp")})
public class EppPolvorinAmbientes implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_POLVORIN_AMBIENTES")
    @SequenceGenerator(name = "SEQ_EPP_POLVORIN_AMBIENTES", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_POLVORIN_AMBIENTES", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LARGO")
    private Double largo;
    @Column(name = "ANCHO")
    private Double ancho;
    @Column(name = "ALTO")
    private Double alto;
    @Column(name = "AREA")
    private Double area;
    @Column(name = "VOLUMEN")
    private Double volumen;
    @Column(name = "CAPACIDAD")
    private Double capacidad;
    @Column(name = "DIAMETRO")
    private Double diametro;
    @Column(name = "FONDO_CONICO")
    private Double fondoConico;
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
    @JoinColumn(name = "TIPO_EXPLOSIVO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoExplosivo;
    @JoinColumn(name = "INSPECCION_POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppInspeccionPolvorin inspeccionPolvorinId;

    public EppPolvorinAmbientes() {
    }

    public EppPolvorinAmbientes(Long id) {
        this.id = id;
    }

    public EppPolvorinAmbientes(Long id, short activo, String audLogin, String audNumIp) {
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

    public Double getLargo() {
        return largo;
    }

    public void setLargo(Double largo) {
        this.largo = largo;
    }

    public Double getAncho() {
        return ancho;
    }

    public void setAncho(Double ancho) {
        this.ancho = ancho;
    }

    public Double getAlto() {
        return alto;
    }

    public void setAlto(Double alto) {
        this.alto = alto;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Double getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Double capacidad) {
        this.capacidad = capacidad;
    }

    public Double getDiametro() {
        return diametro;
    }

    public void setDiametro(Double diametro) {
        this.diametro = diametro;
    }

    public Double getFondoConico() {
        return fondoConico;
    }

    public void setFondoConico(Double fondoConico) {
        this.fondoConico = fondoConico;
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

    public TipoExplosivoGt getTipoExplosivo() {
        return tipoExplosivo;
    }

    public void setTipoExplosivo(TipoExplosivoGt tipoExplosivo) {
        this.tipoExplosivo = tipoExplosivo;
    }

    public EppInspeccionPolvorin getInspeccionPolvorinId() {
        return inspeccionPolvorinId;
    }

    public void setInspeccionPolvorinId(EppInspeccionPolvorin inspeccionPolvorinId) {
        this.inspeccionPolvorinId = inspeccionPolvorinId;
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
        if (!(object instanceof EppPolvorinAmbientes)) {
            return false;
        }
        EppPolvorinAmbientes other = (EppPolvorinAmbientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppPolvorinAmbientes[ id=" + id + " ]";
    }
    
}
