package main.com.Types;

import java.util.List;

public class UserType {
    private String _id;
    private String username = "";
    private String password = "";
    private List<String> joined_rooms;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJoined_rooms(List<String> joined_rooms) {
        this.joined_rooms = joined_rooms;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public List<String > getJoined_rooms() {
        return joined_rooms;
    }
}
