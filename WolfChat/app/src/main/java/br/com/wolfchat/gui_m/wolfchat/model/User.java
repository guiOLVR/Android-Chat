package br.com.wolfchat.gui_m.wolfchat.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.wolfchat.gui_m.wolfchat.config.FirebaseConfig;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;

    public User(){

    }

    public void save(){
        DatabaseReference firebaseReference = FirebaseConfig.getFirebase();
        firebaseReference.child("users").child(getId()).setValue(this);
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
