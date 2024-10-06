package com.example.homebabysit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "homebabysit.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_BABYSITTERS = "babysitters";
    public static final String TABLE_BOOKINGS = "bookings";

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
    public static final String COLUMN_BABYSITTER_RATING = "rating";
    public static final String COLUMN_BABYSITTER_AVAILABILITY = "availability";  // New
    public static final String COLUMN_BABYSITTER_RATE = "hourly_rate";  // New
    public static final String COLUMN_BABYSITTER_PHOTO = "profile_photo";  // New

    // Bookings Table Columns
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_BOOKING_DATE = "date";
    public static final String COLUMN_BOOKING_TIME_SLOT = "time_slot";  // New
    public static final String COLUMN_BOOKING_STATUS = "status";  // New

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

    public boolean isParentExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void insertTestParentData() {
        if (!isParentExists("testuser@example.com")) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_NAME, "Test User");
            values.put(COLUMN_USER_EMAIL, "testparent@test.com");
            values.put(COLUMN_USER_PASSWORD, "123456");
            values.put(COLUMN_USER_LOCATION, "Test City");
            values.put(COLUMN_USER_CHILDREN, 2);
            values.put(COLUMN_USER_PREFERENCES, "No Allergies");

            db.insert(TABLE_USERS, null, values);
        }
    }

    public boolean isBabysitterExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BABYSITTERS + " WHERE " + COLUMN_BABYSITTER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void insertTestBabysitterData() {
        if (!isBabysitterExists("testbabysitter@example.com")) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_BABYSITTER_NAME, "Test Babysitter");
            values.put(COLUMN_BABYSITTER_EMAIL, "testbabysitter@test.com");
            values.put(COLUMN_BABYSITTER_QUALIFICATIONS, "CPR, First Aid");
            values.put(COLUMN_BABYSITTER_EXPERIENCE, 5);
            values.put(COLUMN_BABYSITTER_RATE, 25.0);
            values.put(COLUMN_BABYSITTER_AVAILABILITY, "Weekdays 9am-5pm");

            db.insert(TABLE_BABYSITTERS, null, values);
        }
    }

    public Cursor getParentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        return cursor;
    }

    public Cursor getBabysitterByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BABYSITTERS + " WHERE " + COLUMN_BABYSITTER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        return cursor;
    }

    public Boolean updateParentProfile(String name, String email, String location, int childrenNum, String preferences) {
        if (isParentExists(email)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_NAME, name);
            values.put(COLUMN_USER_LOCATION, location);
            values.put(COLUMN_USER_CHILDREN, childrenNum);
            values.put(COLUMN_USER_PREFERENCES, preferences);

            db.update(TABLE_USERS, values, COLUMN_USER_EMAIL + " = ?", new String[]{email});
            return true;
        }
        return false;
    }

    public Boolean updateBabysitterProfile(String name, String email, String qualifications, int experience, double hourly_rates, String availability) {
        if (isBabysitterExists(email)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_BABYSITTER_NAME, name);
            values.put(COLUMN_BABYSITTER_QUALIFICATIONS, qualifications);
            values.put(COLUMN_BABYSITTER_EXPERIENCE, experience);
            values.put(COLUMN_BABYSITTER_RATE, hourly_rates);
            values.put(COLUMN_BABYSITTER_AVAILABILITY, availability);

            db.update(TABLE_BABYSITTERS, values, COLUMN_BABYSITTER_EMAIL + " = ?", new String[]{email});
            return true;
        }
        return false;
    }
}