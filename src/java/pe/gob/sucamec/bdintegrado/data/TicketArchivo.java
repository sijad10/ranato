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
import org.eclipse.persistence.annotations.Customizer;
import pe.gob.sucamec.sistemabase.data.TipoBase;

/**
 *
 * @author locador463.ogtic
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "TICKET_ARCHIVO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TicketArchivo.findAll", query = "SELECT t FROM TicketArchivo t"),
    @NamedQuery(name = "TicketArchivo.findById", query = "SELECT t FROM TicketArchivo t WHERE t.id = :id"),
    @NamedQuery(name = "TicketArchivo.findByTipoId", query = "SELECT t FROM TicketArchivo t WHERE t.tipoId = :tipoId"),
    @NamedQuery(name = "TicketArchivo.findByNombre", query = "SELECT t FROM TicketArchivo t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TicketArchivo.findByActivo", query = "SELECT t FROM TicketArchivo t WHERE t.activo = :activo"),
    @NamedQuery(name = "TicketArchivo.findByAudLogin", query = "SELECT t FROM TicketArchivo t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "TicketArchivo.findByAudNumIp", query = "SELECT t FROM TicketArchivo t WHERE t.audNumIp = :audNumIp")})
public class TicketArchivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TICKET_ARCHIVO")
    @SequenceGenerator(name = "SEQ_TICKET_ARCHIVO", schema = "BDINTEGRADO", sequenceName = "SEQ_TICKET_ARCHIVO", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NOMBRE")
    private String nombre;
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
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne
    private TipoBase tipoId;
    @JoinColumn(name = "TICKET_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Tickets ticketId;

    public TicketArchivo() {
    }

    public TicketArchivo(Long id) {
        this.id = id;
    }

    public TicketArchivo(Long id, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public TipoBase getTipoId() {
        return tipoId;
    }

    public void setTipoId(TipoBase tipoId) {
        this.tipoId = tipoId;
    }

    public Tickets getTicketId() {
        return ticketId;
    }

    public void setTicketId(Tickets ticketId) {
        this.ticketId = ticketId;
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
        if (!(object instanceof TicketArchivo)) {
            return false;
        }
        TicketArchivo other = (TicketArchivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.bdintegrado.data.TicketArchivo[ id=" + id + " ]";
    }
    
}
