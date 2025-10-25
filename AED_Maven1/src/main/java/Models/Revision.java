package Models;

import java.time.LocalDate;
import java.util.Map;

public class Revision {
    private String id;
    private String idCliente;
    private String idCamion;
    private Map<String, Boolean> checklist;
    private String detalles;
    private LocalDate fechaRevision;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }
    public String getIdCamion() { return idCamion; }
    public void setIdCamion(String idCamion) { this.idCamion = idCamion; }
    public Map<String, Boolean> getChecklist() { return checklist; }
    public void setChecklist(Map<String, Boolean> checklist) { this.checklist = checklist; }
    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }
    public LocalDate getFechaRevision() {return fechaRevision;}
    public void setFechaRevision(LocalDate fechaRevision) {this.fechaRevision = fechaRevision;}
}
