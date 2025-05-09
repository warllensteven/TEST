/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.carmotors.quizzoutsourcing;

import com.carmotors.quizzoutsourcing.controllers.ClienteController;
import com.carmotors.quizzoutsourcing.controllers.EmpleadoController;
import java.util.Scanner;

/**
 *
 * @author warle
 */
public class QuizzOutsourcing {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        
        // Inicializar controladores
        ClienteController clienteController = new ClienteController();
        EmpleadoController empleadoController = new EmpleadoController();

        while (!salir) {
            System.out.println("\nSISTEMA DE GESTIÓN DE OUTSOURCING");
            System.out.println("====================================");
            System.out.println("1. Gestión de Clientes");
            System.out.println("2. Gestión de Servicios");
            System.out.println("3. Gestión de Contratos");
            System.out.println("4. Gestión de Proyectos");
            System.out.println("5. Gestión de Empleados");
            System.out.println("6. Reportes");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    menuClientes(clienteController);
                    break;
                case 0:
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        scanner.close();
    }
    
    private static void menuClientes(ClienteController controller) {
        Scanner scanner = new Scanner(System.in);
        boolean volver = false;
        
        while (!volver) {
            System.out.println("\nGESTIÓN DE CLIENTES");
            System.out.println("====================================");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Listar todos los clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    controller.registrarCliente();
                    break;
                case 2:
                    controller.listarClientes();
                    break;
                case 3:
                    // Implementar
                    break;
                case 4:
                    // Implementar
                    break;
                case 5:
                    // Implementar
                    break;
                case 0:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
}