package main.com.Types;

import java.util.List;

public class RoomType {
    private String _id;
    private String name;
    private UserType author;
    private List<FileType> Files;
    private String created_at;
    private String updated_at;
    private List<String> peopleInside;

    public void setPeopleInside(List<String> peopleInside) {
        this.peopleInside = peopleInside;
    }

    public List<String> getPeopleInside() {
        return peopleInside;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(UserType author) {
        this.author = author;
    }

    public void setFiles(List<FileType> files) {
        Files = files;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public UserType getAuthor() {
        return author;
    }

    public Object getFiles() {
        return Files;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
