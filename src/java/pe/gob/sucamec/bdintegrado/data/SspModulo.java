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

/**
 *
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_MODULO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspModulo.findAll", query = "SELECT s FROM SspModulo s"),
    @NamedQuery(name = "SspModulo.findById", query = "SELECT s FROM SspModulo s WHERE s.id = :id"),
    @NamedQuery(name = "SspModulo.findByNombre", query = "SELECT s FROM SspModulo s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SspModulo.findByCodModulo", query = "SELECT s FROM SspModulo s WHERE s.codModulo = :codModulo"),
    @NamedQuery(name = "SspModulo.findByActivo", query = "SELECT s FROM SspModulo s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspModulo.findByAudLogin", query = "SELECT s FROM SspModulo s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspModulo.findByAudNumIp", query = "SELECT s FROM SspModulo s WHERE s.audNumIp = :audNumIp")})
public class SspModulo implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_MODULO")
    @SequenceGenerator(name = "SEQ_SSP_MODULO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_MODULO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 5)
    @Column(name = "COD_MODULO")
    private String codModulo;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "moduloId")
    private List<SspInstructorModulo> sspInstructorModuloList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "moduloId")
    private List<SspProgramacion> sspProgramacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "moduloId")
    private List<SspNotas> sspNotasList;
    @JoinColumn(name = "TIPO_CURSO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad tipoCursoId;

    public SspModulo() {
    }

    public SspModulo(Long id) {
        this.id = id;
    }

    public SspModulo(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodModulo() {
        return codModulo;
    }

    public void setCodModulo(String codModulo) {
        this.codModulo = codModulo;
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
    public List<SspProgramacion> getSspProgramacionList() {
        return sspProgramacionList;
    }

    public void setSspProgramacionList(List<SspProgramacion> sspProgramacionList) {
        this.sspProgramacionList = sspProgramacionList;
    }

    @XmlTransient
    public List<SspNotas> getSspNotasList() {
        return sspNotasList;
    }

    public void setSspNotasList(List<SspNotas> sspNotasList) {
        this.sspNotasList = sspNotasList;
    }

    public TipoSeguridad getTipoCursoId() {
        return tipoCursoId;
    }

    public void setTipoCursoId(TipoSeguridad tipoCursoId) {
        this.tipoCursoId = tipoCursoId;
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
        if (!(object instanceof SspModulo)) {
            return false;
        }
        SspModulo other = (SspModulo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspModulo[ id=" + id + " ]";
    }
    
    @XmlTransient
    public List<SspInstructorModulo> getSspInstructorModuloList() {
        return sspInstructorModuloList;
    }

    public void setSspInstructorModuloList(List<SspInstructorModulo> sspInstructorModuloList) {
        this.sspInstructorModuloList = sspInstructorModuloList;
    }
}
