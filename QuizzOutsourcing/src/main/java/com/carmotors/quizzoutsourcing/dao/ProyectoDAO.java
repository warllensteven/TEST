package com.carmotors.quizzoutsourcing.dao;

import com.carmotors.quizzoutsourcing.models.Proyecto;
import com.carmotors.quizzoutsourcing.models.Cliente;
import com.carmotors.quizzoutsourcing.models.Empleado;
import com.carmotors.quizzoutsourcing.models.Proyecto.Estado;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    private final ClienteDAO clienteDAO;
    private final EmpleadoDAO empleadoDAO;

    public ProyectoDAO() {
        this.clienteDAO = new ClienteDAO();
        this.empleadoDAO = new EmpleadoDAO();
    }

    // Insertar un nuevo proyecto
    public void insertar(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto (ID_Cliente, Nombre, FechaInicio, FechaFin, Estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, proyecto.getCliente().getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setDate(3, Date.valueOf(proyecto.getFechaInicio()));

            if (proyecto.getFechaFin() != null) {
                stmt.setDate(4, Date.valueOf(proyecto.getFechaFin()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, proyecto.getEstado().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    proyecto.setId(rs.getInt(1));
                }
            }

            // Insertar relaciones con empleados
            insertarEmpleadosProyecto(proyecto);
        }
    }

    // Actualizar un proyecto existente
    public void actualizar(Proyecto proyecto) throws SQLException {
        String sql = "UPDATE Proyecto SET ID_Cliente = ?, Nombre = ?, FechaInicio = ?, "
                + "FechaFin = ?, Estado = ? WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proyecto.getCliente().getId());
            stmt.setString(2, proyecto.getNombre());
            stmt.setDate(3, Date.valueOf(proyecto.getFechaInicio()));

            if (proyecto.getFechaFin() != null) {
                stmt.setDate(4, Date.valueOf(proyecto.getFechaFin()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, proyecto.getEstado().name());
            stmt.setInt(6, proyecto.getId());

            stmt.executeUpdate();

            // Actualizar relaciones con empleados
            actualizarEmpleadosProyecto(proyecto);
        }
    }

    // Eliminar un proyecto
    public void eliminar(int id) throws SQLException {
        // Primero eliminar las relaciones con empleados
        eliminarEmpleadosProyecto(id);

        String sql = "DELETE FROM Proyecto WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Listar todos los proyectos activos
    public List<Proyecto> listarActivos() throws SQLException {
        return listarPorEstado("EN_CURSO");
    }

    // Listar proyectos por estado
    public List<Proyecto> listarPorEstado(String estado) throws SQLException {
        List<Proyecto> proyectos = new ArrayList<>();
        String sql = "SELECT * FROM Proyecto WHERE Estado = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    proyectos.add(crearProyectoDesdeResultSet(rs));
                }
            }
        }
        return proyectos;
    }

    // Buscar proyecto por ID
    public Proyecto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Proyecto WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearProyectoDesdeResultSet(rs);
                }
            }
        }
        return null;
    }

    // Métodos para manejar la relación con empleados
    private void insertarEmpleadosProyecto(Proyecto proyecto) throws SQLException {
        String sql = "INSERT INTO Proyecto_Empleado (ID_Proyecto, ID_Empleado) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Empleado empleado : proyecto.getEmpleados()) {
                stmt.setInt(1, proyecto.getId());
                stmt.setInt(2, empleado.getId());
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }

    private void actualizarEmpleadosProyecto(Proyecto proyecto) throws SQLException {
        // Primero eliminar todas las relaciones existentes
        eliminarEmpleadosProyecto(proyecto.getId());

        // Luego insertar las nuevas relaciones
        insertarEmpleadosProyecto(proyecto);
    }

    private void eliminarEmpleadosProyecto(int idProyecto) throws SQLException {
        String sql = "DELETE FROM Proyecto_Empleado WHERE ID_Proyecto = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProyecto);
            stmt.executeUpdate();
        }
    }

    // Cargar empleados de un proyecto
    public void cargarEmpleados(Proyecto proyecto) throws SQLException {
        String sql = "SELECT e.* FROM Empleado e "
                + "JOIN Proyecto_Empleado pe ON e.ID = pe.ID_Empleado "
                + "WHERE pe.ID_Proyecto = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, proyecto.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                List<Empleado> empleados = new ArrayList<>();
                while (rs.next()) {
                    empleados.add(empleadoDAO.crearEmpleadoDesdeResultSet(rs));
                }
                proyecto.setEmpleados(empleados);
            }
        }
    }

    public Proyecto crearProyectoDesdeResultSet(ResultSet rs) throws SQLException {
        Proyecto proyecto = new Proyecto();
        proyecto.setId(rs.getInt("ID"));

        Cliente clienteSimple = new Cliente();
        clienteSimple.setId(rs.getInt("ID_Cliente"));
        proyecto.setCliente(clienteSimple);

        proyecto.setNombre(rs.getString("Nombre"));
        proyecto.setFechaInicio(rs.getDate("FechaInicio").toLocalDate());

        Date fechaFin = rs.getDate("FechaFin");
        if (fechaFin != null) {
            proyecto.setFechaFin(fechaFin.toLocalDate());
        }

        proyecto.setEstado(Estado.valueOf(rs.getString("Estado")));

        return proyecto;
    }
}
