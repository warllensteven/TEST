package com.carmotors.quizzoutsourcing.models;

import java.time.LocalDate;

public class Contrato {
    private int id;
    private Cliente cliente;
    private Servicio servicio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double costoTotal;
    private Estado estado;

    public enum Estado {
        ACTIVO, EN_ESPERA, FINALIZADO, CANCELADO
    }

    public Contrato() {
    }

    public Contrato(int id, Cliente cliente, Servicio servicio, LocalDate fechaInicio, 
                   LocalDate fechaFin, double costoTotal, Estado estado) {
        this.id = id;
        this.cliente = cliente;
        this.servicio = servicio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoTotal = costoTotal;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public boolean estaActivo() {
        return estado == Estado.ACTIVO;
    }

    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return estaActivo() && 
               !hoy.isBefore(fechaInicio) && 
               (fechaFin == null || !hoy.isAfter(fechaFin));
    }

    public double calcularProrrata() {
        if (fechaFin == null || !estaVigente()) {
            return 0.0;
        }
        
        long diasTotales = fechaInicio.until(fechaFin).getDays();
        long diasTranscurridos = fechaInicio.until(LocalDate.now()).getDays();
        
        return (costoTotal / diasTotales) * diasTranscurridos;
    }

    @Override
    public String toString() {
        return "Contrato{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getId() : "null") +
                ", servicio=" + (servicio != null ? servicio.getId() : "null") +
                ", estado=" + estado +
                ", costoTotal=" + costoTotal +
                '}';
    }
}