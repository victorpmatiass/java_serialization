package com.example.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable; 
import java.util.ArrayList;
import java.util.List;
public class ContactList implements Serializable { 

    @JsonProperty("c")
    private List<Contact> contacts;
    // No-arg constructor requerido para serialização
    public ContactList() {this.contacts = new ArrayList<>();}
    public ContactList(List<Contact> contacts) {this.contacts = contacts;}
    // Getters e Setters
    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }
}
