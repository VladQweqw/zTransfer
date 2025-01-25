package main.com.Types;

import java.util.List;

public class RoomTypeUID {
    private String _id;
    private String name;
    private String author;
    private List<FileType> Files;
    private String createdAt;
    private String updatedAt;
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setFiles(List<FileType> files) {
        Files = files;
    }

    public void setCreated_at(String created_at) {
        this.createdAt = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updatedAt = updated_at;
    }

    public String getAuthor() {
        return author;
    }

    public Object getFiles() {
        return Files;
    }

    public String getCreated_at() {
        return createdAt;
    }

    public String getUpdated_at() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return getName();
    }
}
