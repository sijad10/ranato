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

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_INTERNA_SALIDA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppInternaSalida.findAll", query = "SELECT e FROM EppInternaSalida e"),
    @NamedQuery(name = "EppInternaSalida.findById", query = "SELECT e FROM EppInternaSalida e WHERE e.id = :id"),
    @NamedQuery(name = "EppInternaSalida.findByCondGuiatran", query = "SELECT e FROM EppInternaSalida e WHERE e.condGuiatran = :condGuiatran"),
    @NamedQuery(name = "EppInternaSalida.findByActivo", query = "SELECT e FROM EppInternaSalida e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppInternaSalida.findByAudLogin", query = "SELECT e FROM EppInternaSalida e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppInternaSalida.findByAudNumIp", query = "SELECT e FROM EppInternaSalida e WHERE e.audNumIp = :audNumIp")})
public class EppInternaSalida implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_INTERNA_SALIDA")
    @SequenceGenerator(name = "SEQ_EPP_INTERNA_SALIDA", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_INTERNA_SALIDA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COND_GUIATRAN")
    private short condGuiatran;
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
    @JoinColumn(name = "TIPO_DESPACHO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoDespachoId;
    @JoinColumn(name = "TIPO_REGIMEN_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoExplosivoGt tipoRegimenId;
    @JoinColumn(name = "VIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt viaId;
    @JoinColumn(name = "PAIS_ORIG_DEST_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPaisGt paisOrigDestId;
    @JoinColumn(name = "VERIF_DEPOSITO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppTallerdeposito verifDepositoId;
    @JoinColumn(name = "DEST_ORIG_TALLDEPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppTallerdeposito destOrigTalldepoId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "PUERTO_ADUANERO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppPuertoAduanero puertoAduaneroId;
    @JoinColumn(name = "PROVEEDOR_CLIENTE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppProveedorCliente proveedorClienteId;
    @JoinColumn(name = "DEST_ORIG_ALM_ADU_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero destOrigAlmAduId;
    @JoinColumn(name = "VERIF_ALM_ADU_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppAlmacenAduanero verifAlmAduId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registroId")
    private List<EppIntsalPirotecnico> eppIntsalPirotecnicoList;

    public EppInternaSalida() {
    }

    public EppInternaSalida(Long id) {
        this.id = id;
    }

    public EppInternaSalida(Long id, short condGuiatran, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.condGuiatran = condGuiatran;
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

    public short getCondGuiatran() {
        return condGuiatran;
    }

    public void setCondGuiatran(short condGuiatran) {
        this.condGuiatran = condGuiatran;
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

    public TipoExplosivoGt getTipoDespachoId() {
        return tipoDespachoId;
    }

    public void setTipoDespachoId(TipoExplosivoGt tipoDespachoId) {
        this.tipoDespachoId = tipoDespachoId;
    }

    public TipoExplosivoGt getTipoRegimenId() {
        return tipoRegimenId;
    }

    public void setTipoRegimenId(TipoExplosivoGt tipoRegimenId) {
        this.tipoRegimenId = tipoRegimenId;
    }

    public TipoExplosivoGt getViaId() {
        return viaId;
    }

    public void setViaId(TipoExplosivoGt viaId) {
        this.viaId = viaId;
    }

    public SbPaisGt getPaisOrigDestId() {
        return paisOrigDestId;
    }

    public void setPaisOrigDestId(SbPaisGt paisOrigDestId) {
        this.paisOrigDestId = paisOrigDestId;
    }

    public EppTallerdeposito getVerifDepositoId() {
        return verifDepositoId;
    }

    public void setVerifDepositoId(EppTallerdeposito verifDepositoId) {
        this.verifDepositoId = verifDepositoId;
    }

    public EppTallerdeposito getDestOrigTalldepoId() {
        return destOrigTalldepoId;
    }

    public void setDestOrigTalldepoId(EppTallerdeposito destOrigTalldepoId) {
        this.destOrigTalldepoId = destOrigTalldepoId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
    }

    public EppPuertoAduanero getPuertoAduaneroId() {
        return puertoAduaneroId;
    }

    public void setPuertoAduaneroId(EppPuertoAduanero puertoAduaneroId) {
        this.puertoAduaneroId = puertoAduaneroId;
    }

    public EppProveedorCliente getProveedorClienteId() {
        return proveedorClienteId;
    }

    public void setProveedorClienteId(EppProveedorCliente proveedorClienteId) {
        this.proveedorClienteId = proveedorClienteId;
    }

    public EppAlmacenAduanero getDestOrigAlmAduId() {
        return destOrigAlmAduId;
    }

    public void setDestOrigAlmAduId(EppAlmacenAduanero destOrigAlmAduId) {
        this.destOrigAlmAduId = destOrigAlmAduId;
    }

    public EppAlmacenAduanero getVerifAlmAduId() {
        return verifAlmAduId;
    }

    public void setVerifAlmAduId(EppAlmacenAduanero verifAlmAduId) {
        this.verifAlmAduId = verifAlmAduId;
    }

    @XmlTransient
    public List<EppIntsalPirotecnico> getEppIntsalPirotecnicoList() {
        return eppIntsalPirotecnicoList;
    }

    public void setEppIntsalPirotecnicoList(List<EppIntsalPirotecnico> eppIntsalPirotecnicoList) {
        this.eppIntsalPirotecnicoList = eppIntsalPirotecnicoList;
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
        if (!(object instanceof EppInternaSalida)) {
            return false;
        }
        EppInternaSalida other = (EppInternaSalida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppInternaSalida[ id=" + id + " ]";
    }
    
}
