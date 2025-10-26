package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mariadb://localhost:3306/concessionary_aed"; //enlace de la base de datos
    private static final String USER = "root";
    private static final String PASSWORD = ""; // tu contraseña si tiene

    public static Connection getConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado correctamente a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar:");
            e.printStackTrace();
        }
        return conexion;
    }

    public static void main(String[] args) {
        getConexion();
    }
}