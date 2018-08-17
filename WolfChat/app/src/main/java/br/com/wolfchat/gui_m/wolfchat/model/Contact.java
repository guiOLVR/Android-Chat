package br.com.wolfchat.gui_m.wolfchat.model;

public class Contact {

    private String identifierUser;
    private String name;
    private String email;

    public Contact() {

    }

    public String getIdentifierUser() {
        return identifierUser;
    }

    public void setIdentifierUser(String identifierUser) {
        this.identifierUser = identifierUser;
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
}
