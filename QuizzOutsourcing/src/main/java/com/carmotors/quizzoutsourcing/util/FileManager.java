/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotors.quizzoutsourcing.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author warle
 */
public class FileManager {
    
    private static final String ARCHIVO_CONTRATOS = "contratos_finalizados.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void guardarContratoFinalizado(String datosContrato) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_CONTRATOS, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.write("[" + timestamp + "] " + datosContrato);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo: " + e.getMessage());
        }
    }
    
    public static void leerContratosFinalizados() {
        // Implementar lectura del archivo
    }
    
}
