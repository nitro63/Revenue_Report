package Controller.Gets;

public class GetUser {
    String username, firstName, lastName, email, password, level, user_center;

    public GetUser(){}
    public GetUser(String username, String firstName, String lastName, String email, String password, String level, String user_center){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user_center = user_center;
        this.level = level;
        this.password = password;
        this.username = username;
    }
    public GetUser(String username, String firstName, String lastName, String email, String level, String user_center){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user_center = user_center;
        this.level = level;
        this.username = username;
    }
    public GetUser(String username, String firstName, String lastName, String email, String level){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUser_center() {
        return user_center;
    }

    public void setUser_center(String user_center) {
        this.user_center = user_center;
    }
}
