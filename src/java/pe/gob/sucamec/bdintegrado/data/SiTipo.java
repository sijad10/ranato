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
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SI_TIPO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SiTipo.findAll", query = "SELECT t FROM SiTipo t order by t.nombre"),
    @NamedQuery(name = "SiTipo.findById", query = "SELECT t FROM SiTipo t WHERE t.id = :id"),
    @NamedQuery(name = "SiTipo.findByTipo", query = "SELECT t FROM SiTipo t WHERE t.tipo = :tipo and t.activo=1"),
    @NamedQuery(name = "SiTipo.findByTkEst", query = "SELECT t FROM SiTipo t WHERE t.tipo = 'TKEST'"),
    @NamedQuery(name = "SiTipo.findByNombre", query = "SELECT t FROM SiTipo t WHERE t.nombre = :nombre order by t.nombre"),
    @NamedQuery(name = "SiTipo.findByCodProg", query = "SELECT t FROM SiTipo t WHERE t.codProg = :codProg"),
    @NamedQuery(name = "SiTipo.findByActivo", query = "SELECT t FROM SiTipo t WHERE t.activo = :activo"),
    @NamedQuery(name = "SiTipo.findByAudLogin", query = "SELECT t FROM SiTipo t WHERE t.audLogin = :audLogin"),
    @NamedQuery(name = "SiTipo.findByAudNumIp", query = "SELECT t FROM SiTipo t WHERE t.audNumIp = :audNumIp")})
public class SiTipo implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVO")
    private short activo;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SI_TIPO")
    @SequenceGenerator(name = "SEQ_SI_TIPO", schema = "BDINTEGRADO", sequenceName = "SEQ_SI_TIPO", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "TIPO")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 15)
    @Column(name = "COD_PROG")
    private String codProg;
    @Basic(optional = false)
    @Size(min = 1, max = 20)
    @Column(name = "AUD_LOGIN")
    private String audLogin;
    @Basic(optional = false)
    @Size(min = 1, max = 40)
    @Column(name = "AUD_NUM_IP")
    private String audNumIp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<TicketObs> ticketObsList;
    @JoinColumn(name = "USUARIO_CANAL", referencedColumnName = "ID")
    @ManyToOne
    private SbUsuarioGt usuarioCanal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "canal")
    private List<Tickets> ticketsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<Tickets> ticketsList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<Tickets> ticketsList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tramite")
    private List<Tickets> ticketsList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<TicketRutas> ticketRutasList;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    //private List<SiDocumentos> documentosList;

    public SiTipo() {
    }

    public SiTipo(Long id) {
        this.id = id;
    }

    public SiTipo(Long id, String tipo, String nombre, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodProg() {
        return codProg;
    }

    public void setCodProg(String codProg) {
        this.codProg = codProg;
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

    public SbUsuarioGt getUsuarioCanal() {
        return usuarioCanal;
    }

    public void setUsuarioCanal(SbUsuarioGt usuarioCanal) {
        this.usuarioCanal = usuarioCanal;
    }

    @XmlTransient
    public List<Tickets> getTicketsList() {
        return ticketsList;
    }

    public void setTicketsList(List<Tickets> ticketsList) {
        this.ticketsList = ticketsList;
    }

    @XmlTransient
    public List<Tickets> getTicketsList1() {
        return ticketsList1;
    }

    public void setTicketsList1(List<Tickets> ticketsList1) {
        this.ticketsList1 = ticketsList1;
    }

    @XmlTransient
    public List<Tickets> getTicketsList2() {
        return ticketsList2;
    }

    public void setTicketsList2(List<Tickets> ticketsList2) {
        this.ticketsList2 = ticketsList2;
    }

    @XmlTransient
    public List<Tickets> getTicketsList3() {
        return ticketsList3;
    }

    public void setTicketsList3(List<Tickets> ticketsList3) {
        this.ticketsList3 = ticketsList3;
    }

    @XmlTransient
    public List<TicketRutas> getTicketRutasList() {
        return ticketRutasList;
    }

    public void setTicketRutasList(List<TicketRutas> ticketRutasList) {
        this.ticketRutasList = ticketRutasList;
    }

    /*
    @XmlTransient
    public List<SiDocumentos> getDocumentosList() {
        return documentosList;
    }

    public void setDocumentosList(List<SiDocumentos> documentosList) {
        this.documentosList = documentosList;
    }
    */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SiTipo)) {
            return false;
        }
        SiTipo other = (SiTipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;//"pe.gob.sucamec.si.data.SiTipo[ id=" + id + " ]";
    }

    public short getActivo() {
        return activo;
    }

    public void setActivo(short activo) {
        this.activo = activo;
    }

}
