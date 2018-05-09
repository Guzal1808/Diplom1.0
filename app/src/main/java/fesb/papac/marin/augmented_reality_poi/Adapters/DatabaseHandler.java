package fesb.papac.marin.augmented_reality_poi.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.Favorite;

import static fesb.papac.marin.augmented_reality_poi.View.ViewMain.DEBUG_TAG;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    public static final String DATABASE_NAME = "favoritePlaces";

    // Labels table name
    public static final String TABLE_LABELS = "favorite_table";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DETAILS = "details";
    public static  final String KEY_IMG = "img";
    public static final String KEY_TYPE = "type";


    private String[] columns = {KEY_ID, KEY_NAME, KEY_LOCATION, KEY_DETAILS, KEY_IMG,KEY_TYPE};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
                + KEY_NAME + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_DETAILS + " TEXT,"
                + KEY_IMG + " BLOB,"
                + KEY_TYPE + " TEXT)";
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     */
    public boolean addFavorite(Favorite favorite) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, favorite.getName());
        values.put(KEY_LOCATION, favorite.getLocation());
        values.put(KEY_DETAILS, favorite.getDetails());
        values.put(KEY_IMG, favorite.getImage());
        values.put(KEY_TYPE, favorite.getType());

        Cursor cursor = db.rawQuery("select * from " + TABLE_LABELS + " where " + KEY_NAME + " ='" + favorite.getName() + "'", null);
        if (!cursor.moveToFirst()) {
            long rowID = db.insert(TABLE_LABELS, null, values);
            return true;
        }

        db.close(); // Closing database connection
        return false;
    }

    public List<Favorite> readAllFavorite() {
        // Get db writable

        SQLiteDatabase db = this.getWritableDatabase();

        // Define contacts list
        List<Favorite> favorites = new ArrayList<Favorite>();

        Cursor cursor = db.query(TABLE_LABELS, columns, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Favorite favorite = new Favorite();
            try {
                favorite.setId(Integer.parseInt(cursor.getString(0)));
                favorite.setName(cursor.getString(1));
                favorite.setLocation(cursor.getString(2));
                favorite.setDetails(cursor.getString(3));
                favorite.setImage(cursor.getBlob(4));
                favorite.setType(cursor.getString(5));
                favorites.add(favorite);
                cursor.moveToNext();
            }
            catch (Exception ex)
            {
                Log.d(DEBUG_TAG, "readAllFavorite"+ex);
            }
        }

        // Make sure to close the cursor
        cursor.close();
        return favorites;
    }

    public Favorite getFavoriteById(String id)
    {
        // Get db writable
        SQLiteDatabase db = this.getWritableDatabase();
        Favorite favorite = new Favorite();
        // Define contacts list

        Cursor cursor = db.query(TABLE_LABELS, columns, KEY_ID + " = ?", new String[]{id}, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            favorite.setId(Integer.parseInt(cursor.getString(0)));
            favorite.setName(cursor.getString(1));
            favorite.setLocation(cursor.getString(2));
            favorite.setDetails(cursor.getString(3));
            favorite.setImage(cursor.getBlob(4));
            favorite.setType(cursor.getString(5));
        }

        // Make sure to close the cursor
        cursor.close();

        return favorite;
    }

    public boolean deleteFavorite(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_LABELS, KEY_ID + " = ?", new String[]{id});

        db.close();

        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteFavoriteByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_LABELS, KEY_NAME + " = ?", new String[]{name});

        db.close();

        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfExist(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Favorite favorite = new Favorite();
        boolean ifExist=false;
        // Define contacts list

        Cursor cursor = db.query(TABLE_LABELS, columns, KEY_NAME + " = ?", new String[]{name}, null, null, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            ifExist=true;
        }

        // Make sure to close the cursor
        cursor.close();

        return ifExist;
    }

    public boolean deleteAllRecords()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(TABLE_LABELS, null, null);

        db.close();

        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }

}
