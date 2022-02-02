package ytVisualizer.GUI;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class MainView extends Stage implements Initializable {

    public MainView(){

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GridPane root = new MainViewController();
        this.setTitle("Youtube Visualizer");
        this.setScene(new Scene(root, 1280, 720));
        this.show();
    }
}
