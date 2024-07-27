/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rchipana
 */
@Embeddable
public class CampoPorDocumentoPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_DOCUMENTO")
    private BigInteger idDocumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CAMPO")
    private BigInteger idCampo;

    public CampoPorDocumentoPK() {
    }

    public CampoPorDocumentoPK(BigInteger idDocumento, BigInteger idCampo) {
        this.idDocumento = idDocumento;
        this.idCampo = idCampo;
    }

    public BigInteger getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(BigInteger idDocumento) {
        this.idDocumento = idDocumento;
    }

    public BigInteger getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(BigInteger idCampo) {
        this.idCampo = idCampo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocumento != null ? idDocumento.hashCode() : 0);
        hash += (idCampo != null ? idCampo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CampoPorDocumentoPK)) {
            return false;
        }
        CampoPorDocumentoPK other = (CampoPorDocumentoPK) object;
        if ((this.idDocumento == null && other.idDocumento != null) || (this.idDocumento != null && !this.idDocumento.equals(other.idDocumento))) {
            return false;
        }
        if ((this.idCampo == null && other.idCampo != null) || (this.idCampo != null && !this.idCampo.equals(other.idCampo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.CampoPorDocumentoPK[ idDocumento=" + idDocumento + ", idCampo=" + idCampo + " ]";
    }
    
}
