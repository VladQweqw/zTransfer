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

    public String createMultipartBody(Map<String, String> credentials, String boundary) throws IOException {
        Path filePath = Path.of(credentials.get("upload_file"));
        String author = credentials.get("author");
        String name = credentials.get("name");

        // Start building the multipart body
        StringBuilder sb = new StringBuilder();

        // Add author field
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"author\"\r\n\r\n");
        sb.append(author).append("\r\n");

        // Add name field
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"name\"\r\n\r\n");
        sb.append(name).append("\r\n");

        // Add the file field
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"upload_file\"; filename=\"")
                .append(filePath.getFileName()).append("\"\r\n");
        sb.append("Content-Type: application/octet-stream\r\n\r\n");

        // Get the file content as a byte array
        byte[] fileData = Files.readAllBytes(filePath);

        // Convert StringBuilder to byte array
        byte[] headerBytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        // Prepare final byte array (header + file data + closing boundary)
        byte[] finalRequest = new byte[headerBytes.length + fileData.length + ("--" + boundary + "--\r\n").getBytes().length];

        // Copy the header bytes to the final byte array
        System.arraycopy(headerBytes, 0, finalRequest, 0, headerBytes.length);
        // Copy the file data to the final byte array
        System.arraycopy(fileData, 0, finalRequest, headerBytes.length, fileData.length);
        // Append the closing boundary
        System.arraycopy(("--" + boundary + "--\r\n").getBytes(), 0, finalRequest, headerBytes.length + fileData.length, ("--" + boundary + "--\r\n").length());

        // Return the multipart body as a string (but it is actually byte data that will be sent)
        return new String(finalRequest, StandardCharsets.UTF_8);
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
            String multipartBody = createMultipartBody(credentials, boundary);
            System.out.println(multipartBody);

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
