package DAO;

import Models.Revision;
import Utils.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RevisionDAO {

    // Inserta los detalles en tabla detalles_revision
    public static boolean insertarRevision(Revision r) {
        String sqlRevision = "INSERT INTO revision (id_cliente, id_camion, fecha_revision) VALUES (?, ?, ?)";
        String sqlDetalles = """
            INSERT INTO detalles_revision (
                id_revision, frenos, aceite, agua, remolque, filtro_combustible,
                alineacion, cabina, caja_de_cambios, correa, neumaticos, filtro_aire, extra
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConexionDB.getConexion()) {
            con.setAutoCommit(false); //Transacción: si falla, se revierte todo

            //Insertar revisión principal
            int idRevisionGenerado = -1;
            try (PreparedStatement psRev = con.prepareStatement(sqlRevision, Statement.RETURN_GENERATED_KEYS)) {
                psRev.setString(1, r.getIdCliente());
                psRev.setString(2, r.getIdCamion());
                psRev.setDate(3, Date.valueOf(r.getFechaRevision()));
                psRev.executeUpdate();

                ResultSet rs = psRev.getGeneratedKeys();
                if (rs.next()) {
                    idRevisionGenerado = rs.getInt(1);
                }
            }

            if (idRevisionGenerado == -1) {
                con.rollback();
                return false;
            }

            // Insertar los detalles del checklist
            try (PreparedStatement psDet = con.prepareStatement(sqlDetalles)) {
                psDet.setInt(1, idRevisionGenerado);
                Map<String, Boolean> c = r.getChecklist();

                psDet.setBoolean(2, c.getOrDefault("frenos", false));
                psDet.setBoolean(3, c.getOrDefault("aceite", false));
                psDet.setBoolean(4, c.getOrDefault("agua", false));
                psDet.setBoolean(5, c.getOrDefault("remolque", false));
                psDet.setBoolean(6, c.getOrDefault("filtro_combustion", false));
                psDet.setBoolean(7, c.getOrDefault("alineación", false));
                psDet.setBoolean(8, c.getOrDefault("cabina", false));
                psDet.setBoolean(9, c.getOrDefault("caja_cambios", false));
                psDet.setBoolean(10, c.getOrDefault("correa", false));
                psDet.setBoolean(11, c.getOrDefault("neumáticos", false));
                psDet.setBoolean(12, c.getOrDefault("filtro_aire", false));

                psDet.setString(13, r.getDetalles());

                psDet.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error SQL RevisionDAO.insertarRevision(): " + e.getMessage());
            return false;
        }
    }

    public static List<Revision> buscarRevisiones(String idCliente, String idCamion) {
        List<Revision> revisiones = new ArrayList<>();

        String sql = """
        SELECT r.id_revision, r.id_cliente, r.id_camion, r.fecha_revision,
               d.frenos, d.aceite, d.agua, d.remolque, d.filtro_combustible,
               d.alineacion, d.cabina, d.caja_de_cambios, d.correa,
               d.neumaticos, d.filtro_aire, d.extra
        FROM revision r
        JOIN detalles_revision d ON r.id_revision = d.id_revision
        
        ORDER BY r.id_revision DESC
    """;

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idCliente);
            ps.setString(2, idCamion);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Revision r = new Revision();
                r.setId(String.valueOf(rs.getInt("id_revision")));
                r.setIdCliente(rs.getString("id_cliente"));
                r.setIdCamion(rs.getString("id_camion"));

                java.sql.Date fechaSQL = rs.getDate("fecha_revision");
                if (fechaSQL != null) {
                    r.setFechaRevision(fechaSQL.toLocalDate());
                }

                //Cargar datos de la tabla detalles_revision directamente
                Map<String, Boolean> checklist = new LinkedHashMap<>();
                checklist.put("frenos", rs.getBoolean("frenos"));
                checklist.put("aceite", rs.getBoolean("aceite"));
                checklist.put("agua", rs.getBoolean("agua"));
                checklist.put("remolque", rs.getBoolean("remolque"));
                checklist.put("filtro_combustible", rs.getBoolean("filtro_combustible"));
                checklist.put("alineacion", rs.getBoolean("alineacion"));
                checklist.put("cabina", rs.getBoolean("cabina"));
                checklist.put("caja_cambios", rs.getBoolean("caja_de_cambios"));
                checklist.put("correa", rs.getBoolean("correa"));
                checklist.put("neumaticos", rs.getBoolean("neumaticos"));
                checklist.put("filtro_aire", rs.getBoolean("filtro_aire"));
                r.setChecklist(checklist);

                // ✅ Guardar texto del campo extra
                r.setDetalles(rs.getString("extra"));

                revisiones.add(r);
            }

        } catch (SQLException e) {
            System.out.println("Error SQL buscarRevisiones(): " + e.getMessage());
            e.printStackTrace();
        }

        return revisiones;
    }

    public static boolean actualizarRevision(Revision r) {
        String sql = """
        UPDATE detalles_revision d
        JOIN revision rev ON rev.id_revision = d.id_revision
        SET rev.fecha_revision = ?, 
            d.frenos = ?, d.aceite = ?, d.agua = ?, d.remolque = ?, d.filtro_combustible = ?,
            d.alineacion = ?, d.cabina = ?, d.caja_de_cambios = ?, d.correa = ?, d.neumaticos = ?,
            d.filtro_aire = ?, d.extra = ?
        WHERE rev.id_revision = (
            SELECT id_revision FROM revision
            WHERE id_cliente = ? AND id_camion = ?
            ORDER BY id_revision DESC
            LIMIT 1
        )
    """;

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            Map<String, Boolean> c = r.getChecklist();

            ps.setDate(1, Date.valueOf(r.getFechaRevision()));
            ps.setBoolean(2, c.getOrDefault("frenos", false));
            ps.setBoolean(3, c.getOrDefault("aceite", false));
            ps.setBoolean(4, c.getOrDefault("agua", false));
            ps.setBoolean(5, c.getOrDefault("remolque", false));
            ps.setBoolean(6, c.getOrDefault("filtro_combustion", false));
            ps.setBoolean(7, c.getOrDefault("alineación", false));
            ps.setBoolean(8, c.getOrDefault("cabina", false));
            ps.setBoolean(9, c.getOrDefault("caja_cambios", false));
            ps.setBoolean(10, c.getOrDefault("correa", false));
            ps.setBoolean(11, c.getOrDefault("neumáticos", false));
            ps.setBoolean(12, c.getOrDefault("filtro_aire", false));
            ps.setString(13, r.getDetalles());

            ps.setString(14, r.getIdCliente());
            ps.setString(15, r.getIdCamion());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error SQL actualizarRevisionPorClienteCamion(): " + e.getMessage());
            return false;
        }
    }

}
