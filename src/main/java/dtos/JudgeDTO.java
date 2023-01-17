package dtos;

import entities.User;

public class JudgeDTO {


    String name;
    String email;
    int phone;

    public JudgeDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
