/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotors.quizzoutsourcing.controllers;

import com.carmotors.quizzoutsourcing.dao.EmpleadoDAO;
import com.carmotors.quizzoutsourcing.dao.ProyectoDAO;
import com.carmotors.quizzoutsourcing.models.Empleado;
import java.util.Scanner;

/**
 *
 * @author warle
 */
public class EmpleadoController {
    private final EmpleadoDAO empleadoDAO;
    private final ProyectoDAO proyectoDAO;
    private final Scanner scanner;

    public EmpleadoController() {
        this.empleadoDAO = new EmpleadoDAO();
        this.proyectoDAO = new ProyectoDAO();
        this.scanner = new Scanner(System.in);
    }

    public void registrarEmpleado() {
        try {
            Empleado empleado = new Empleado();
            System.out.println("\nREGISTRO DE NUEVO EMPLEADO");
            System.out.println("========================");
            
            System.out.print("Nombre: ");
            empleado.setNombre(scanner.nextLine());
            
            System.out.print("Cargo: ");
            empleado.setCargo(scanner.nextLine());
            
            System.out.print("Salario: ");
            empleado.setSalario(Double.parseDouble(scanner.nextLine()));
            
            System.out.println("Especialidades disponibles:");
            for (Empleado.Especialidad esp : Empleado.Especialidad.values()) {
                System.out.println((esp.ordinal() + 1) + ". " + esp);
            }
            System.out.print("Seleccione especialidad: ");
            int espOp = Integer.parseInt(scanner.nextLine());
            empleado.setEspecialidad(Empleado.Especialidad.values()[espOp - 1]);

            empleadoDAO.insertar(empleado);
            System.out.println("✅ Empleado registrado exitosamente!");
        } catch (Exception e) {
            System.out.println("❌ Error al registrar empleado: " + e.getMessage());
        }
    }

    public void asignarProyecto() {
        try {
            System.out.println("\nASIGNAR EMPLEADO A PROYECTO");
            System.out.println("========================");
            
            // Listar empleados disponibles
            System.out.println("Empleados disponibles:");
            empleadoDAO.listarTodos().forEach(e -> 
                System.out.println(e.getId() + ". " + e.getNombre() + 
                                 " (" + e.getEspecialidad() + ")"));
            System.out.print("Seleccione ID empleado: ");
            int idEmpleado = Integer.parseInt(scanner.nextLine());
            
            // Listar proyectos disponibles
            System.out.println("\nProyectos disponibles:");
            proyectoDAO.listarActivos().forEach(p -> 
                System.out.println(p.getId() + ". " + p.getNombre()));
            System.out.print("Seleccione ID proyecto: ");
            int idProyecto = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Horas trabajadas: ");
            int horas = Integer.parseInt(scanner.nextLine());
            
            empleadoDAO.asignarProyecto(idEmpleado, idProyecto, horas);
            System.out.println("✅ Empleado asignado exitosamente!");
        } catch (Exception e) {
            System.out.println("❌ Error al asignar empleado: " + e.getMessage());
        }
    }
    
}
