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

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_ESPECTACULO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppEspectaculo.findAll", query = "SELECT e FROM EppEspectaculo e"),
    @NamedQuery(name = "EppEspectaculo.findById", query = "SELECT e FROM EppEspectaculo e WHERE e.id = :id"),
    @NamedQuery(name = "EppEspectaculo.findByFechahora", query = "SELECT e FROM EppEspectaculo e WHERE e.fechahora = :fechahora"),
    @NamedQuery(name = "EppEspectaculo.findByDuracion", query = "SELECT e FROM EppEspectaculo e WHERE e.duracion = :duracion"),
    @NamedQuery(name = "EppEspectaculo.findByActivo", query = "SELECT e FROM EppEspectaculo e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppEspectaculo.findByAudLogin", query = "SELECT e FROM EppEspectaculo e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppEspectaculo.findByAudNumIp", query = "SELECT e FROM EppEspectaculo e WHERE e.audNumIp = :audNumIp")})
public class EppEspectaculo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_ESPECTACULO")
    @SequenceGenerator(name = "SEQ_EPP_ESPECTACULO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_ESPECTACULO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAHORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DURACION")
    private short duracion;
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
    @JoinColumn(name = "TIPO_PRODPIRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoProdpiroId;
    @JoinColumn(name = "TIPO_ESPECTACULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEspectaculoId;
    @JoinColumn(name = "TIPO_UBICACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoUbicacionId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLocal localId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espectaculoId")
    private List<EppEspectaculoPirotec> eppEspectaculoPirotecList;

    public EppEspectaculo() {
    }

    public EppEspectaculo(Long id) {
        this.id = id;
    }

    public EppEspectaculo(Long id, Date fechahora, short duracion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechahora = fechahora;
        this.duracion = duracion;
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

    public Date getFechahora() {
        return fechahora;
    }

    public void setFechahora(Date fechahora) {
        this.fechahora = fechahora;
    }

    public short getDuracion() {
        return duracion;
    }

    public void setDuracion(short duracion) {
        this.duracion = duracion;
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

    public TipoExplosivoGt getTipoProdpiroId() {
        return tipoProdpiroId;
    }

    public void setTipoProdpiroId(TipoExplosivoGt tipoProdpiroId) {
        this.tipoProdpiroId = tipoProdpiroId;
    }

    public TipoExplosivoGt getTipoEspectaculoId() {
        return tipoEspectaculoId;
    }

    public void setTipoEspectaculoId(TipoExplosivoGt tipoEspectaculoId) {
        this.tipoEspectaculoId = tipoEspectaculoId;
    }

    public TipoExplosivoGt getTipoUbicacionId() {
        return tipoUbicacionId;
    }

    public void setTipoUbicacionId(TipoExplosivoGt tipoUbicacionId) {
        this.tipoUbicacionId = tipoUbicacionId;
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
    public List<EppEspectaculoPirotec> getEppEspectaculoPirotecList() {
        return eppEspectaculoPirotecList;
    }

    public void setEppEspectaculoPirotecList(List<EppEspectaculoPirotec> eppEspectaculoPirotecList) {
        this.eppEspectaculoPirotecList = eppEspectaculoPirotecList;
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
        if (!(object instanceof EppEspectaculo)) {
            return false;
        }
        EppEspectaculo other = (EppEspectaculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppEspectaculo[ id=" + id + " ]";
    }
    
}
