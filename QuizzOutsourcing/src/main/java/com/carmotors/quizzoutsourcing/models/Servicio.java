package com.carmotors.quizzoutsourcing.models;

public class Servicio {
    private int id;
    private String nombre;
    private String descripcion;
    private double precioPorHora;
    private Categoria categoria;

    public enum Categoria {
        TI, LIMPIEZA, SEGURIDAD, ADMINISTRACION
    }

    // Constructor vacío sin excepción
    public Servicio() {
    }

    // Constructor completo
    public Servicio(int id, String nombre, String descripcion, double precioPorHora, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioPorHora = precioPorHora;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    // Métodos adicionales
    public double calcularCostoTotal(int horas) {
        return horas * precioPorHora;
    }
}