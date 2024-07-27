/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "EPP_LIBRO_SALDO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroSaldo.findAll", query = "SELECT e FROM EppLibroSaldo e"),
    @NamedQuery(name = "EppLibroSaldo.findById", query = "SELECT e FROM EppLibroSaldo e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroSaldo.findByFechaRegistro", query = "SELECT e FROM EppLibroSaldo e WHERE e.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "EppLibroSaldo.findBySaldoInicial", query = "SELECT e FROM EppLibroSaldo e WHERE e.saldoInicial = :saldoInicial"),
    @NamedQuery(name = "EppLibroSaldo.findBySaldoActual", query = "SELECT e FROM EppLibroSaldo e WHERE e.saldoActual = :saldoActual"),
    @NamedQuery(name = "EppLibroSaldo.findByActivo", query = "SELECT e FROM EppLibroSaldo e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroSaldo.findByAudLogin", query = "SELECT e FROM EppLibroSaldo e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroSaldo.findByAudNumIp", query = "SELECT e FROM EppLibroSaldo e WHERE e.audNumIp = :audNumIp")})
public class EppLibroSaldo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_INICIAL")
    private BigDecimal saldoInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SALDO_ACTUAL")
    private BigDecimal saldoActual;
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
    @JoinTable(name = "EPP_LIBRO_SALDO_MANIPULADORES", joinColumns = {
        @JoinColumn(name = "LIBRO_SALDO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "LICENCIA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppLicencia> eppLicenciaList;
    @JoinColumn(name = "TIPO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoRegistroId;
    @JoinColumn(name = "GUIA_TRANSITO_ID", referencedColumnName = "ID")
    @ManyToOne
    private EppRegistroGuiaTransito guiaTransitoId;
    @JoinColumn(name = "LIBRO_POLVORIN_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibro libroPolvorinId;
    @JoinColumn(name = "EXPLOSIVO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppExplosivo explosivoId;

    public EppLibroSaldo() {
    }

    public EppLibroSaldo(Long id) {
        this.id = id;
    }

    public EppLibroSaldo(Long id, Date fechaRegistro, BigDecimal saldoInicial, BigDecimal saldoActual, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
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
    public List<EppLicencia> getEppLicenciaList() {
        return eppLicenciaList;
    }

    public void setEppLicenciaList(List<EppLicencia> eppLicenciaList) {
        this.eppLicenciaList = eppLicenciaList;
    }

    public TipoExplosivoGt getTipoRegistroId() {
        return tipoRegistroId;
    }

    public void setTipoRegistroId(TipoExplosivoGt tipoRegistroId) {
        this.tipoRegistroId = tipoRegistroId;
    }

    public EppRegistroGuiaTransito getGuiaTransitoId() {
        return guiaTransitoId;
    }

    public void setGuiaTransitoId(EppRegistroGuiaTransito guiaTransitoId) {
        this.guiaTransitoId = guiaTransitoId;
    }

    public EppLibro getLibroPolvorinId() {
        return libroPolvorinId;
    }

    public void setLibroPolvorinId(EppLibro libroPolvorinId) {
        this.libroPolvorinId = libroPolvorinId;
    }

    public EppExplosivo getExplosivoId() {
        return explosivoId;
    }

    public void setExplosivoId(EppExplosivo explosivoId) {
        this.explosivoId = explosivoId;
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
        if (!(object instanceof EppLibroSaldo)) {
            return false;
        }
        EppLibroSaldo other = (EppLibroSaldo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroSaldo[ id=" + id + " ]";
    }

}
