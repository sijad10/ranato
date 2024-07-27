/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.bdintegrado.data.SspRegistro;

/**
 *
 * @author locador845.ogtic
 */
@Entity
@Table(name = "SSP_MODULO_CARNE", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspModuloCarne.findAll", query = "SELECT s FROM SspModuloCarne s"),
    @NamedQuery(name = "SspModuloCarne.findById", query = "SELECT s FROM SspModuloCarne s WHERE s.id = :id"),
    @NamedQuery(name = "SspModuloCarne.findByHeredado", query = "SELECT s FROM SspModuloCarne s WHERE s.heredado = :heredado"),
    @NamedQuery(name = "SspModuloCarne.findByFechaIni", query = "SELECT s FROM SspModuloCarne s WHERE s.fechaIni = :fechaIni"),
    @NamedQuery(name = "SspModuloCarne.findByFechaFin", query = "SELECT s FROM SspModuloCarne s WHERE s.fechaFin = :fechaFin"),
    @NamedQuery(name = "SspModuloCarne.findByActivo", query = "SELECT s FROM SspModuloCarne s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspModuloCarne.findByAudLogin", query = "SELECT s FROM SspModuloCarne s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspModuloCarne.findByAudNumIp", query = "SELECT s FROM SspModuloCarne s WHERE s.audNumIp = :audNumIp")})
public class SspModuloCarne implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_MODULO_CARNE")
    @SequenceGenerator(name = "SEQ_SSP_MODULO_CARNE", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_MODULO_CARNE", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "HEREDADO")
    private Short heredado;
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
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
    @JoinColumn(name = "MODULO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad moduloId;
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;

    public SspModuloCarne() {
    }

    public SspModuloCarne(Long id) {
        this.id = id;
    }

    public SspModuloCarne(Long id, short activo, String audLogin, String audNumIp) {
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

    public Short getHeredado() {
        return heredado;
    }

    public void setHeredado(Short heredado) {
        this.heredado = heredado;
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

    public TipoSeguridad getModuloId() {
        return moduloId;
    }

    public void setModuloId(TipoSeguridad moduloId) {
        this.moduloId = moduloId;
    }

    public SspRegistro getRegistroId() {
        return registroId;
    }

    public void setRegistroId(SspRegistro registroId) {
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
        if (!(object instanceof SspModuloCarne)) {
            return false;
        }
        SspModuloCarne other = (SspModuloCarne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prueba1.SspModuloCarne[ id=" + id + " ]";
    }
    
}
