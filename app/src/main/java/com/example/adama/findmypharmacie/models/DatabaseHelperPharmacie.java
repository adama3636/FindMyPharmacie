package com.example.adama.findmypharmacie.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by adama on 22/02/2016.
 */
public class DatabaseHelperPharmacie extends SQLiteOpenHelper {

    private static String DB_NAME = "pharmacie.db";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;

    private static String TABLE_NAME = "Pharmacie";

    private static String CREATE_TABLE_CONTACTS = "create table "
            + TABLE_NAME + " ("
            + "pharmacieId integer PRIMARY KEY  AUTOINCREMENT,"
            + "name VARCHAR(250) NOT NULL," + "telephone VARCHAR(250) NOT NULL,"
            + "address VARCHAR(250) NOT NULL," + "emei VARCHAR(250) NOT NULL,"
            + "latitude VARCHAR(250) NOT NULL," + "longitude VARCHAR(250) NOT NULL,"
            + "accuracy VARCHAR(250) NOT NULL," + "image VARCHAR(250)"
            + ")";

    public DatabaseHelperPharmacie(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertPharmacie(Pharmacie pharmacie) {
        ContentValues pharmacieToInsert = new ContentValues();
        pharmacieToInsert.put("name", pharmacie.getName());
        pharmacieToInsert.put("telephone", pharmacie.getTelephone());
        pharmacieToInsert.put("address", pharmacie.getAddress());
        pharmacieToInsert.put("emei", pharmacie.getEmei());
        pharmacieToInsert.put("latitude", pharmacie.getLatitude());
        pharmacieToInsert.put("longitude", pharmacie.getLongitude());
        pharmacieToInsert.put("accuracy", pharmacie.getAccracy());
        pharmacieToInsert.put("image", pharmacie.getImage());
        return db.insert(TABLE_NAME, null, pharmacieToInsert);
    }

    public boolean deletePharamacie(Pharmacie pharmacie){
         return db.delete(TABLE_NAME, "name=? AND telephone=? AND address =?",
                new String[]{pharmacie.getName(), pharmacie.getTelephone(), pharmacie.getAddress()})> 0;
    }

    public ArrayList<String> getPharmacie() {
        ArrayList<String> output = new ArrayList<>();

        String[] colonnesARecup = new String[] { "name",  "telephone", "address" , "emei", "latitude" , "longitude", "accuracy", "image" };

        Cursor cursorResults = db.query(TABLE_NAME, colonnesARecup, null,
                null, null, null, "name asc, address asc", null);
        if (null != cursorResults) {
            if (cursorResults.moveToFirst()) {
                do {
                    String name =  cursorResults.getString(cursorResults.getColumnIndex("name"));
                    String telephone =  cursorResults.getString(cursorResults.getColumnIndex("telephone"));
                    String address =  cursorResults.getString(cursorResults.getColumnIndex("address"));
                    String imei =  cursorResults.getString(cursorResults.getColumnIndex("emei"));
                    String latitude =  cursorResults.getString(cursorResults.getColumnIndex("latitude"));
                    String longitude =  cursorResults.getString(cursorResults.getColumnIndex("longitude"));
                    String accuracy =  cursorResults.getString(cursorResults.getColumnIndex("accuracy"));
                    String image =  cursorResults.getString(cursorResults.getColumnIndex("image"));

                    //String nomPrenom = cursorResults.getString(columnIdxNom)+ " " + cursorResults.getString(columnIdxPrenom);
                    output.add(name  +","+ telephone+ "," + address + ","+ imei + "," + latitude+ ","+ longitude + "," + accuracy+ "," + image);
                } while (cursorResults.moveToNext());
            } // end§if
        }

        return output;
    }
}
