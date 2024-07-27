/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.encuesta.data.SbEncuesta;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_SISTEMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbSistema.findAll", query = "SELECT s FROM SbSistema s"),
    @NamedQuery(name = "SbSistema.findById", query = "SELECT s FROM SbSistema s WHERE s.id = :id"),
    @NamedQuery(name = "SbSistema.findByNombre", query = "SELECT s FROM SbSistema s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbSistema.findByDescripcion", query = "SELECT s FROM SbSistema s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbSistema.findByCodProg", query = "SELECT s FROM SbSistema s WHERE s.codProg = :codProg"),
    @NamedQuery(name = "SbSistema.findByActivo", query = "SELECT s FROM SbSistema s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbSistema.findByAudLogin", query = "SELECT s FROM SbSistema s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbSistema.findByAudNumIp", query = "SELECT s FROM SbSistema s WHERE s.audNumIp = :audNumIp")})
public class SbSistema implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_SISTEMA")
    @SequenceGenerator(name = "SEQ_SB_SISTEMA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_SISTEMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 1000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 10)
    @Column(name = "COD_PROG")
    private String codProg;
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
    @OneToMany(mappedBy = "sistemaId")
    private List<SbPerfil> sbPerfilList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sistemaId")
    private List<SbPagina> sbPaginaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sistemaId")
    private List<SbEncuesta> sbEncuestaList;

    public SbSistema() {
    }

    public SbSistema(Long id) {
        this.id = id;
    }

    public SbSistema(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
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
    public List<SbPerfil> getSbPerfilList() {
        return sbPerfilList;
    }

    public void setSbPerfilList(List<SbPerfil> sbPerfilList) {
        this.sbPerfilList = sbPerfilList;
    }

    @XmlTransient
    public List<SbPagina> getSbPaginaList() {
        return sbPaginaList;
    }

    public void setSbPaginaList(List<SbPagina> sbPaginaList) {
        this.sbPaginaList = sbPaginaList;
    }
    
    @XmlTransient
    public List<SbEncuesta> getSbEncuestaList() {
        return sbEncuestaList;
    }

    public void setSbEncuestaList(List<SbEncuesta> sbEncuestaList) {
        this.sbEncuestaList = sbEncuestaList;
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
        if (!(object instanceof SbSistema)) {
            return false;
        }
        SbSistema other = (SbSistema) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbSistema[ id=" + id + " ]";
    }
    
}
