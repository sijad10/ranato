/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_PROCESO_TUPA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbProcesoTupa.findAll", query = "SELECT s FROM SbProcesoTupa s"),
    @NamedQuery(name = "SbProcesoTupa.findById", query = "SELECT s FROM SbProcesoTupa s WHERE s.id = :id"),
    @NamedQuery(name = "SbProcesoTupa.findByVigencia", query = "SELECT s FROM SbProcesoTupa s WHERE s.vigencia = :vigencia"),
    @NamedQuery(name = "SbProcesoTupa.findByCydocIdProceso", query = "SELECT s FROM SbProcesoTupa s WHERE s.cydocIdProceso = :cydocIdProceso"),
    @NamedQuery(name = "SbProcesoTupa.findByVigente", query = "SELECT s FROM SbProcesoTupa s WHERE s.vigente = :vigente"),
    @NamedQuery(name = "SbProcesoTupa.findByActivo", query = "SELECT s FROM SbProcesoTupa s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbProcesoTupa.findByAudLogin", query = "SELECT s FROM SbProcesoTupa s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbProcesoTupa.findByAudNumIp", query = "SELECT s FROM SbProcesoTupa s WHERE s.audNumIp = :audNumIp")})
public class SbProcesoTupa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Column(name = "VIGENCIA")
    private Integer vigencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CYDOC_ID_PROCESO")
    private int cydocIdProceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENTE")
    private short vigente;
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
    @JoinColumn(name = "TIPO_OPERACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoOperacionId;
    @JoinColumn(name = "PROCESO_INTEGRADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt procesoIntegradoId;
    @JoinColumn(name = "TUPA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tupaId;
    @JoinColumn(name = "TIPO_VIGENCIA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoVigenciaId;
    @Column(name = "COD_TRIBUTO")
    private Long codTributo;    
    @Column(name = "IMPORTE")
    private Double importe;
    @JoinColumn(name = "PROCESO_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbProcesoTupa procesoId;

    public SbProcesoTupa() {
    }

    public SbProcesoTupa(Long id) {
        this.id = id;
    }

    public SbProcesoTupa(Long id, int cydocIdProceso, short vigente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.cydocIdProceso = cydocIdProceso;
        this.vigente = vigente;
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

    public Integer getVigencia() {
        return vigencia;
    }

    public void setVigencia(Integer vigencia) {
        this.vigencia = vigencia;
    }

    public int getCydocIdProceso() {
        return cydocIdProceso;
    }

    public void setCydocIdProceso(int cydocIdProceso) {
        this.cydocIdProceso = cydocIdProceso;
    }

    public short getVigente() {
        return vigente;
    }

    public void setVigente(short vigente) {
        this.vigente = vigente;
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

    public TipoBaseGt getTipoOperacionId() {
        return tipoOperacionId;
    }

    public void setTipoOperacionId(TipoBaseGt tipoOperacionId) {
        this.tipoOperacionId = tipoOperacionId;
    }

    public TipoBaseGt getProcesoIntegradoId() {
        return procesoIntegradoId;
    }

    public void setProcesoIntegradoId(TipoBaseGt procesoIntegradoId) {
        this.procesoIntegradoId = procesoIntegradoId;
    }

    public TipoBaseGt getTupaId() {
        return tupaId;
    }

    public void setTupaId(TipoBaseGt tupaId) {
        this.tupaId = tupaId;
    }

    public TipoBaseGt getTipoVigenciaId() {
        return tipoVigenciaId;
    }

    public void setTipoVigenciaId(TipoBaseGt tipoVigenciaId) {
        this.tipoVigenciaId = tipoVigenciaId;
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
        if (!(object instanceof SbProcesoTupa)) {
            return false;
        }
        SbProcesoTupa other = (SbProcesoTupa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbProcesoTupa[ id=" + id + " ]";
    }

    public Long getCodTributo() {
        return codTributo;
    }

    public void setCodTributo(Long codTributo) {
        this.codTributo = codTributo;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public SbProcesoTupa getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(SbProcesoTupa procesoId) {
        this.procesoId = procesoId;
    }

    
}
