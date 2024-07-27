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
 * @author mpalomino
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_AGENCIA_FINANCIERA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspAgenciaFinanciera.findAll", query = "SELECT s FROM SspAgenciaFinanciera s"),
    @NamedQuery(name = "SspAgenciaFinanciera.findById", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.id = :id"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByCodigoOficina", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.codigoOficina = :codigoOficina"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByNonbreOficina", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.nonbreOficina = :nonbreOficina"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByDistritoId", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.distritoId = :distritoId"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByDireccion", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.direccion = :direccion"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByReferencia", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.referencia = :referencia"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByLatitud", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.latitud = :latitud"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByLongitud", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.longitud = :longitud"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByActivo", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByAudLogin", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspAgenciaFinanciera.findByAudNumIp", query = "SELECT s FROM SspAgenciaFinanciera s WHERE s.audNumIp = :audNumIp")})
public class SspAgenciaFinanciera implements Serializable {

    
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_AGENCIA_FINANCIERA")
    @SequenceGenerator(name = "SEQ_SSP_AGENCIA_FINANCIERA", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_AGENCIA_FINANCIERA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 20)
    @Column(name = "CODIGO_OFICINA")
    private String codigoOficina;
    @Size(max = 20)
    @Column(name = "NONBRE_OFICINA")
    private String nonbreOficina;
    @Basic(optional = false)
    @Size(max = 30)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 200)
    @Column(name = "REFERENCIA")
    private String referencia;
    @Size(max = 20)
    @Column(name = "LATITUD")
    private String latitud;
    @Size(max = 20)
    @Column(name = "LONGITUD")
    private String longitud;
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
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;
    @JoinColumn(name = "DISTRITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistritoGt distritoId;
    @JoinColumn(name = "TIPO_LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoLocalId;
    
    @Size(max = 20)
    @Column(name = "NUMERO_RS_SBS")
    private String numeroRsSbs;
    @Column(name = "FECHA_RS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRs;
    @Size(max = 20)
    @Column(name = "ARCHIVO_RS")
    private String archivoRs;
    @Size(max = 20)
    @Column(name = "ARCHIVO_MATRIZ")
    private String archivoMatriz;
    @Column(name = "PRIORIZACION")
    private Short priorizacion;


    public SspAgenciaFinanciera() {
    }

    public SspAgenciaFinanciera(Long id) {
        this.id = id;
    }

    public SspAgenciaFinanciera(Long id, long distritoId, short activo, String audLogin, String audNumIp) {
        this.id = id;
        //this.distritoId = distritoId;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        //this.sspRegistroId = sspRegistroId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(String codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getNonbreOficina() {
        return nonbreOficina;
    }

    public void setNonbreOficina(String nonbreOficina) {
        this.nonbreOficina = nonbreOficina;
    }

    public SbDistritoGt getDistritoId() {
        return distritoId;
    }

    public void setDistritoId(SbDistritoGt distritoId) {
        this.distritoId = distritoId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
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

    public TipoSeguridad getTipoLocalId() {
        return tipoLocalId;
    }

    public void setTipoLocalId(TipoSeguridad tipoLocalId) {
        this.tipoLocalId = tipoLocalId;
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
        if (!(object instanceof SspAgenciaFinanciera)) {
            return false;
        }
        SspAgenciaFinanciera other = (SspAgenciaFinanciera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspAgenciaFinanciera[ id=" + id + " ]";
    }

    public String getNumeroRsSbs() {
        return numeroRsSbs;
    }

    public void setNumeroRsSbs(String numeroRsSbs) {
        this.numeroRsSbs = numeroRsSbs;
    }

    public Date getFechaRs() {
        return fechaRs;
    }

    public void setFechaRs(Date fechaRs) {
        this.fechaRs = fechaRs;
    }

    public String getArchivoRs() {
        return archivoRs;
    }

    public void setArchivoRs(String archivoRs) {
        this.archivoRs = archivoRs;
    }

    public String getArchivoMatriz() {
        return archivoMatriz;
    }

    public void setArchivoMatriz(String archivoMatriz) {
        this.archivoMatriz = archivoMatriz;
    }

    public Short getPriorizacion() {
        return priorizacion;
    }

    public void setPriorizacion(Short priorizacion) {
        this.priorizacion = priorizacion;
    }
    
}
