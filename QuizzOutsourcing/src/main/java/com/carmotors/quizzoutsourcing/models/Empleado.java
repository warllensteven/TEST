package com.carmotors.quizzoutsourcing.models;

public class Empleado {
    private int id;
    private String nombre;
    private String cargo;
    private double salario;
    private Especialidad especialidad;
    private Proyecto proyecto;

    public enum Especialidad {
        TI, ADMINISTRACION, LIMPIEZA, SEGURIDAD
    }

    public Empleado() {
    }

    public Empleado(int id, String nombre, String cargo, double salario, 
                   Especialidad especialidad, Proyecto proyecto) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
        this.especialidad = especialidad;
        this.proyecto = proyecto;
    }

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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public void asignarProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                ", especialidad=" + especialidad +
                ", proyecto=" + (proyecto != null ? proyecto.getId() : "null") +
                '}';
    }
}