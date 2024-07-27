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
 * @author rarevalo
 */
@Entity
@Customizer(pe.gob.sucamec.sel.citas.data.AuditoriaEntidad.class)
@Table(name = "AMA_TURNO_ACTA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitaAmaTurnoActa.findAll", query = "SELECT a FROM CitaAmaTurnoActa a"),
    @NamedQuery(name = "CitaAmaTurnoActa.findById", query = "SELECT a FROM CitaAmaTurnoActa a WHERE a.id = :id"),
    @NamedQuery(name = "CitaAmaTurnoActa.findByNroLic", query = "SELECT a FROM CitaAmaTurnoActa a WHERE a.nroLic = :nroLic"),
    @NamedQuery(name = "CitaAmaTurnoActa.findBySerie", query = "SELECT a FROM CitaAmaTurnoActa a WHERE a.serie = :serie"),
    @NamedQuery(name = "CitaAmaTurnoActa.findByActivo", query = "SELECT a FROM CitaAmaTurnoActa a WHERE a.activo = :activo"),
    @NamedQuery(name = "CitaAmaTurnoActa.findByAudLogin", query = "SELECT a FROM CitaAmaTurnoActa a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "CitaAmaTurnoActa.findByAudNumIp", query = "SELECT a FROM CitaAmaTurnoActa a WHERE a.audNumIp = :audNumIp")})
public class CitaAmaTurnoActa implements Serializable {

    private static final long serialVersionUID = 1L;
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
    private long nroLic;
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
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CitaTurTurno turnoId;
    //@JoinColumn(name = "ACTA_ID", referencedColumnName = "ID")
    //@ManyToOne
    //private AmaGuiaTransito actaId;

    public CitaAmaTurnoActa() {
    }

    public CitaAmaTurnoActa(Long id) {
        this.id = id;
    }

    public CitaAmaTurnoActa(Long id, long nroLic, String serie, short activo, String audLogin, String audNumIp) {
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

    public long getNroLic() {
        return nroLic;
    }

    public void setNroLic(long nroLic) {
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

    public CitaTurTurno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(CitaTurTurno turnoId) {
        this.turnoId = turnoId;
    }

    /*public AmaGuiaTransito getActaId() {
        return actaId;
    }

    public void setActaId(AmaGuiaTransito actaId) {
        this.actaId = actaId;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitaAmaTurnoActa)) {
            return false;
        }
        CitaAmaTurnoActa other = (CitaAmaTurnoActa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sel.citas.data.CitaAmaTurnoActa[ id=" + id + " ]";
    }
    
}
