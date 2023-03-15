package com.example.pe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pe.adapter.DataAdapter;

public class DeleteActivity extends AppCompatActivity {

    EditText id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        id = (EditText) findViewById(R.id.edit_id);
    }

    public void deleteData(View view) {
        String productId = id.getText().toString();
        if (!productId.isEmpty()) {
            try {
                DataAdapter handler = new DataAdapter(this, null, null, 1);
                boolean result = handler.deleteDataAdapter(Integer.parseInt(productId));
                if (result) {
                    id.setText("");
                    Toast.makeText(this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please fill right id!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Id must be a number!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill id!", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToHome(View view) {
        Intent intent = new Intent(DeleteActivity.this, MainActivity.class);
        startActivity(intent);
    }
}