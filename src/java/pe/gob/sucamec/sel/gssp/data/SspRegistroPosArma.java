/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sel.gssp.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import pe.gob.sucamec.bdintegrado.data.AmaCatalogo;
import pe.gob.sucamec.bdintegrado.data.AmaModelos;
import pe.gob.sucamec.bdintegrado.data.TipoSeguridad;
import pe.gob.sucamec.sistemabase.data.SbPersona;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author jrosales
 */
@Entity
@Table(name = "SSP_REGISTRO_POS_ARMA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SspRegistroPosArma.findAll", query = "SELECT s FROM SspRegistroPosArma s")
    , @NamedQuery(name = "SspRegistroPosArma.findById", query = "SELECT s FROM SspRegistroPosArma s WHERE s.id = :id")
    , @NamedQuery(name = "SspRegistroPosArma.findByNumeroSerie", query = "SELECT s FROM SspRegistroPosArma s WHERE s.numeroSerie = :numeroSerie")
    , @NamedQuery(name = "SspRegistroPosArma.findByNuevoMarca", query = "SELECT s FROM SspRegistroPosArma s WHERE s.nuevoMarca = :nuevoMarca")
    , @NamedQuery(name = "SspRegistroPosArma.findByNuevoModelo", query = "SELECT s FROM SspRegistroPosArma s WHERE s.nuevoModelo = :nuevoModelo")
    , @NamedQuery(name = "SspRegistroPosArma.findByNuevoCalibre", query = "SELECT s FROM SspRegistroPosArma s WHERE s.nuevoCalibre = :nuevoCalibre")
    , @NamedQuery(name = "SspRegistroPosArma.findByFlagSinCatalogo", query = "SELECT s FROM SspRegistroPosArma s WHERE s.flagSinCatalogo = :flagSinCatalogo")
    , @NamedQuery(name = "SspRegistroPosArma.findByNombreFoto1", query = "SELECT s FROM SspRegistroPosArma s WHERE s.nombreFoto1 = :nombreFoto1")
    , @NamedQuery(name = "SspRegistroPosArma.findByNombreFoto2", query = "SELECT s FROM SspRegistroPosArma s WHERE s.nombreFoto2 = :nombreFoto2")
    , @NamedQuery(name = "SspRegistroPosArma.findByDescripcion", query = "SELECT s FROM SspRegistroPosArma s WHERE s.descripcion = :descripcion")
    , @NamedQuery(name = "SspRegistroPosArma.findByFechaRegistro", query = "SELECT s FROM SspRegistroPosArma s WHERE s.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "SspRegistroPosArma.findByActivo", query = "SELECT s FROM SspRegistroPosArma s WHERE s.activo = :activo")
    , @NamedQuery(name = "SspRegistroPosArma.findByAudLogin", query = "SELECT s FROM SspRegistroPosArma s WHERE s.audLogin = :audLogin")
    , @NamedQuery(name = "SspRegistroPosArma.findByAudNumIp", query = "SELECT s FROM SspRegistroPosArma s WHERE s.audNumIp = :audNumIp")})
public class SspRegistroPosArma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SSP_REGISTRO_POS_ARMA")
    @SequenceGenerator(name = "SEQ_SSP_REGISTRO_POS_ARMA", schema = "BDINTEGRADO", sequenceName = "SEQ_SSP_REGISTRO_POS_ARMA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;
    @Column(name = "NUEVO_MARCA")
    private String nuevoMarca;
    @Column(name = "NUEVO_MODELO")
    private String nuevoModelo;
    @Column(name = "NUEVO_CALIBRE")
    private String nuevoCalibre;
    @Basic(optional = false)
    @Column(name = "FLAG_SIN_CATALOGO")
    private short flagSinCatalogo;
    @Basic(optional = false)
    @Column(name = "NOMBRE_FOTO1")
    private String nombreFoto1;
    @Basic(optional = false)
    @Column(name = "NOMBRE_FOTO2")
    private String nombreFoto2;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "FECHA_REGISTRO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private short activo;
    @Basic(optional = false)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "NUEVO_TIPO_ARMA_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaCatalogo nuevoTipoArmaId;
    @JoinColumn(name = "MODELO_CATALOGADO_ID", referencedColumnName = "ID")
    @ManyToOne
    private AmaModelos modeloCatalogadoId;
    @JoinColumn(name = "EMPRESA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPersona empresaId;
    @JoinColumn(name = "USUARIO_REGISTRO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuarioRegistroId;
    @JoinColumn(name = "SITUACION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad situacionId;
    @JoinColumn(name = "ESTADO_FISICO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoSeguridad estadoFisicoId;

    public SspRegistroPosArma() {
    }

    public SspRegistroPosArma(Long id) {
        this.id = id;
    }

    public SspRegistroPosArma(Long id, String numeroSerie, short flagSinCatalogo, String nombreFoto1, String nombreFoto2, Date fechaRegistro, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.flagSinCatalogo = flagSinCatalogo;
        this.nombreFoto1 = nombreFoto1;
        this.nombreFoto2 = nombreFoto2;
        this.fechaRegistro = fechaRegistro;
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

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNuevoMarca() {
        return nuevoMarca;
    }

    public void setNuevoMarca(String nuevoMarca) {
        this.nuevoMarca = nuevoMarca;
    }

    public String getNuevoModelo() {
        return nuevoModelo;
    }

    public void setNuevoModelo(String nuevoModelo) {
        this.nuevoModelo = nuevoModelo;
    }

    public String getNuevoCalibre() {
        return nuevoCalibre;
    }

    public void setNuevoCalibre(String nuevoCalibre) {
        this.nuevoCalibre = nuevoCalibre;
    }

    public short getFlagSinCatalogo() {
        return flagSinCatalogo;
    }

    public void setFlagSinCatalogo(short flagSinCatalogo) {
        this.flagSinCatalogo = flagSinCatalogo;
    }

    public String getNombreFoto1() {
        return nombreFoto1;
    }

    public void setNombreFoto1(String nombreFoto1) {
        this.nombreFoto1 = nombreFoto1;
    }

    public String getNombreFoto2() {
        return nombreFoto2;
    }

    public void setNombreFoto2(String nombreFoto2) {
        this.nombreFoto2 = nombreFoto2;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public AmaCatalogo getNuevoTipoArmaId() {
        return nuevoTipoArmaId;
    }

    public void setNuevoTipoArmaId(AmaCatalogo nuevoTipoArmaId) {
        this.nuevoTipoArmaId = nuevoTipoArmaId;
    }

    public AmaModelos getModeloCatalogadoId() {
        return modeloCatalogadoId;
    }

    public void setModeloCatalogadoId(AmaModelos modeloCatalogadoId) {
        this.modeloCatalogadoId = modeloCatalogadoId;
    }

    public SbPersona getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(SbPersona empresaId) {
        this.empresaId = empresaId;
    }

    public SbUsuario getUsuarioRegistroId() {
        return usuarioRegistroId;
    }

    public void setUsuarioRegistroId(SbUsuario usuarioRegistroId) {
        this.usuarioRegistroId = usuarioRegistroId;
    }

    public TipoSeguridad getSituacionId() {
        return situacionId;
    }

    public void setSituacionId(TipoSeguridad situacionId) {
        this.situacionId = situacionId;
    }

    public TipoSeguridad getEstadoFisicoId() {
        return estadoFisicoId;
    }

    public void setEstadoFisicoId(TipoSeguridad estadoFisicoId) {
        this.estadoFisicoId = estadoFisicoId;
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
        if (!(object instanceof SspRegistroPosArma)) {
            return false;
        }
        SspRegistroPosArma other = (SspRegistroPosArma) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clases.SspRegistroPosArma[ id=" + id + " ]";
    }
    
}
