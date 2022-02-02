package ytVisualizer;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class YtVideo {

    private String title;
    private Image thumbnail;
    private Media media;

    public YtVideo(String video){
        try {
            String var = getJsonScript(video);
            if (var == null) return;
            JSONObject ytInitialPlayerResponse = new JSONObject(var);
            JSONObject videoInfo = ytInitialPlayerResponse.getJSONObject("videoDetails");

            // Récupération du titre de la vidéo
            title = videoInfo.getString("title");

            // Récupération du lien de la plus grande miniature
            JSONArray thumbs = videoInfo.getJSONObject("thumbnail").getJSONArray("thumbnails");
            JSONObject biggestThumb = new JSONObject(thumbs.get(thumbs.length()-1).toString());
            thumbnail = new Image(biggestThumb.getString("url"));
        }
        catch (IOException e){
            title = e.getMessage();
            thumbnail = new Image("/img/error.png");
        }
    }

    private String getJsonScript(String url) throws IOException {
        Document page = Jsoup.connect(url).get();
        Elements all = page.getAllElements();
        Elements zob = all.select("script");
        for (Element element : zob) {
            String elementString = element.toString();
            if (elementString.contains("var ytInitialPlayerResponse")){
                elementString = elementString.substring(elementString.indexOf("var ytInitialPlayerResponse")+30,elementString.indexOf("}}}};")+4);
                return elementString;
            }
        }
        return null;
    }

    public Image getThumbnail(){
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }
}
