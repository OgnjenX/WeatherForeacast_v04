package com.example.pnrs1_projekat;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ElementProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.pnrs1_projekat";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, ElementDbHelper.TABLE_NAME);

    public static final String CONTENT_TYPE =
            "vnd.android.cursor.dir/vnd.com.example.pnrs1_projekat.Weather";
    public static final String CONTENT_ITEM_TYPE =
            "vnd.android.cursor.item/vnd.com.example.pnrs1_projekat.Weather";

    private static final int ELEMENT = 1;
    private static final int CITY_NAME = 2;
    private static final int CITY_NAME_AND_DATE = 3;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, ElementDbHelper.TABLE_NAME, ELEMENT);
        sUriMatcher.addURI(AUTHORITY, ElementDbHelper.TABLE_NAME + "/*", CITY_NAME);
        sUriMatcher.addURI(AUTHORITY, ElementDbHelper.TABLE_NAME + "/*/*", CITY_NAME_AND_DATE);
    }

    private ElementDbHelper mHelper = null;

    public ElementProvider() {
    }

    private int delete(String selection, String[] selectionArgs) {
        int deleted = 0;

        SQLiteDatabase db = mHelper.getWritableDatabase();
        deleted = db.delete(ElementDbHelper.TABLE_NAME, selection, selectionArgs);
        mHelper.close();

        return deleted;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted = 0;

        switch (sUriMatcher.match(uri)) {
            case ELEMENT:
                deleted = delete(selection, selectionArgs);
                break;
            case CITY_NAME:
                deleted = delete(ElementDbHelper.COLUMN_CITY + "=?", new String[] {uri.getLastPathSegment()});
                break;
            default:
        }

        if (deleted > 0 ) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        String type = null;

        switch (sUriMatcher.match(uri)) {
            case ELEMENT:
                type = CONTENT_TYPE;    // We expect Cursor to have 0 to infinity items
                break;
            case CITY_NAME:
                type = CONTENT_TYPE;    // We expect Cursor to have 0 to infinity items
                break;
            case CITY_NAME_AND_DATE:
                type = CONTENT_ITEM_TYPE;   // We expect Cursor to have 1 item
                break;
            default:
        }

        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(ElementDbHelper.TABLE_NAME, null, values);
        mHelper.close();

        if (id != -1) {
            ContentResolver resolver = getContext().getContentResolver();
            if (resolver == null)
                return null;

            resolver.notifyChange(uri, null);
        }

        return Uri.withAppendedPath(uri, Long.toString(id));
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mHelper = new ElementDbHelper(getContext());
        return true;
    }

    private Cursor query(String[] projection, String selection, String[] selectionArgs,
                         String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        return db.query(ElementDbHelper.TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case ELEMENT:
                cursor = query(projection, selection, selectionArgs, sortOrder);
                break;
            case CITY_NAME:
                cursor = query(projection, "City=?",
                        new String[]{uri.getLastPathSegment()}, null);
                break;
            case CITY_NAME_AND_DATE:
                cursor = query(projection, "City=? and Date=?",
                        selectionArgs, null);
                break;
            default:
        }

        return cursor;
    }

    private int update(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updated = db.update(ElementDbHelper.TABLE_NAME, values, selection,
                selectionArgs);
        mHelper.close();

        return updated;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int updated = 0;

        switch (sUriMatcher.match(uri)) {
            case ELEMENT:
                updated = update(values, selection, selectionArgs);
                break;
            case CITY_NAME:
                updated = update(values, "City=?", new String[]{uri.getLastPathSegment()});
                break;
            case CITY_NAME_AND_DATE:
                updated = update(values, "City=? and Date=?", selectionArgs);
                break;
            default:
        }

        if (updated > 0) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return updated;
    }
}
