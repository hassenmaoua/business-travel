package edu.businesstravel.dao.entities;

public class User {
    private long idUser;
    private String email;
    private String pswd;
    private Role role;

    public User() {}

    public User(String email, String pswd, Role role) {
        this.email = email;
        this.pswd = pswd;
        this.role = role;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", email='" + email + '\'' +
                ", pswd='" + pswd + '\'' +
                ", role=" + role +
                '}';
    }
}
