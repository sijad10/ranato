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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_TALLERDEPOSITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppTallerdeposito.findAll", query = "SELECT e FROM EppTallerdeposito e"),
    @NamedQuery(name = "EppTallerdeposito.findById", query = "SELECT e FROM EppTallerdeposito e WHERE e.id = :id"),
    @NamedQuery(name = "EppTallerdeposito.findByCapacidadAlm", query = "SELECT e FROM EppTallerdeposito e WHERE e.capacidadAlm = :capacidadAlm"),
    @NamedQuery(name = "EppTallerdeposito.findByNroAmbientes", query = "SELECT e FROM EppTallerdeposito e WHERE e.nroAmbientes = :nroAmbientes"),
    @NamedQuery(name = "EppTallerdeposito.findByActivo", query = "SELECT e FROM EppTallerdeposito e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppTallerdeposito.findByAudLogin", query = "SELECT e FROM EppTallerdeposito e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppTallerdeposito.findByAudNumIp", query = "SELECT e FROM EppTallerdeposito e WHERE e.audNumIp = :audNumIp")})
public class EppTallerdeposito implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_TALLERDEPOSITO")
    @SequenceGenerator(name = "SEQ_EPP_TALLERDEPOSITO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_TALLERDEPOSITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAPACIDAD_ALM")
    private Double capacidadAlm;
    @Column(name = "NRO_AMBIENTES")
    private Short nroAmbientes;
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
    @ManyToMany(mappedBy = "eppTallerdepositoList")
    private List<EppRegistro> eppRegistroList;
    @OneToMany(mappedBy = "origTalldepoId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList;
    @OneToMany(mappedBy = "destTalldepoId")
    private List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "depositoId")
    private List<EppDepositoContrato> eppDepositoContratoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "depositoId")
    private List<EppDepositoAmbientes> eppDepositoAmbientesList;
    @JoinColumn(name = "TIPO_LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoLocalId;
    @JoinColumn(name = "CLASIFICACION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt clasificacionId;
    @JoinColumn(name = "TIPO_USO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoUsoId;
    @JoinColumn(name = "TIPO_PRODPIRO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoProdpiroId;
    @JoinColumn(name = "TIPO_ZONA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoZonaId;
    @JoinColumn(name = "COND_INMUEBLE_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt condInmuebleId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLocal localId;
    @OneToMany(mappedBy = "verifDepositoId")
    private List<EppInternaSalida> eppInternaSalidaList;
    @OneToMany(mappedBy = "destOrigTalldepoId")
    private List<EppInternaSalida> eppInternaSalidaList1;
    @OneToMany(mappedBy = "tallerdepositoId")
    private List<EppPiroproyectado> eppPiroproyectadoList;
    @JoinColumn(name = "ADMINISTRADOR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppCarne administradorId;

    public EppTallerdeposito() {
    }

    public EppTallerdeposito(Long id) {
        this.id = id;
    }

    public EppTallerdeposito(Long id, Double capacidadAlm, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.capacidadAlm = capacidadAlm;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public EppCarne getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(EppCarne administradorId) {
        this.administradorId = administradorId;
    }

    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCapacidadAlm() {
        return capacidadAlm;
    }

    public void setCapacidadAlm(Double capacidadAlm) {
        this.capacidadAlm = capacidadAlm;
    }

    public Short getNroAmbientes() {
        return nroAmbientes;
    }

    public void setNroAmbientes(Short nroAmbientes) {
        this.nroAmbientes = nroAmbientes;
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
    public List<EppRegistro> getEppRegistroList() {
        return eppRegistroList;
    }

    public void setEppRegistroList(List<EppRegistro> eppRegistroList) {
        this.eppRegistroList = eppRegistroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList() {
        return eppGuiaTransitoPiroList;
    }

    public void setEppGuiaTransitoPiroList(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList) {
        this.eppGuiaTransitoPiroList = eppGuiaTransitoPiroList;
    }

    @XmlTransient
    public List<EppGuiaTransitoPiro> getEppGuiaTransitoPiroList1() {
        return eppGuiaTransitoPiroList1;
    }

    public void setEppGuiaTransitoPiroList1(List<EppGuiaTransitoPiro> eppGuiaTransitoPiroList1) {
        this.eppGuiaTransitoPiroList1 = eppGuiaTransitoPiroList1;
    }

    @XmlTransient
    public List<EppDepositoContrato> getEppDepositoContratoList() {
        return eppDepositoContratoList;
    }

    public void setEppDepositoContratoList(List<EppDepositoContrato> eppDepositoContratoList) {
        this.eppDepositoContratoList = eppDepositoContratoList;
    }

    @XmlTransient
    public List<EppDepositoAmbientes> getEppDepositoAmbientesList() {
        return eppDepositoAmbientesList;
    }

    public void setEppDepositoAmbientesList(List<EppDepositoAmbientes> eppDepositoAmbientesList) {
        this.eppDepositoAmbientesList = eppDepositoAmbientesList;
    }

    public TipoExplosivoGt getTipoLocalId() {
        return tipoLocalId;
    }

    public void setTipoLocalId(TipoExplosivoGt tipoLocalId) {
        this.tipoLocalId = tipoLocalId;
    }

    public TipoExplosivoGt getClasificacionId() {
        return clasificacionId;
    }

    public void setClasificacionId(TipoExplosivoGt clasificacionId) {
        this.clasificacionId = clasificacionId;
    }

    public TipoExplosivoGt getTipoUsoId() {
        return tipoUsoId;
    }

    public void setTipoUsoId(TipoExplosivoGt tipoUsoId) {
        this.tipoUsoId = tipoUsoId;
    }

    public TipoExplosivoGt getTipoProdpiroId() {
        return tipoProdpiroId;
    }

    public void setTipoProdpiroId(TipoExplosivoGt tipoProdpiroId) {
        this.tipoProdpiroId = tipoProdpiroId;
    }

    public TipoExplosivoGt getTipoZonaId() {
        return tipoZonaId;
    }

    public void setTipoZonaId(TipoExplosivoGt tipoZonaId) {
        this.tipoZonaId = tipoZonaId;
    }

    public TipoExplosivoGt getCondInmuebleId() {
        return condInmuebleId;
    }

    public void setCondInmuebleId(TipoExplosivoGt condInmuebleId) {
        this.condInmuebleId = condInmuebleId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppLocal getLocalId() {
        return localId;
    }

    public void setLocalId(EppLocal localId) {
        this.localId = localId;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList() {
        return eppInternaSalidaList;
    }

    public void setEppInternaSalidaList(List<EppInternaSalida> eppInternaSalidaList) {
        this.eppInternaSalidaList = eppInternaSalidaList;
    }

    @XmlTransient
    public List<EppInternaSalida> getEppInternaSalidaList1() {
        return eppInternaSalidaList1;
    }

    public void setEppInternaSalidaList1(List<EppInternaSalida> eppInternaSalidaList1) {
        this.eppInternaSalidaList1 = eppInternaSalidaList1;
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
        if (!(object instanceof EppTallerdeposito)) {
            return false;
        }
        EppTallerdeposito other = (EppTallerdeposito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppTallerdeposito[ id=" + id + " ]";
    }

}
