package com.example.pe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pe.adapter.DataAdapter;
import com.example.pe.dto.ClockDTO;

import java.time.Clock;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView dataList;
    TextView txtColumnName;
    EditText productName;
    EditText productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("LE CHI CUONG - SE151508");
        setContentView(R.layout.activity_main);
//        dataList = findViewById(R.id.txtData);
//        txtColumnName = findViewById(R.id.txtColumnName);
        productName = (EditText) findViewById(R.id.edit_name);
        productPrice = (EditText) findViewById(R.id.edit_price);
        DataAdapter handler = new DataAdapter(this, null, null, 1);
        //        add table UI
        TableLayout tableLayout = findViewById(R.id.my_table);
        TableRow header = new TableRow(this);
        TextView idHeaderCell = new TextView(this);
        idHeaderCell.setPadding(16, 16, 16, 16);
        idHeaderCell.setBackgroundResource(R.drawable.cell_border);
        idHeaderCell.setTypeface(Typeface.DEFAULT_BOLD);
        idHeaderCell.setText("ID");
        header.addView(idHeaderCell);

        TextView nameHeaderCell = new TextView(this);
        nameHeaderCell.setText("Name");
        nameHeaderCell.setPadding(16, 16, 16, 16);
        nameHeaderCell.setBackgroundResource(R.drawable.cell_border);
        nameHeaderCell.setTypeface(Typeface.DEFAULT_BOLD);
        header.addView(nameHeaderCell);

        TextView priceHeaderCell = new TextView(this);
        priceHeaderCell.setText("Price($)");
        priceHeaderCell.setPadding(16, 16, 16, 16);
        priceHeaderCell.setBackgroundResource(R.drawable.cell_border);
        priceHeaderCell.setTypeface(Typeface.DEFAULT_BOLD);
        header.addView(priceHeaderCell);

        tableLayout.addView(header);
        List<ClockDTO> data = handler.loadDataAdapter();

        for (ClockDTO clock: data){
            TableRow row = new TableRow(this);
            TextView idCell = new TextView(this);
            idCell.setText(String.valueOf(clock.getId()));
            idCell.setPadding(16, 16, 16, 16);
            idCell.setBackgroundResource(R.drawable.cell_border);
            row.addView(idCell);

            TextView nameCell = new TextView(this);
            nameCell.setText(String.valueOf(clock.getName()));
            nameCell.setPadding(16, 16, 16, 16);
            nameCell.setBackgroundResource(R.drawable.cell_border);
            row.addView(nameCell);

            TextView priceCell = new TextView(this);
            priceCell.setText(String.valueOf(clock.getPrice()));
            priceCell.setPadding(16, 16, 16, 16);
            priceCell.setBackgroundResource(R.drawable.cell_border);
            row.addView(priceCell);

            tableLayout.addView(row);
        }
        handler.loadDataAdapter();
//        dataList.setText(handler.loadDataAdapter());
//        if (!dataList.getText().toString().isEmpty()) {
//            txtColumnName.setVisibility(View.VISIBLE);
//        }
    }

    public void addData(View view) {
        String name = productName.getText().toString();
        String price = productPrice.getText().toString();
        if (!name.isEmpty()) {
            if (!price.isEmpty() && Integer.parseInt(price) > 0) {
                DataAdapter handler = new DataAdapter(this, null, null, 1);
                System.out.println(price);
                ClockDTO product = new ClockDTO(name, Integer.parseInt(price));
                handler.addDataAdapter(product);
                productName.setText("");
                productPrice.setText("");
                Toast.makeText(this, "Add successfully!", Toast.LENGTH_SHORT).show();
                handler.loadDataAdapter();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
//                finish();
//                overridePendingTransition(0, 0);
                startActivity(i);
//                overridePendingTransition(0, 0);
            } else {
                Toast.makeText(this, "Price of product is required and greater than 0!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Name of product is required!", Toast.LENGTH_LONG).show();
        }
    }

    public void addProduct(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

    public void deleteProduct(View view) {
        DataAdapter handler = new DataAdapter(this, null, null, 1);

        List<ClockDTO> data = handler.loadDataAdapter();

        if (!data.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Doesn't have any clock to delete!", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateProduct(View view) {
        DataAdapter handler = new DataAdapter(this, null, null, 1);
        List<ClockDTO> data = handler.loadDataAdapter();
        if (!data.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Doesn't have any clock to update!", Toast.LENGTH_SHORT).show();
        }

    }
}