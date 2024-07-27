/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import pe.gob.sucamec.sistemabase.data.SbDireccion;

/**
 *
 * @author locador192.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INGSAL_TEM_DEF",catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaIngsalTemDef.findAll", query = "SELECT a FROM AmaIngsalTemDef a"),
    @NamedQuery(name = "AmaIngsalTemDef.findById", query = "SELECT a FROM AmaIngsalTemDef a WHERE a.id = :id"),
    @NamedQuery(name = "AmaIngsalTemDef.findByNroPartidapresIpd", query = "SELECT a FROM AmaIngsalTemDef a WHERE a.nroPartidapresIpd = :nroPartidapresIpd"),
    @NamedQuery(name = "AmaIngsalTemDef.findByActivo", query = "SELECT a FROM AmaIngsalTemDef a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaIngsalTemDef.findByAudLogin", query = "SELECT a FROM AmaIngsalTemDef a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaIngsalTemDef.findByAudNumIp", query = "SELECT a FROM AmaIngsalTemDef a WHERE a.audNumIp = :audNumIp")})
public class AmaIngsalTemDef implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INGSAL_TEM_DEF")
    @SequenceGenerator(name = "SEQ_AMA_INGSAL_TEM_DEF", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INGSAL_TEM_DEF", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 30)
    @Column(name = "NRO_PARTIDAPRES_IPD")
    private String nroPartidapresIpd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsaltemdefId")
    private List<AmaIngsaltdRutaPersona> amaIngsaltdRutaPersonaList;
    @JoinColumn(name = "TIPO_INGSAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoIngsalId;
    @JoinColumn(name = "TIPO_USO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoGamac tipoUsoId;
    @JoinColumn(name = "ALMACEN_SUCAMEC_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbDireccion almacenSucamecId;
    @JoinColumn(name = "RESOLUCION_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private AmaResolucion resolucionId;
    @JoinColumn(name = "RG_FUNCIONA_FDT_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaResolucion rgFuncionaFdtId;
    @JoinColumn(name = "EVENTO_DIP_DEP_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaEventoDiploDepor eventoDipDepId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ingsaltemdefId")
    private List<AmaDelegacionPais> amaDelegacionPaisList;

    public AmaIngsalTemDef() {
    }

    public AmaIngsalTemDef(Long id) {
        this.id = id;
    }

    public AmaIngsalTemDef(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getNroPartidapresIpd() {
        return nroPartidapresIpd;
    }

    public void setNroPartidapresIpd(String nroPartidapresIpd) {
        this.nroPartidapresIpd = nroPartidapresIpd;
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
    public List<AmaIngsaltdRutaPersona> getAmaIngsaltdRutaPersonaList() {
        return amaIngsaltdRutaPersonaList;
    }

    public void setAmaIngsaltdRutaPersonaList(List<AmaIngsaltdRutaPersona> amaIngsaltdRutaPersonaList) {
        this.amaIngsaltdRutaPersonaList = amaIngsaltdRutaPersonaList;
    }

    public TipoGamac getTipoIngsalId() {
        return tipoIngsalId;
    }

    public void setTipoIngsalId(TipoGamac tipoIngsalId) {
        this.tipoIngsalId = tipoIngsalId;
    }

    public TipoGamac getTipoUsoId() {
        return tipoUsoId;
    }

    public void setTipoUsoId(TipoGamac tipoUsoId) {
        this.tipoUsoId = tipoUsoId;
    }

    public SbDireccion getAlmacenSucamecId() {
        return almacenSucamecId;
    }

    public void setAlmacenSucamecId(SbDireccion almacenSucamecId) {
        this.almacenSucamecId = almacenSucamecId;
    }

    public AmaResolucion getResolucionId() {
        return resolucionId;
    }

    public void setResolucionId(AmaResolucion resolucionId) {
        this.resolucionId = resolucionId;
    }

    public AmaResolucion getRgFuncionaFdtId() {
        return rgFuncionaFdtId;
    }

    public void setRgFuncionaFdtId(AmaResolucion rgFuncionaFdtId) {
        this.rgFuncionaFdtId = rgFuncionaFdtId;
    }

    public AmaEventoDiploDepor getEventoDipDepId() {
        return eventoDipDepId;
    }

    public void setEventoDipDepId(AmaEventoDiploDepor eventoDipDepId) {
        this.eventoDipDepId = eventoDipDepId;
    }

    @XmlTransient
    public List<AmaDelegacionPais> getAmaDelegacionPaisList() {
        return amaDelegacionPaisList;
    }

    public void setAmaDelegacionPaisList(List<AmaDelegacionPais> amaDelegacionPaisList) {
        this.amaDelegacionPaisList = amaDelegacionPaisList;
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
        if (!(object instanceof AmaIngsalTemDef)) {
            return false;
        }
        AmaIngsalTemDef other = (AmaIngsalTemDef) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaIngsalTemDef[ id=" + id + " ]";
    }
    
}
