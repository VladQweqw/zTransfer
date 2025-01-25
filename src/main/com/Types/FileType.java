package main.com.Types;

public class FileType {
    private String _id;
    private String name;
    private String url;
    private String size;
    private String author;
    private String createdAt;
    private String updatedAt;

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }
}
