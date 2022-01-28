import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {
        Scanner read = new Scanner(System.in);
        String toRead = read.nextLine();
        Element node = getJsonScript(toRead);
        if (node != null){
            System.out.println(node);
        }
        else {
            System.out.println("ça marche pas T-T");
        }
    }

    private static Element getJsonScript(String url) throws IOException {
        Document page = Jsoup.connect(url).get();
        Elements all = page.getAllElements();
        Elements zob = all.select("script");
        for (Element element : zob) {
            if (element.toString().contains("var ytInitialPlayerResponse")) return element;
        }
        return null;
    }
}
