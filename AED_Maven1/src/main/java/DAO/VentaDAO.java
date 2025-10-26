package DAO;

import Models.Venta;
import Utils.ConexionDB;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VentaDAO {

    public static boolean registrarVenta(Venta v) {
        String sql = "INSERT INTO venta (id_cliente, id_camion, estado, fecha_venta) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, v.getIdCliente());
            ps.setString(2, v.getIdCamion());
            ps.setString(3, v.getEstado());
            ps.setDate(4, Date.valueOf(v.getFechaVenta()));

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar la venta: " + e.getMessage());
            return false;
        }
    }
}
