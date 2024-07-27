/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rchipana
 */
@Entity
@Table(name = "CAMPO_POR_DOCUMENTO", catalog = "", schema = "TRAMDOC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CampoPorDocumento.findAll", query = "SELECT c FROM CampoPorDocumento c"),
    @NamedQuery(name = "CampoPorDocumento.findByIdDocumento", query = "SELECT c FROM CampoPorDocumento c WHERE c.campoPorDocumentoPK.idDocumento = :idDocumento"),
    @NamedQuery(name = "CampoPorDocumento.findByIdCampo", query = "SELECT c FROM CampoPorDocumento c WHERE c.campoPorDocumentoPK.idCampo = :idCampo"),
    @NamedQuery(name = "CampoPorDocumento.findByValor", query = "SELECT c FROM CampoPorDocumento c WHERE c.valor = :valor")})
public class CampoPorDocumento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CampoPorDocumentoPK campoPorDocumentoPK;
    @Size(max = 1024)
    @Column(name = "VALOR")    
    private String valor;
    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID_DOCUMENTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Documento documento;
    @JoinColumn(name = "ID_CAMPO", referencedColumnName = "ID_CAMPO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Campo campo;

    public CampoPorDocumento() {
    }

    public CampoPorDocumento(CampoPorDocumentoPK campoPorDocumentoPK) {
        this.campoPorDocumentoPK = campoPorDocumentoPK;
    }

    public CampoPorDocumento(BigInteger idDocumento, BigInteger idCampo) {
        this.campoPorDocumentoPK = new CampoPorDocumentoPK(idDocumento, idCampo);
    }

    public CampoPorDocumentoPK getCampoPorDocumentoPK() {
        return campoPorDocumentoPK;
    }

    public void setCampoPorDocumentoPK(CampoPorDocumentoPK campoPorDocumentoPK) {
        this.campoPorDocumentoPK = campoPorDocumentoPK;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (campoPorDocumentoPK != null ? campoPorDocumentoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CampoPorDocumento)) {
            return false;
        }
        CampoPorDocumento other = (CampoPorDocumento) object;
        if ((this.campoPorDocumentoPK == null && other.campoPorDocumentoPK != null) || (this.campoPorDocumentoPK != null && !this.campoPorDocumentoPK.equals(other.campoPorDocumentoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.CampoPorDocumento[ campoPorDocumentoPK=" + campoPorDocumentoPK + " ]";
    }
    
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }
}
