package ytVisualizer.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import ytVisualizer.YtVideo;

import java.io.IOException;

public class MainViewController extends GridPane {

    @FXML
    private Label title;

    @FXML
    private ImageView thumbnail;

    @FXML
    private Button sendButton;

    @FXML
    private TextField urlInput;

    @FXML
    private MediaView media;

    private MediaPlayer player;

    public MainViewController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            sendButton.setOnAction(event -> updateInfo());
        }
        catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    public void updateInfo(){
        String url = urlInput.getText();
        urlInput.clear();
        YtVideo vid = new YtVideo(url);
        thumbnail.setImage(vid.getThumbnail());
        title.setText(vid.getTitle());
    }
}
