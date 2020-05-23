package com.covid19tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        final TextView github = findViewById(R.id.textView35);
        github.setMovementMethod(LinkMovementMethod.getInstance());
        final TextView volley = findViewById(R.id.textView36);
        volley.setMovementMethod(LinkMovementMethod.getInstance());
        final TextView api = findViewById(R.id.textView38);
        api.setMovementMethod(LinkMovementMethod.getInstance());
        final TextView contact = findViewById(R.id.textView50);
        contact.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void home() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
