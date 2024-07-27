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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "EPP_COMERCIALIZACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppComercializacion.findAll", query = "SELECT e FROM EppComercializacion e"),
    @NamedQuery(name = "EppComercializacion.findById", query = "SELECT e FROM EppComercializacion e WHERE e.id = :id"),
    @NamedQuery(name = "EppComercializacion.findByVigenciaSolicitada", query = "SELECT e FROM EppComercializacion e WHERE e.vigenciaSolicitada = :vigenciaSolicitada"),
    @NamedQuery(name = "EppComercializacion.findByNroModulos", query = "SELECT e FROM EppComercializacion e WHERE e.nroModulos = :nroModulos"),
    @NamedQuery(name = "EppComercializacion.findByActivo", query = "SELECT e FROM EppComercializacion e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppComercializacion.findByAudLogin", query = "SELECT e FROM EppComercializacion e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppComercializacion.findByAudNumIp", query = "SELECT e FROM EppComercializacion e WHERE e.audNumIp = :audNumIp")})
public class EppComercializacion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_COMERCIALIZACION")
    @SequenceGenerator(name = "SEQ_EPP_COMERCIALIZACION", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_COMERCIALIZACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENCIA_SOLICITADA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vigenciaSolicitada;
    @Column(name = "NRO_MODULOS")
    private Short nroModulos;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_COMERCIALI_PIROTECNICO", joinColumns = {
        @JoinColumn(name = "COMERCIALIZACION_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PIRONOMCOM_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppNombrecomercial> eppNombrecomercialList;
    @JoinColumn(name = "TIPO_DEPOSITO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoDepositoId;
    @JoinColumn(name = "TIPO_USO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoUsoId;
    @JoinColumn(name = "TIPO_PRODPIRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoProdpiroId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;

    public EppComercializacion() {
    }

    public EppComercializacion(Long id) {
        this.id = id;
    }

    public EppComercializacion(Long id, Date vigenciaSolicitada, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.vigenciaSolicitada = vigenciaSolicitada;
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

    public Date getVigenciaSolicitada() {
        return vigenciaSolicitada;
    }

    public void setVigenciaSolicitada(Date vigenciaSolicitada) {
        this.vigenciaSolicitada = vigenciaSolicitada;
    }

    public Short getNroModulos() {
        return nroModulos;
    }

    public void setNroModulos(Short nroModulos) {
        this.nroModulos = nroModulos;
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
    public List<EppNombrecomercial> getEppNombrecomercialList() {
        return eppNombrecomercialList;
    }

    public void setEppNombrecomercialList(List<EppNombrecomercial> eppNombrecomercialList) {
        this.eppNombrecomercialList = eppNombrecomercialList;
    }

    public TipoExplosivoGt getTipoDepositoId() {
        return tipoDepositoId;
    }

    public void setTipoDepositoId(TipoExplosivoGt tipoDepositoId) {
        this.tipoDepositoId = tipoDepositoId;
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

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof EppComercializacion)) {
            return false;
        }
        EppComercializacion other = (EppComercializacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppComercializacion[ id=" + id + " ]";
    }
    
}
