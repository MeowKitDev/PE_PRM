package com.example.pe.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pe.adapter.DataAdapter;

public class ClockProvider extends ContentProvider{
    private static final String AUTHORITY ="com.example.pe.provider.ClockProvider";
    private static final String TABLE_NAME = "Clocks";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+TABLE_NAME);


    public static final int Items = 1;
    public static final int Item_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, Items);
        uriMatcher.addURI(AUTHORITY,TABLE_NAME + "/#", Item_ID);
    }

    private DataAdapter myAdapter;

    @Override
    public boolean onCreate() {
        myAdapter = new DataAdapter(getContext(), null, null, 1);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DataAdapter.TABLE_NAME);
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case Item_ID:
                queryBuilder.appendWhere(DataAdapter.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case Items:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor =queryBuilder.query(myAdapter.getReadableDatabase(),strings,
                s, strings1, null,null, s1);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = myAdapter.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case Items:
                id = sqlDB.insert(myAdapter.TABLE_NAME,null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLE_NAME + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = myAdapter.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case Items:
                rowsDeleted = sqlDB.delete(myAdapter.TABLE_NAME, s,
                        strings);
                break;
            case Item_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    rowsDeleted = sqlDB.delete(DataAdapter.TABLE_NAME,
                            myAdapter.COLUMN_ID + "=" + id,null);
                } else {
                    rowsDeleted = sqlDB.delete(DataAdapter.TABLE_NAME,
                            myAdapter.COLUMN_ID + "=" + id + " and " + s,
                            strings);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = myAdapter.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case Items:
                rowsUpdated = sqlDB.update(DataAdapter.TABLE_NAME,contentValues,
                        s, strings);
                break;
            case Item_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(s)) {
                    rowsUpdated = sqlDB.update(DataAdapter.TABLE_NAME,
                            contentValues, DataAdapter.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(DataAdapter.TABLE_NAME,
                            contentValues, DataAdapter.COLUMN_ID + "=" + id + " and "
                                    + s, strings);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
