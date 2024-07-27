/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.epp.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbDistrito;

/**
 *
 * @author rmoscoso
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LUGAR_USO_UBIGEO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LugarUsoUbigeo.findAll", query = "SELECT l FROM LugarUsoUbigeo l"),
    @NamedQuery(name = "LugarUsoUbigeo.findById", query = "SELECT l FROM LugarUsoUbigeo l WHERE l.id = :id"),
    @NamedQuery(name = "LugarUsoUbigeo.findByDptoId", query = "SELECT l FROM LugarUsoUbigeo l WHERE l.dptoId = :dptoId"),
    @NamedQuery(name = "LugarUsoUbigeo.findByProvId", query = "SELECT l FROM LugarUsoUbigeo l WHERE l.provId = :provId"),
    @NamedQuery(name = "LugarUsoUbigeo.findByActivo", query = "SELECT l FROM LugarUsoUbigeo l WHERE l.activo = :activo"),
    @NamedQuery(name = "LugarUsoUbigeo.findByAudLogin", query = "SELECT l FROM LugarUsoUbigeo l WHERE l.audLogin = :audLogin"),
    @NamedQuery(name = "LugarUsoUbigeo.findByAudNumIp", query = "SELECT l FROM LugarUsoUbigeo l WHERE l.audNumIp = :audNumIp")})
public class LugarUsoUbigeo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LUGAR_USO_UBIGEO")
    @SequenceGenerator(name = "SEQ_EPP_LUGAR_USO_UBIGEO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LUGAR_USO_UBIGEO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DPTO_ID")
    private Long dptoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROV_ID")
    private Long provId;
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
    @OneToMany(mappedBy = "lugarUsoUbigeoDestino")
    private List<RegistroGuiaTransito> registroGuiaTransitoList;
    @OneToMany(mappedBy = "lugarUsoUbigeoOrigen")
    private List<RegistroGuiaTransito> registroGuiaTransitoList1;
    @JoinColumn(name = "DIST_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDistrito distId;
    @JoinColumn(name = "LUGAR_USO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private LugarUso lugarUsoId;

    public LugarUsoUbigeo() {
    }

    public LugarUsoUbigeo(Long id) {
        this.id = id;
    }

    public LugarUsoUbigeo(Long id, Long dptoId, Long provId, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.dptoId = dptoId;
        this.provId = provId;
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

    public Long getDptoId() {
        return dptoId;
    }

    public void setDptoId(Long dptoId) {
        this.dptoId = dptoId;
    }

    public Long getProvId() {
        return provId;
    }

    public void setProvId(Long provId) {
        this.provId = provId;
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
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList() {
        return registroGuiaTransitoList;
    }

    public void setRegistroGuiaTransitoList(List<RegistroGuiaTransito> registroGuiaTransitoList) {
        this.registroGuiaTransitoList = registroGuiaTransitoList;
    }

    @XmlTransient
    public List<RegistroGuiaTransito> getRegistroGuiaTransitoList1() {
        return registroGuiaTransitoList1;
    }

    public void setRegistroGuiaTransitoList1(List<RegistroGuiaTransito> registroGuiaTransitoList1) {
        this.registroGuiaTransitoList1 = registroGuiaTransitoList1;
    }

    public SbDistrito getDistId() {
        return distId;
    }

    public void setDistId(SbDistrito distId) {
        this.distId = distId;
    }

    public LugarUso getLugarUsoId() {
        return lugarUsoId;
    }

    public void setLugarUsoId(LugarUso lugarUsoId) {
        this.lugarUsoId = lugarUsoId;
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
        if (!(object instanceof LugarUsoUbigeo)) {
            return false;
        }
        LugarUsoUbigeo other = (LugarUsoUbigeo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.epp.data.LugarUsoUbigeo[ id=" + id + " ]";
    }
    
}
