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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author locador772.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_CONTACTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspContacto.findAll", query = "SELECT s FROM SspContacto s"),
    @NamedQuery(name = "SspContacto.findById", query = "SELECT s FROM SspContacto s WHERE s.id = :id"),
    @NamedQuery(name = "SspContacto.findByValor", query = "SELECT s FROM SspContacto s WHERE s.valor = :valor"),
    @NamedQuery(name = "SspContacto.findByDescripcion", query = "SELECT s FROM SspContacto s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SspContacto.findByFecha", query = "SELECT s FROM SspContacto s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "SspContacto.findByActivo", query = "SELECT s FROM SspContacto s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspContacto.findByAudLogin", query = "SELECT s FROM SspContacto s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspContacto.findByAudNumIp", query = "SELECT s FROM SspContacto s WHERE s.audNumIp = :audNumIp")})
public class SspContacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_CONTACTO")
    @SequenceGenerator(name = "SEQ_SSP_CONTACTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_CONTACTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    
    @Size(max = 200)
    @Column(name = "VALOR")
    private String valor;
    
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
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
    
    @JoinColumn(name = "TIPO_MEDIO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoMedioId;
    
    @JoinColumn(name = "REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspRegistro registroId;

    public SspContacto() {
    }

    public SspContacto(Long id) {
        this.id = id;
    }

    public SspContacto(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    public TipoBaseGt getTipoMedioId() {
        return tipoMedioId;
    }

    public void setTipoMedioId(TipoBaseGt tipoMedioId) {
        this.tipoMedioId = tipoMedioId;
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
        if (!(object instanceof SspContacto)) {
            return false;
        }
        SspContacto other = (SspContacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspContacto[ id=" + id + " ]";
    }
    
}
