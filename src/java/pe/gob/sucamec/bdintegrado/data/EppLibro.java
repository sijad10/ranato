/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import pe.gob.sucamec.bdintegrado.data.TipoExplosivoGt;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author mespinoza
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "EPP_LIBRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EppLibro.findAll", query = "SELECT e FROM EppLibro e"),
    @NamedQuery(name = "EppLibro.findById", query = "SELECT e FROM EppLibro e WHERE e.id = :id"),
    @NamedQuery(name = "EppLibro.findByCodLibro", query = "SELECT e FROM EppLibro e WHERE e.codLibro = :codLibro"),
    @NamedQuery(name = "EppLibro.findBySemestre", query = "SELECT e FROM EppLibro e WHERE e.semestre = :semestre"),
    @NamedQuery(name = "EppLibro.findByObservacion", query = "SELECT e FROM EppLibro e WHERE e.observacion = :observacion"),
    @NamedQuery(name = "EppLibro.findByActivo", query = "SELECT e FROM EppLibro e WHERE e.activo = :activo"),
    @NamedQuery(name = "EppLibro.findByAudLogin", query = "SELECT e FROM EppLibro e WHERE e.audLogin = :audLogin"),
    @NamedQuery(name = "EppLibro.findByAudNumIp", query = "SELECT e FROM EppLibro e WHERE e.audNumIp = :audNumIp")})
public class EppLibro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EPP_LIBRO")
    @SequenceGenerator(name = "SEQ_EPP_LIBRO", schema = "BDINTEGRADO", sequenceName = "SEQ_EPP_LIBRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "COD_LIBRO")
    private String codLibro;
    @Size(max = 10)
    @Column(name = "SEMESTRE")
    private String semestre;
    @Size(max = 200)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    @JoinTable(schema="BDINTEGRADO", name = "EPP_LIBRO_POLVORIN", joinColumns = {
        @JoinColumn(name = "LIBRO_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "POLVORIN_ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<EppPolvorin> eppPolvorinList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroId")
    private List<EppLibroEventos> eppLibroEventosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroPolvorinId")
    private List<EppLibroSaldo> eppLibroSaldoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libroId")
    private List<EppLibroMes> eppLibroMesList;
    @JoinColumn(name = "TIPO_ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoExplosivoGt tipoEstado;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersonaGt empresaId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private EppRegistro registroId;

    public EppLibro() {
    }

    public EppLibro(Long id) {
        this.id = id;
    }

    public EppLibro(Long id, String codLibro, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.codLibro = codLibro;
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

    public String getCodLibro() {
        return codLibro;
    }

    public void setCodLibro(String codLibro) {
        this.codLibro = codLibro;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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
    public List<EppPolvorin> getEppPolvorinList() {
        return eppPolvorinList;
    }

    public void setEppPolvorinList(List<EppPolvorin> eppPolvorinList) {
        this.eppPolvorinList = eppPolvorinList;
    }

    @XmlTransient
    public List<EppLibroEventos> getEppLibroEventosList() {
        return eppLibroEventosList;
    }

    public void setEppLibroEventosList(List<EppLibroEventos> eppLibroEventosList) {
        this.eppLibroEventosList = eppLibroEventosList;
    }

    @XmlTransient
    public List<EppLibroSaldo> getEppLibroSaldoList() {
        return eppLibroSaldoList;
    }

    public void setEppLibroSaldoList(List<EppLibroSaldo> eppLibroSaldoList) {
        this.eppLibroSaldoList = eppLibroSaldoList;
    }

    @XmlTransient
    public List<EppLibroMes> getEppLibroMesList() {
        return eppLibroMesList;
    }

    public void setEppLibroMesList(List<EppLibroMes> eppLibroMesList) {
        this.eppLibroMesList = eppLibroMesList;
    }

    public TipoExplosivoGt getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(TipoExplosivoGt tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public SbPersonaGt getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersonaGt empresaId) {
        this.empresaId = empresaId;
    }

    public EppRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(EppRegistro registroId) {
        this.registroId = registroId;
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
        if (!(object instanceof EppLibro)) {
            return false;
        }
        EppLibro other = (EppLibro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.explosivos.data.EppLibro[ id=" + id + " ]";
    }
    
}
