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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "SB_PREGUNTA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbPregunta.findAll", query = "SELECT s FROM SbPregunta s"),
    @NamedQuery(name = "SbPregunta.findById", query = "SELECT s FROM SbPregunta s WHERE s.id = :id"),
    @NamedQuery(name = "SbPregunta.findByDescripcion", query = "SELECT s FROM SbPregunta s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbPregunta.findByCondicionRespuesta", query = "SELECT s FROM SbPregunta s WHERE s.condicionRespuesta = :condicionRespuesta"),
    @NamedQuery(name = "SbPregunta.findByActivo", query = "SELECT s FROM SbPregunta s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbPregunta.findByAudLogin", query = "SELECT s FROM SbPregunta s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbPregunta.findByAudNumIp", query = "SELECT s FROM SbPregunta s WHERE s.audNumIp = :audNumIp")})
public class SbPregunta implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PREGUNTA")
    @SequenceGenerator(name = "SEQ_SB_PREGUNTA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PREGUNTA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_RESPUESTA")
    private short condicionRespuesta;
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
    @ManyToMany(mappedBy = "sbPreguntaList")
    private List<SbAlternativa> sbAlternativaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntaId")
    private List<SbRespuesta> sbRespuestaList;
    @JoinColumn(name = "ENCUESTA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbEncuesta encuestaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipoId;
    @JoinColumn(name = "GRUPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase grupoId;

    public SbPregunta() {
    }

    public SbPregunta(Long id) {
        this.id = id;
    }

    public SbPregunta(Long id, String descripcion, short condicionRespuesta, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.descripcion = descripcion;
        this.condicionRespuesta = condicionRespuesta;
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

    public short getCondicionRespuesta() {
        return condicionRespuesta;
    }

    public void setCondicionRespuesta(short condicionRespuesta) {
        this.condicionRespuesta = condicionRespuesta;
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
    public List<SbAlternativa> getSbAlternativaList() {
        return sbAlternativaList;
    }

    public void setSbAlternativaList(List<SbAlternativa> sbAlternativaList) {
        this.sbAlternativaList = sbAlternativaList;
    }

    @XmlTransient
    public List<SbRespuesta> getSbRespuestaList() {
        return sbRespuestaList;
    }

    public void setSbRespuestaList(List<SbRespuesta> sbRespuestaList) {
        this.sbRespuestaList = sbRespuestaList;
    }

    public SbEncuesta getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(SbEncuesta encuestaId) {
        this.encuestaId = encuestaId;
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
        if (!(object instanceof SbPregunta)) {
            return false;
        }
        SbPregunta other = (SbPregunta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.encuesta.data.SbPregunta[ id=" + id + " ]";
    }

    public TipoBase getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(TipoBase grupoId) {
        this.grupoId = grupoId;
    }
    
}
