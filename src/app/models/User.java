package app.models;
import java.io.Serializable;

public class User implements Serializable {
    private String username, password, role;

    public User(String u, String p, String r) {
        username=u; password=p; role=r;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}