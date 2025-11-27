package sistemadeventas;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SistemaDeVentas extends Application{
    
    public void start(Stage stage) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/VentasView.fxml"));
        
        Scene scene = new Scene(loader.load());
        
        stage.setScene(scene);
        
        stage.setTitle("Sistema de Ventas");
        
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
    
}
