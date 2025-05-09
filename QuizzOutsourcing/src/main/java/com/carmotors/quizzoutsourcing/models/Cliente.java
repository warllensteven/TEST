package com.carmotors.quizzoutsourcing.models;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nombre;
    private String representante;
    private String correo;
    private String telefono;
    private String direccion;
    private Sector sector;
    private List<Contrato> contratos;
    private List<Proyecto> proyectos;

    public enum Sector {
        TECNOLOGIA, SALUD, EDUCACION, COMERCIO, MANUFACTURA
    }

    public Cliente() {
        this.contratos = new ArrayList<>();
        this.proyectos = new ArrayList<>();
    }

    public Cliente(int id, String nombre, String representante, String correo, 
                  String telefono, String direccion, Sector sector, 
                  List<Contrato> contratos, List<Proyecto> proyectos) {
        this.id = id;
        this.nombre = nombre;
        this.representante = representante;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.sector = sector;
        this.contratos = (contratos != null) ? new ArrayList<>(contratos) : new ArrayList<>();
        this.proyectos = (proyectos != null) ? new ArrayList<>(proyectos) : new ArrayList<>();
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

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public List<Contrato> getContratos() {
        return new ArrayList<>(contratos);
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = (contratos != null) ? new ArrayList<>(contratos) : new ArrayList<>();
    }

    public List<Proyecto> getProyectos() {
        return new ArrayList<>(proyectos);
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = (proyectos != null) ? new ArrayList<>(proyectos) : new ArrayList<>();
    }

    public void agregarContrato(Contrato contrato) {
        if (contrato != null && !contratos.contains(contrato)) {
            contratos.add(contrato);
        }
    }

    public void removerContrato(Contrato contrato) {
        contratos.remove(contrato);
    }

    public void agregarProyecto(Proyecto proyecto) {
        if (proyecto != null && !proyectos.contains(proyecto)) {
            proyectos.add(proyecto);
        }
    }

    public void removerProyecto(Proyecto proyecto) {
        proyectos.remove(proyecto);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", representante='" + representante + '\'' +
                ", sector=" + sector +
                ", proyectos=" + proyectos.size() +
                ", contratos=" + contratos.size() +
                '}';
    }
}