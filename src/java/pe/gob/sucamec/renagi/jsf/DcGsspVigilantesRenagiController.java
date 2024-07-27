package pe.gob.sucamec.renagi.jsf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.primefaces.model.DefaultStreamedContent;
import pe.gob.sucamec.bdintegrado.data.SbParametro;
import pe.gob.sucamec.renagi.jsf.util.EstadoCrud;
import pe.gob.sucamec.renagi.jsf.util.JsfUtil;
import pe.gob.sucamec.sistemabase.seguridad.LogController;
import pe.gob.sucamec.sistemabase.seguridad.LoginController;

// Nombre de la instancia en la aplicacion //
@Named("dcGsspVigilantesRenagiController")
@SessionScoped

/**
 * Clase con DcGsspVigilantesRenagiController instanciada como
 * dcGsspVigilantesRenagiController. Contiene funciones utiles para la entidad
 * DcGsspVigilantes. Esta vinculada a las páginas DcGsspVigilantes/create.xhtml,
 * DcGsspVigilantes/update.xhtml, DcGsspVigilantes/list.xhtml,
 * DcGsspVigilantes/view.xhtml Nota: Las tablas deben tener la estructura de
 * Sucamec para que funcione adecuadamente, revisar si tiene el campo activo y
 * modificar las búsquedas.
 */
public class DcGsspVigilantesRenagiController implements Serializable {

    /**
     * Estado del crud: BUSCAR, CREAR, EDITAR, VER agregar mas estados de
     * acuerdo a las necesidades
     */
    EstadoCrud estado;

    private EstadoCrud estadoVerAux;
    private EstadoCrud estadoBuscarAux;

    /**
     * Filtro basico para las búsquedas
     */
    String filtro, tipo;

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

    private SbParametro paramOriDatArma;
    private String desdeMigra;
    
    @Inject
    LoginController loginController;

    @Inject
    LogController logController;

    /**
     * Facade de la clase del controller, se pueden agregar más facades de
     * acuerdo a la implementación.
     */
    @EJB
    private pe.gob.sucamec.renagi.beans.ConsultasDiscaRenagiFacade consultasDiscaFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspRegistroCursoFacade ejbSspRegistroCursoFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SspCarneFacade ejbSspCarneFacade;
    @EJB
    private pe.gob.sucamec.bdintegrado.bean.SbParametroFacade ejbSbParametroFacade;

    /**
     * Constructor
     *
     */
    public DcGsspVigilantesRenagiController() {
        estado = estadoBuscarAux;
    }

    @PostConstruct
    public void inicializar() {
        setDesdeMigra("MIGRA");
        // Parámetro origen data ARMAS
        setParamOriDatArma(ejbSbParametroFacade.obtenerParametroXSistemaCodProg("TP_ORIGDAT_ARMDIS", 5L));
        if (paramOriDatArma != null) {
            setDesdeMigra(paramOriDatArma.getValor());
        }
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

    public SbParametro getParamOriDatArma() {
        return paramOriDatArma;
    }

    public void setParamOriDatArma(SbParametro paramOriDatArma) {
        this.paramOriDatArma = paramOriDatArma;
    }

    public String getDesdeMigra() {
        return desdeMigra;
    }

    public void setDesdeMigra(String desdeMigra) {
        this.desdeMigra = desdeMigra;
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
            if (registro != null) {
                if (registro.get("NOMBRE_FOTO") != null && !registro.get("NOMBRE_FOTO").toString().equals("S/F")) {
                    fotoByte = FileUtils.readFileToByteArray(new File(ejbSbParametroFacade.obtenerParametroXNombre("Documentos_pathUpload_carnesGssp").getValor() + registro.get("NOMBRE_FOTO").toString()));
                    foto = JsfUtil.oracleTiffenJpg(fotoByte, 180, 240);
                } else {
                    foto = JsfUtil.oracleTiffenJpg(consultasDiscaFacade.selectFoto(registro.get("COD_USR").toString(), true), 180, 240);
                }
            }
        } catch (Exception e) {
            JsfUtil.mensajeAdvertencia("El vigilante no cuenta con foto.");
        }
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
                ex.printStackTrace();
            }
        }
        return new DefaultStreamedContent(new ByteArrayInputStream(foto), "image/jpg");
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
        estadoBuscarAux = EstadoCrud.BUSCAR;
        estadoVerAux = EstadoCrud.VER;
        resultados = null;
        filtro = "";
        mostrarBuscar();
        return "/aplicacion/consultasRenagi/DcGsspVigilantes/List";
    }

    /**
     * Funcion tipo 'action' para inicial una búsqueda
     *
     * @return URL de busqueda
     */
    public String direccionarBuscarTercero() {
        estadoBuscarAux = EstadoCrud.BUSCAR_TER;
        estadoVerAux = EstadoCrud.VER_TER;
        resultados = null;
        filtro = "";
        mostrarBuscar();
        return "/aplicacion/consultasRenagi/DcGsspVigilantes/ListTercero";
    }

    /**
     * Cambia el estado del controller a buscar
     */
    public void mostrarBuscar() {
        estado = estadoBuscarAux;
    }

    /**
     * Realiza una búsqueda. Llena la información en la variable resultados.
     */
    public void buscar() {
        /*String numdoc = null, carne = null, nombre = null, ruc = null;
        if (tipo.equals("numdoc")) {
            numdoc = filtro;
        }
        if (tipo.equals("carne")) {
            carne = filtro;
        }
        if (tipo.equals("nombre")) {
            nombre = filtro;
        }
        if (tipo.equals("ruc")) {
            ruc = filtro;
        }*/

        if (filtro != null && !filtro.equals("")) {
            boolean buscar = false;
            if (tipo.equals("nombre")) {
                if (filtro.trim().length() <= 3) {
                    JsfUtil.mensajeAdvertencia("Digitar por lo menos 4 carácteres para búsqueda!");
                } else {
                    buscar = true;
                }
            } else {
                buscar = true;
            }

            if (buscar) {
                logController.escribirLogBuscar("" + tipo + "\t" + filtro, "WS_VIGILANTES");
                resultados = new ListDataModel(ejbSspCarneFacade.listarVigilantesxFiltro(tipo, filtro));
            }

        } else {
            resultados = null;
        }
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void regresarVer() {
        estado = estadoVerAux;
    }

    /**
     * Funcion tipo 'actionListener' que muestra el formulario para ver un
     * registro.
     *
     */
    public void mostrarVer() {
        registro = (ArrayRecord) resultados.getRowData();
        String codUsr = registro.get("COD_USR").toString();
        
        switch (desdeMigra){
            case "DISCA":
                armas = new ListDataModel(
                    consultasDiscaFacade.listarArmasDeFuego(null, null, null, null, codUsr, null)
                );
                break;

            case "MIGRA":
                /*Comentado 01/02/2012 porque no tiene filtro de PORTADOR
                armas = new ListDataModel(
                    consultasDiscaFacade.listarArmasDeFuegoMigra(null, null, null, null, codUsr, null)
                );
                break;*/
        }        
        historico = new ListDataModel(ejbSspCarneFacade.listarHistVigilantes(codUsr));
        cursos = new ListDataModel(ejbSspRegistroCursoFacade.listarCursosVigilantesIntegrado(codUsr));
        crearFoto();
        logController.escribirLogVer("" + registro.get("NRO_CRN_VIG"), "WS_VIGILANTES");
        estado = estadoVerAux;
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

}
