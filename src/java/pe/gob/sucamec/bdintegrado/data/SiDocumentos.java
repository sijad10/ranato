/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.TipoBase;
import pe.gob.sucamec.sistemabase.data.SbUsuario;
import pe.gob.sucamec.bdintegrado.data.SiTipo;


/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SI_DOCUMENTOS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiDocumentos.findAll", query = "SELECT d FROM SiDocumentos d"),
    @NamedQuery(name = "SiDocumentos.findById", query = "SELECT d FROM SiDocumentos d WHERE d.id = :id"),
    @NamedQuery(name = "SiDocumentos.findByNumero", query = "SELECT d FROM SiDocumentos d WHERE d.numero = :numero"),
    @NamedQuery(name = "SiDocumentos.findByDescripcion", query = "SELECT d FROM SiDocumentos d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "SiDocumentos.findByFecha", query = "SELECT d FROM SiDocumentos d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "SiDocumentos.findByFechaIngreso", query = "SELECT d FROM SiDocumentos d WHERE d.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "SiDocumentos.findByAnho", query = "SELECT d FROM SiDocumentos d WHERE d.anho = :anho"),
    @NamedQuery(name = "SiDocumentos.findByActivo", query = "SELECT d FROM SiDocumentos d WHERE d.activo = :activo"),
    @NamedQuery(name = "SiDocumentos.findByMotivoAnulado", query = "SELECT d FROM SiDocumentos d WHERE d.motivoAnulado = :motivoAnulado"),
    @NamedQuery(name = "SiDocumentos.findByCopiaArchivo", query = "SELECT d FROM SiDocumentos d WHERE d.copiaArchivo = :copiaArchivo"),
    @NamedQuery(name = "SiDocumentos.findByFechaArchivo", query = "SELECT d FROM SiDocumentos d WHERE d.fechaArchivo = :fechaArchivo"),
    @NamedQuery(name = "SiDocumentos.findByDestino", query = "SELECT d FROM SiDocumentos d WHERE d.destino = :destino"),
    @NamedQuery(name = "SiDocumentos.findByAudLogin", query = "SELECT d FROM SiDocumentos d WHERE d.audLogin = :audLogin"),
    @NamedQuery(name = "SiDocumentos.findByAudNumIp", query = "SELECT d FROM SiDocumentos d WHERE d.audNumIp = :audNumIp")})
public class SiDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "EXPEDIENTE")
    private Long expediente;
    @Size(max = 20)
    @Column(name = "NUM_GUIA")
    private String numGuia;
    @Column(name = "FECHA_ENVIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    @Size(max = 2000)
    @Column(name = "DETALLE")
    private String detalle;
    @JoinColumn(name = "DEPARTAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private SiTipo departamento;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SI_DOCUMENTOS")
    @SequenceGenerator(name = "SEQ_SI_DOCUMENTOS", schema = "BDINTEGRADO", sequenceName = "SEQ_SI_DOCUMENTOS", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO")
    private long numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANHO")
    private short anho;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    @Size(max = 200)
    @Column(name = "MOTIVO_ANULADO")
    private String motivoAnulado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COPIA_ARCHIVO")
    private short copiaArchivo;
    @Column(name = "FECHA_ARCHIVO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaArchivo;
    @Size(max = 200)
    @Column(name = "DESTINO")
    private String destino;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @JoinColumn(name = "USUARIO_ARCHIVO", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuario usuarioArchivo;
    @JoinColumn(name = "USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuario;
    @JoinColumn(name = "TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase tipo;
    @JoinColumn(name = "AREA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBase area;

    public SiDocumentos() {
    }

    public SiDocumentos(Long id) {
        this.id = id;
    }

    public SiDocumentos(Long id, long numero, String descripcion, Date fecha, Date fechaIngreso, short anho, short activo, short copiaArchivo, String audLogin, String audNumIp) {
        this.id = id;
        this.numero = numero;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.fechaIngreso = fechaIngreso;
        this.anho = anho;
        this.activo = activo;
        this.copiaArchivo = copiaArchivo;
        this.audLogin = audLogin;
        this.audNumIp = audNumIp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public short getAnho() {
        return anho;
    }

    public void setAnho(short anho) {
        this.anho = anho;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public String getMotivoAnulado() {
        return motivoAnulado;
    }

    public void setMotivoAnulado(String motivoAnulado) {
        this.motivoAnulado = motivoAnulado;
    }

    public short getCopiaArchivo() {
        return copiaArchivo;
    }

    public void setCopiaArchivo(short copiaArchivo) {
        this.copiaArchivo = copiaArchivo;
    }

    public Date getFechaArchivo() {
        return fechaArchivo;
    }

    public void setFechaArchivo(Date fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
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

    public SbUsuario getUsuarioArchivo() {
        return usuarioArchivo;
    }

    public void setUsuarioArchivo(SbUsuario usuarioArchivo) {
        this.usuarioArchivo = usuarioArchivo;
    }

    public SbUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(SbUsuario usuario) {
        this.usuario = usuario;
    }

    public TipoBase getTipo() {
        return tipo;
    }

    public void setTipo(TipoBase tipo) {
        this.tipo = tipo;
    }

    public TipoBase getArea() {
        return area;
    }

    public void setArea(TipoBase area) {
        this.area = area;
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
        if (!(object instanceof SiDocumentos)) {
            return false;
        }
        SiDocumentos other = (SiDocumentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion; //"pe.gob.sucamec.si.data.SiDocumentos[ id=" + id + " ]";
    }

    public Long getExpediente() {
        return expediente;
    }

    public void setExpediente(Long expediente) {
        this.expediente = expediente;
    }

    public String getNumGuia() {
        return numGuia;
    }

    public void setNumGuia(String numGuia) {
        this.numGuia = numGuia;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public SiTipo getDepartamento() {
        return departamento;
    }

    public void setDepartamento(SiTipo departamento) {
        this.departamento = departamento;
    }

}
