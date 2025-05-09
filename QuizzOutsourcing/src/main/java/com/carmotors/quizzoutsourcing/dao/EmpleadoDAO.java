package com.carmotors.quizzoutsourcing.dao;

import com.carmotors.quizzoutsourcing.models.Empleado;
import com.carmotors.quizzoutsourcing.models.Empleado.Especialidad;
import com.carmotors.quizzoutsourcing.models.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    private final ProyectoDAO proyectoDAO;

    public EmpleadoDAO() {
        this.proyectoDAO = new ProyectoDAO();
    }

    // Insertar un nuevo empleado
    public void insertar(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO Empleado (Nombre, Cargo, Salario, Especialidad, ID_Proyecto) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getCargo());
            stmt.setDouble(3, empleado.getSalario());
            stmt.setString(4, empleado.getEspecialidad().name());
            
            if (empleado.getProyecto() != null) {
                stmt.setInt(5, empleado.getProyecto().getId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setId(rs.getInt(1));
                }
            }
        }
    }

    // Actualizar un empleado existente
    public void actualizar(Empleado empleado) throws SQLException {
        String sql = "UPDATE Empleado SET Nombre = ?, Cargo = ?, Salario = ?, " +
                     "Especialidad = ?, ID_Proyecto = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getCargo());
            stmt.setDouble(3, empleado.getSalario());
            stmt.setString(4, empleado.getEspecialidad().name());
            
            if (empleado.getProyecto() != null) {
                stmt.setInt(5, empleado.getProyecto().getId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(6, empleado.getId());
            stmt.executeUpdate();
        }
    }

    // Eliminar un empleado
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Empleado WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Listar todos los empleados
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleado";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                empleados.add(crearEmpleadoDesdeResultSet(rs));
            }
        }
        return empleados;
    }

    // Buscar empleado por ID
    public Empleado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Empleado WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearEmpleadoDesdeResultSet(rs);
                }
            }
        }
        return null;
    }

    // Listar empleados por proyecto
    public List<Empleado> listarPorProyecto(int idProyecto) throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleado WHERE ID_Proyecto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProyecto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(crearEmpleadoDesdeResultSet(rs));
                }
            }
        }
        return empleados;
    }

    // Listar empleados por especialidad
    public List<Empleado> listarPorEspecialidad(Especialidad especialidad) throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleado WHERE Especialidad = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, especialidad.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    empleados.add(crearEmpleadoDesdeResultSet(rs));
                }
            }
        }
        return empleados;
    }

    // Asignar proyecto a empleado
    public void asignarProyecto(int idEmpleado, int idProyecto, int horas) throws SQLException {
        // Actualizar el empleado
        String sqlEmpleado = "UPDATE Empleado SET ID_Proyecto = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlEmpleado)) {
            
            stmt.setInt(1, idProyecto);
            stmt.setInt(2, idEmpleado);
            stmt.executeUpdate();
        }
        
        // Registrar en Asignación
        registrarHorasTrabajadas(idEmpleado, idProyecto, horas);
    }

    // Registrar horas trabajadas
    public void registrarHorasTrabajadas(int idEmpleado, int idProyecto, int horas) throws SQLException {
        String sql = "INSERT INTO Asignacion (ID_Empleado, ID_Proyecto, HorasTrabajadas, FechaAsignacion) " +
                    "VALUES (?, ?, ?, CURRENT_DATE) " +
                    "ON DUPLICATE KEY UPDATE HorasTrabajadas = HorasTrabajadas + ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
            stmt.setInt(2, idProyecto);
            stmt.setInt(3, horas);
            stmt.setInt(4, horas);
            stmt.executeUpdate();
        }
    }

    // Obtener horas trabajadas por empleado
    public int obtenerHorasTrabajadas(int idEmpleado) throws SQLException {
        String sql = "SELECT SUM(HorasTrabajadas) AS total FROM Asignacion WHERE ID_Empleado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // Obtener horas trabajadas por empleado en un proyecto específico
    public int obtenerHorasTrabajadas(int idEmpleado, int idProyecto) throws SQLException {
        String sql = "SELECT SUM(HorasTrabajadas) AS total FROM Asignacion " +
                    "WHERE ID_Empleado = ? AND ID_Proyecto = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEmpleado);
            stmt.setInt(2, idProyecto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // Método auxiliar para crear un empleado desde un ResultSet
    public Empleado crearEmpleadoDesdeResultSet(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setId(rs.getInt("ID"));
        empleado.setNombre(rs.getString("Nombre"));
        empleado.setCargo(rs.getString("Cargo"));
        empleado.setSalario(rs.getDouble("Salario"));
        empleado.setEspecialidad(Especialidad.valueOf(rs.getString("Especialidad")));
        
        // Cargar proyecto si existe
        int idProyecto = rs.getInt("ID_Proyecto");
        if (!rs.wasNull()) {
            Proyecto proyecto = proyectoDAO.buscarPorId(idProyecto);
            empleado.setProyecto(proyecto);
        }
        
        return empleado;
    }
}