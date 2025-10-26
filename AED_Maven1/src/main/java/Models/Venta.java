package Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Venta {
    private int idVenta;
    private String idCliente;
    private String idCamion;
    private String estado;
    private LocalDate fechaVenta;

    //Constructor usado desde VentaForm
    public Venta(String idCliente, String idCamion, String fecha, String estado) {
        this.idCliente = idCliente;
        this.idCamion = idCamion;
        this.estado = estado;

        // Convertir la fecha de texto a LocalDate (formato dd/MM/yyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaVenta = LocalDate.parse(fecha, formatter);
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

    public String getIdCamion() { return idCamion; }
    public void setIdCamion(String idCamion) { this.idCamion = idCamion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }
}
