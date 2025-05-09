package com.carmotors.quizzoutsourcing.dao;

import com.carmotors.quizzoutsourcing.models.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    public void insertar(Servicio servicio) throws SQLException {
        String sql = "INSERT INTO Servicio (Nombre, Descripcion, PrecioPorHora, Categoria) " +
                   "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, servicio.getNombre());
            stmt.setString(2, servicio.getDescripcion());
            stmt.setDouble(3, servicio.getPrecioPorHora());
            stmt.setString(4, servicio.getCategoria().name());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    servicio.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Servicio servicio) throws SQLException {
        String sql = "UPDATE Servicio SET Nombre = ?, Descripcion = ?, " +
                   "PrecioPorHora = ?, Categoria = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, servicio.getNombre());
            stmt.setString(2, servicio.getDescripcion());
            stmt.setDouble(3, servicio.getPrecioPorHora());
            stmt.setString(4, servicio.getCategoria().name());
            stmt.setInt(5, servicio.getId());
            
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Servicio WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Servicio> listarTodos() throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT * FROM Servicio";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                servicios.add(mapearServicio(rs));
            }
        }
        return servicios;
    }

    public Servicio buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Servicio WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearServicio(rs);
                }
            }
        }
        return null;
    }

    public List<Servicio> listarPorCategoria(Servicio.Categoria categoria) throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT * FROM Servicio WHERE Categoria = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    servicios.add(mapearServicio(rs));
                }
            }
        }
        return servicios;
    }

    private Servicio mapearServicio(ResultSet rs) throws SQLException {
        Servicio servicio = new Servicio();
        servicio.setId(rs.getInt("ID"));
        servicio.setNombre(rs.getString("Nombre"));
        servicio.setDescripcion(rs.getString("Descripcion"));
        servicio.setPrecioPorHora(rs.getDouble("PrecioPorHora"));
        servicio.setCategoria(Servicio.Categoria.valueOf(rs.getString("Categoria")));
        return servicio;
    }
}