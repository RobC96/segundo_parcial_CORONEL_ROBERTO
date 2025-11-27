package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GestorArchivoTicket {
    
    public void escribirTicket(List<ItemCarrito> itemsCarrito, double totalFinal){
        File archivoContador = new File("carrito.dat");
        
        if (itemsCarrito == null || itemsCarrito.isEmpty()) {
            return;
        }
            
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoContador))) {
            
            for (ItemCarrito item : itemsCarrito){
                bw.write(item.getNombreProducto());
                bw.newLine();
                bw.write(String.valueOf(item.getCantidad()));
                bw.newLine();
                bw.write(String.valueOf(item.getSubtotal()));
                bw.newLine();
            }
            bw.newLine();
            bw.write("Total: " + String.valueOf(totalFinal));
            
        } catch (IOException e) {
            System.out.println("Error al escribir el ticket: " + e.getMessage());
        }
    }
}