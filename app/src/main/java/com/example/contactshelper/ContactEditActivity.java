package com.example.contactshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactEditActivity extends AppCompatActivity {

    long prevNumber = -1;

    EditText nameEt;
    EditText numEt;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        nameEt = findViewById(R.id.name_et);
        numEt = findViewById(R.id.number_et);

        Bundle args = getIntent().getBundleExtra("BUNDLE");

        String name = args.getString("name");
        prevNumber = args.getLong("number");

        nameEt.setText(name == null ? "" : name);
        numEt.setText(prevNumber == -1 ? "" : prevNumber + "");

        saveBtn = findViewById(R.id.save_btn);
        setSaveButton();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsHandler.saveContact(getApplicationContext(), nameEt.getText().toString(), prevNumber, Long.parseLong(numEt.getText().toString()));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                setSaveButton();
            }
        };

        nameEt.addTextChangedListener(textWatcher);
        numEt.addTextChangedListener(textWatcher);

        final Button deleteBtn = findViewById(R.id.delete_btn);

        if (prevNumber == -1) {
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsHandler.saveContact(getApplicationContext(), nameEt.getText().toString(), prevNumber, -1);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            }
        });
    }

    void setSaveButton() {
        if (nameEt.getText().length() > 0 && numEt.getText().length() == 10) {
            saveBtn.getBackground().setColorFilter(null);
            saveBtn.setEnabled(true);
        } else {
            saveBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            saveBtn.setEnabled(false);
        }
    }
}
