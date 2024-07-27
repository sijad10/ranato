package pe.gob.sucamec.sel.disca.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pe.gob.sucamec.bdintegrado.bean.AmaTipoLicenciaFacade;
import pe.gob.sucamec.bdintegrado.data.AmaTipoLicencia;
import pe.gob.sucamec.sel.disca.jsf.util.EstadoCrud;
import pe.gob.sucamec.sel.disca.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("dcGsspVigilantesController")
@SessionScoped

/**
 * Clase con DcGsspVigilantesController instanciada como
 * dcGsspVigilantesController. Contiene funciones utiles para la entidad
 * DcGsspVigilantes. Esta vinculada a las páginas DcGsspVigilantes/create.xhtml,
 * DcGsspVigilantes/update.xhtml, DcGsspVigilantes/list.xhtml,
 * DcGsspVigilantes/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class DcGsspVigilantesController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, tipo, estadoLic;

    private byte[] foto;
    private byte[] fotoByte;

    /**
     * Registro actual para editar o ver.
     */
    ArrayRecord registro, arma;

    /**
     * Lista de resultados de la búsqueda
     */
    ListDataModel<ArrayRecord> resultados = null,
            armas = null, historico = null, cursos = null;

    private List<AmaTipoLicencia> listLicVigentes;

    @Inject
    LoginController loginController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.sel.disca.beans.ConsultasDiscaFacade consultasDiscaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroCursoFacade ejbSspRegistroCursoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCarneFacade ejbSspCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;
    @EJB
    private AmaTipoLicenciaFacade ejbAmaTipoLicenciaFacade;

    public String getEstadoLic() {
        return estadoLic;
    }

    public void setEstadoLic(String estadoLic) {
        this.estadoLic = estadoLic;
    }

    public ArrayRecord getArma() {
        return arma;
    }

    public void setArma(ArrayRecord arma) {
        this.arma = arma;
    }

    public ListDataModel<ArrayRecord> getCursos() {
        return cursos;
    }

    public void setCursos(ListDataModel<ArrayRecord> cursos) {
        this.cursos = cursos;
    }

    public ListDataModel<ArrayRecord> getHistorico() {
        return historico;
    }

    public void setHistorico(ListDataModel<ArrayRecord> historico) {
        this.historico = historico;
    }

    /**
     * Tipo de busqueda
     *
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void crearFoto() {
        try {
            String nombreFoto = "";
            if (registro != null) {
                if (registro.get("NOMBRE_FOTO") != null && !registro.get("NOMBRE_FOTO").toString().equals("S/F")) {
                    nombreFoto = registro.get("NOMBRE_FOTO").toString();                    
                    
                    try {
                        nombreFoto = nombreFoto.substring(0,nombreFoto.lastIndexOf('.')) + nombreFoto.substring(nombreFoto.lastIndexOf('.'), nombreFoto.length()).toUpperCase();
                        fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + nombreFoto ));
                        foto = JsfUtil.oracleTiffenJpg(fotoByte, 180, 240);
                    } catch (Exception e) {
                        nombreFoto = nombreFoto.substring(0,nombreFoto.lastIndexOf('.')) + nombreFoto.substring(nombreFoto.lastIndexOf('.'), nombreFoto.length()).toLowerCase();
                        fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + nombreFoto ));
                        foto = JsfUtil.oracleTiffenJpg(fotoByte, 180, 240);
                    }
                } else {
                    foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(registro.get("COD_USR").toString(), true), 180, 240);
                }
            }
        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("El vigilante no cuenta con foto.");
        }
    }

    public StreamedContent exportVigilantePdf() {
        try {
            ArrayRecord datos = registro;
            String jasper = "/aplicacion/consultas/DcGsspTotVigilantes/reporte/ReporteVigilante.jasper";
            List<Map> lp = new ArrayList();
            Map vig = new HashMap();
            vig.put("nroCarne", datos.get("NRO_CRN_VIG") == null ? "" : datos.get("NRO_CRN_VIG").toString());
            vig.put("modalidad", datos.get("DES_MOD"));
            vig.put("ruc", datos.get("RUC") == null ? "" : datos.get("RUC").toString());
            vig.put("empresa", datos.get("EMPRESA"));
            vig.put("portador", datos.get("COD_USR") + " - " + datos.get("NOMBRE"));
            vig.put("nroExp", datos.get("NRO_EXP") == null ? "" : datos.get("NRO_EXP").toString());
            vig.put("anioExp", datos.get("ANO_EXP") == null ? "" : ((90 <= Integer.valueOf(datos.get("ANO_EXP").toString()) ? "19" : "20") + datos.get("ANO_EXP").toString()));
            vig.put("fecEmi", (datos.get("FEC_EMI") == null ? "" : fechaSimple((Date) datos.get("FEC_EMI"))));
            //vig.put("fecVenc", (datos.get("FEC_VENC") == null ? "" : obtenerFechaFinCarne((Date) datos.get("FEC_VENC"))));    // rfv - descomentar
            vig.put("fecVenc", "");  // rfv - eliminar
            vig.put("tipoPer", datos.get("DES_USR"));
            vig.put("estado", obtenerEstadoVigilante(datos));
            lp.add(vig);
            try {
                return JsfUtil.generarReportePdf(jasper, lp, parametrosPdf(datos), datos.get("id") + "_borrador.pdf");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsfUtil.errorDescarga("Error: DcGsspTotVigilantesController.reporte :", ex);
        }
        return null;
    }

    /**
     * FUNCION PARA OBTENER LOS PARAMETROS DEL REPORTE
     *
     * @author Gino Chávez
     * @version 2.0
     * @param item Registro a crear reporte
     * @param nombreJasper Nombre de reporte (.jasper)
     * @return Flag de proceso de subir pdf
     */
    private HashMap parametrosPdf(ArrayRecord ppdf) {
        ExternalContext ec = (ExternalContext) FacesContext.getCurrentInstance().getExternalContext();
        HashMap ph = new HashMap();
        ph.put("TIPO_REP", ppdf.get("TIPO") == null ? null : ppdf.get("TIPO").toString());
        ph.put("P_LISTA_LICENCIAS", listLicVigentes);
        ph.put("P_LISTA_HIS_ARMAS", obtenerListaLicencias(armas));
        ph.put("P_LISTA_EMPRESAS", obtenerListaEmpresas(historico));
        ph.put("P_LISTA_CURSOS", obtenerListaCursos(cursos));
        ph.put("P_FECHA_CONSULTA", new Date());
        ph.put("P_FOTO", foto == null ? null : new ByteArrayInputStream(foto));
        ph.put("LOGO_SUCAMEC", ec.getRealPath("/resources/imagenes/logo_sucamec.png"));
        ph.put("SELLO_AGUA", ec.getRealPath("/resources/imagenes/img_sello_vigilantes.png"));
        return ph;
    }

    public List<Map> obtenerListaLicencias(ListDataModel<ArrayRecord> listIn) {
        List<Map> listaArmasHis = null;
        if (listIn != null && listIn.getRowCount() > 0) {
            listaArmasHis = new ArrayList<>();
            Map arma = null;
            for (ArrayRecord item : listIn) {
                arma = new HashMap();
                arma.put("lic", item.get("NRO_LIC") == null ? "" : item.get("NRO_LIC").toString());
                arma.put("arma", (item.get("TIPO_ARMA") + " " + item.get("MARCA") + " " + item.get("MODELO") + " " + item.get("CALIBRE")));
                arma.put("serie", item.get("NRO_SERIE"));
                arma.put("fecEmi", (item.get("FEC_EMISION") == null ? "" : fechaSimple((Date) item.get("FEC_EMISION"))));
                arma.put("fecVenc", (item.get("FEC_VENCIMIENTO") == null ? "" : fechaSimple((Date) item.get("FEC_VENCIMIENTO"))));
                arma.put("portador", item.get("DOC_PORTADOR") + " - " + item.get("PORTADOR"));
                arma.put("situacion", item.get("SITUACION"));
                arma.put("estado", item.get("ESTADO"));
                listaArmasHis.add(arma);
            }
        }
        return listaArmasHis;
    }

    public List<Map> obtenerListaEmpresas(ListDataModel<ArrayRecord> listIn) {
        List<Map> listaEmpresasHis = null;
        if (listIn != null && listIn.getRowCount() > 0) {
            listaEmpresasHis = new ArrayList<>();
            Map emp = null;
            for (Map item : listIn) {
                emp = new HashMap();
                emp.put("ruc", item.get("RUC") == null ? "" : item.get("RUC").toString());
                emp.put("rzonSoc", item.get("RZN_SOC"));
                emp.put("descMod", item.get("DES_MOD"));
                emp.put("fecEmi", (item.get("FEC_EMI") == null ? "" : fechaSimple((Date) item.get("FEC_EMI"))));
                emp.put("fecVenc", (item.get("FEC_VENC") == null ? "" : fechaSimple((Date) item.get("FEC_VENC"))));
                emp.put("fecBaja", (item.get("FEC_BAJA") == null ? "" : fechaSimple((Date) item.get("FEC_BAJA"))));
                listaEmpresasHis.add(emp);
            }
        }
        return listaEmpresasHis;
    }

    public List<Map> obtenerListaCursos(ListDataModel<ArrayRecord> listIn) {
        List<Map> listaCursos = null;
        if (listIn != null && listIn.getRowCount() > 0) {
            listaCursos = new ArrayList<>();
            Map curso = null;
            for (Map item : listIn) {
                curso = new HashMap();
                curso.put("ruc", item.get("RUC") == null ? "" : item.get("RUC").toString());
                curso.put("rzonSoc", item.get("RZN_SOC"));
                curso.put("eval", item.get("EVALUACION"));
                curso.put("tipo", item.get("TIPO"));
                curso.put("fecFin", (item.get("FEC_FINAL") == null ? "" : fechaSimple((Date) item.get("FEC_FINAL"))));
                curso.put("fecVenc", (item.get("FEC_VENC") == null ? "" : fechaSimple((Date) item.get("FEC_VENC"))));
                curso.put("vigente", item.get("VIGENTE"));
                listaCursos.add(curso);
            }
        }
        return listaCursos;
    }

    public String fechaSimple(Date fecha) {
        if (fecha != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        }
        return null;
    }

    public ListDataModel<ArrayRecord> getArmas() {
        return armas;
    }

    public void setArmas(ListDataModel<ArrayRecord> armas) {
        this.armas = armas;
    }

    public DefaultStreamedContent getFoto() {
        if (foto == null) {
            try {
                return new DefaultStreamedContent(FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/imagenes/ninguno_320.jpg"), "image/jpg");
            } catch (Exception ex) {
            }
        }
        return new DefaultStreamedContent(new ByteArrayInputStream(foto), "image/jpg");
    }

    public String obtenerEstadoVigilante(Map vig) {
        if (vig != null) {
            String estado = "" + vig.get("ESTADO");
            if ("H".equals(vig.get("TIPO_REG"))) {
                estado = "CESADO";
            }
            return estado;
        }
        return "";
    }

    /**
     * Propiedad para el registro actual.
     *
     * @return El registro actual.
     */
    public ArrayRecord getRegistro() {
        return registro;
    }

    /**
     * Propiedad para el registro actual
     *
     * @param registro El nuevo registro
     */
    public void setRegistro(ArrayRecord registro) {
        this.registro = registro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @return el filtro de busquedas
     */
    public String getFiltro() {
        return filtro;
    }

    /**
     * Propiedad para el filtro de busquedas
     *
     * @param filtro el nuevo filtro de busquedas
     */
    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    /**
     * Propiedad para el estado del CRUD.
     *
     * @return el estado actual del CRUD.
     */
    public EstadoCrud getEstado() {
        return estado;
    }

    /**
     * Propiedad para los resultados de la búsqueda del CRUD.
     *
     * @return Los resultados de la búsqueda.
     */
    public ListDataModel<ArrayRecord> getResultados() {
        return resultados;
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscar() {
        resultados = null;
        mostrarBuscar();
        return "/aplicacion/consultas/DcGsspMisVigilantes/List";
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarCargarDatos() {
        resultados = new ListDataModel(consultasDiscaFacade.obtenerVigilante(loginController.getUsuario().getNumDoc(), "VIGENTE"));//loginController.getUsuario().getPersona().getRuc()
        if (mostrarVer()) {
            return "/aplicacion/consultas/DcGsspMisVigilantes/ViewVig";
        }
        return null;
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        estado = EstadoCrud.BUSCAR;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        String numdoc = null, carne = null;
        if (tipo.equals("numdoc")) {
            numdoc = filtro;
        }
        if (tipo.equals("carne")) {
            carne = filtro;
        }
        resultados = new ListDataModel(ejbSspCarneFacade.listarVigilantes(numdoc, carne, estadoLic, loginController.getUsuario().getNumDoc()));
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void regresarVer() {
        estado = EstadoCrud.VER;
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public Boolean mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        if (registro != null) {
            String codUsr = registro.get("COD_USR").toString();
            listLicVigentes = ejbAmaTipoLicenciaFacade.selectLicenciasUso(codUsr);
            armas = new ListDataModel(
                    consultasDiscaFacade.listarArmasDeFuego(null, null, codUsr, loginController.getUsuario().getNumDoc(), null)
            );
            historico = new ListDataModel(ejbSspCarneFacade.listarHistVigilantes(codUsr));
            cursos = null;
            cursos = new ListDataModel(ejbSspRegistroCursoFacade.listarCursosVigilantesIntegrado(codUsr));
            crearFoto();
            estado = EstadoCrud.VER;
            return Boolean.TRUE;
        } else {
            JsfUtil.mensajeAdvertencia("No existe información de Vigilante.");
        }
        return Boolean.FALSE;
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVerArma() {
        arma = (ArrayRecord) armas.getRowData();
        estado = EstadoCrud.VER_ARMA;
    }

    /**
     * Obtiene el valor de un campo del hashmap, por algun motivo (bug) el xhtml
     * no permite esto directamente.
     *
     * @param r El resultado del native query como Map.
     * @param c El campo que se desea obtener.
     * @return El valor del campo como Object
     */
    public Object valorCampo(ArrayRecord r, String c) {
        return r.get(c);
    }

    /**
     * Constructor
     *
     */
    public DcGsspVigilantesController() {
        estado = EstadoCrud.BUSCAR;
    }

    public List<AmaTipoLicencia> getListLicVigentes() {
        return listLicVigentes;
    }

    public void setListLicVigentes(List<AmaTipoLicencia> listLicVigentes) {
        this.listLicVigentes = listLicVigentes;
    }
    
    public String obtenerFechaFinCarne(Date fechaFin){
        if(fechaFin == null){
            return null;
        }
        return JsfUtil.dateToString(fechaFin, "dd/MM/yyyy");
    }
    
    public String obtenerDescTipoRegistro(Map item){
       if(item == null){
            return null;
        }
        if(item.get("TIPO_REG_CODPROG").toString().equals("TP_REGIST_EMP")){
            return item.get("TIPO_REG").toString();
        }
        
        return item.get("TIPO_OPE").toString();
    }

}
