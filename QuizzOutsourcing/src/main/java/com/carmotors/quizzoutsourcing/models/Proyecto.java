package com.carmotors.quizzoutsourcing.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private int id;
    private Cliente cliente;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Estado estado;
    private List<Empleado> empleados;

    public enum Estado {
        EN_CURSO, COMPLETADO, CANCELADO
    }

    public Proyecto() {
        this.empleados = new ArrayList<>();
    }

    public Proyecto(int id, Cliente cliente, String nombre, LocalDate fechaInicio, 
                   LocalDate fechaFin, Estado estado, List<Empleado> empleados) {
        this.id = id;
        this.cliente = cliente;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.empleados = (empleados != null) ? new ArrayList<>(empleados) : new ArrayList<>();
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<Empleado> getEmpleados() {
        return new ArrayList<>(empleados);
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = (empleados != null) ? new ArrayList<>(empleados) : new ArrayList<>();
    }

    public void agregarEmpleado(Empleado empleado) {
        if (empleado != null && !empleados.contains(empleado)) {
            empleados.add(empleado);
        }
    }

    public void removerEmpleado(Empleado empleado) {
        empleados.remove(empleado);
    }

    public long calcularDuracion() {
        if (fechaFin == null) {
            return LocalDate.now().until(fechaInicio).getDays();
        }
        return fechaInicio.until(fechaFin).getDays();
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", nombre='" + nombre + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                ", empleados=" + empleados.size() +
                '}';
    }
}