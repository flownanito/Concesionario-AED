package DAO;

import Utils.ConexionDB;
import Models.Camion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CamionDAO {
    public static boolean insertarCamion(Camion c) {
        String sql = "INSERT INTO camion (id_camion, marca, modelo, color, matricula, precio_venta) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getId_camion());
            ps.setString(2, c.getMarca());
            ps.setString(3, c.getModelo());
            ps.setString(4, c.getColor());
            ps.setString(5, c.getMatricula());
            ps.setDouble(6, c.getPrecioVenta());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
            return false;
        }
    }
     
    public static boolean actualizarColor (String idCamion, String nuevoColor) {
        String sql = "UPDATE camion SET color = ? WHERE id_camion = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoColor);
            ps.setString(2, idCamion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean actualizarPrecio (String idCamion, Double nuevoPrecio) {
        String sql = "UPDATE camion SET precio_venta = ? WHERE id_camion = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoPrecio);
            ps.setString(2, idCamion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean actualizarMatricula (String idCamion, String nuevoMatricula) {
        String sql = "UPDATE camion SET matricula = ? WHERE id_camion = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoMatricula);
            ps.setString(2, idCamion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    //Acceso a contenido de tabla camiones
    public static List<Camion> obtenerTodosCamiones() {
    List<Camion> camiones = new ArrayList<>();
    String sql = "SELECT id_camion, marca, modelo, color, matricula, precio_venta FROM camion";

    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Camion c = new Camion();
            c.setId(rs.getString("id_camion"));
            c.setMarca(rs.getString("marca"));
            c.setModelo(rs.getString("modelo"));
            c.setColor(rs.getString("color"));
            c.setMatricula(rs.getString("matricula"));
            c.setPrecioVenta(rs.getDouble("precio_venta"));
            camiones.add(c);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error SQL obtenerTodosCamiones(): " + e.getMessage());
    }
    return camiones;
}
}