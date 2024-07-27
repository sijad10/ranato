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
 * @author rfernandezv
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SSP_LOCAL_CONTACTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspLocalContacto.findAll", query = "SELECT s FROM SspLocalContacto s"),
    @NamedQuery(name = "SspLocalContacto.findById", query = "SELECT s FROM SspLocalContacto s WHERE s.id = :id"),
    @NamedQuery(name = "SspLocalContacto.findByValor", query = "SELECT s FROM SspLocalContacto s WHERE s.valor = :valor"),
    @NamedQuery(name = "SspLocalContacto.findByDescripcion", query = "SELECT s FROM SspLocalContacto s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SspLocalContacto.findByActivo", query = "SELECT s FROM SspLocalContacto s WHERE s.activo = :activo"),
    @NamedQuery(name = "SspLocalContacto.findByAudLogin", query = "SELECT s FROM SspLocalContacto s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SspLocalContacto.findByAudNumIp", query = "SELECT s FROM SspLocalContacto s WHERE s.audNumIp = :audNumIp")})
public class SspLocalContacto implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_LOCAL_CONTACTO")
    @SequenceGenerator(name = "SEQ_SSP_LOCAL_CONTACTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_LOCAL_CONTACTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "VALOR")
    private String valor;
    @Size(max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
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
    @JoinColumn(name = "TIPO_MEDIO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoSeguridad tipoMedioId;
    @JoinColumn(name = "LOCAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SspLocal localId;

    public SspLocalContacto() {
    }

    public SspLocalContacto(Long id) {
        this.id = id;
    }

    public SspLocalContacto(Long id, String valor, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.valor = valor;
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

    public TipoSeguridad getTipoMedioId() {
        return tipoMedioId;
    }

    public void setTipoMedioId(TipoSeguridad tipoMedioId) {
        this.tipoMedioId = tipoMedioId;
    }

    public SspLocal getLocalId() {
        return localId;
    }

    public void setLocalId(SspLocal localId) {
        this.localId = localId;
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
        if (!(object instanceof SspLocalContacto)) {
            return false;
        }
        SspLocalContacto other = (SspLocalContacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SspLocalContacto[ id=" + id + " ]";
    }
    
}
