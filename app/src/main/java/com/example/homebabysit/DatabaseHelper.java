package com.example.homebabysit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "homebabysit.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_BABYSITTERS = "babysitters";
    public static final String TABLE_BOOKINGS = "bookings";

    // Users Table Columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
<<<<<<< HEAD
    public static final String COLUMN_USER_LOCATION = "location";  // New
    public static final String COLUMN_USER_CHILDREN = "num_children";  // New
    public static final String COLUMN_USER_PREFERENCES = "preferences";  // New
=======
>>>>>>> 6bb0bc72d039b8bfa2dc77ef3f9272249b6bb3a7

    // Babysitters Table Columns
    public static final String COLUMN_BABYSITTER_ID = "babysitter_id";
    public static final String COLUMN_BABYSITTER_NAME = "name";
<<<<<<< HEAD
    public static final String COLUMN_BABYSITTER_QUALIFICATIONS = "qualifications";  // New
    public static final String COLUMN_BABYSITTER_EXPERIENCE = "experience";
    public static final String COLUMN_BABYSITTER_RATING = "rating";
    public static final String COLUMN_BABYSITTER_AVAILABILITY = "availability";  // New
    public static final String COLUMN_BABYSITTER_RATE = "hourly_rate";  // New
    public static final String COLUMN_BABYSITTER_PHOTO = "profile_photo";  // New

    // Bookings Table Columns
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_BOOKING_DATE = "date";
    public static final String COLUMN_BOOKING_TIME_SLOT = "time_slot";  // New
    public static final String COLUMN_BOOKING_STATUS = "status";  // New
=======
    public static final String COLUMN_BABYSITTER_EXPERIENCE = "experience";
    public static final String COLUMN_BABYSITTER_RATING = "rating";
>>>>>>> 6bb0bc72d039b8bfa2dc77ef3f9272249b6bb3a7

    // SQL to Create Users Table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT, "
<<<<<<< HEAD
            + COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_LOCATION + " TEXT, "
            + COLUMN_USER_CHILDREN + " INTEGER, "
            + COLUMN_USER_PREFERENCES + " TEXT" + ")";
=======
            + COLUMN_USER_PASSWORD + " TEXT" + ")";
>>>>>>> 6bb0bc72d039b8bfa2dc77ef3f9272249b6bb3a7

    // SQL to Create Babysitters Table
    private static final String CREATE_TABLE_BABYSITTERS = "CREATE TABLE " + TABLE_BABYSITTERS + "("
            + COLUMN_BABYSITTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BABYSITTER_NAME + " TEXT, "
<<<<<<< HEAD
            + COLUMN_BABYSITTER_QUALIFICATIONS + " TEXT, "
            + COLUMN_BABYSITTER_EXPERIENCE + " INTEGER, "
            + COLUMN_BABYSITTER_RATING + " REAL, "
            + COLUMN_BABYSITTER_AVAILABILITY + " TEXT, "
            + COLUMN_BABYSITTER_RATE + " REAL, "
            + COLUMN_BABYSITTER_PHOTO + " BLOB" + ")";  // Assuming profile photo is stored as a blob

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
=======
            + COLUMN_BABYSITTER_EXPERIENCE + " INTEGER, "
            + COLUMN_BABYSITTER_RATING + " REAL" + ")";

    // SQL to Create Bookings Table
    private static final String CREATE_TABLE_BOOKINGS = "CREATE TABLE " + TABLE_BOOKINGS + "("
            + "booking_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "parent_id INTEGER, "
            + "babysitter_id INTEGER, "
            + "date TEXT, "
            + "FOREIGN KEY (parent_id) REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
            + "FOREIGN KEY (babysitter_id) REFERENCES " + TABLE_BABYSITTERS + "(" + COLUMN_BABYSITTER_ID + "))";
>>>>>>> 6bb0bc72d039b8bfa2dc77ef3f9272249b6bb3a7

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the tables when the database is created
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BABYSITTERS);
        db.execSQL(CREATE_TABLE_BOOKINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BABYSITTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);

        // Create new tables
        onCreate(db);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 6bb0bc72d039b8bfa2dc77ef3f9272249b6bb3a7
