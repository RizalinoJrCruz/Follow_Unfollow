package com.example.follow_feature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "threadlyv3.db";

    //For USER TABLE
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_UID = "uid";

    //For FOLLOW TABLE
    private static final String COLUMN_FOLLOWER_ID = "followerId";
    private static final String COLUMN_FOLLOWING_ID = "followingId";
    private static final String COLUMN_NAME = "name";

    public static final String USER_TABLE = "Users";
    public static final String FOLLOW_TABLE = "Follow";


    public DatabaseHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE " + USER_TABLE + " ("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_UID +" TEXT NOT NULL, "
                    + COLUMN_NAME + "TEXT NOT NULL)");

            db.execSQL("CREATE TABLE " + FOLLOW_TABLE + " ("
                    + COLUMN_FOLLOWER_ID +" TEXT NOT NULL, "
                    + COLUMN_FOLLOWING_ID + " TEXT NOT NULL, "
                    + "PRIMARY KEY (" + COLUMN_FOLLOWER_ID + ", " + COLUMN_FOLLOWING_ID + "))");
        } catch (Exception e)
        {
            Log.e("DB_ERROR", "Error creating tables: "+ e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FOLLOW_TABLE);
        onCreate(db);
    }

    //STORE USER INFO IN DATABASE
    public boolean addUser(String userId, String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UID, userId );
        values.put(COLUMN_NAME, name);
        long result = db.insert(USER_TABLE, null, values);
        db.close();
        return result != 1;
    }

    //INSERT THE FOLLOWING USER TO DATABASE
    public boolean followUser(String followId, String followerId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLLOWER_ID, followId);
        values.put(COLUMN_FOLLOWING_ID, followerId);
        long result = db.insert(FOLLOW_TABLE, null, values);
        db.close();
        return result != 1;
    }

    //DELETE FOLLOWING DATA
    public boolean unfollowUser(String followIdNew, String followerIdNew)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(FOLLOW_TABLE, COLUMN_FOLLOWER_ID + "=? AND " + COLUMN_FOLLOWING_ID + "=?", new String[]{followIdNew,followerIdNew});
        return result > 0;
    }

    //USER CHECK IF FOLLOWED
    public boolean checkFollowing(String followIdNew, String followerIdNew)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FOLLOW_TABLE, null, COLUMN_FOLLOWER_ID+ "=? AND " + COLUMN_FOLLOWING_ID + "=?", new String[]{followIdNew,followerIdNew},null,null,null);
        boolean checkFollowing = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return checkFollowing;
    }
}
