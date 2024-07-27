/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoBaseGt;
import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LIBRO_ADJUNTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibroAdjunto.findAll", query = "SELECT e FROM EppLibroAdjunto e"),
    @NamedQuery(name = "EppLibroAdjunto.findById", query = "SELECT e FROM EppLibroAdjunto e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibroAdjunto.findByNombre", query = "SELECT e FROM EppLibroAdjunto e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EppLibroAdjunto.findByExtension", query = "SELECT e FROM EppLibroAdjunto e WHERE e.extension = :extension"),
    @NamedQuery(name = "EppLibroAdjunto.findByActivo", query = "SELECT e FROM EppLibroAdjunto e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibroAdjunto.findByAudLogin", query = "SELECT e FROM EppLibroAdjunto e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibroAdjunto.findByAudNumIp", query = "SELECT e FROM EppLibroAdjunto e WHERE e.audNumIp = :audNumIp")})
public class EppLibroAdjunto implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO_ADJUNTO")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO_ADJUNTO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO_ADJUNTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "EXTENSION")
    private String extension;
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "LIBRO_PERDIDA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EppLibroPerdida libroPerdidaId;

    public EppLibroAdjunto() {
    }

    public EppLibroAdjunto(Long id) {
        this.id = id;
    }

    public EppLibroAdjunto(Long id, String nombre, String extension, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.extension = extension;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
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

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public EppLibroPerdida getLibroPerdidaId() {
        return libroPerdidaId;
    }

    public void setLibroPerdidaId(EppLibroPerdida libroPerdidaId) {
        this.libroPerdidaId = libroPerdidaId;
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
        if (!(object instanceof EppLibroAdjunto)) {
            return false;
        }
        EppLibroAdjunto other = (EppLibroAdjunto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibroAdjunto[ id=" + id + " ]";
    }
    
}
