package com.example.homebabysit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

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
            + COLUMN_BABYSITTER_AVAILABILITY + " TEXT, "// 0: unavailable 1: available
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

    // SQL to Create Rating & Review Table
    private static final String CREATE_TABLE_RATING_REVIEWS = "CREATE TABLE " + TABLE_RATING_REVIEWS + "("
            + COLUMN_RATING_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_RATING + " INTEGER, "  // should be 0 ~ 10
            + COLUMN_REVIEW + " TEXT, "
            + COLUMN_REVIEWER_ID + " INTEGER, "
            + COLUMN_REVIEWEE_ID + " INTEGER, "
            + COLUMN_REVIEW_TIME + " TIME, "
            + COLUMN_STATUS + " INTEGER DEFAULT 0, "  // 0 stands for valid review
            + "FOREIGN KEY (" + COLUMN_REVIEWER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
            + "FOREIGN KEY (" + COLUMN_REVIEWEE_ID + ") REFERENCES " + TABLE_BABYSITTERS + "(" + COLUMN_BABYSITTER_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the tables when the database is created
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_BABYSITTERS);
        db.execSQL(CREATE_TABLE_BOOKINGS);
        db.execSQL(CREATE_TABLE_RATING_REVIEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BABYSITTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING_REVIEWS);

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

    public void insertTestData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Random random = new Random();

        if (!isParentExists("testuser@example.com")) {
            ContentValues parentValues = new ContentValues();

            parentValues.put(COLUMN_USER_NAME, "Test User");
            parentValues.put(COLUMN_USER_EMAIL, "testparent@test.com");
            parentValues.put(COLUMN_USER_PASSWORD, "123456");
            parentValues.put(COLUMN_USER_LOCATION, "Test City");
            parentValues.put(COLUMN_USER_CHILDREN, 2);
            parentValues.put(COLUMN_USER_PREFERENCES, "No Allergies");

            db.insert(TABLE_USERS, null, parentValues);
        }

        if (!isBabysitterExists("testbabysitter@example.com")) {
            ContentValues babysitterValues = new ContentValues();

            babysitterValues.put(COLUMN_BABYSITTER_NAME, "Test Babysitter");
            babysitterValues.put(COLUMN_BABYSITTER_EMAIL, "testbabysitter@test.com");
            babysitterValues.put(COLUMN_BABYSITTER_QUALIFICATIONS, "CPR, First Aid");
            babysitterValues.put(COLUMN_BABYSITTER_EXPERIENCE, 5);
            babysitterValues.put(COLUMN_BABYSITTER_RATE, 25.0);
            babysitterValues.put(COLUMN_BABYSITTER_AVAILABILITY, "1");

            db.insert(TABLE_BABYSITTERS, null, babysitterValues);
        }

        Cursor parentCursor = getParentByEmail("testparent@test.com");
        Cursor babysitterCursor = getBabysitterByEmail("testbabysitter@test.com");

        if (parentCursor.moveToFirst() && babysitterCursor.moveToFirst()) {
            int parentId = parentCursor.getInt(parentCursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            int babysitterId = babysitterCursor.getInt(babysitterCursor.getColumnIndexOrThrow(COLUMN_BABYSITTER_ID));

            ContentValues ratingReviewValues = generateRatingReviewValues(parentId, babysitterId, random);

            db.insert(TABLE_RATING_REVIEWS, null, ratingReviewValues);
        }

        parentCursor.close();
        babysitterCursor.close();
    }

    private ContentValues generateRatingReviewValues(int parentId, int babysitterId, Random random) {
        int randomRating = random.nextInt(10) + 1;
        String randomReview;

        if (randomRating >= 8) {
            randomReview = "Amazing babysitter! Would definitely hire again.";
        } else if (randomRating >= 5) {
            randomReview = "Babysitter was fine, nothing exceptional.";
        } else {
            randomReview = "Not very punctual, could have done better.";
        }

        ContentValues ratingReviewValues = new ContentValues();
        ratingReviewValues.put(COLUMN_RATING, randomRating);
        ratingReviewValues.put(COLUMN_REVIEW, randomReview);
        ratingReviewValues.put(COLUMN_REVIEWER_ID, parentId);
        ratingReviewValues.put(COLUMN_REVIEWEE_ID, babysitterId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        ratingReviewValues.put(COLUMN_REVIEW_TIME, currentTime);
        ratingReviewValues.put(COLUMN_STATUS, 0);

        return ratingReviewValues;
    }

    public boolean isBabysitterExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BABYSITTERS + " WHERE " + COLUMN_BABYSITTER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public Cursor getParentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{email});
    }

    public Cursor getBabysitterByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BABYSITTERS + " WHERE " + COLUMN_BABYSITTER_EMAIL + " = ?";
        return db.rawQuery(query, new String[]{email});
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

    public double getRatingByBabysitterId(int babysitter_id){
        double averageRating = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT AVG(" + COLUMN_RATING + ") AS average_rating FROM " + TABLE_RATING_REVIEWS +
                " WHERE " + COLUMN_REVIEWEE_ID + " = ? AND " + COLUMN_STATUS + " = 0";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(babysitter_id)});

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow("average_rating");
            averageRating = cursor.getDouble(columnIndex);

            averageRating = Math.round(averageRating * 100.0) / 100.0;
        }

        cursor.close();
        return averageRating;
    }

    public int getReviewsNumByBabysitterId(int babysitter_id){
        int reviewsCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) AS reviews_count FROM " + TABLE_RATING_REVIEWS +
                " WHERE " + COLUMN_REVIEWEE_ID + " = ? AND " + COLUMN_STATUS + " = 0";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(babysitter_id)});

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow("reviews_count");
            reviewsCount = cursor.getInt(columnIndex);
        }

        cursor.close();
        return reviewsCount;
    }

    public List<Review> getReviewsByBabysitterId(int babysitterId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to get reviews for a specific babysitter, including reviewer name and review time
        String query = "SELECT u." + COLUMN_USER_NAME + ", " +
                COLUMN_REVIEW_TIME + ", " +
                COLUMN_RATING + ", " +
                COLUMN_REVIEW + " FROM " +
                TABLE_RATING_REVIEWS + " rr " +
                "JOIN " + TABLE_USERS + " u ON rr." + COLUMN_REVIEWER_ID + " = u." + COLUMN_USER_ID +
                " WHERE rr." + COLUMN_REVIEWEE_ID + " = ? AND rr." + COLUMN_STATUS + " = 0";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(babysitterId)});

        // Loop through the results and add reviews to the list
        if (cursor.moveToFirst()) {
            do {
                String reviewerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String reviewTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TIME));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RATING));
                String reviewText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW));

                Review review = new Review(reviewerName, reviewTime, rating, reviewText);
                reviews.add(review);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reviews;
    }

}