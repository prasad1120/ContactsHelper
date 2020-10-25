package com.example.contactshelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactEditActivity(null, -1);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapter(ContactsHandler.getContacts(getApplicationContext()), this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Contact[] contacts = ContactsHandler.getContacts(getApplicationContext());

        TextView addContactsTv = findViewById(R.id.add_contacts_tv);
        addContactsTv.setVisibility(contacts.length == 0 ? View.VISIBLE : View.INVISIBLE);

        recyclerAdapter.setContacts(contacts);
        recyclerAdapter.notifyDataSetChanged();

        checkPermission();
    }

    public void checkPermission() {
        String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE};
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 1);
        }
    }

    public void openContactEditActivity(String name, long number) {
        Intent intent = new Intent(this, ContactEditActivity.class);
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putLong("number", number);
        intent.putExtra("BUNDLE",args);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Contact[] contacts = ContactsHandler.getContacts(getApplicationContext());
        openContactEditActivity(contacts[position].name, contacts[position].number);
    }

    @Override
    public void onCallClick(int position) {
        Contact[] contacts = ContactsHandler.getContacts(getApplicationContext());
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+contacts[position].number));
        startActivity(callIntent);
    }

    @Override
    public void onWhatsappClick(int position) {
        Contact[] contacts = ContactsHandler.getContacts(getApplicationContext());
        String url = "https://api.whatsapp.com/send?phone=+91 " + contacts[position].number;

        try {
            PackageManager pm = getApplicationContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(), "WhatsApp is not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

