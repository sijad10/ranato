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
 * @author locador192.ogtic
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "AMA_INGSALTD_MR_ADIC",catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaIngsaltdMrAdic.findAll", query = "SELECT a FROM AmaIngsaltdMrAdic a"),
    @NamedQuery(name = "AmaIngsaltdMrAdic.findById", query = "SELECT a FROM AmaIngsaltdMrAdic a WHERE a.id = :id"),
    @NamedQuery(name = "AmaIngsaltdMrAdic.findByValor", query = "SELECT a FROM AmaIngsaltdMrAdic a WHERE a.valor = :valor"),
    @NamedQuery(name = "AmaIngsaltdMrAdic.findByActivo", query = "SELECT a FROM AmaIngsaltdMrAdic a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaIngsaltdMrAdic.findByAudLogin", query = "SELECT a FROM AmaIngsaltdMrAdic a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaIngsaltdMrAdic.findByAudNumIp", query = "SELECT a FROM AmaIngsaltdMrAdic a WHERE a.audNumIp = :audNumIp")})
public class AmaIngsaltdMrAdic implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_INGSALTD_MR_ADIC")
    @SequenceGenerator(name = "SEQ_AMA_INGSALTD_MR_ADIC", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_INGSALTD_MR_ADIC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "VALOR")
    private String valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "INGSALTD_MATREL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaIngsaltdMatrel ingsaltdMatrelId;
    @JoinColumn(name = "DATO_ADICIONAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaDatoAdicional datoAdicionalId;

    public AmaIngsaltdMrAdic() {
    }

    public AmaIngsaltdMrAdic(Long id) {
        this.id = id;
    }

    public AmaIngsaltdMrAdic(Long id, String valor, short activo, String audLogin, String audNumIp) {
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

    public AmaIngsaltdMatrel getIngsaltdMatrelId() {
        return ingsaltdMatrelId;
    }

    public void setIngsaltdMatrelId(AmaIngsaltdMatrel ingsaltdMatrelId) {
        this.ingsaltdMatrelId = ingsaltdMatrelId;
    }

    public AmaDatoAdicional getDatoAdicionalId() {
        return datoAdicionalId;
    }

    public void setDatoAdicionalId(AmaDatoAdicional datoAdicionalId) {
        this.datoAdicionalId = datoAdicionalId;
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
        if (!(object instanceof AmaIngsaltdMrAdic)) {
            return false;
        }
        AmaIngsaltdMrAdic other = (AmaIngsaltdMrAdic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaIngsaltdMrAdic[ id=" + id + " ]";
    }
    
}
