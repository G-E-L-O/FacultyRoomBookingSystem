package psgra.sgra.servicio;

import psgra.sgra.servicio.MySQLConnection;
import psgra.sgra.modelo.Reserva;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ReservaDAO {

    public static List<Reserva> obtenerReservasPorFecha(LocalDate fecha) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE fecha = ?";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String aula = rs.getString("aula");
                String curso = rs.getString("curso");
                String hora = rs.getString("hora");
                String detalle = rs.getString("detalle");

                reservas.add(new Reserva(aula, curso, hora, detalle));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservas;
    }



    public static void guardarReserva(LocalDate fecha, Reserva reserva) {
        String sql = "INSERT INTO reservas (fecha, hora, aula, curso, detalle) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setString(2, reserva.getHora());
            stmt.setString(3, reserva.getAula());
            stmt.setString(4, reserva.getCurso());
            stmt.setString(5, reserva.getDetalle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Reserva> buscarReservas(String query) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE aula LIKE ? OR curso LIKE ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String q = "%" + query + "%";
            stmt.setString(1, q);
            stmt.setString(2, q);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Reserva(
                        rs.getString("aula"),
                        rs.getString("curso"),
                        rs.getString("hora"),
                        rs.getString("detalle")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static boolean existeReserva(LocalDate fecha, String aula, String hora) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE fecha = ? AND aula = ? AND hora = ?";
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setString(2, aula);
            stmt.setString(3, hora);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void actualizarReserva(LocalDate fecha, String hora, String aulaOriginal, Reserva nueva) {
        String sql = "UPDATE reservas SET aula = ?, curso = ?, detalle = ? WHERE fecha = ? AND hora = ? AND aula = ?";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nueva.getAula());
            stmt.setString(2, nueva.getCurso());
            stmt.setString(3, nueva.getDetalle());
            stmt.setDate(4, Date.valueOf(fecha));
            stmt.setString(5, hora);
            stmt.setString(6, aulaOriginal);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void eliminarReserva(LocalDate fecha, String hora, String aula) {
        String sql = "DELETE FROM reservas WHERE fecha = ? AND hora = ? AND aula = ?";
        try (Connection conn = MySQLConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setString(2, hora);
            stmt.setString(3, aula);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> obtenerTodasLasAulas() {
        List<String> aulas = new ArrayList<>();
        String sql = "SELECT DISTINCT aula FROM reservas ORDER BY aula ASC";

        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                aulas.add(rs.getString("aula"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aulas;
    }
}
