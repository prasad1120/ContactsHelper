package com.example.contactshelper;

import com.google.gson.annotations.SerializedName;

public class Contact implements Comparable<Contact> {

    @SerializedName(ContactsHandler.NAME_KEY)
    String name;

    @SerializedName(ContactsHandler.NUM_KEY)
    long number;

    Contact(String name, long number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public int compareTo(Contact contact) {
        return this.name.toUpperCase().compareTo(contact.name.toUpperCase());
    }
}

