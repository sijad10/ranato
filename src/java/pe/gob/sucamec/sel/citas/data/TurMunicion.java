/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.citas.data;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author ocastillo
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TUR_MUNICION", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurMunicion.findAll", query = "SELECT t FROM TurMunicion t"),
    @NamedQuery(name = "TurMunicion.findById", query = "SELECT t FROM TurMunicion t WHERE t.id = :id"),
    @NamedQuery(name = "TurMunicion.findByMarca", query = "SELECT t FROM TurMunicion t WHERE t.marca = :marca"),
    @NamedQuery(name = "TurMunicion.findByCalibre", query = "SELECT t FROM TurMunicion t WHERE t.calibre = :calibre"),
    @NamedQuery(name = "TurMunicion.findByCantidad", query = "SELECT t FROM TurMunicion t WHERE t.cantidad = :cantidad"),
    @NamedQuery(name = "TurMunicion.findByActivo", query = "SELECT t FROM TurMunicion t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurMunicion.findByAudLogin", query = "SELECT t FROM TurMunicion t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurMunicion.findByAudNumIp", query = "SELECT t FROM TurMunicion t WHERE t.audNumIp = :audNumIp")})
public class TurMunicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_MUNICION")
    @SequenceGenerator(name = "SEQ_TUR_MUNICION", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_MUNICION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Size(max = 200)
    @Column(name = "MARCA")
    private String marca;
    @Size(max = 200)
    @Column(name = "CALIBRE")
    private String calibre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "CANTIDAD")
    private Integer cantidad;
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
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne
    private CitaTurTurno turnoId;
    @Column(name = "ACTA_INTERNAMIENTO_ID")
    private Long actaInternamientoId;
    
    public TurMunicion() {
    }

    public TurMunicion(Long id) {
        this.id = id;
    }

    public TurMunicion(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCalibre() {
        return calibre;
    }

    public void setCalibre(String calibre) {
        this.calibre = calibre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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

    public CitaTurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(CitaTurTurno turnoId) {
        this.turnoId = turnoId;
    }

    public Long getActaInternamientoId() {
        return actaInternamientoId;
    }

    public void setActaInternamientoId(Long actaInternamientoId) {
        this.actaInternamientoId = actaInternamientoId;
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
        if (!(object instanceof TurMunicion)) {
            return false;
        }
        TurMunicion other = (TurMunicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.TurMunicion[ id=" + id + " ]";
    }
    
}
