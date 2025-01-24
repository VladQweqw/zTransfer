package main.com.Types;

public class FileType {
    private String _id;
    private String name;
    private String url;
    private String size;
    private UserType author;

    public UserType getAuthor() {
        return author;
    }

    public void setAuthor(UserType author) {
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
