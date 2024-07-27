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
@Table(name = "SB_CS_CERTIF_MEDICO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbCsCertifMedico.findAll", query = "SELECT s FROM SbCsCertifMedico s"),
    @NamedQuery(name = "SbCsCertifMedico.findById", query = "SELECT s FROM SbCsCertifMedico s WHERE s.id = :id"),
    @NamedQuery(name = "SbCsCertifMedico.findByActivo", query = "SELECT s FROM SbCsCertifMedico s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbCsCertifMedico.findByAudLogin", query = "SELECT s FROM SbCsCertifMedico s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbCsCertifMedico.findByAudNumIp", query = "SELECT s FROM SbCsCertifMedico s WHERE s.audNumIp = :audNumIp")})
public class SbCsCertifMedico implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_CS_CERTIF_MEDICO")
    @SequenceGenerator(name = "SEQ_SB_CS_CERTIF_MEDICO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_CS_CERTIF_MEDICO", allocationSize = 1)
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
    @JoinColumn(name = "CARGO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt cargoId;
    @JoinColumn(name = "MEDICO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsMedico medicoId;
    @JoinColumn(name = "CERTIFMEDICO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbCsCertifsalud certifmedicoId;

    public SbCsCertifMedico() {
    }

    public SbCsCertifMedico(Long id) {
        this.id = id;
    }

    public SbCsCertifMedico(Long id, short activo, String audLogin, String audNumIp) {
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

    public TipoBaseGt getCargoId() {
        return cargoId;
    }

    public void setCargoId(TipoBaseGt cargoId) {
        this.cargoId = cargoId;
    }

    public SbCsMedico getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(SbCsMedico medicoId) {
        this.medicoId = medicoId;
    }

    public SbCsCertifsalud getCertifmedicoId() {
        return certifmedicoId;
    }

    public void setCertifmedicoId(SbCsCertifsalud certifmedicoId) {
        this.certifmedicoId = certifmedicoId;
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
        if (!(object instanceof SbCsCertifMedico)) {
            return false;
        }
        SbCsCertifMedico other = (SbCsCertifMedico) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.SbCsCertifMedico[ id=" + id + " ]";
    }
    
}
