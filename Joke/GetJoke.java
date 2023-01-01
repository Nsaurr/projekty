import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetJoke {
    private String result;
    public void getJokes(String link){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(link))
            .build();
        HttpResponse response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.result = response.body().toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getResult() {
        return result;
    }
}
