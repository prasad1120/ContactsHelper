package com.example.contactshelper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

class ContactsHandler {

    static final String NAME_KEY = "name";
    static final String NUM_KEY = "number";

    private ContactsHandler() { }

    // num=-1 when contact is to be deleted
    static void saveContact(Context context, String name, long prevNum, long num) {
        SharedPreferences sharedPref = context.getSharedPreferences("contacts", MODE_PRIVATE);
        String contactsJson = sharedPref.getString("contactsJson", "[]");

        try {
            JSONArray contactsArray = new JSONArray(contactsJson);
            JSONObject object = null;

            for (int i = 0; i < contactsArray.length(); i++) {
                JSONObject tempObj = contactsArray.getJSONObject(i);

                if (tempObj.getLong(NUM_KEY) == prevNum) {
                    if (num == -1) {
                        contactsArray.remove(i);
                        break;
                    }
                    object = tempObj;
                    object.put(NAME_KEY, name);
                    object.put(NUM_KEY, num);
                    break;
                }
            }

            if (object == null && num != -1) {

                object = new JSONObject();
                object.put(NAME_KEY, name);
                object.put(NUM_KEY, num);
                contactsArray.put(object);
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("contactsJson", contactsArray.toString());
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Contact[] getContacts(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences("contacts", MODE_PRIVATE);
        String contactsJson = sharedPref.getString("contactsJson", "[]");

        Gson gson = new Gson();
        Contact[] contacts = gson.fromJson(contactsJson, Contact[].class);
        Arrays.sort(contacts);
        return contacts;
    }

    static Contact getContact(Context context, long num) {

        SharedPreferences sharedPref = context.getSharedPreferences("contacts", MODE_PRIVATE);
        String contactsJson = sharedPref.getString("contactsJson", "[]");

        Gson gson = new Gson();
        Contact[] contacts = gson.fromJson(contactsJson, Contact[].class);

        for (Contact contact: contacts) {
            if (contact.number == num) {
                return contact;
            }
        }

        return null;
    }
}
