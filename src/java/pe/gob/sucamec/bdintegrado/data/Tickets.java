/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.bdintegrado.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.SbUsuario;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TICKETS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tickets.findAll", query = "SELECT t FROM Tickets t"),
    @NamedQuery(name = "Tickets.findById", query = "SELECT t FROM Tickets t WHERE t.id = :id"),
    @NamedQuery(name = "Tickets.findByNumero", query = "SELECT t FROM Tickets t WHERE t.numero = :numero"),
    @NamedQuery(name = "Tickets.findByNombre", query = "SELECT t FROM Tickets t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Tickets.findByEmail", query = "SELECT t FROM Tickets t WHERE t.email = :email"),
    @NamedQuery(name = "Tickets.findByDni", query = "SELECT t FROM Tickets t WHERE t.dni = :dni"),
    @NamedQuery(name = "Tickets.findByRuc", query = "SELECT t FROM Tickets t WHERE t.ruc = :ruc"),
    @NamedQuery(name = "Tickets.findByAsunto", query = "SELECT t FROM Tickets t WHERE t.asunto = :asunto"),
    @NamedQuery(name = "Tickets.findByDescripcion", query = "SELECT t FROM Tickets t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tickets.findByOficioRef", query = "SELECT t FROM Tickets t WHERE t.oficioRef = :oficioRef"),
    @NamedQuery(name = "Tickets.findByTelefono", query = "SELECT t FROM Tickets t WHERE t.telefono = :telefono"),
    @NamedQuery(name = "Tickets.findByCelular", query = "SELECT t FROM Tickets t WHERE t.celular = :celular"),
    @NamedQuery(name = "Tickets.findByExpediente", query = "SELECT t FROM Tickets t WHERE t.expediente = :expediente"),
    @NamedQuery(name = "Tickets.findByFechaIni", query = "SELECT t FROM Tickets t WHERE t.fechaIni = :fechaIni"),
    @NamedQuery(name = "Tickets.findByFechaFin", query = "SELECT t FROM Tickets t WHERE t.fechaFin = :fechaFin"),
    @NamedQuery(name = "Tickets.findByActivo", query = "SELECT t FROM Tickets t WHERE t.activo = :activo"),
    @NamedQuery(name = "Tickets.findByImportado", query = "SELECT t FROM Tickets t WHERE t.importado = :importado"),
    @NamedQuery(name = "Tickets.findByAudLogin", query = "SELECT t FROM Tickets t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "Tickets.findByAudNumIp", query = "SELECT t FROM Tickets t WHERE t.audNumIp = :audNumIp")})
public class Tickets implements Serializable {

    @OneToMany(mappedBy = "ticketId")
    private List<TicketArchivo> ticketArchivoList;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_ACT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAct;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CERRADO")
    private short cerrado;
    
    @OneToMany(mappedBy = "ticketRel")
    private List<Tickets> ticketsList;
    @JoinColumn(name = "TICKET_REL", referencedColumnName = "ID")
    @ManyToOne
    private Tickets ticketRel;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TICKETS")
    @SequenceGenerator(name = "SEQ_TICKETS", schema = "BDINTEGRADO", sequenceName = "SEQ_TICKETS", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO")
    private long numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "DNI")
    private String dni;
    @Size(max = 11)
    @Column(name = "RUC")
    private String ruc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "ASUNTO")
    private String asunto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 200)
    @Column(name = "OFICIO_REF")
    private String oficioRef;
    @Size(max = 20)
    @Column(name = "TELEFONO")
    private String telefono;
    @Size(max = 20)
    @Column(name = "CELULAR")
    private String celular;
    @Size(max = 50)
    @Column(name = "EXPEDIENTE")
    private String expediente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_INI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIni;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMPORTADO")
    private long importado;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket")
    private List<TicketObs> ticketObsList;
    @JoinColumn(name = "USUARIO_ACTUAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioActual;
    @JoinColumn(name = "USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuario usuario;
    @JoinColumn(name = "CANAL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SiTipo canal;
    @JoinColumn(name = "ESTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SiTipo estado;
    @JoinColumn(name = "TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SiTipo tipo;
    @JoinColumn(name = "TRAMITE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SiTipo tramite;
    @JoinColumn(name = "AREA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoBaseGt area;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ticket")
    private List<TicketRutas> ticketRutasList;

    public Tickets() {
    }

    public Tickets(Long id) {
        this.id = id;
    }

    public Tickets(Long id, long numero, String nombre, String dni, String asunto, String descripcion, Date fechaIni, short activo, long importado, String audLogin, String audNumIp) {
        this.id = id;
        this.numero = numero;
        this.nombre = nombre;
        this.dni = dni;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fechaIni = fechaIni;
        this.activo = activo;
        this.importado = importado;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getOficioRef() {
        return oficioRef;
    }

    public void setOficioRef(String oficioRef) {
        this.oficioRef = oficioRef;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }


    public long getImportado() {
        return importado;
    }

    public void setImportado(long importado) {
        this.importado = importado;
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
    public List<TicketObs> getTicketObsList() {
        return ticketObsList;
    }

    public void setTicketObsList(List<TicketObs> ticketObsList) {
        this.ticketObsList = ticketObsList;
    }

    public SbUsuarioGt getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(SbUsuarioGt usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public SbUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(SbUsuario usuario) {
        this.usuario = usuario;
    }

    public SiTipo getCanal() {
        return canal;
    }

    public void setCanal(SiTipo canal) {
        this.canal = canal;
    }

    public SiTipo getEstado() {
        return estado;
    }

    public void setEstado(SiTipo estado) {
        this.estado = estado;
    }

    public SiTipo getTipo() {
        return tipo;
    }

    public void setTipo(SiTipo tipo) {
        this.tipo = tipo;
    }

    public SiTipo getTramite() {
        return tramite;
    }

    public void setTramite(SiTipo tramite) {
        this.tramite = tramite;
    }

    public TipoBaseGt getArea() {
        return area;
    }

    public void setArea(TipoBaseGt area) {
        this.area = area;
    }

    @XmlTransient
    public List<TicketRutas> getTicketRutasList() {
        return ticketRutasList;
    }

    public void setTicketRutasList(List<TicketRutas> ticketRutasList) {
        this.ticketRutasList = ticketRutasList;
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
        if (!(object instanceof Tickets)) {
            return false;
        }
        Tickets other = (Tickets) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.Tickets[ id=" + id + " ]";
    }

    public Tickets getTicketRel() {
        return ticketRel;
    }

    public void setTicketRel(Tickets ticketRel) {
        this.ticketRel = ticketRel;
    }

    @XmlTransient
    public List<Tickets> getTicketsList() {
        return ticketsList;
    }

    public void setTicketsList(List<Tickets> ticketsList) {
        this.ticketsList = ticketsList;
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

    public short getCerrado() {
        return cerrado;
    }

    public void setCerrado(short cerrado) {
        this.cerrado = cerrado;
    }

    public Date getFechaAct() {
        return fechaAct;
    }

    public void setFechaAct(Date fechaAct) {
        this.fechaAct = fechaAct;
    }

    @XmlTransient
    public List<TicketArchivo> getTicketArchivoList() {
        return ticketArchivoList;
    }

    public void setTicketArchivoList(List<TicketArchivo> ticketArchivoList) {
        this.ticketArchivoList = ticketArchivoList;
    }

}
