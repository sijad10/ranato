/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LIBRO_DETALLE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroDetalle.findAll", query = "SELECT e FROM EppLibroDetalle e"),
    @NamedQuery(name = "EppLibroDetalle.findById", query = "SELECT e FROM EppLibroDetalle e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroDetalle.findByCantidad", query = "SELECT e FROM EppLibroDetalle e WHERE e.cantidad = :cantidad"),
    @NamedQuery(name = "EppLibroDetalle.findByNroLote", query = "SELECT e FROM EppLibroDetalle e WHERE e.nroLote = :nroLote"),
    @NamedQuery(name = "EppLibroDetalle.findByActivo", query = "SELECT e FROM EppLibroDetalle e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroDetalle.findByAudLogin", query = "SELECT e FROM EppLibroDetalle e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroDetalle.findByAudNumIp", query = "SELECT e FROM EppLibroDetalle e WHERE e.audNumIp = :audNumIp")})
public class EppLibroDetalle implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO_DETALLE")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO_DETALLE", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO_DETALLE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private Double cantidad;
    @Size(max = 20)
    @Column(name = "NRO_LOTE")
    private String nroLote;
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
    @JoinColumn(name = "TIPO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoRegistroId;
//    @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EppRegistroGuiaTransito guiaTransitoId;
    @JoinColumn(name = "REGCOMPRADOR_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistro regcompradorId;
    @JoinColumn(name = "LUGAR_USO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppLugarUso lugarUsoId;
    @JoinColumn(name = "LIBRO_USO_DIARIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibroUsoDiario libroUsoDiarioId;

    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    public EppLibroDetalle() {
    }

    public EppLibroDetalle(Long id) {
        this.id = id;
    }

    public EppLibroDetalle(Long id, Double cantidad, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cantidad = cantidad;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public Long getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    
    
    public void setId(Long id) {
        this.id = id;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getNroLote() {
        return nroLote;
    }

    public void setNroLote(String nroLote) {
        this.nroLote = nroLote;
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

    public TipoExplosivoGt getTipoRegistroId() {
        return tipoRegistroId;
    }

    public void setTipoRegistroId(TipoExplosivoGt tipoRegistroId) {
        this.tipoRegistroId = tipoRegistroId;
    }

//    public EppRegistroGuiaTransito getGuiaTransitoId() {
//        return guiaTransitoId;
//    }
//
//    public void setGuiaTransitoId(EppRegistroGuiaTransito guiaTransitoId) {
//        this.guiaTransitoId = guiaTransitoId;
//    }
    public EppRegistro getRegcompradorId() {
        return regcompradorId;
    }

    public void setRegcompradorId(EppRegistro regcompradorId) {
        this.regcompradorId = regcompradorId;
    }

    public EppLugarUso getLugarUsoId() {
        return lugarUsoId;
    }

    public void setLugarUsoId(EppLugarUso lugarUsoId) {
        this.lugarUsoId = lugarUsoId;
    }

    public EppLibroUsoDiario getLibroUsoDiarioId() {
        return libroUsoDiarioId;
    }

    public void setLibroUsoDiarioId(EppLibroUsoDiario libroUsoDiarioId) {
        this.libroUsoDiarioId = libroUsoDiarioId;
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
        if (!(object instanceof EppLibroDetalle)) {
            return false;
        }
        EppLibroDetalle other = (EppLibroDetalle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroDetalle[ id=" + id + " ]";
    }

}
