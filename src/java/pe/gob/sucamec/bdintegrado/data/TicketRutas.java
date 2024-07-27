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

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TICKET_RUTAS", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketRutas.findAll", query = "SELECT t FROM TicketRutas t"),
    @NamedQuery(name = "TicketRutas.findById", query = "SELECT t FROM TicketRutas t WHERE t.id = :id"),
    @NamedQuery(name = "TicketRutas.findByFechaHora", query = "SELECT t FROM TicketRutas t WHERE t.fechaHora = :fechaHora"),
    @NamedQuery(name = "TicketRutas.findByActivo", query = "SELECT t FROM TicketRutas t WHERE t.activo = :activo"),
    @NamedQuery(name = "TicketRutas.findByAudLogin", query = "SELECT t FROM TicketRutas t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TicketRutas.findByAudNumIp", query = "SELECT t FROM TicketRutas t WHERE t.audNumIp = :audNumIp")})
public class TicketRutas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TICKET_RUTAS")
    @SequenceGenerator(name = "SEQ_TICKET_RUTAS", schema = "BDINTEGRADO", sequenceName = "SEQ_TICKET_RUTAS", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
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
    @JoinColumn(name = "USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuario;
    @JoinColumn(name = "USUARIO_DESTINO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbUsuarioGt usuarioDestino;
    @JoinColumn(name = "TIPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SiTipo tipo;
    @JoinColumn(name = "TICKET", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Tickets ticket;

    public TicketRutas() {
    }

    public TicketRutas(Long id) {
        this.id = id;
    }

    public TicketRutas(Long id, Date fechaHora, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.fechaHora = fechaHora;
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

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
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

    public SbUsuarioGt getUsuario() {
        return usuario;
    }

    public void setUsuario(SbUsuarioGt usuario) {
        this.usuario = usuario;
    }

    public SbUsuarioGt getUsuarioDestino() {
        return usuarioDestino;
    }

    public void setUsuarioDestino(SbUsuarioGt usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }

    public SiTipo getTipo() {
        return tipo;
    }

    public void setTipo(SiTipo tipo) {
        this.tipo = tipo;
    }

    public Tickets getTicket() {
        return ticket;
    }

    public void setTicket(Tickets ticket) {
        this.ticket = ticket;
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
        if (!(object instanceof TicketRutas)) {
            return false;
        }
        TicketRutas other = (TicketRutas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.si.data.TicketRutas[ id=" + id + " ]";
    }

}
