/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

import java.io.Serializable;
import org.eclipse.persistence.annotations.Customizer;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import javax.persistence.SequenceGenerator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "TUR_FOTO_REG", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TurFotoReg.findAll", query = "SELECT t FROM TurFotoReg t"),
    @NamedQuery(name = "TurFotoReg.findById", query = "SELECT t FROM TurFotoReg t WHERE t.id = :id"),
    @NamedQuery(name = "TurFotoReg.findByActivo", query = "SELECT t FROM TurFotoReg t WHERE t.activo = :activo"),
    @NamedQuery(name = "TurFotoReg.findByAudLogin", query = "SELECT t FROM TurFotoReg t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TurFotoReg.findByAudNumIp", query = "SELECT t FROM TurFotoReg t WHERE t.audNumIp = :audNumIp")})
public class TurFotoReg implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TUR_FOTO_REG")
    @SequenceGenerator(name = "SEQ_TUR_FOTO_REG", schema = "BDINTEGRADO", sequenceName = "SEQ_TUR_FOTO_REG", allocationSize = 1)
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "RUTA_FILE")
    private String rutaFile;
    @JoinColumn(name = "ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TurLicenciaReg armaId;
    @JoinColumn(name = "TIPO_POSICION_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase tipoPosicionId;
    @OneToMany(mappedBy = "turFotoId")
    private List<TurLicenciaReg> turLicenciaRegList;

    public TurFotoReg() {
    }

    public TurFotoReg(Long id) {
        this.id = id;
    }

    public TurFotoReg(Long id, short activo, String audLogin, String audNumIp) {
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

    public String getRutaFile() {
        return rutaFile;
    }

    public void setRutaFile(String rutaFile) {
        this.rutaFile = rutaFile;
    }

    public TurLicenciaReg getArmaId() {
        return armaId;
    }

    public void setArmaId(TurLicenciaReg armaId) {
        this.armaId = armaId;
    }

    public TipoBase getTipoPosicionId() {
        return tipoPosicionId;
    }

    public void setTipoPosicionId(TipoBase tipoPosicionId) {
        this.tipoPosicionId = tipoPosicionId;
    }

    @XmlTransient
    public List<TurLicenciaReg> getTurLicenciaRegList() {
        return turLicenciaRegList;
    }

    public void setTurLicenciaRegList(List<TurLicenciaReg> turLicenciaRegList) {
        this.turLicenciaRegList = turLicenciaRegList;
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
        if (!(object instanceof TurFotoReg)) {
            return false;
        }
        TurFotoReg other = (TurFotoReg) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.TurFotoReg[ id=" + id + " ]";
    }
    
}
