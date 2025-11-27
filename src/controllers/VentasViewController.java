package controllers;

import exceptions.OutOfStockException;
import exceptions.ProductoNoSeleccionadoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.ItemCarrito;
import modelo.Producto;


/**
 *
 * @author romicux
 */
public class VentasViewController implements Initializable {
    
    private static final String NOMBRE_ARCHIVO_PRODUCTOS = "productos.dat"; 

    private List<ItemCarrito> itemsCarrito = new ArrayList();
    
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnVerCarrito;
    
    @FXML
    private TableView<Producto> tbvProductos;
    
    @FXML
    private TableColumn<Producto, String> colNombre;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colStock;

    
    @FXML
    private void agregar(ActionEvent e){
        
        try {
            Producto productoSeleccionado = tbvProductos.getSelectionModel().getSelectedItem();
        
            if (productoSeleccionado == null) {
                throw new ProductoNoSeleccionadoException("No se seleccionó un producto");
            }

            if (productoSeleccionado.getStock() <= 0) {
               throw new OutOfStockException("No queda mas stock de este producto");
            }
            boolean encontrado = false;
        
            for (ItemCarrito item : itemsCarrito){
                if (item.getProducto().getNombre().equals(productoSeleccionado.getNombre())) {
                    item.setCantidad(item.getCantidad() + 1);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                ItemCarrito nuevoItem = new ItemCarrito(productoSeleccionado, 1);
                itemsCarrito.add(nuevoItem);
            }

            int stockActual = productoSeleccionado.getStock();
            productoSeleccionado.setStock(stockActual - 1);
            
          
        tbvProductos.refresh();
        } catch (ProductoNoSeleccionadoException ex) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error!", "Producto no seleccionado", "Seleccione un producto para agregar");
        } catch (OutOfStockException ex){
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error!", "Sin stock", "No queda stock de este producto");
        }
    }
    
    @FXML
    private void verCarrito(ActionEvent e){
        abrirFormulario(itemsCarrito);
    }
    
   
    public void guardarProductosPersistir() {
        guardarProductos(tbvProductos.getItems());
    }
    
    
    private void guardarProductos(List<Producto> lista) {
        if (lista == null || lista.isEmpty()) {
            return; 
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO_PRODUCTOS))) {
            oos.writeObject(new ArrayList<>(lista)); 
            System.out.println("Productos guardados exitosamente en " + NOMBRE_ARCHIVO_PRODUCTOS);
        } catch (IOException e) {
            System.err.println("Error al guardar productos: " + e.getMessage());
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error de Guardado", "No se pudo guardar el stock actualizado.");
        }
    }
    
    private void cargarProductos(){
        List<Producto> productos = new ArrayList<>(); 
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO_PRODUCTOS))) {
            productos = (List<Producto>) ois.readObject();
            System.out.println("Productos cargados exitosamente desde " + NOMBRE_ARCHIVO_PRODUCTOS);
        } catch (FileNotFoundException e) {
            System.out.println(NOMBRE_ARCHIVO_PRODUCTOS + " no encontrado. Se inicia la tabla vacía.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar productos: " + e.getMessage());
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error de Persistencia", "Archivo Corrupto", "No se pudieron leer los datos del archivo serializado.");
        }

        ControllerUtils.actualizarTableView(tbvProductos, productos);
    }
    
    private void abrirFormulario(List<ItemCarrito> items){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/CarritoView.fxml"));
            Scene scene = new Scene(loader.load());
            
            CarritoViewController controller = loader.getController();
            controller.setCarritoData(itemsCarrito);
            
            controller.setParentController(this);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setScene(scene);
            stage.showAndWait();
            
            tbvProductos.refresh();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        cargarProductos();
    }
}