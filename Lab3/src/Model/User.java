package Model;

import java.util.Objects;

public class User {
    protected String login;
    protected String password;
    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public boolean login(String login, String password){
        return Objects.equals(login, this.login) && Objects.equals(password, this.password);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

