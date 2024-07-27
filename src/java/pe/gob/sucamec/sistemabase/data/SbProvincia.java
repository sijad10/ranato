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
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_PROVINCIA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbProvincia.findAll", query = "SELECT s FROM SbProvincia s"),
    @NamedQuery(name = "SbProvincia.findById", query = "SELECT s FROM SbProvincia s WHERE s.id = :id"),
    @NamedQuery(name = "SbProvincia.findByNombre", query = "SELECT s FROM SbProvincia s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbProvincia.findByActivo", query = "SELECT s FROM SbProvincia s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbProvincia.findByAudLogin", query = "SELECT s FROM SbProvincia s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbProvincia.findByAudNumIp", query = "SELECT s FROM SbProvincia s WHERE s.audNumIp = :audNumIp")})
public class SbProvincia implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PROVINCIA")
    @SequenceGenerator(name = "SEQ_SB_PROVINCIA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PROVINCIA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinciaId")
    private List<SbDistrito> sbDistritoList;
    @JoinColumn(name = "DEPARTAMENTO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbDepartamento departamentoId;

    public SbProvincia() {
    }

    public SbProvincia(Long id) {
        this.id = id;
    }

    public SbProvincia(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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
    public List<SbDistrito> getSbDistritoList() {
        return sbDistritoList;
    }

    public void setSbDistritoList(List<SbDistrito> sbDistritoList) {
        this.sbDistritoList = sbDistritoList;
    }

    public SbDepartamento getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(SbDepartamento departamentoId) {
        this.departamentoId = departamentoId;
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
        if (!(object instanceof SbProvincia)) {
            return false;
        }
        SbProvincia other = (SbProvincia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbProvincia[ id=" + id + " ]";
    }
    
}
