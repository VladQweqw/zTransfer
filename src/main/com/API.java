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
import main.com.Exceptions.ApiException;
import main.com.Exceptions.HostUnreachable;
import main.com.Exceptions.NotFound;

public class API<T> {
    private String ENDPOINT = "http://192.168.1.69:3003/";

    public API(String endpoint) {
        this.ENDPOINT += endpoint;
    }
    public API() {}

    public byte[] createMultipartBody(Map<String, String> credentials, String boundary) throws IOException {
        Path filePath = Path.of(credentials.get("upload_file"));
        String author = credentials.get("author");
        String name = credentials.get("name");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"author\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write((author + "\r\n").getBytes(StandardCharsets.UTF_8));

        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"name\"\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write((name + "\r\n").getBytes(StandardCharsets.UTF_8));

        baos.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Disposition: form-data; name=\"upload_file\"; filename=\"" + filePath.getFileName() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
        baos.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes(StandardCharsets.UTF_8));

        byte[] fileBytes = Files.readAllBytes(filePath);
        baos.write(fileBytes);
        baos.write("\r\n".getBytes(StandardCharsets.UTF_8));

        baos.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        return baos.toByteArray();
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

            if(response.statusCode() >= 500) {
                throw new HostUnreachable();
            }

            if(response.statusCode() >= 400) {
                throw new ApiException();
            }

            return response.body();
        }catch (IOException e) {
            throw new HostUnreachable();
        } catch (HostUnreachable ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            throw new ApiException();
        } catch (ApiException ex) {
            System.out.println(ex.getMessage());
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

            if(response.statusCode() >= 500) {
                throw new HostUnreachable();
            }

            if(response.statusCode() >= 400) {
                throw new ApiException();
            }

            Gson gson = new Gson();
            return gson.fromJson(response.body(), typeOfT);
        }catch (IOException e) {
            throw new HostUnreachable();
        } catch (HostUnreachable ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            throw new ApiException();
        } catch (ApiException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }
    public <T> T POSTFile(String url, Type typeOfT, Map<String, String> credentials) {

        try {
            String boundary = "Boundary-" + System.currentTimeMillis();
            byte[] multipartBody = createMultipartBody(credentials, boundary);
            System.out.println(multipartBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT + url))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                    .build();

            HttpResponse<String> response = null;

            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                if(response.statusCode() >= 500) {
                    throw new HostUnreachable();
                }

                if(response.statusCode() == 404) {
                    throw new NotFound();
                }

                Gson gson = new Gson();
                return gson.fromJson(response.body(), typeOfT);
            }catch (IOException e) {
                throw new HostUnreachable();
            } catch (HostUnreachable ex) {
                System.out.println(ex.getMessage());
            } catch (InterruptedException e) {
                throw new ApiException();
            } catch (ApiException ex) {
                System.out.println(ex.getMessage());
            }catch (NotFound ex) {
                System.out.println(ex.getMessage());
            }

        }catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to send file");
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

            if(response.statusCode() >= 500) {
                throw new HostUnreachable();
            }

            if(response.statusCode() >= 400) {
                throw new ApiException();
            }

            Gson gson = new Gson();
            System.out.println(response.body());
            return gson.fromJson(response.body(), typeOfT);
        }catch (IOException e) {
            throw new HostUnreachable();
        } catch (HostUnreachable ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException e) {
            throw new ApiException();
        } catch (ApiException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

}
