package app.model;

public class Admin {

    private int admin_id;
    private String userName;
    private String password;

    public Admin(int admin_id, String userName, String password) {
        this.admin_id = admin_id;
        this.userName = userName;
        this.password = password;
    }

    public Admin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
