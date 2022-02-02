package ytVisualizer.GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ytVisualizer.YtVideo;

import java.io.IOException;
import java.util.Objects;

public class PlaylistItem extends GridPane {

    @FXML
    private Label author;

    @FXML
    private Label title;

    @FXML
    private StackPane thumbnail;


    public PlaylistItem(YtVideo video) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlaylistItem.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
            title.setText(video.getTitle());
            author.setText(video.getArtiste());
            getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
            String style = "-fx-background-image: url('" + video.getThumbnail() + "');";
            thumbnail.setStyle(style);
        }
        catch (IOException error) {
            throw new RuntimeException(error);
        }
    }
}
