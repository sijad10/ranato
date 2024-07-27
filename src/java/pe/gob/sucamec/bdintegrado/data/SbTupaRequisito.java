/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author msalinas
 */
@Entity
@Cache(type=CacheType.SOFT_WEAK, size=64000, expiry=60000*5) // Cache de 5 min.
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class) // Auditoria para historial y campos AUD
@Table(name = "SB_TUPA_REQUISITO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbTupaRequisito.findAll", query = "SELECT s FROM SbTupaRequisito s"),
    @NamedQuery(name = "SbTupaRequisito.findById", query = "SELECT s FROM SbTupaRequisito s WHERE s.id = :id"),
    @NamedQuery(name = "SbTupaRequisito.findByDescripcion", query = "SELECT s FROM SbTupaRequisito s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SbTupaRequisito.findByCondicionPresentacion", query = "SELECT s FROM SbTupaRequisito s WHERE s.condicionPresentacion = :condicionPresentacion"),
    @NamedQuery(name = "SbTupaRequisito.findByCondicionFormatoDescarga", query = "SELECT s FROM SbTupaRequisito s WHERE s.condicionFormatoDescarga = :condicionFormatoDescarga"),
    @NamedQuery(name = "SbTupaRequisito.findByVigente", query = "SELECT s FROM SbTupaRequisito s WHERE s.vigente = :vigente"),
    @NamedQuery(name = "SbTupaRequisito.findByActivo", query = "SELECT s FROM SbTupaRequisito s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbTupaRequisito.findByAudLogin", query = "SELECT s FROM SbTupaRequisito s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbTupaRequisito.findByAudNumIp", query = "SELECT s FROM SbTupaRequisito s WHERE s.audNumIp = :audNumIp")})
public class SbTupaRequisito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_TUPA_REQUISITO")
    @SequenceGenerator(name = "SEQ_SB_TUPA_REQUISITO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_TUPA_REQUISITO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_PRESENTACION")
    private short condicionPresentacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_FORMATO_DESCARGA")
    private short condicionFormatoDescarga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_VISIBLE_ADJUNTO")
    private short condicionVisibleAdjunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_VISIBLE_TEXTO")
    private short condicionVisibleTexto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CONDICION_VISIBLE_ENLACE")
    private short condicionVisibleEnlace;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VIGENTE")
    private short vigente;
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
    @ManyToMany(mappedBy = "sbTupaRequisitoList")
    private List<TipoBaseGt> tipoBaseList;
    @JoinColumn(name = "PROCEDIMIENTO_TUPA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTupa procedimientoTupaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tupaRequisitoId")
    private List<SbExpVirtualRequisito> sbExpVirtualRequisitoList;

    public SbTupaRequisito() {
    }

    public SbTupaRequisito(Long id) {
        this.id = id;
    }

    public SbTupaRequisito(Long id, String descripcion, short condicionPresentacion, short condicionFormatoDescarga, short vigente, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.descripcion = descripcion;
        this.condicionPresentacion = condicionPresentacion;
        this.condicionFormatoDescarga = condicionFormatoDescarga;
        this.vigente = vigente;
        this.activo = activo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public short getCondicionVisibleAdjunto() {
        return condicionVisibleAdjunto;
    }

    public void setCondicionVisibleAdjunto(short condicionVisibleAdjunto) {
        this.condicionVisibleAdjunto = condicionVisibleAdjunto;
    }

    public short getCondicionVisibleTexto() {
        return condicionVisibleTexto;
    }

    public void setCondicionVisibleTexto(short condicionVisibleTexto) {
        this.condicionVisibleTexto = condicionVisibleTexto;
    }

    public short getCondicionVisibleEnlace() {
        return condicionVisibleEnlace;
    }

    public void setCondicionVisibleEnlace(short condicionVisibleEnlace) {
        this.condicionVisibleEnlace = condicionVisibleEnlace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getCondicionPresentacion() {
        return condicionPresentacion;
    }

    public void setCondicionPresentacion(short condicionPresentacion) {
        this.condicionPresentacion = condicionPresentacion;
    }

    public short getCondicionFormatoDescarga() {
        return condicionFormatoDescarga;
    }

    public void setCondicionFormatoDescarga(short condicionFormatoDescarga) {
        this.condicionFormatoDescarga = condicionFormatoDescarga;
    }

    public short getVigente() {
        return vigente;
    }

    public void setVigente(short vigente) {
        this.vigente = vigente;
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

    @XmlTransient
    public List<TipoBaseGt> getTipoBaseList() {
        return tipoBaseList;
    }

    public void setTipoBaseList(List<TipoBaseGt> tipoBaseList) {
        this.tipoBaseList = tipoBaseList;
    }

    public SbTupa getProcedimientoTupaId() {
        return procedimientoTupaId;
    }

    public void setProcedimientoTupaId(SbTupa procedimientoTupaId) {
        this.procedimientoTupaId = procedimientoTupaId;
    }

    @XmlTransient
    public List<SbExpVirtualRequisito> getSbExpVirtualRequisitoList() {
        return sbExpVirtualRequisitoList;
    }

    public void setSbExpVirtualRequisitoList(List<SbExpVirtualRequisito> sbExpVirtualRequisitoList) {
        this.sbExpVirtualRequisitoList = sbExpVirtualRequisitoList;
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
        if (!(object instanceof SbTupaRequisito)) {
            return false;
        }
        SbTupaRequisito other = (SbTupaRequisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "data.SbTupaRequisito[ id=" + id + " ]";
    }
    
}
