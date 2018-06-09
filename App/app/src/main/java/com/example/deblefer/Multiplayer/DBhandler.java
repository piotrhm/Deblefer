package com.example.deblefer.Multiplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "userDB.db";
    public static final String TABLE_NAME = "users";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WIN = "wins";
    public static final String COLUMN_LOSS = "loss";

    public DBhandler(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    static int id;
    static String name = "";
    static int win = 0, loss = 0;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (id integer PRIMARY KEY AUTOINCREMENT, "
                + "name VARCHAR not null, wins INTEGER, loss INTEGER );";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(UserData userData) {
        try {
            String query = "SELECT * FROM users WHERE name='" + userData.getName() + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                id = Integer.parseInt(cursor.getString(0));
                name = cursor.getString(1);
                win = Integer.parseInt(cursor.getString(2));
                loss = Integer.parseInt(cursor.getString(3));
                System.out.println("User already exists");
            }
            else {
                name = userData.getName();
                win = 0;
                loss = 0;
                try {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_NAME, name);
                    values.put(COLUMN_WIN, win);
                    values.put(COLUMN_LOSS, loss);
                    db.insert(TABLE_NAME, null, values);
                } catch (Exception e) {
                    System.out.println(e);
                }
                System.out.println("User added: "+name);
            }
            db.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void update(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WIN, win);
        values.put(COLUMN_LOSS, loss);
        db.update(TABLE_NAME, values, COLUMN_NAME + " = " + name, null);
        System.out.println("User updated: "+name);
    }

    public void delete(UserData userData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME+" = ?", new String[] { String.valueOf(userData.getName())});
        db.close();
        System.out.println("User deleted: "+userData.getName());

    }

    public void findUser(String userName) {
        String query = "SELECT * FROM users WHERE name = '" + userName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                id = cursor.getInt(0);
                name = cursor.getString(1);
                win = Integer.parseInt(cursor.getString(4));
                loss = Integer.parseInt(cursor.getString(5));
                cursor.close();
                System.out.println("User founded: "+name);
            } else {
                name = "";
            }
        } else {
            name = "";
        }
        db.close();
        System.out.println("User not founded: "+name);
    }


    public Cursor getUserData(String name) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor =
                db.query(true, TABLE_NAME, new String[] {COLUMN_ID,
                COLUMN_NAME, COLUMN_WIN, COLUMN_LOSS},
                COLUMN_NAME + "=" + name, null,null, null, null, null);
        if (mCursor != null) { mCursor.moveToFirst();}
        return mCursor;
    }

}

	

