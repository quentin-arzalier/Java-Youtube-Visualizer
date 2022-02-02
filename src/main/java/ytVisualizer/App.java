package ytVisualizer;

import javafx.application.Application;
import javafx.stage.Stage;
import ytVisualizer.GUI.MainView;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Youtube Visualizer");
        MainView mainView = new MainView();
        mainView.initialize(null, null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
