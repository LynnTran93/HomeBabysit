package com.example.homebabysit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "homebabysit.db";
    private static final int DATABASE_VERSION = 1;

    // Babysitters table
    public static final String TABLE_BABYSITTERS = "babysitters";
    public static final String COLUMN_BABYSITTER_ID = "id";
    public static final String COLUMN_BABYSITTER_NAME = "name";
    public static final String COLUMN_BABYSITTER_LOCATION = "location";
    public static final String COLUMN_BABYSITTER_QUALIFICATIONS = "qualifications";
    public static final String COLUMN_BABYSITTER_EXPERIENCE = "experience";
    public static final String COLUMN_BABYSITTER_RATE = "hourly_rate";
    public static final String COLUMN_BABYSITTER_AVAILABILITY = "availability";
    public static final String COLUMN_BABYSITTER_AVERAGE_RATING = "average_rating";
    public static final String COLUMN_BABYSITTER_EMAIL = "email";

    // Reviews table
    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "id";
    public static final String COLUMN_BABYSITTER_REVIEW_ID = "babysitter_id";
    public static final String COLUMN_REVIEWER_NAME = "reviewer_name";
    public static final String COLUMN_REVIEW_TIME = "review_time";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_REVIEW_TEXT = "review_text";

    // Parents table
    public static final String TABLE_PARENTS = "parents";
    public static final String COLUMN_PARENT_ID = "id";
    public static final String COLUMN_PARENT_NAME = "name";
    public static final String COLUMN_PARENT_EMAIL = "email";
    public static final String COLUMN_PARENT_LOCATION = "location";
    public static final String COLUMN_PARENT_CHILDREN = "children";
    public static final String COLUMN_PARENT_PREFERENCES = "preferences";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create babysitters table
        String CREATE_BABYSITTERS_TABLE = "CREATE TABLE " + TABLE_BABYSITTERS + " (" +
                COLUMN_BABYSITTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BABYSITTER_NAME + " TEXT, " +
                COLUMN_BABYSITTER_EMAIL + " TEXT," +
                COLUMN_BABYSITTER_LOCATION + " TEXT, " +
                COLUMN_BABYSITTER_QUALIFICATIONS + " TEXT, " +
                COLUMN_BABYSITTER_EXPERIENCE + " INTEGER, " +
                COLUMN_BABYSITTER_RATE + " REAL, " +
                COLUMN_BABYSITTER_AVAILABILITY + " TEXT, " +
                COLUMN_BABYSITTER_AVERAGE_RATING + " REAL)";
        db.execSQL(CREATE_BABYSITTERS_TABLE);

        // Create reviews table
        String CREATE_REVIEWS_TABLE = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BABYSITTER_REVIEW_ID + " INTEGER, " +
                COLUMN_REVIEWER_NAME + " TEXT, " +
                COLUMN_REVIEW_TIME + " TEXT, " +
                COLUMN_RATING + " INTEGER, " +
                COLUMN_REVIEW_TEXT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_BABYSITTER_REVIEW_ID + ") REFERENCES " + TABLE_BABYSITTERS + "(" + COLUMN_BABYSITTER_ID + "))";
        db.execSQL(CREATE_REVIEWS_TABLE);

        // Create parents table
        String CREATE_PARENTS_TABLE = "CREATE TABLE " + TABLE_PARENTS + " (" +
                COLUMN_PARENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PARENT_NAME + " TEXT, " +
                COLUMN_PARENT_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PARENT_LOCATION + " TEXT, " +
                COLUMN_PARENT_CHILDREN + " INTEGER, " +
                COLUMN_PARENT_PREFERENCES + " TEXT)";
        db.execSQL(CREATE_PARENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BABYSITTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENTS);
        onCreate(db);
    }

    // Get all babysitters
    public List<Babysitter> getAllBabysitters() {
        List<Babysitter> babysitters = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BABYSITTERS,
                null, // Select all columns
                null, null, null, null,
                COLUMN_BABYSITTER_NAME + " ASC"); // Sort by name

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_LOCATION));
                int experience = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_EXPERIENCE));
                double hourlyRate = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_RATE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_AVAILABILITY));
                double averageRating = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_AVERAGE_RATING));

                babysitters.add(new Babysitter(id, name, location, experience, hourlyRate, availability, averageRating));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return babysitters;
    }

    public List<Review> getReviewsByBabysitterId(int babysitterId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the reviews table for the given babysitterId
        Cursor cursor = db.query(TABLE_REVIEWS,
                null, // Select all columns
                COLUMN_BABYSITTER_REVIEW_ID + " = ?", // Where babysitter_id matches
                new String[]{String.valueOf(babysitterId)}, // Bind babysitterId to query
                null, null, COLUMN_REVIEW_TIME + " DESC"); // Sort by review time (optional)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String reviewerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEWER_NAME));
                String reviewTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TIME));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING));
                String reviewText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TEXT));

                // Create a new Review object and add it to the list
                reviews.add(new Review(reviewerName, reviewTime, rating, reviewText));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return reviews;
    }


    // Get babysitter by email
    public Cursor getBabysitterByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BABYSITTERS + " WHERE email = ?", new String[]{email});
    }

    // Update babysitter profile
    public boolean updateBabysitterProfile(String name, String email, String qualifications, int experience, double hourlyRate, String location, String availability) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BABYSITTERS +
                        " SET " + COLUMN_BABYSITTER_NAME + " = ?, " +
                        COLUMN_BABYSITTER_QUALIFICATIONS + " = ?, " +
                        COLUMN_BABYSITTER_EXPERIENCE + " = ?, " +
                        COLUMN_BABYSITTER_RATE + " = ?, " +
                        COLUMN_BABYSITTER_LOCATION + " = ?, " +
                        COLUMN_BABYSITTER_AVAILABILITY + " = ? WHERE " + COLUMN_BABYSITTER_EMAIL + " = ?",  // Use the email column
                new Object[]{name, qualifications, experience, hourlyRate, location, availability, email});
        return true;
    }


    // Method to get parent by email
    public Cursor getParentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PARENTS + " WHERE " + COLUMN_PARENT_EMAIL + " = ?", new String[]{email});
    }

    // Method to update parent profile
    public boolean updateParentProfile(String name, String email, String location, int childrenNum, String preferences) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_PARENTS +
                        " SET " + COLUMN_PARENT_NAME + " = ?, " +
                        COLUMN_PARENT_LOCATION + " = ?, " +
                        COLUMN_PARENT_CHILDREN + " = ?, " +
                        COLUMN_PARENT_PREFERENCES + " = ? WHERE " + COLUMN_PARENT_EMAIL + " = ?",
                new Object[]{name, location, childrenNum, preferences, email});
        return true;
    }
}
