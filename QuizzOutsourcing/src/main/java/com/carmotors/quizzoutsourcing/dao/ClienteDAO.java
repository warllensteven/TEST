package com.carmotors.quizzoutsourcing.dao;

import com.carmotors.quizzoutsourcing.models.Cliente;
import com.carmotors.quizzoutsourcing.models.Cliente.Sector;
import com.carmotors.quizzoutsourcing.models.Contrato;
import com.carmotors.quizzoutsourcing.models.Proyecto;
import com.carmotors.quizzoutsourcing.models.Proyecto.Estado;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private final ContratoDAO contratoDAO;

    public ClienteDAO() {
        this.contratoDAO = new ContratoDAO();
    }

    public void insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (Nombre, Representante, Correo, Telefono, Direccion, Sector) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getRepresentante());
            stmt.setString(3, cliente.getCorreo());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getDireccion());
            stmt.setString(6, cliente.getSector().name());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET Nombre = ?, Representante = ?, Correo = ?, " +
                     "Telefono = ?, Direccion = ?, Sector = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getRepresentante());
            stmt.setString(3, cliente.getCorreo());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getDireccion());
            stmt.setString(6, cliente.getSector().name());
            stmt.setInt(7, cliente.getId());
            
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clientes.add(crearClienteDesdeResultSet(rs));
            }
        }
        return clientes;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearClienteDesdeResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Cliente> listarPorSector(Sector sector) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente WHERE Sector = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sector.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(crearClienteDesdeResultSet(rs));
                }
            }
        }
        return clientes;
    }

    public void cargarProyectos(Cliente cliente) throws SQLException {
        String sql = "SELECT * FROM Proyecto WHERE ID_Cliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliente.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Proyecto> proyectos = new ArrayList<>();
                while (rs.next()) {
                    // Crear el proyecto directamente aquí
                    Proyecto proyecto = new Proyecto();
                    proyecto.setId(rs.getInt("ID"));
                    proyecto.setCliente(cliente); // Ya tenemos el cliente
                    proyecto.setNombre(rs.getString("Nombre"));
                    proyecto.setFechaInicio(rs.getDate("FechaInicio").toLocalDate());
                    
                    Date fechaFin = rs.getDate("FechaFin");
                    if (fechaFin != null) {
                        proyecto.setFechaFin(fechaFin.toLocalDate());
                    }
                    
                    proyecto.setEstado(Estado.valueOf(rs.getString("Estado")));
                    
                    proyectos.add(proyecto);
                }
                cliente.setProyectos(proyectos);
            }
        }
    }

    // Cargar contratos de un cliente
    public void cargarContratos(Cliente cliente) throws SQLException {
        String sql = "SELECT * FROM Contrato WHERE ID_Cliente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliente.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Contrato> contratos = new ArrayList<>();
                while (rs.next()) {
                    contratos.add(contratoDAO.crearContratoDesdeResultSet(rs));
                }
                cliente.setContratos(contratos);
            }
        }
    }

    // Método auxiliar para crear un cliente desde un ResultSet
    private Cliente crearClienteDesdeResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("ID"));
        cliente.setNombre(rs.getString("Nombre"));
        cliente.setRepresentante(rs.getString("Representante"));
        cliente.setCorreo(rs.getString("Correo"));
        cliente.setTelefono(rs.getString("Telefono"));
        cliente.setDireccion(rs.getString("Direccion"));
        cliente.setSector(Sector.valueOf(rs.getString("Sector")));
        
        // Nota: Los proyectos y contratos no se cargan aquí por defecto para mejorar el rendimiento
        // Se deben cargar explícitamente con cargarProyectos() o cargarContratos() cuando sea necesario
        
        return cliente;
    }
}