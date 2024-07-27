/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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

/**
 *
 * @author rchipana
 */
@Entity
@Table(name = "AMA_DETALLE_RESO_ARMA_ADIC", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmaDetalleResoArmaAdic.findAll", query = "SELECT a FROM AmaDetalleResoArmaAdic a"),
    @NamedQuery(name = "AmaDetalleResoArmaAdic.findById", query = "SELECT a FROM AmaDetalleResoArmaAdic a WHERE a.id = :id"),
    @NamedQuery(name = "AmaDetalleResoArmaAdic.findByValor", query = "SELECT a FROM AmaDetalleResoArmaAdic a WHERE a.valor = :valor"),
    @NamedQuery(name = "AmaDetalleResoArmaAdic.findByActivo", query = "SELECT a FROM AmaDetalleResoArmaAdic a WHERE a.activo = :activo"),
    @NamedQuery(name = "AmaDetalleResoArmaAdic.findByAudLogin", query = "SELECT a FROM AmaDetalleResoArmaAdic a WHERE a.audLogin = :audLogin"),
    @NamedQuery(name = "AmaDetalleResoArmaAdic.findByAudNumIp", query = "SELECT a FROM AmaDetalleResoArmaAdic a WHERE a.audNumIp = :audNumIp")})
public class AmaDetalleResoArmaAdic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AMA_DETALLE_RESO_MR_ADIC")
    @SequenceGenerator(name = "SEQ_AMA_DETALLE_RESO_MR_ADIC", schema = "BDINTEGRADO", sequenceName = "SEQ_AMA_DETALLE_RESO_MR_ADIC", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "VALOR")
    private String valor;
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
    @Size(min = 1, max = 60)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "DETALLE_RESO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaDetalleResoArma detalleResoArmaId;
    @JoinColumn(name = "DATO_ADICIONAL_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AmaDatoAdicional datoAdicionalId;

    public AmaDetalleResoArmaAdic() {
    }

    public AmaDetalleResoArmaAdic(Long id) {
        this.id = id;
    }

    public AmaDetalleResoArmaAdic(Long id, String valor, short activo, String audLogin, String audNumIp) {
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

    public AmaDetalleResoArma getDetalleResoArmaId() {
        return detalleResoArmaId;
    }

    public void setDetalleResoArmaId(AmaDetalleResoArma detalleResoArmaId) {
        this.detalleResoArmaId = detalleResoArmaId;
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
        if (!(object instanceof AmaDetalleResoArmaAdic)) {
            return false;
        }
        AmaDetalleResoArmaAdic other = (AmaDetalleResoArmaAdic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.AmaDetalleResoArmaAdic[ id=" + id + " ]";
    }

}
