/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.turreg.data;

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
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.bdintegrado.data.AmaGuiaTransito;

/**
 *
 * @author gchavez
 */
@Entity
@Cache(type = CacheType.SOFT_WEAK, size = 64000, expiry = 60000 * 5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_TURNO_ACTA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaTurnoActa.findAll", query = "SELECT a FROM AmaTurnoActa a"),
    @NamedQuery(name = "AmaTurnoActa.findById", query = "SELECT a FROM AmaTurnoActa a WHERE a.id = :id"),
    @NamedQuery(name = "AmaTurnoActa.findByNroLic", query = "SELECT a FROM AmaTurnoActa a WHERE a.nroLic = :nroLic"),
    @NamedQuery(name = "AmaTurnoActa.findBySerie", query = "SELECT a FROM AmaTurnoActa a WHERE a.serie = :serie"),
    @NamedQuery(name = "AmaTurnoActa.findByActivo", query = "SELECT a FROM AmaTurnoActa a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaTurnoActa.findByAudLogin", query = "SELECT a FROM AmaTurnoActa a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaTurnoActa.findByAudNumIp", query = "SELECT a FROM AmaTurnoActa a WHERE a.audNumIp = :audNumIp")})
public class AmaTurnoActa implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_TURNO_ACTA")
    @SequenceGenerator(name = "SEQ_AMA_TURNO_ACTA", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_TURNO_ACTA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NRO_LIC")
    private Long nroLic;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "SERIE")
    private String serie;
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
    @JoinColumn(name = "ACTA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaGuiaTransito actaId;
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TurTurno turnoId;

    public AmaTurnoActa() {
    }

    public AmaTurnoActa(Long id) {
        this.id = id;
    }

    public AmaTurnoActa(Long id, Long nroLic, String serie, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nroLic = nroLic;
        this.serie = serie;
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

    public Long getNroLic() {
        return nroLic;
    }

    public void setNroLic(Long nroLic) {
        this.nroLic = nroLic;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
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

    public AmaGuiaTransito getActaId() {
        return actaId;
    }

    public void setActaId(AmaGuiaTransito actaId) {
        this.actaId = actaId;
    }

    public TurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(TurTurno turnoId) {
        this.turnoId = turnoId;
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
        if (!(object instanceof AmaTurnoActa)) {
            return false;
        }
        AmaTurnoActa other = (AmaTurnoActa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.turreg.data.AmaTurnoActa[ id=" + id + " ]";
    }

}
