package com.example.homebabysit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "homebabysit.db";
    private static final int DATABASE_VERSION = 4;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_BABYSITTERS = "babysitters";
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String TABLE_RATING_REVIEWS = "rating_reviews";

    // Users Table Columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_LOCATION = "location";  // New
    public static final String COLUMN_USER_CHILDREN = "num_children";  // New
    public static final String COLUMN_USER_PREFERENCES = "preferences";  // New

    // Babysitters Table Columns
    public static final String COLUMN_BABYSITTER_ID = "babysitter_id";
    public static final String COLUMN_BABYSITTER_NAME = "name";
    public static final String COLUMN_BABYSITTER_EMAIL = "email";
    public static final String COLUMN_BABYSITTER_QUALIFICATIONS = "qualifications";  // New
    public static final String COLUMN_BABYSITTER_EXPERIENCE = "experience";
    public static final String COLUMN_BABYSITTER_AVAILABILITY = "availability";  // New
    public static final String COLUMN_BABYSITTER_RATE = "hourly_rate";  // New
    public static final String COLUMN_BABYSITTER_PHOTO = "profile_photo";  // New
    public static final String COLUMN_BABYSITTER_LOCATION = "location"; // New

    // Bookings Table Columns
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_BOOKING_DATE = "date";
    public static final String COLUMN_BOOKING_TIME_SLOT = "time_slot";  // New
    public static final String COLUMN_BOOKING_STATUS = "status";  // New

    // Rating_reviews Table Columns
    public static final String COLUMN_RATING_REVIEW_ID = "rating_review_id";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_REVIEW = "review";
    public static final String COLUMN_REVIEWER_ID = "reviewer_id";
    public static final String COLUMN_REVIEWEE_ID = "reviewee_id";
    public static final String COLUMN_REVIEW_TIME = "time";
    public static final String COLUMN_STATUS = "status";

    // SQL to Create Users Table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_LOCATION + " TEXT, "
            + COLUMN_USER_CHILDREN + " INTEGER, "
            + COLUMN_USER_PREFERENCES + " TEXT" + ")";

    // SQL to Create Babysitters Table
    private static final String CREATE_TABLE_BABYSITTERS = "CREATE TABLE " + TABLE_BABYSITTERS + "("
            + COLUMN_BABYSITTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BABYSITTER_NAME + " TEXT, "
            + COLUMN_BABYSITTER_EMAIL + " TEXT, "
            + COLUMN_BABYSITTER_QUALIFICATIONS + " TEXT, "
            + COLUMN_BABYSITTER_EXPERIENCE + " INTEGER, "
            + COLUMN_BABYSITTER_AVAILABILITY + " TEXT, "
            + COLUMN_BABYSITTER_RATE + " REAL, "
            + COLUMN_BABYSITTER_LOCATION + " TEXT, "
            + COLUMN_BABYSITTER_PHOTO + " BLOB" + ")";

    // SQL to Create Bookings Table
    private static final String CREATE_TABLE_BOOKINGS = "CREATE TABLE " + TABLE_BOOKINGS + "("
            + COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_ID + " INTEGER, "
            + COLUMN_BABYSITTER_ID + " INTEGER, "
            + COLUMN_BOOKING_DATE + " TEXT, "
            + COLUMN_BOOKING_TIME_SLOT + " TEXT, "
            + COLUMN_BOOKING_STATUS + " TEXT, "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
            + "FOREIGN KEY (" + COLUMN_BABYSITTER_ID + ") REFERENCES " + TABLE_BABYSITTERS + "(" + COLUMN_BABYSITTER_ID + "))";

    // SQL to Create Rating & Review Table
    private static final String CREATE_TABLE_RATING_REVIEWS = "CREATE TABLE " + TABLE_RATING_REVIEWS + "("
            + COLUMN_RATING_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RATING + " INTEGER, "
            + COLUMN_REVIEW + " TEXT, "
            + COLUMN_REVIEWER_ID + " INTEGER, "
            + COLUMN_REVIEWEE_ID + " INTEGER, "
            + COLUMN_REVIEW_TIME + " TIME, "
            + COLUMN_STATUS + " INTEGER DEFAULT 0, "
            + "FOREIGN KEY (" + COLUMN_REVIEWER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
            + "FOREIGN KEY (" + COLUMN_REVIEWEE_ID + ") REFERENCES " + TABLE_BABYSITTERS + "(" + COLUMN_BABYSITTER_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BABYSITTERS);
        db.execSQL(CREATE_TABLE_BOOKINGS);
        db.execSQL(CREATE_TABLE_RATING_REVIEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BABYSITTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING_REVIEWS);
        onCreate(db);
    }

    // New Method for Filtering Babysitters
    public List<Babysitter> getFilteredBabysitters(String searchText, int minRating, int maxRating,
                                                   int experienceFilter, List<String> selectedDays) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Babysitter> babysitters = new ArrayList<>();

        String query = "SELECT b.*, " +
                "(SELECT AVG(" + COLUMN_RATING + ") FROM " + TABLE_RATING_REVIEWS +
                " WHERE " + COLUMN_REVIEWEE_ID + " = b." + COLUMN_BABYSITTER_ID + " AND " +
                COLUMN_STATUS + " = 0) AS average_rating " +
                "FROM " + TABLE_BABYSITTERS + " b";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_LOCATION));
                int experience = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_EXPERIENCE));
                double rate = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_RATE));
                String availability = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_AVAILABILITY));
                double averageRating = cursor.getDouble(cursor.getColumnIndexOrThrow("average_rating"));

                boolean matchesSearch = name.toLowerCase().contains(searchText.toLowerCase());
                boolean matchesExperience = (experienceFilter == 0) ||
                        (experienceFilter == 1 && experience < 2) ||
                        (experienceFilter == 2 && experience >= 2 && experience < 5) ||
                        (experienceFilter == 3 && experience >= 5);
                boolean matchesRating = (averageRating >= minRating && averageRating <= maxRating);
                boolean matchesAvailability = matchesSelectedDays(availability, selectedDays);

                if (matchesSearch && matchesExperience && matchesRating && matchesAvailability) {
                    Babysitter babysitter = new Babysitter(id, name, location, experience, rate, availability, averageRating);
                    babysitters.add(babysitter);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return babysitters;
    }

    private boolean matchesSelectedDays(String babysitterAvailability, List<String> selectedDays) {
        for (String day : selectedDays) {
            if (babysitterAvailability != null && babysitterAvailability.toLowerCase().contains(day.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    public double getRatingByBabysitterId(int babysitterId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double averageRating = 0.0;
        int ratingCount = 0;

        // Query to calculate the average rating for the babysitter
        String query = "SELECT AVG(" + COLUMN_RATING + ") AS AverageRating " +
                "FROM " + TABLE_RATING_REVIEWS +
                " WHERE " + COLUMN_BABYSITTER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(babysitterId)});

        if (cursor.moveToFirst()) {
            averageRating = cursor.getDouble(cursor.getColumnIndexOrThrow("AverageRating"));
        }
        cursor.close();
        return averageRating;
    }

}
