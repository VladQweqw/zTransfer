package main.com;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

    public byte[] createMultipartBody(Map<String, String> credentials, String boundary) throws IOException {
        Path filePath = Path.of(credentials.get("upload_file"));
        String author = credentials.get("author");
        String name = credentials.get("name");

        // Use ByteArrayOutputStream for binary-safe operations
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Add author field
        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"author\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write((author + "\r\n").getBytes(StandardCharsets.UTF_8));

        // Add name field
        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"name\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write((name + "\r\n").getBytes(StandardCharsets.UTF_8));

        // Add file field
        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"upload_file\"; filename=\"" + filePath.getFileName() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));

        // Add the file bytes
        byte[] fileBytes = Files.readAllBytes(filePath);
        baos.write(fileBytes);
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));

        // End boundary
        baos.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        return baos.toByteArray();
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
            byte[] multipartBody = createMultipartBody(credentials, boundary);
            System.out.println(multipartBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
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
