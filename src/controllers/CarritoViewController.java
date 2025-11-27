package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.GestorArchivoTicket;
import modelo.ItemCarrito;


public class CarritoViewController implements Initializable {
    
    GestorArchivoTicket gestorTicket = new GestorArchivoTicket();
    private List<ItemCarrito> carritoData = new ArrayList<>();
    
    private VentasViewController parentController;
    
    @FXML
    private Button btnConfirmar;
    
    @FXML
    private TableView<ItemCarrito> tbvCarrito;
    
    @FXML
    private TableColumn<ItemCarrito, String> colNombre;
    @FXML
    private TableColumn<ItemCarrito, Integer> colCantidad;
    @FXML
    private TableColumn<ItemCarrito, Double> colPrecio;

    @FXML
    private Label lblPrecioTotal;
    
    
    @FXML
    private void confirmar(ActionEvent e){
        if (carritoData.isEmpty()) {
            ControllerUtils.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Carrito Vacío", "No hay productos para confirmar la compra.");
            return;
        }
        
        double total = calcularTotal();
        gestorTicket.escribirTicket(carritoData, total);
        
        if (parentController != null) {
            parentController.guardarProductosPersistir();
        }
        
        carritoData.clear();
        
        ControllerUtils.mostrarAlerta(Alert.AlertType.INFORMATION, "Confirmado", "Compra realizada", "Se generó un ticket con los datos de su carrito. Total: $" + String.format("%.2f", total));
        
        Stage stage = (Stage) btnConfirmar.getScene().getWindow();
        stage.close();
    }
    
    public void setParentController(VentasViewController vc) {
        this.parentController = vc;
    }
    
    public void setCarritoData(List<ItemCarrito> items){
        this.carritoData = items;
        tbvCarrito.getItems().setAll(carritoData);
        tbvCarrito.refresh();
        actualizarTotal();
    }
    
    private double calcularTotal(){
        if (carritoData == null) {
            return 0.0;
        }
        double total = 0.0;
        for (ItemCarrito item : carritoData){
            total += item.getSubtotal();
        }
        return total;
    }
    
    private void actualizarTotal(){
        double total = calcularTotal();
        lblPrecioTotal.setText("Total:" + String.valueOf(total));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }    
}