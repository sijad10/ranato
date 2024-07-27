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
@Table(name = "SB_PARAMETRO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbParametro.findAll", query = "SELECT s FROM SbParametro s"),
    @NamedQuery(name = "SbParametro.findById", query = "SELECT s FROM SbParametro s WHERE s.id = :id"),
    @NamedQuery(name = "SbParametro.findByNombre", query = "SELECT s FROM SbParametro s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbParametro.findByValor", query = "SELECT s FROM SbParametro s WHERE s.valor = :valor"),
    @NamedQuery(name = "SbParametro.findByCodProg", query = "SELECT s FROM SbParametro s WHERE s.codProg = :codProg"),
    @NamedQuery(name = "SbParametro.findByActivo", query = "SELECT s FROM SbParametro s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbParametro.findByAudLogin", query = "SELECT s FROM SbParametro s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbParametro.findByAudNumIp", query = "SELECT s FROM SbParametro s WHERE s.audNumIp = :audNumIp")})
public class SbParametro implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_PARAMETRO")
    @SequenceGenerator(name = "SEQ_SB_PARAMETRO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_PARAMETRO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "VALOR")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt tipoId;
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbSistemaGt sistemaId;

    public SbParametro() {
    }

    public SbParametro(Long id) {
        this.id = id;
    }

    public SbParametro(Long id, String nombre, String valor, String codProg, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.valor = valor;
        this.codProg = codProg;
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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

    public TipoBaseGt getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBaseGt tipoId) {
        this.tipoId = tipoId;
    }

    public SbSistemaGt getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(SbSistemaGt sistemaId) {
        this.sistemaId = sistemaId;
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
        if (!(object instanceof SbParametro)) {
            return false;
        }
        SbParametro other = (SbParametro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbParametro[ id=" + id + " ]";
    }
    
}
