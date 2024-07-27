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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
@Table(name = "EPP_GUIA_TRANSITO_POLICIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppGuiaTransitoPolicia.findAll", query = "SELECT e FROM EppGuiaTransitoPolicia e"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findById", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.id = :id"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByDniPolicia", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.dniPolicia = :dniPolicia"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByNombresPolicia", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.nombresPolicia = :nombresPolicia"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByApaternoPolicia", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.apaternoPolicia = :apaternoPolicia"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByAmaternoPolicia", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.amaternoPolicia = :amaternoPolicia"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByNroPlaca", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.nroPlaca = :nroPlaca"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByGradoId", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.gradoId = :gradoId"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByUnidadPolicialId", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.unidadPolicialId = :unidadPolicialId"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByActivo", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByAudLogin", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppGuiaTransitoPolicia.findByAudNumIp", query = "SELECT e FROM EppGuiaTransitoPolicia e WHERE e.audNumIp = :audNumIp")})
public class EppGuiaTransitoPolicia implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_GUIA_TRANSITO_POLICIA")
    @SequenceGenerator(name = "SEQ_EPP_GUIA_TRANSITO_POLICIA", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_GUIA_TRANSITO_POLICIA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)

    @Column(name = "DNI_POLICIA")
    private String dniPolicia;
    @Basic(optional = false)

    
    @Column(name = "NOMBRES_POLICIA")
    private String nombresPolicia;
    @Basic(optional = false)

    
    @Column(name = "APATERNO_POLICIA")
    private String apaternoPolicia;
    @Basic(optional = false)

    
    @Column(name = "AMATERNO_POLICIA")
    private String amaternoPolicia;
    @Basic(optional = false)

    @Column(name = "NRO_PLACA")
    private Long nroPlaca;
    @Basic(optional = false)

    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)

    
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)

    
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "UNIDAD_POLICIAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt unidadPolicialId;
    @JoinColumn(name = "GRADO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt gradoId;
    @ManyToMany(mappedBy = "eppGuiaTransitoPoliciaList")
    private List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList;

    public TipoBaseGt getUnidadPolicialId() {
        return unidadPolicialId;
    }

    public void setUnidadPolicialId(TipoBaseGt unidadPolicialId) {
        this.unidadPolicialId = unidadPolicialId;
    }

    public EppGuiaTransitoPolicia() {
    }

    public EppGuiaTransitoPolicia(Long id) {
        this.id = id;
    }

    public EppGuiaTransitoPolicia(Long id,
            String dniPolicia,
            String nombresPolicia,
            String apaternoPolicia,
            String amaternoPolicia,
            Long nroPlaca,
            TipoBaseGt gradoId,
            TipoBaseGt unidadPolicialId,
            short activo,
            String audLogin,
            String audNumIp) {
        this.id = id;
        this.dniPolicia = dniPolicia;
        this.nombresPolicia = nombresPolicia;
        this.apaternoPolicia = apaternoPolicia;
        this.amaternoPolicia = amaternoPolicia;
        this.nroPlaca = nroPlaca;
        this.gradoId = gradoId;
        this.unidadPolicialId = unidadPolicialId;
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

    public String getDniPolicia() {
        return dniPolicia;
    }

    public void setDniPolicia(String dniPolicia) {
        this.dniPolicia = dniPolicia;
    }

    public String getNombresPolicia() {
        return nombresPolicia;
    }

    public void setNombresPolicia(String nombresPolicia) {
        this.nombresPolicia = nombresPolicia;
    }

    public String getApaternoPolicia() {
        return apaternoPolicia;
    }

    public void setApaternoPolicia(String apaternoPolicia) {
        this.apaternoPolicia = apaternoPolicia;
    }

    public String getAmaternoPolicia() {
        return amaternoPolicia;
    }

    public void setAmaternoPolicia(String amaternoPolicia) {
        this.amaternoPolicia = amaternoPolicia;
    }

    public Long getNroPlaca() {
        return nroPlaca;
    }

    public void setNroPlaca(Long nroPlaca) {
        this.nroPlaca = nroPlaca;
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
    public List<EppRegistroGuiaTransito> getEppRegistroGuiaTransitoList() {
        return eppRegistroGuiaTransitoList;
    }

    public void setEppRegistroGuiaTransitoList(List<EppRegistroGuiaTransito> eppRegistroGuiaTransitoList) {
        this.eppRegistroGuiaTransitoList = eppRegistroGuiaTransitoList;
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
        if (!(object instanceof EppGuiaTransitoPolicia)) {
            return false;
        }
        EppGuiaTransitoPolicia other = (EppGuiaTransitoPolicia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppGuiaTransitoPolicia[ id=" + id + " ]";
    }

    public TipoBaseGt getGradoId() {
        return gradoId;
    }

    public void setGradoId(TipoBaseGt gradoId) {
        this.gradoId = gradoId;
    }

}
