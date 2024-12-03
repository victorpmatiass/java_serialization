package com.example.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
public class Contact implements Serializable { 

    @JsonProperty("n")
    private String name;

    @JsonProperty("e")
    private String email;

    // No-arg constructor requerido para serialização
    public Contact() {}
    public Contact(String name, String email) {this.name = name;
                                               this.email = email;}
    // Getters e Setters
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public String getEmail() { return email;}
    public void setEmail(String email) { this.email = email;}
}
