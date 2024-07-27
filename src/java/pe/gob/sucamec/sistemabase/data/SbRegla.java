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

/**
 *
 * @author Renato
 */
@Entity
@Customizer(pe.gob.sucamec.sistemabase.seguridad.AuditoriaEntidad.class)
@Table(name = "SB_REGLA", catalog = "", schema = "BDINTEGRADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SbRegla.findAll", query = "SELECT s FROM SbRegla s"),
    @NamedQuery(name = "SbRegla.findById", query = "SELECT s FROM SbRegla s WHERE s.id = :id"),
    @NamedQuery(name = "SbRegla.findByNombre", query = "SELECT s FROM SbRegla s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SbRegla.findByTabla", query = "SELECT s FROM SbRegla s WHERE s.tabla = :tabla"),
    @NamedQuery(name = "SbRegla.findByCampo", query = "SELECT s FROM SbRegla s WHERE s.campo = :campo"),
    @NamedQuery(name = "SbRegla.findByCondicion", query = "SELECT s FROM SbRegla s WHERE s.condicion = :condicion"),
    @NamedQuery(name = "SbRegla.findByValor", query = "SELECT s FROM SbRegla s WHERE s.valor = :valor"),
    @NamedQuery(name = "SbRegla.findByCampo2", query = "SELECT s FROM SbRegla s WHERE s.campo2 = :campo2"),
    @NamedQuery(name = "SbRegla.findByCondicion2", query = "SELECT s FROM SbRegla s WHERE s.condicion2 = :condicion2"),
    @NamedQuery(name = "SbRegla.findByValor2", query = "SELECT s FROM SbRegla s WHERE s.valor2 = :valor2"),
    @NamedQuery(name = "SbRegla.findByCampoFecha", query = "SELECT s FROM SbRegla s WHERE s.campoFecha = :campoFecha"),
    @NamedQuery(name = "SbRegla.findByHoras", query = "SELECT s FROM SbRegla s WHERE s.horas = :horas"),
    @NamedQuery(name = "SbRegla.findByRespuesta", query = "SELECT s FROM SbRegla s WHERE s.respuesta = :respuesta"),
    @NamedQuery(name = "SbRegla.findByOrden", query = "SELECT s FROM SbRegla s WHERE s.orden = :orden"),
    @NamedQuery(name = "SbRegla.findByActivo", query = "SELECT s FROM SbRegla s WHERE s.activo = :activo"),
    @NamedQuery(name = "SbRegla.findByAudLogin", query = "SELECT s FROM SbRegla s WHERE s.audLogin = :audLogin"),
    @NamedQuery(name = "SbRegla.findByAudNumIp", query = "SELECT s FROM SbRegla s WHERE s.audNumIp = :audNumIp")})
public class SbRegla implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SB_REGLA")
    @SequenceGenerator(name = "SEQ_SB_REGLA", schema = "BDINTEGRADO", sequenceName = "SEQ_SB_REGLA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "TABLA")
    private String tabla;
    @Size(max = 50)
    @Column(name = "CAMPO")
    private String campo;
    @Size(max = 10)
    @Column(name = "CONDICION")
    private String condicion;
    @Size(max = 50)
    @Column(name = "VALOR")
    private String valor;
    @Size(max = 50)
    @Column(name = "CAMPO2")
    private String campo2;
    @Size(max = 10)
    @Column(name = "CONDICION2")
    private String condicion2;
    @Size(max = 50)
    @Column(name = "VALOR2")
    private String valor2;
    @Size(max = 50)
    @Column(name = "CAMPO_FECHA")
    private String campoFecha;
    @Column(name = "HORAS")
    private Long horas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RESPUESTA")
    private short respuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDEN")
    private int orden;
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
    @JoinColumn(name = "PAGINA_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SbPagina paginaId;
    @JoinColumn(name = "PERFIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private SbPerfil perfilId;

    public SbRegla() {
    }

    public SbRegla(Long id) {
        this.id = id;
    }

    public SbRegla(Long id, String nombre, String tabla, String campo, String condicion, String valor, short respuesta, int orden, short activo, String audLogin, String audNumIp) {
        this.id = id;
        this.nombre = nombre;
        this.tabla = tabla;
        this.campo = campo;
        this.condicion = condicion;
        this.valor = valor;
        this.respuesta = respuesta;
        this.orden = orden;
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

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public String getCondicion2() {
        return condicion2;
    }

    public void setCondicion2(String condicion2) {
        this.condicion2 = condicion2;
    }

    public String getValor2() {
        return valor2;
    }

    public void setValor2(String valor2) {
        this.valor2 = valor2;
    }

    public String getCampoFecha() {
        return campoFecha;
    }

    public void setCampoFecha(String campoFecha) {
        this.campoFecha = campoFecha;
    }

    public Long getHoras() {
        return horas;
    }

    public void setHoras(Long horas) {
        this.horas = horas;
    }

    public short getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(short respuesta) {
        this.respuesta = respuesta;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
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

    public SbPagina getPaginaId() {
        return paginaId;
    }

    public void setPaginaId(SbPagina paginaId) {
        this.paginaId = paginaId;
    }

    public SbPerfil getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(SbPerfil perfilId) {
        this.perfilId = perfilId;
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
        if (!(object instanceof SbRegla)) {
            return false;
        }
        SbRegla other = (SbRegla) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.gob.sucamec.sistemabase.data.SbRegla[ id=" + id + " ]";
    }
    
}
