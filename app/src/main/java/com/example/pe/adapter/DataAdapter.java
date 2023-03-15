package com.example.pe.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pe.dto.ClockDTO;
import com.example.pe.provider.ClockProvider;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends SQLiteOpenHelper {

    private ContentResolver contentResolver;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ClocksSQLite";
    public static final String TABLE_NAME = "Clocks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "model";
    public static final String COLUMN_PRICE = "price";

    public DataAdapter(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CLOCKS_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PRICE + " INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_CLOCKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public List<ClockDTO> loadDataAdapter() {
        List<ClockDTO> result = new ArrayList<ClockDTO>();
        String query = "SELECT * FROM " + TABLE_NAME;
        String RESET_ID_COLUMN = "UPDATE SQLITE_SEQUENCE SET seq = 1 WHERE name = '" + TABLE_NAME + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.moveToFirst()) {
            db.rawQuery(RESET_ID_COLUMN, null);
        } else {
            do {
                int clock_id = cursor.getInt(0);
                String clock_name = cursor.getString(1);
                int clock_price = cursor.getInt(2);
                ClockDTO new_car = new ClockDTO(clock_id, clock_name, clock_price);
                result.add(new_car);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
    public void addDataAdapter(ClockDTO clock) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,  clock.getName());
        values.put(COLUMN_PRICE, clock.getPrice());
        contentResolver.insert(ClockProvider.CONTENT_URI, values);
    }

    public boolean deleteDataAdapter(int id){
        boolean result = false;
        String selection ="ID = \"" + id + "\"";
        int rowDeleted = contentResolver.delete(ClockProvider.CONTENT_URI, selection,null);
        if (rowDeleted > 0) {
            result = true;
        }
        return result;
    }

    public boolean updateDataAdapter(int ID, String name, int price) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, ID);
        args.put(COLUMN_NAME, name);
        args.put(COLUMN_PRICE, price);
        boolean result = false;
        String selection = "ID = \"" + ID + "\"";
        int rowsUpdated =
                contentResolver.update(ClockProvider.CONTENT_URI,args,selection,null);
        if (rowsUpdated > 0)
            result = true;
        return result;
    }
}
