package com.carmotors.quizzoutsourcing.dao;

import com.carmotors.quizzoutsourcing.models.Contrato;
import com.carmotors.quizzoutsourcing.models.Contrato.Estado;
import com.carmotors.quizzoutsourcing.models.Cliente;
import com.carmotors.quizzoutsourcing.models.Servicio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {
    private final ClienteDAO clienteDAO;
    private final ServicioDAO servicioDAO;

    public ContratoDAO() {
        this.clienteDAO = new ClienteDAO();
        this.servicioDAO = new ServicioDAO();
    }

    // Insertar un nuevo contrato
    public void insertar(Contrato contrato) throws SQLException {
        String sql = "INSERT INTO Contrato (ID_Cliente, ID_Servicio, FechaInicio, FechaFin, CostoTotal, Estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, contrato.getCliente().getId());
            stmt.setInt(2, contrato.getServicio().getId());
            stmt.setDate(3, Date.valueOf(contrato.getFechaInicio()));
            
            if (contrato.getFechaFin() != null) {
                stmt.setDate(4, Date.valueOf(contrato.getFechaFin()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setDouble(5, contrato.getCostoTotal());
            stmt.setString(6, contrato.getEstado().name());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    contrato.setId(rs.getInt(1));
                }
            }
        }
    }

    // Actualizar un contrato existente
    public void actualizar(Contrato contrato) throws SQLException {
        String sql = "UPDATE Contrato SET ID_Cliente = ?, ID_Servicio = ?, FechaInicio = ?, " +
                     "FechaFin = ?, CostoTotal = ?, Estado = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, contrato.getCliente().getId());
            stmt.setInt(2, contrato.getServicio().getId());
            stmt.setDate(3, Date.valueOf(contrato.getFechaInicio()));
            
            if (contrato.getFechaFin() != null) {
                stmt.setDate(4, Date.valueOf(contrato.getFechaFin()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setDouble(5, contrato.getCostoTotal());
            stmt.setString(6, contrato.getEstado().name());
            stmt.setInt(7, contrato.getId());
            
            stmt.executeUpdate();
        }
    }

    // Eliminar un contrato
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Contrato WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Listar todos los contratos activos
    public List<Contrato> listarActivos() throws SQLException {
        return listarPorEstado(Estado.ACTIVO);
    }

    // Listar contratos por estado
    public List<Contrato> listarPorEstado(Estado estado) throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM Contrato WHERE Estado = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contratos.add(crearContratoDesdeResultSet(rs));
                }
            }
        }
        return contratos;
    }

    // Listar contratos vigentes (activos y dentro de fecha)
    public List<Contrato> listarVigentes() throws SQLException {
        List<Contrato> contratos = new ArrayList<>();
        String sql = "SELECT * FROM Contrato WHERE Estado = 'ACTIVO' " +
                    "AND FechaInicio <= CURRENT_DATE " +
                    "AND (FechaFin IS NULL OR FechaFin >= CURRENT_DATE)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                contratos.add(crearContratoDesdeResultSet(rs));
            }
        }
        return contratos;
    }

    // Buscar contrato por ID
    public Contrato buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Contrato WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearContratoDesdeResultSet(rs);
                }
            }
        }
        return null;
    }

    // Finalizar un contrato
    public void finalizarContrato(int idContrato) throws SQLException {
        String sql = "UPDATE Contrato SET Estado = 'FINALIZADO', FechaFin = CURRENT_DATE WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idContrato);
            stmt.executeUpdate();
        }
    }

    // Cancelar un contrato
    public void cancelarContrato(int idContrato) throws SQLException {
        String sql = "UPDATE Contrato SET Estado = 'CANCELADO' WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idContrato);
            stmt.executeUpdate();
        }
    }

    // Contar contratos por servicio
    public int contarPorServicio(int idServicio) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Contrato WHERE ID_Servicio = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idServicio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    // Obtener ingresos totales por servicio
    public double obtenerIngresosPorServicio(int idServicio) throws SQLException {
        String sql = "SELECT SUM(CostoTotal) AS total FROM Contrato WHERE ID_Servicio = ? AND Estado = 'ACTIVO'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idServicio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }

    // Método auxiliar para crear un contrato desde un ResultSet
    public Contrato crearContratoDesdeResultSet(ResultSet rs) throws SQLException {
        Contrato contrato = new Contrato();
        contrato.setId(rs.getInt("ID"));
        
        // Obtener cliente completo
        int idCliente = rs.getInt("ID_Cliente");
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        contrato.setCliente(cliente);
        
        // Obtener servicio completo
        int idServicio = rs.getInt("ID_Servicio");
        Servicio servicio = servicioDAO.buscarPorId(idServicio);
        contrato.setServicio(servicio);
        
        contrato.setFechaInicio(rs.getDate("FechaInicio").toLocalDate());
        
        Date fechaFin = rs.getDate("FechaFin");
        if (fechaFin != null) {
            contrato.setFechaFin(fechaFin.toLocalDate());
        }
        
        contrato.setCostoTotal(rs.getDouble("CostoTotal"));
        contrato.setEstado(Estado.valueOf(rs.getString("Estado")));
        
        return contrato;
    }
}