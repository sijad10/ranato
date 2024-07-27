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
import pe.gob.sucamec.sel.epp.data.Com;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_DEPARTAMENTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbDepartamento.findAll", query = "SELECT s FROM SbDepartamento s"),
    @NamedQuery(name = "SbDepartamento.findById", query = "SELECT s FROM SbDepartamento s WHERE s.id = :id"),
    @NamedQuery(name = "SbDepartamento.findByNombre", query = "SELECT s FROM SbDepartamento s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbDepartamento.findByActivo", query = "SELECT s FROM SbDepartamento s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbDepartamento.findByAudLogin", query = "SELECT s FROM SbDepartamento s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbDepartamento.findByAudNumIp", query = "SELECT s FROM SbDepartamento s WHERE s.audNumIp = :audNumIp")})
public class SbDepartamento implements Serializable {

    @OneToMany(mappedBy = "direccionRegional")
    private List<Com> comList;
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_DEPARTAMENTO")
    @SequenceGenerator(name = "SEQ_SB_DEPARTAMENTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_DEPARTAMENTO", allocationSize = 1)
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departamentoId")
    private List<SbProvincia> sbProvinciaList;

    public SbDepartamento() {
    }

    public SbDepartamento(Long id) {
        this.id = id;
    }

    public SbDepartamento(Long id, String nombre, short activo, String audLogin, String audNumIp) {
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
    public List<SbProvincia> getSbProvinciaList() {
        return sbProvinciaList;
    }

    public void setSbProvinciaList(List<SbProvincia> sbProvinciaList) {
        this.sbProvinciaList = sbProvinciaList;
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
        if (!(object instanceof SbDepartamento)) {
            return false;
        }
        SbDepartamento other = (SbDepartamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbDepartamento[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Com> getComList() {
        return comList;
    }

    public void setComList(List<Com> comList) {
        this.comList = comList;
    }
    
}
