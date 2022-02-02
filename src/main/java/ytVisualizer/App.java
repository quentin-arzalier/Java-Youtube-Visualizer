package ytVisualizer;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

public class App extends Application {

    private Stage primaryStage;
    private MainView mainView;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("Youtube Visualizer");
        mainView = new MainView();
    }

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        String toRead = read.nextLine();
        YtVideo toDownload = new YtVideo(toRead);
        System.out.println(toDownload.getThumbnail());
    }
}
