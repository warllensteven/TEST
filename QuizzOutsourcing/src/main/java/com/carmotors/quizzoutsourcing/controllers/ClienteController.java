/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotors.quizzoutsourcing.controllers;

import com.carmotors.quizzoutsourcing.dao.ClienteDAO;
import com.carmotors.quizzoutsourcing.models.Cliente;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author warle
 */
public class ClienteController {
    
    private ClienteDAO clienteDAO;
    private Scanner scanner;
    
    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
        this.scanner = new Scanner(System.in);
    }
    
    public void registrarCliente() {
        try {
            Cliente cliente = new Cliente();
            
            System.out.println("Ingrese el nombre del cliente:");
            cliente.setNombre(scanner.nextLine());
            
            System.out.println("Ingrese el nombre del representante:");
            cliente.setRepresentante(scanner.nextLine());
            
            System.out.println("Ingrese el correo electrónico:");
            cliente.setCorreo(scanner.nextLine());
            
            System.out.println("Ingrese el teléfono:");
            cliente.setTelefono(scanner.nextLine());
            
            System.out.println("Ingrese la dirección:");
            cliente.setDireccion(scanner.nextLine());
            
            System.out.println("Seleccione el sector:");
            for (Cliente.Sector sector : Cliente.Sector.values()) {
                System.out.println((sector.ordinal() + 1) + ". " + sector.name());
            }
            int opcionSector = Integer.parseInt(scanner.nextLine());
            cliente.setSector(Cliente.Sector.values()[opcionSector - 1]);
            
            clienteDAO.insertar(cliente);
            System.out.println("Cliente registrado exitosamente!");
        } catch (Exception e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }
    
    public void listarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            
            System.out.println("\nLISTADO DE CLIENTES");
            System.out.println("====================================");
            
            clientes.forEach(cliente -> {
                System.out.println("ID: " + cliente.getId());
                System.out.println("Nombre: " + cliente.getNombre());
                System.out.println("Representante: " + cliente.getRepresentante());
                System.out.println("Sector: " + cliente.getSector());
                System.out.println("------------------------------------");
            });
        } catch (Exception e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
    }
    
}
