package com.start_up.dev.apilinkus.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Huong on 10/10/2016.
 */
//#ADD
public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "linkus_db";
    // Contacts table name
    private static final String TABLE_AUTHENTIFICATION = "Authentification";

    /*TOKENS TABLE COLUMN NAME*/
    public static final String NAME_USER_ID="user_id";
    public static final String NAME_USER_NAME="user_name";
    public static final String NAME_USER_LASTNAME="user_lastname";
    public static final String NAME_USER_FIRSTNAME="user_firstname";
    public static final String NAME_MODE_AUTH="mode_auth";
    public static final String NAME_ACCESS_TOKEN="access_token";
    public static final String NAME_TOKEN_TYPE="token_type";
    public static final String NAME_REFRESH_TOKEN="refresh_token";

    //DBHandler db = new DBHandler(this);
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_AUTHENTIFICATION + "("
                + NAME_USER_ID + " TEXT PRIMARY KEY,"
                + NAME_USER_NAME + " TEXT ,"
                + NAME_USER_LASTNAME + " TEXT ,"
                + NAME_USER_FIRSTNAME + " TEXT ,"
                + NAME_MODE_AUTH + " TEXT,"
                + NAME_ACCESS_TOKEN + " TEXT,"
                + NAME_TOKEN_TYPE + " TEXT,"
                + NAME_REFRESH_TOKEN + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHENTIFICATION);
    // Creating tables again
        onCreate(db);
    }


    public void writeAuthentificationInBD(String userId,String userName,String lastName,String firstName,String mode_auth, String access_token,String token_type, String refresh_token ) {
        SQLiteDatabase db = this.getWritableDatabase();
        //On suprimer le contenu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHENTIFICATION);
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(NAME_USER_ID, userId);
        values.put(NAME_USER_NAME, userName);
        values.put(NAME_USER_LASTNAME, lastName);
        values.put(NAME_USER_FIRSTNAME, firstName);
        values.put(NAME_MODE_AUTH, mode_auth);
        values.put(NAME_ACCESS_TOKEN, access_token);
        values.put(NAME_TOKEN_TYPE, token_type);
        values.put(NAME_REFRESH_TOKEN, refresh_token);
        // Inserting Row
        db.insert(TABLE_AUTHENTIFICATION, null, values);
        db.close(); // Closing database connection
        updateAuthentificationFromDB();
    }

    /**
     * Cette fonction ne devrait être appelé qu'une seule et unique fois. Ce concerter avec l'équipe avant de l'appeler
     * @param accessToken
     */
    public void writeOnlyAccessTokenInBD(String accessToken) {
        SQLiteDatabase db = this.getWritableDatabase();
        //On suprimer le contenu
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHENTIFICATION);
        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(NAME_USER_ID, "");
        values.put(NAME_USER_NAME, "");
        values.put(NAME_USER_LASTNAME, "");
        values.put(NAME_USER_FIRSTNAME, "");
        values.put(NAME_MODE_AUTH, "");
        values.put(NAME_ACCESS_TOKEN, accessToken);
        values.put(NAME_TOKEN_TYPE, "");
        values.put(NAME_REFRESH_TOKEN, "");
        // Inserting Row
        db.insert(TABLE_AUTHENTIFICATION, null, values);
        db.close(); // Closing database connection
        updateAuthentificationFromDB();
    }



    private void updateAuthentificationFromDB() {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_AUTHENTIFICATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        //There should be only one line
        if (cursor.moveToFirst()) {
            do {
                Authentification.withBDsetUserId(cursor.getString(0));
                Authentification.withBDsetUserName(cursor.getString(1));
                Authentification.withBDsetLastName(cursor.getString(2));
                Authentification.withBDsetFirstName(cursor.getString(3));
                Authentification.withBDsetMode_auth(cursor.getString(4));
                Authentification.withBDsetAccess_token(cursor.getString(5));
                Authentification.withBDsetToken_type(cursor.getString(6));
                Authentification.withBDsetRefresh_token(cursor.getString(7));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
