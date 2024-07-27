/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.gob.sucamec.tickets.jsf;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;


@Named("graficosTicketsController")
@SessionScoped
public class GraficosTicketsController implements Serializable {

    @EJB
    private pe.gob.sucamec.bdintegrado.bean.GraficosTicketsFacade ejbFacade;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd/MM");
    private PieChartModel pieTicketsTipo, pieTicketsAreas, pieTicketsCerrado,
            pieTicketsCanal, pieTicketsUsuario, pieObsUsuario, pieTicketsDias, pieTicketsTramite;
    private LineChartModel graficoTicketsPorDia;
    private Date fechaIni, fechaFin;
    private String area, canal, cerrado;
    private String[] usuarios;
    private DataModel datos, datosObs;

    private boolean mostrar;

    public DataModel getDatosObs() {
        return datosObs;
    }

    public DataModel getDatos() {
        return datos;
    }

    public PieChartModel getPieTicketsTramite() {
        return pieTicketsTramite;
    }

    public LineChartModel getGraficoTicketsPorDia() {
        return graficoTicketsPorDia;
    }

    public PieChartModel getPieTicketsDias() {
        return pieTicketsDias;
    }

    public PieChartModel getPieObsUsuario() {
        return pieObsUsuario;
    }

    public PieChartModel getPieTicketsUsuario() {
        return pieTicketsUsuario;
    }

    public PieChartModel getPieTicketsCanal() {
        return pieTicketsCanal;
    }

    public PieChartModel getPieTicketsCerrado() {
        return pieTicketsCerrado;
    }

    public String getCerrado() {
        return cerrado;
    }

    public void setCerrado(String cerrado) {
        this.cerrado = cerrado;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String[] getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(String[] usuarios) {
        this.usuarios = usuarios;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public GraficosTicketsController() {

    }

    public String nuevoReporte() {
        fechaIni = null;
        fechaFin = null;
        mostrar = true;
        usuarios = null;
        area = null;
        canal = null;
        cerrado = null;
        return resumen();
    }

    public String resumen() {
        if ((fechaIni == null) || (fechaFin == null)) {
            Calendar c = Calendar.getInstance();
            fechaFin = c.getTime();
            c.add(Calendar.MONTH, -1);
            fechaIni = c.getTime();
        }
        mostrar = true;
        crearPieTicketsAreas();
        crearPieTicketsTipo();
        crearPieTicketsCerrado();
        crearPieTicketsCanal();
        crearPieTicketsUsuario();
        crearPieObsUsuario();
        crearPieTicketsDias();
        crearPieTicketsTramite();
        crearGraficoTicketsPorDia();
        obtenerDatosTickets();
        obtenerDatosObs();
        
        return "/aplicacion/sistemaIntegrado/tickets/Resumen";
    }

    public PieChartModel getPieTicketsAreas() {
        return pieTicketsAreas;
    }

    public PieChartModel getPieTicketsTipo() {
        return pieTicketsTipo;
    }

    public void pieTicketsAreasSelect(ItemSelectEvent e) {
        area = (String) pieTicketsAreas.getData().keySet().toArray()[e.getItemIndex()];
        resumen();
    }

    public void pieTicketsCanalSelect(ItemSelectEvent e) {
        canal = (String) pieTicketsCanal.getData().keySet().toArray()[e.getItemIndex()];
        resumen();
    }

    public void pieTicketsUsuarioSelect(ItemSelectEvent e) {
        usuarios = new String[1];
        usuarios[0] = (String) pieTicketsUsuario.getData().keySet().toArray()[e.getItemIndex()];
        resumen();
    }

    public void pieTicketsCerradoSelect(ItemSelectEvent e) {
        cerrado = (String) pieTicketsCerrado.getData().keySet().toArray()[e.getItemIndex()];
        if (cerrado.equals("SI")) {
            cerrado = "1";
        } else {
            cerrado = "0";
        }
        resumen();
    }

    private void crearPieTicketsAreas() {
        List<Map> r = ejbFacade.countTicketsAreas(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsAreas = new PieChartModel();
        for (Map m : r) {
            pieTicketsAreas.set((String) m.get("area"), (long) m.get("c"));
        }
        pieTicketsAreas.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsArea"));
        pieTicketsAreas.setLegendPosition("w");
        pieTicketsAreas.setShowDataLabels(true);
    }

    private void crearPieTicketsTipo() {
        List<Map> r = ejbFacade.countTicketsTipo(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsTipo = new PieChartModel();
        for (Map m : r) {
            pieTicketsTipo.set((String) m.get("tipo"), (long) m.get("c"));
        }
        pieTicketsTipo.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsTipo"));
        pieTicketsTipo.setLegendPosition("w");
        pieTicketsTipo.setShowDataLabels(true);
    }

    private void crearPieTicketsCerrado() {
        List<Map> r = ejbFacade.countTicketsCerrado(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsCerrado = new PieChartModel();
        for (Map m : r) {
            pieTicketsCerrado.set((short) m.get("cerrado") == 0 ? "NO" : "SI", (long) m.get("c"));
        }
        pieTicketsCerrado.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsCerrados"));
        pieTicketsCerrado.setLegendPosition("w");
        pieTicketsCerrado.setShowDataLabels(true);
    }

    private void crearPieTicketsCanal() {
        List<Map> r = ejbFacade.countTicketsCanal(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsCanal = new PieChartModel();
        for (Map m : r) {
            String c = (String) m.get("canal");
            pieTicketsCanal.set(c, (long) m.get("c"));
        }
        pieTicketsCanal.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsCanal"));
        pieTicketsCanal.setLegendPosition("w");
        pieTicketsCanal.setShowDataLabels(true);
    }

    private void crearPieTicketsTramite() {
        List<Map> r = ejbFacade.countTicketsTramite(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsTramite = new PieChartModel();
        for (Map m : r) {
            String c = (String) m.get("tramite");
            pieTicketsTramite.set(c, (long) m.get("c"));
        }
        pieTicketsTramite.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsTramite"));
        pieTicketsTramite.setLegendPosition("w");
        pieTicketsTramite.setShowDataLabels(true);
    }

    private void crearPieTicketsUsuario() {
        List<Map> r = ejbFacade.countTicketsUsuario(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsUsuario = new PieChartModel();
        for (Map m : r) {
            pieTicketsUsuario.set((String) m.get("usuario"), (long) m.get("c"));
        }
        pieTicketsUsuario.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsUsuario"));
        pieTicketsUsuario.setLegendPosition("w");
        pieTicketsUsuario.setShowDataLabels(true);
    }

    private void crearPieObsUsuario() {
        List<Map> r = ejbFacade.countObsUsuario(fechaIni, fechaFin, usuarios);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieObsUsuario = new PieChartModel();
        for (Map m : r) {
            pieObsUsuario.set((String) m.get("usuario"), (long) m.get("c"));
        }
        pieObsUsuario.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloObsUsuario"));
        pieObsUsuario.setLegendPosition("w");
        pieObsUsuario.setShowDataLabels(true);
    }

    private void crearPieTicketsDias() {
        List<Map> r = ejbFacade.countTicketsDias(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        if (r.isEmpty()) {
            mostrar = false;
        }
        pieTicketsDias = new PieChartModel();
        long n[] = new long[8];
        for (Map m : r) {
            int d = ((BigDecimal) m.get("dias")).intValue();
            int cerrado = (short) m.get("cerrado");
            if (cerrado == 0) {
                n[7] += (long) m.get("c");
            } else if (d <= 5) {
                n[d] += (long) m.get("c");
            } else {
                n[6] += (long) m.get("c");
            }
        }
        for (int i = 0; i < n.length; i++) {
            String s = "" + i + " dias";
            if (i == 0) {
                s = " < 1 dia";
            }
            if (i == 6) {
                s = " > 6 dias";
            }
            if (i == 7) {
                s = " pendiente";
            }
            pieTicketsDias.set(s, n[i]);
        }

        pieTicketsDias.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloTicketsNumeroDias"));
        pieTicketsDias.setLegendPosition("w");
        pieTicketsDias.setShowDataLabels(true);
    }

    private void crearGraficoTicketsPorDia() {
        List<Map> r = ejbFacade.getTicketsPorDia(fechaIni, fechaFin, area, canal, usuarios, cerrado);
        ChartSeries cs;

        if (r.isEmpty()) {
            mostrar = false;
        }

        graficoTicketsPorDia = new LineChartModel();
        cs = new ChartSeries("Total");
        for (Map m : r) {
            String d = m.get("dias").toString().substring(5);
            cs.set(d, (long) m.get("c"));
        }
        cs.set("2", 10);
        graficoTicketsPorDia.addSeries(cs);

        graficoTicketsPorDia.setTitle(ResourceBundle.getBundle("/BundleIntegrado").getString("TituloResumenCasos"));
        graficoTicketsPorDia.setLegendPosition("w");
        graficoTicketsPorDia.setShowPointLabels(true);
        graficoTicketsPorDia.getAxes().put(AxisType.X, new CategoryAxis("Dias"));
        Axis yAxis = graficoTicketsPorDia.getAxis(AxisType.Y);
        yAxis.setLabel("Tickets");
        yAxis.setMin(0);
        //yAxis.setMax(180);
    }

    private void obtenerDatosObs() {
        datosObs = new ListDataModel(ejbFacade.getObsData(fechaIni, fechaFin, usuarios));
    }

    private void obtenerDatosTickets() {
        datos = new ListDataModel(ejbFacade.getTicketsData(fechaIni, fechaFin, area, canal, usuarios, cerrado));
    }
}
