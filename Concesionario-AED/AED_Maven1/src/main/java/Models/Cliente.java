package Models;



import java.util.List;

public class Cliente {
    private String id;
    private String nif;
    private String nombre;
    private String direccion;
    private String ciudad;
    private List<String> telefonos;

    // getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public List<String> getTelefonos() { return telefonos; }
    public void setTelefonos(List<String> telefonos) { this.telefonos = telefonos; }
    
    
    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
