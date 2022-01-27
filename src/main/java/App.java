import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        Document page = Jsoup.connect("https://www.youtube.com/watch?v=9ghBNVF3BwM").get();
        System.out.println(page.title());
    }
}
