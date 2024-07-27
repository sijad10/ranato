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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbPersona;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_GUIA_TRANSITO_PIRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppGuiaTransitoPiro.findAll", query = "SELECT e FROM EppGuiaTransitoPiro e"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findById", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.id = :id"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByResolucionId", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.resolucionId = :resolucionId"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByFechaSalida", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByFechaLlegada", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.fechaLlegada = :fechaLlegada"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByNroFactura", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.nroFactura = :nroFactura"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByFechaFactura", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.fechaFactura = :fechaFactura"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByActivo", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByAudLogin", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppGuiaTransitoPiro.findByAudNumIp", query = "SELECT e FROM EppGuiaTransitoPiro e WHERE e.audNumIp = :audNumIp")})
public class EppGuiaTransitoPiro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_GUIA_TRANSITO_PIRO")
    @SequenceGenerator(name = "SEQ_EPP_GUIA_TRANSITO_PIRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_GUIA_TRANSITO_PIRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "RESOLUCION_ID")
    private Long resolucionId;
    @Column(name = "FECHA_SALIDA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;
    @Column(name = "FECHA_LLEGADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLlegada;
    @Size(max = 50)
    @Column(name = "NRO_FACTURA")
    private String nroFactura;
    @Column(name = "FECHA_FACTURA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppDetalleGtransPiro> eppDetalleGtransPiroList;
    @JoinColumn(name = "TIPO_GUIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoGuiaId;
    @JoinColumn(name = "TIPO_TRANSPORTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoTransporteId;
    @JoinColumn(name = "EMPRESA_TRANSPORTE_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersona empresaTransporteId;
    @JoinColumn(name = "ORIG_ALMACEN_ADUA_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero origAlmacenAduaId;
    @JoinColumn(name = "DEST_ALMACEN_ADUA_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero destAlmacenAduaId;
    @JoinColumn(name = "CONDUCTOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCarne conductorId;
    @JoinColumn(name = "GUIA_TRANSITO_VEHICULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppGuiaTransitoVehiculo guiaTransitoVehiculoId;
    @JoinColumn(name = "ORIG_LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppLocal origLocalId;
    @JoinColumn(name = "DEST_LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppLocal destLocalId;
    @JoinColumn(name = "ORIG_PUERTO_ADUA_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero origPuertoAduaId;
    @JoinColumn(name = "DEST_PUERTO_ADUA_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppPuertoAduanero destPuertoAduaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "ORIG_TALLDEPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppTallerdeposito origTalldepoId;
    @JoinColumn(name = "DEST_TALLDEPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppTallerdeposito destTalldepoId;
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona personaId;

    public EppGuiaTransitoPiro() {
    }

    public EppGuiaTransitoPiro(Long id) {
        this.id = id;
    }

    public EppGuiaTransitoPiro(Long id, short activo, String audLogin, String audNumIp) {
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

    public Long getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(Long resolucionId) {
        this.resolucionId = resolucionId;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(Date fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public String getNroFactura() {
        return nroFactura;
    }

    public void setNroFactura(String nroFactura) {
        this.nroFactura = nroFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
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
    public List<EppDetalleGtransPiro> getEppDetalleGtransPiroList() {
        return eppDetalleGtransPiroList;
    }

    public void setEppDetalleGtransPiroList(List<EppDetalleGtransPiro> eppDetalleGtransPiroList) {
        this.eppDetalleGtransPiroList = eppDetalleGtransPiroList;
    }

    public TipoExplosivoGt getTipoGuiaId() {
        return tipoGuiaId;
    }

    public void setTipoGuiaId(TipoExplosivoGt tipoGuiaId) {
        this.tipoGuiaId = tipoGuiaId;
    }

    public TipoExplosivoGt getTipoTransporteId() {
        return tipoTransporteId;
    }

    public void setTipoTransporteId(TipoExplosivoGt tipoTransporteId) {
        this.tipoTransporteId = tipoTransporteId;
    }

    public SbPersona getEmpresaTransporteId() {
        return empresaTransporteId;
    }

    public void setEmpresaTransporteId(SbPersona empresaTransporteId) {
        this.empresaTransporteId = empresaTransporteId;
    }

    public EppAlmacenAduanero getOrigAlmacenAduaId() {
        return origAlmacenAduaId;
    }

    public void setOrigAlmacenAduaId(EppAlmacenAduanero origAlmacenAduaId) {
        this.origAlmacenAduaId = origAlmacenAduaId;
    }

    public EppAlmacenAduanero getDestAlmacenAduaId() {
        return destAlmacenAduaId;
    }

    public void setDestAlmacenAduaId(EppAlmacenAduanero destAlmacenAduaId) {
        this.destAlmacenAduaId = destAlmacenAduaId;
    }

    public EppCarne getConductorId() {
        return conductorId;
    }

    public void setConductorId(EppCarne conductorId) {
        this.conductorId = conductorId;
    }

    public EppGuiaTransitoVehiculo getGuiaTransitoVehiculoId() {
        return guiaTransitoVehiculoId;
    }

    public void setGuiaTransitoVehiculoId(EppGuiaTransitoVehiculo guiaTransitoVehiculoId) {
        this.guiaTransitoVehiculoId = guiaTransitoVehiculoId;
    }

    public EppLocal getOrigLocalId() {
        return origLocalId;
    }

    public void setOrigLocalId(EppLocal origLocalId) {
        this.origLocalId = origLocalId;
    }

    public EppLocal getDestLocalId() {
        return destLocalId;
    }

    public void setDestLocalId(EppLocal destLocalId) {
        this.destLocalId = destLocalId;
    }

    public EppPuertoAduanero getOrigPuertoAduaId() {
        return origPuertoAduaId;
    }

    public void setOrigPuertoAduaId(EppPuertoAduanero origPuertoAduaId) {
        this.origPuertoAduaId = origPuertoAduaId;
    }

    public EppPuertoAduanero getDestPuertoAduaId() {
        return destPuertoAduaId;
    }

    public void setDestPuertoAduaId(EppPuertoAduanero destPuertoAduaId) {
        this.destPuertoAduaId = destPuertoAduaId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppTallerdeposito getOrigTalldepoId() {
        return origTalldepoId;
    }

    public void setOrigTalldepoId(EppTallerdeposito origTalldepoId) {
        this.origTalldepoId = origTalldepoId;
    }

    public EppTallerdeposito getDestTalldepoId() {
        return destTalldepoId;
    }

    public void setDestTalldepoId(EppTallerdeposito destTalldepoId) {
        this.destTalldepoId = destTalldepoId;
    }

    public SbPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersona personaId) {
        this.personaId = personaId;
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
        if (!(object instanceof EppGuiaTransitoPiro)) {
            return false;
        }
        EppGuiaTransitoPiro other = (EppGuiaTransitoPiro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppGuiaTransitoPiro[ id=" + id + " ]";
    }
    
}
