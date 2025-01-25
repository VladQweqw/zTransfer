package main.com;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import com.google.gson.Gson;
public class API<T> {
    private String ENDPOINT = "http://192.168.1.69:3003/";

    public API(String endpoint) {
        this.ENDPOINT = endpoint;
    }
    public API() {

    }

    private static String createMultipartBody(String boundary, Map<String, String> credentials) throws Exception {
        StringBuilder sb = new StringBuilder();

        Path filePath = Path.of(credentials.get("upload_file"));
        String author = credentials.get("author");
        String name = credentials.get("name");

        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"author\"\r\n\r\n");
        sb.append(author).append("\r\n");

        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"name\"\r\n\r\n");
        sb.append(name).append("\r\n");

        // Add the file field
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"upload_file\"; filename=\"")
                .append(filePath.getFileName()).append("\"\r\n");
        sb.append("Content-Type: application/octet-stream\r\n\r\n");
        sb.append(new String(java.nio.file.Files.readAllBytes(filePath)));
        sb.append("\r\n");

        sb.append("--").append(boundary).append("--\r\n");

        return sb.toString();
    }

    public <T> T POST(String json, Type typeOfT) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            return gson.fromJson(response.body(), typeOfT);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
    public <T> T POSTFile(String url, Type typeOfT, Map<String, String> credentials) {

        try {
            String boundary = "Boundary-" + System.currentTimeMillis();
            String multipartBody = createMultipartBody(boundary, credentials);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofString(multipartBody))
                    .build();

            HttpResponse<String> response = null;

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                Gson gson = new Gson();
                return gson.fromJson(response.body(), typeOfT);
            }catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to send file");
        }

        return null;
    }

    public <T> T POST(String url, String json, Type typeOfT) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT + url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            return gson.fromJson(response.body(), typeOfT);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String POST(String url, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT + url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            return response.body();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T GET(Type typeOfT) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            return gson.fromJson(response.body(), typeOfT);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public <T> T GET(String url, Type typeOfT) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT + url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            System.out.println(response.body());
            return gson.fromJson(response.body(), typeOfT);
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
