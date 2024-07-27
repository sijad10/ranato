/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.encuesta.data;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_ALTERNATIVA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbAlternativa.findAll", query = "SELECT s FROM SbAlternativa s"),
    @NamedQuery(name = "SbAlternativa.findById", query = "SELECT s FROM SbAlternativa s WHERE s.id = :id"),
    @NamedQuery(name = "SbAlternativa.findByDescripcion", query = "SELECT s FROM SbAlternativa s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbAlternativa.findByActivo", query = "SELECT s FROM SbAlternativa s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbAlternativa.findByAudLogin", query = "SELECT s FROM SbAlternativa s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbAlternativa.findByAudNumIp", query = "SELECT s FROM SbAlternativa s WHERE s.audNumIp = :audNumIp")})
public class SbAlternativa implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_ALTERNATIVA")
    @SequenceGenerator(name = "SEQ_SB_ALTERNATIVA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_ALTERNATIVA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "DESCRIPCION")
    private String descripcion;
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
    @JoinTable(schema = "BDINTEGRADO", name = "SB_ALTERNATIVA_PREGUNTA", joinColumns = {
        @JoinColumn(name = "ALTERNATIVA_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "PREGUNTA_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<SbPregunta> sbPreguntaList;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoId;

    public SbAlternativa() {
    }

    public SbAlternativa(Long id) {
        this.id = id;
    }

    public SbAlternativa(Long id, String descripcion, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
    public List<SbPregunta> getSbPreguntaList() {
        return sbPreguntaList;
    }

    public void setSbPreguntaList(List<SbPregunta> sbPreguntaList) {
        this.sbPreguntaList = sbPreguntaList;
    }

    public TipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBase tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof SbAlternativa)) {
            return false;
        }
        SbAlternativa other = (SbAlternativa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.encuesta.data.SbAlternativa[ id=" + id + " ]";
    }
    
}
