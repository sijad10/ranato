/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.renagi.data;

import pe.gob.sucamec.renagi.data.ConsultaTipoBase;
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
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_NUMERACION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement 
@NamedQueries({
    @NamedQuery(name = "ConsultaSbNumeracion.findAll", query = "SELECT s FROM ConsultaSbNumeracion s"),
    @NamedQuery(name = "ConsultaSbNumeracion.findById", query = "SELECT s FROM ConsultaSbNumeracion s WHERE s.id = :id"),
    @NamedQuery(name = "ConsultaSbNumeracion.findByValor", query = "SELECT s FROM ConsultaSbNumeracion s WHERE s.valor = :valor"),
    @NamedQuery(name = "ConsultaSbNumeracion.findByActivo", query = "SELECT s FROM ConsultaSbNumeracion s WHERE s.activo = :activo"),
    @NamedQuery(name = "ConsultaSbNumeracion.findByAudLogin", query = "SELECT s FROM ConsultaSbNumeracion s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "ConsultaSbNumeracion.findByAudNumIp", query = "SELECT s FROM ConsultaSbNumeracion s WHERE s.audNumIp = :audNumIp")})
public class ConsultaSbNumeracion implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONSULTA_SB_NUMERACION")
    @SequenceGenerator(name = "SEQ_CONSULTA_SB_NUMERACION", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_NUMERACION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
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
    @JoinColumn(name = "TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConsultaTipoBase tipo;
    
    //REGULARIZACION
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR")
    private long valor;

    public ConsultaSbNumeracion() {
    }

    public ConsultaSbNumeracion(Long id) {
        this.id = id;
    }

    public ConsultaSbNumeracion(Long id, Long valor, short activo, String audLogin, String audNumIp) {
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

    public ConsultaTipoBase getTipo() {
        return tipo;
    }

    public void setTipo(ConsultaTipoBase tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof ConsultaSbNumeracion)) {
            return false;
        }
        ConsultaSbNumeracion other = (ConsultaSbNumeracion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.renagi.data.ConsultaSbNumeracion[ id=" + id + " ]";
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }
    
}
