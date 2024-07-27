/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mespinoza
 */
@Entity
@Table(name = "SB_RECIBOS_PROCESO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbRecibosProceso.findAll", query = "SELECT s FROM SbRecibosProceso s"),
    @NamedQuery(name = "SbRecibosProceso.findById", query = "SELECT s FROM SbRecibosProceso s WHERE s.id = :id"),
    @NamedQuery(name = "SbRecibosProceso.findByCodProceso", query = "SELECT s FROM SbRecibosProceso s WHERE s.codProceso = :codProceso"),
    @NamedQuery(name = "SbRecibosProceso.findByFechaIni", query = "SELECT s FROM SbRecibosProceso s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SbRecibosProceso.findByFechaFin", query = "SELECT s FROM SbRecibosProceso s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SbRecibosProceso.findByRutaArchivo", query = "SELECT s FROM SbRecibosProceso s WHERE s.rutaArchivo = :rutaArchivo"),
    @NamedQuery(name = "SbRecibosProceso.findByActivo", query = "SELECT s FROM SbRecibosProceso s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbRecibosProceso.findByAudLogin", query = "SELECT s FROM SbRecibosProceso s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbRecibosProceso.findByAudNumIp", query = "SELECT s FROM SbRecibosProceso s WHERE s.audNumIp = :audNumIp")})
public class SbRecibosProceso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_RECIBOS_PROCESO")
    @SequenceGenerator(name = "SEQ_SB_RECIBOS_PROCESO",schema = "BDINTEGRADO", sequenceName = "SEQ_SB_RECIBOS_PROCESO", allocationSize = 1)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COD_PROCESO")
    private short codProceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Size(max = 4000)
    @Column(name = "RUTA_ARCHIVO")
    private String rutaArchivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoId")
    private List<SbRecibosTemporal> sbRecibosTemporalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procesoId")
    private List<SbRecibos> sbRecibosList;

    public SbRecibosProceso() {
    }

    public SbRecibosProceso(Long id) {
        this.id = id;
    }

    public SbRecibosProceso(Long id, short codProceso, Date fechaIni, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.codProceso = codProceso;
        this.fechaIni = fechaIni;
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

    public short getCodProceso() {
        return codProceso;
    }

    public void setCodProceso(short codProceso) {
        this.codProceso = codProceso;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
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
    public List<SbRecibosTemporal> getSbRecibosTemporalList() {
        return sbRecibosTemporalList;
    }

    public void setSbRecibosTemporalList(List<SbRecibosTemporal> sbRecibosTemporalList) {
        this.sbRecibosTemporalList = sbRecibosTemporalList;
    }

    @XmlTransient
    public List<SbRecibos> getSbRecibosList() {
        return sbRecibosList;
    }

    public void setSbRecibosList(List<SbRecibos> sbRecibosList) {
        this.sbRecibosList = sbRecibosList;
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
        if (!(object instanceof SbRecibosProceso)) {
            return false;
        }
        SbRecibosProceso other = (SbRecibosProceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbRecibosProceso[ id=" + id + " ]";
    }
    
}
