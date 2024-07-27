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

/**
 *
 * @author lbartolo
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_VEHICULO_CERTIFICACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspVehiculoCertificacion.findAll", query = "SELECT s FROM SspVehiculoCertificacion s"),
    @NamedQuery(name = "SspVehiculoCertificacion.findById", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.id = :id"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByCategoria", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.categoria = :categoria"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByCarroceria", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.carroceria = :carroceria"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByPlaca", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.placa = :placa"),
    @NamedQuery(name = "SspVehiculoCertificacion.findBySerie", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.serie = :serie"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByMarca", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.marca = :marca"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByModelo", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.modelo = :modelo"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByArchPartidaRegistral", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.archPartidaRegistral = :archPartidaRegistral"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByArrendado", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.arrendado = :arrendado"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByArchContratArrenda", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.archContratArrenda = :archContratArrenda"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByActivo", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByAudLogin", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspVehiculoCertificacion.findByAudNumIp", query = "SELECT s FROM SspVehiculoCertificacion s WHERE s.audNumIp = :audNumIp")})
public class SspVehiculoCertificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_VEHICULO_CERTIFICACION")
    @SequenceGenerator(name = "SEQ_SSP_VEHICULO_CERTIFICACION", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_VEHICULO_CERTIFICACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    
    @Size(max = 20)
    @Column(name = "CATEGORIA")
    private String categoria;
    
    @Size(max = 30)
    @Column(name = "CARROCERIA")
    private String carroceria;
    
    @Size(max = 20)
    @Column(name = "PLACA")
    private String placa;
    
    @Size(max = 20)
    @Column(name = "SERIE")
    private String serie;
    
    @Size(max = 50)
    @Column(name = "MARCA")
    private String marca;
    
    @Size(max = 50)
    @Column(name = "MODELO")
    private String modelo;
    
    @JoinColumn(name = "ASIENTO_SUNARP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbAsientoSunarp asientoSunarpId;
    
    @Size(max = 200)
    @Column(name = "ARCH_PARTIDA_REGISTRAL")
    private String archPartidaRegistral;
    
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distritoId;
    
    @Column(name = "ARRENDADO")
    private Short arrendado;
    
    @Size(max = 200)
    @Column(name = "ARCH_CONTRAT_ARRENDA")
    private String archContratArrenda;
    
    @Column(name = "ACTIVO")
    private Short activo;
    
    @Size(max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    
    @Size(max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;

    public SspVehiculoCertificacion() {
    }

    public SspVehiculoCertificacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = carroceria;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getArchPartidaRegistral() {
        return archPartidaRegistral;
    }

    public void setArchPartidaRegistral(String archPartidaRegistral) {
        this.archPartidaRegistral = archPartidaRegistral;
    }

    public Short getArrendado() {
        return arrendado;
    }

    public void setArrendado(Short arrendado) {
        this.arrendado = arrendado;
    }

    public String getArchContratArrenda() {
        return archContratArrenda;
    }

    public void setArchContratArrenda(String archContratArrenda) {
        this.archContratArrenda = archContratArrenda;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SspVehiculoCertificacion)) {
            return false;
        }
        SspVehiculoCertificacion other = (SspVehiculoCertificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspVehiculoCertificacion[ id=" + id + " ]";
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
        this.registroId = registroId;
    }

    public SbAsientoSunarp getAsientoSunarpId() {
        return asientoSunarpId;
    }

    public void setAsientoSunarpId(SbAsientoSunarp asientoSunarpId) {
        this.asientoSunarpId = asientoSunarpId;
    }

    public SbDistritoGt getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistritoGt distritoId) {
        this.distritoId = distritoId;
    }
    
    
    
    
}
