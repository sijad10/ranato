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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.SbPais;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_REGISTRO_INT_SAL", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppRegistroIntSal.findAll", query = "SELECT e FROM EppRegistroIntSal e"),
    @NamedQuery(name = "EppRegistroIntSal.findById", query = "SELECT e FROM EppRegistroIntSal e WHERE e.id = :id"),
    @NamedQuery(name = "EppRegistroIntSal.findByGuiaTransito", query = "SELECT e FROM EppRegistroIntSal e WHERE e.guiaTransito = :guiaTransito"),
    @NamedQuery(name = "EppRegistroIntSal.findByInformacionAdicionalDuaDam", query = "SELECT e FROM EppRegistroIntSal e WHERE e.informacionAdicionalDuaDam = :informacionAdicionalDuaDam"),
    @NamedQuery(name = "EppRegistroIntSal.findByOtroRegimen", query = "SELECT e FROM EppRegistroIntSal e WHERE e.otroRegimen = :otroRegimen"),
    @NamedQuery(name = "EppRegistroIntSal.findByAudLogin", query = "SELECT e FROM EppRegistroIntSal e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppRegistroIntSal.findByAudNumIp", query = "SELECT e FROM EppRegistroIntSal e WHERE e.audNumIp = :audNumIp"),
    @NamedQuery(name = "EppRegistroIntSal.findByActivo", query = "SELECT e FROM EppRegistroIntSal e WHERE e.activo = :activo")})
public class EppRegistroIntSal implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_REGISTRO_INT_SAL")
    @SequenceGenerator(name = "SEQ_EPP_REGISTRO_INT_SAL", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_REGISTRO_INT_SAL", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "GUIA_TRANSITO")
    private String guiaTransito;
    @Size(max = 500)
    @Column(name = "INFORMACION_ADICIONAL_DUA_DAM")
    private String informacionAdicionalDuaDam;
    @Size(max = 50)
    @Column(name = "OTRO_REGIMEN")
    private String otroRegimen;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt viaId;
    @JoinColumn(name = "TIPO_DESPACHO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoDespachoId;
    @JoinColumn(name = "ALM_DEST_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacen almDestId;
    @JoinColumn(name = "ALM_INSPEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacen almInspecId;
    @JoinColumn(name = "ALM_ADU_INSPEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero almAduInspecId;
    @JoinColumn(name = "ALM_ADU_DEST_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero almAduDestId;
    @JoinColumn(name = "POLV_INSPEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin polvInspecId;
    @JoinColumn(name = "POLV_DEST_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppPolvorin polvDestId;
    @JoinColumn(name = "PROVEEDOR_CLIENTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppProveedorCliente proveedorClienteId;
    @JoinColumn(name = "PUERTO_ADUANERO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppPuertoAduanero puertoAduaneroId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "PAIS_ORIG_DEST_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPais paisOrigDestId;
    @JoinColumn(name = "TIPO_INTENDENCIA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoIntendenciaId;
    @JoinColumn(name = "TIPO_REGIMEN_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoRegimenId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppDetalleIntSal> eppDetalleIntSalList;

    public EppRegistroIntSal() {
    }

    public EppRegistroIntSal(Long id) {
        this.id = id;
    }

    public EppRegistroIntSal(Long id, String guiaTransito, String audLogin, String audNumIp, short activo) {
        this.id = id;
        this.guiaTransito = guiaTransito;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuiaTransito() {
        return guiaTransito;
    }

    public void setGuiaTransito(String guiaTransito) {
        this.guiaTransito = guiaTransito;
    }

    public String getInformacionAdicionalDuaDam() {
        return informacionAdicionalDuaDam;
    }

    public void setInformacionAdicionalDuaDam(String informacionAdicionalDuaDam) {
        this.informacionAdicionalDuaDam = informacionAdicionalDuaDam;
    }

    public String getOtroRegimen() {
        return otroRegimen;
    }

    public void setOtroRegimen(String otroRegimen) {
        this.otroRegimen = otroRegimen;
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

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public TipoExplosivoGt getViaId() {
        return viaId;
    }

    public void setViaId(TipoExplosivoGt viaId) {
        this.viaId = viaId;
    }

    public TipoExplosivoGt getTipoDespachoId() {
        return tipoDespachoId;
    }

    public void setTipoDespachoId(TipoExplosivoGt tipoDespachoId) {
        this.tipoDespachoId = tipoDespachoId;
    }

    public EppAlmacen getAlmDestId() {
        return almDestId;
    }

    public void setAlmDestId(EppAlmacen almDestId) {
        this.almDestId = almDestId;
    }

    public EppAlmacen getAlmInspecId() {
        return almInspecId;
    }

    public void setAlmInspecId(EppAlmacen almInspecId) {
        this.almInspecId = almInspecId;
    }

    public EppAlmacenAduanero getAlmAduInspecId() {
        return almAduInspecId;
    }

    public void setAlmAduInspecId(EppAlmacenAduanero almAduInspecId) {
        this.almAduInspecId = almAduInspecId;
    }

    public EppAlmacenAduanero getAlmAduDestId() {
        return almAduDestId;
    }

    public void setAlmAduDestId(EppAlmacenAduanero almAduDestId) {
        this.almAduDestId = almAduDestId;
    }

    public EppPolvorin getPolvInspecId() {
        return polvInspecId;
    }

    public void setPolvInspecId(EppPolvorin polvInspecId) {
        this.polvInspecId = polvInspecId;
    }

    public EppPolvorin getPolvDestId() {
        return polvDestId;
    }

    public void setPolvDestId(EppPolvorin polvDestId) {
        this.polvDestId = polvDestId;
    }

    public EppProveedorCliente getProveedorClienteId() {
        return proveedorClienteId;
    }

    public void setProveedorClienteId(EppProveedorCliente proveedorClienteId) {
        this.proveedorClienteId = proveedorClienteId;
    }

    public EppPuertoAduanero getPuertoAduaneroId() {
        return puertoAduaneroId;
    }

    public void setPuertoAduaneroId(EppPuertoAduanero puertoAduaneroId) {
        this.puertoAduaneroId = puertoAduaneroId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public SbPais getPaisOrigDestId() {
        return paisOrigDestId;
    }

    public void setPaisOrigDestId(SbPais paisOrigDestId) {
        this.paisOrigDestId = paisOrigDestId;
    }

    public TipoExplosivoGt getTipoIntendenciaId() {
        return tipoIntendenciaId;
    }

    public void setTipoIntendenciaId(TipoExplosivoGt tipoIntendenciaId) {
        this.tipoIntendenciaId = tipoIntendenciaId;
    }

    public TipoExplosivoGt getTipoRegimenId() {
        return tipoRegimenId;
    }

    public void setTipoRegimenId(TipoExplosivoGt tipoRegimenId) {
        this.tipoRegimenId = tipoRegimenId;
    }

    @XmlTransient
    public List<EppDetalleIntSal> getEppDetalleIntSalList() {
        return eppDetalleIntSalList;
    }

    public void setEppDetalleIntSalList(List<EppDetalleIntSal> eppDetalleIntSalList) {
        this.eppDetalleIntSalList = eppDetalleIntSalList;
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
        if (!(object instanceof EppRegistroIntSal)) {
            return false;
        }
        EppRegistroIntSal other = (EppRegistroIntSal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppRegistroIntSal[ id=" + id + " ]";
    }
    
}
