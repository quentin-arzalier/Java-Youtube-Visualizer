package ytVisualizer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class YtVideo {

    private String title;
    private String thumbnail;
    private String artiste;

    public YtVideo(String video){
        try {
            String var = getJsonScript(video);
            if (var == null) return;
            JSONObject ytInitialPlayerResponse = new JSONObject(var);
            JSONObject videoInfo = ytInitialPlayerResponse.getJSONObject("videoDetails");

            // Récupération du titre de la vidéo
            title = videoInfo.getString("title");
            artiste = videoInfo.getString("author");

            // Récupération du lien de la plus grande miniature
            JSONArray thumbs = videoInfo.getJSONObject("thumbnail").getJSONArray("thumbnails");
            String fullAddress = "webp";
            int i = 1;
            while (fullAddress.endsWith("webp")){
                JSONObject biggestThumb = new JSONObject(thumbs.get(thumbs.length()-i).toString());
                fullAddress = biggestThumb.getString("url");
                i++;
            }
            thumbnail = fullAddress.split("\\?")[0];
        }
        catch (IOException e){
            title = e.getMessage();
            thumbnail = "/img/error.png";
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

    public String getThumbnail(){
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getArtiste() {
        return artiste;
    }
}
