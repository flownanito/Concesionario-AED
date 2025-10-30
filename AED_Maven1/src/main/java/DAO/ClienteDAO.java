package DAO;

import Models.Cliente;
import Utils.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // Inserta un cliente en la tabla 'cliente'
    public static boolean insertarCliente(Cliente c) {
        String sql = "INSERT INTO cliente (id_cliente, nif, nombre, direccion, ciudad) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getId());
            ps.setString(2, c.getNif());
            ps.setString(3, c.getNombre());
            ps.setString(4, c.getDireccion());
            ps.setString(5, c.getCiudad());
            ps.executeUpdate();

            // Si hay teléfonos, los insertamos en tabla aparte
            if (c.getTelefonos() != null && !c.getTelefonos().isEmpty()) {
                insertarTelefonos(c.getId(), c.getTelefonos(), con);
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error SQL ClienteDAO: " + e.getMessage());
            return false;
        }
    }

    // Inserta los teléfonos en tabla 'telefonos_cliente'
    private static void insertarTelefonos(String idCliente, List<String> telefonos, Connection con) throws SQLException {
        String sqlTel = "INSERT INTO telefonos_cliente (id_cliente, telefono) VALUES (?, ?)";
        try (PreparedStatement psTel = con.prepareStatement(sqlTel)) {
            for (String t : telefonos) {
                psTel.setString(1, idCliente);
                psTel.setString(2, t);
                psTel.addBatch();
            }
            psTel.executeBatch();
        }
    }
    
public static boolean actualizarNombre(String idCliente, String nuevoNombre) {
    String sql = "UPDATE cliente SET nombre = ? WHERE id_cliente = ?";
    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, nuevoNombre);
        ps.setString(2, idCliente);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// Actualizar solo Ciudad
public static boolean actualizarCiudad(String idCliente, String nuevaCiudad) {
    String sql = "UPDATE cliente SET ciudad = ? WHERE id_cliente = ?";
    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, nuevaCiudad);
        ps.setString(2, idCliente);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    //Acceso a contenido de tabla clientes
    public static List<Cliente> obtenerTodosClientes() {
    List<Cliente> clientes = new ArrayList<>();
    String sql = "SELECT id_cliente, nif, nombre, direccion, ciudad FROM cliente";

    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Cliente c = new Cliente();
            c.setId(rs.getString("id_cliente"));
            c.setNif(rs.getString("nif"));
            c.setNombre(rs.getString("nombre"));
            c.setDireccion(rs.getString("direccion"));
            c.setCiudad(rs.getString("ciudad"));
            clientes.add(c);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error SQL obtenerTodosClientes(): " + e.getMessage());
    }
    return clientes;
}
}