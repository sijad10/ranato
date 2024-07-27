/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.sistemabase.data;

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
import pe.gob.sucamec.bdintegrado.data.SbPersonaGt;

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_MEDIO_CONTACTO", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbMedioContacto.findAll", query = "SELECT s FROM SbMedioContacto s"),
    @NamedQuery(name = "SbMedioContacto.findById", query = "SELECT s FROM SbMedioContacto s WHERE s.id = :id"),
    @NamedQuery(name = "SbMedioContacto.findByValor", query = "SELECT s FROM SbMedioContacto s WHERE s.valor = :valor"),
    @NamedQuery(name = "SbMedioContacto.findByObservacion", query = "SELECT s FROM SbMedioContacto s WHERE s.observacion = :observacion"),
    @NamedQuery(name = "SbMedioContacto.findByActivo", query = "SELECT s FROM SbMedioContacto s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbMedioContacto.findByAudLogin", query = "SELECT s FROM SbMedioContacto s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbMedioContacto.findByAudNumIp", query = "SELECT s FROM SbMedioContacto s WHERE s.audNumIp = :audNumIp")})
public class SbMedioContacto implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_MEDIO_CONTACTO")
    @SequenceGenerator(name = "SEQ_SB_MEDIO_CONTACTO", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_MEDIO_CONTACTO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "VALOR")
    private String valor;
    @JoinColumn(name = "TIPO_PRIORIDAD", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoPrioridad;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "HABILITADO")
//    private short habilitado;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
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
    @JoinColumn(name = "PERSONA_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPersonaGt personaId;
    @JoinColumn(name = "TIPO_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbTipo tipoId;

    public SbMedioContacto() {
    }

    public SbMedioContacto(Long id) {
        this.id = id;
    }

    public SbMedioContacto(Long id, String valor, short habilitado, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.valor = valor;
        //this.habilitado = habilitado;
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

//    public short getHabilitado() {
//        return habilitado;
//    }
//
//    public void setHabilitado(short habilitado) {
//        this.habilitado = habilitado;
//    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public SbPersonaGt getPersonaId() {
        return personaId;
    }

    public void setPersonaId(SbPersonaGt personaId) {
        this.personaId = personaId;
    }

    public SbTipo getTipoId() {
        return tipoId;
    }

    public void setTipoId(SbTipo tipoId) {
        this.tipoId = tipoId;
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
        if (!(object instanceof SbMedioContacto)) {
            return false;
        }
        SbMedioContacto other = (SbMedioContacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbMedioContacto[ id=" + id + " ]";
    }

    /**
     * @return the tipoPrioridad
     */
    public SbTipo getTipoPrioridad() {
        return tipoPrioridad;
    }

    /**
     * @param tipoPrioridad the tipoPrioridad to set
     */
    public void setTipoPrioridad(SbTipo tipoPrioridad) {
        this.tipoPrioridad = tipoPrioridad;
    }
    
}
