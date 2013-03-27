package com.jahama.proyectotwitter;

import java.util.HashMap;



import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class TweetsProvider extends ContentProvider {
	public TweetsProvider() {
	}

	public static final Uri CONTENT_URI = 
		      Uri.parse("content://com.jahama.proyectotwitter/tweets");
		  
		  //Column Names
		  public static final String KEY_ID = "_id";
		  public static final String KEY_DATE = "Fecha";
		  public static final String KEY_USER = "Usuario";
		  public static final String KEY_USER_NAME = "NombreUsuario";
		  public static final String KEY_TEXT = "Texto";
		 
		  
		  TweetsDatabaseHelper dbHelper;

		  @Override
		  public boolean onCreate() {
		    Context context = getContext();

		    dbHelper = new TweetsDatabaseHelper(context,
		    		TweetsDatabaseHelper.DATABASE_NAME, null,
		    		TweetsDatabaseHelper.DATABASE_VERSION);

		    return true;
		  }
		  
		  private static final HashMap<String, String> SEARCH_PROJECTION_MAP;
		  static {
		    SEARCH_PROJECTION_MAP = new HashMap<String, String>();
		    SEARCH_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, KEY_TEXT +
		      " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
		    SEARCH_PROJECTION_MAP.put("_id", KEY_ID + " AS " + "_id");
		  }

		  //Create the constants used to differentiate between the different URI
		  //requests.
		  private static final int TWEET = 1;
		  private static final int TWEET_ID = 2;
		  private static final int SEARCH = 3;

		  private static final UriMatcher uriMatcher;

		  //Allocate the UriMatcher object, where a URI ending in 'earthquakes' will
		  //correspond to a request for all earthquakes, and 'earthquakes' with a
		  //trailing '/[rowID]' will represent a single earthquake row.
		  static {
		   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		   uriMatcher.addURI("com.jahama.proyectotwitter", "earthquakes", TWEET);
		   uriMatcher.addURI("com.jahama.proyectotwitter", "earthquakes/#", TWEET_ID);
		   uriMatcher.addURI("com.jahama.proyectotwitter",
		     SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH);
		   uriMatcher.addURI("com.jahama.proyectotwitter",
		     SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH);
		   uriMatcher.addURI("com.jahama.proyectotwitter",
		     SearchManager.SUGGEST_URI_PATH_SHORTCUT, SEARCH);
		   uriMatcher.addURI("com.jahama.proyectotwitter",
		     SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SEARCH);
		  }
		  
		  @Override
		  public String getType(Uri uri) {
		    switch (uriMatcher.match(uri)) {
		      case TWEET  : return "vnd.android.cursor.dir/vnd.paad.earthquake";
		      case TWEET_ID: return "vnd.android.cursor.item/vnd.paad.earthquake";
		      case SEARCH  : return SearchManager.SUGGEST_MIME_TYPE;
		      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		    }
		  }


		  @Override
		  public Cursor query(Uri uri,
		                      String[] projection,
		                      String selection,
		                      String[] selectionArgs,
		                      String sort) {

		    SQLiteDatabase database = dbHelper.getWritableDatabase();

		    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		    qb.setTables(TweetsDatabaseHelper.TWEETS_TABLE);

		    // If this is a row query, limit the result set to the passed in row.
		    switch (uriMatcher.match(uri)) {
		      case TWEET_ID: qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
		                     break;
		      case SEARCH  : qb.appendWhere(KEY_TEXT + " LIKE \"%" +
		                       uri.getPathSegments().get(1) + "%\"");
		                       qb.setProjectionMap(SEARCH_PROJECTION_MAP);
		                     break;
		      default      : break;
		    }

		    // If no sort order is specified, sort by date / time
		    String orderBy;
		    if (TextUtils.isEmpty(sort)) {
		      orderBy = KEY_DATE;
		    } else {
		      orderBy = sort;
		    }

		    // Apply the query to the underlying database.
		    Cursor c = qb.query(database,
		                        projection,
		                        selection, selectionArgs,
		                        null, null,
		                        orderBy);

		    // Register the contexts ContentResolver to be notified if
		    // the cursor result set changes.
		    c.setNotificationUri(getContext().getContentResolver(), uri);

		    // Return a cursor to the query result.
		    return c;
		  }

		  @Override
		  public Uri insert(Uri _uri, ContentValues _initialValues) {
		    SQLiteDatabase database = dbHelper.getWritableDatabase();
		    
		    // Insert the new row. The call to database.insert will return the row number
		    // if it is successful.
		    long rowID = database.insert(
		    		TweetsDatabaseHelper.TWEETS_TABLE, "tweet", _initialValues);

		    // Return a URI to the newly inserted row on success.
		    if (rowID > 0) {
		      Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
		      getContext().getContentResolver().notifyChange(uri, null);
		      return uri;
		    }
		    
		    throw new SQLException("Failed to insert row into " + _uri);
		  }

		  @Override
		  public int delete(Uri uri, String where, String[] whereArgs) {
		    SQLiteDatabase database = dbHelper.getWritableDatabase();
		    
		    int count;
		    switch (uriMatcher.match(uri)) {
		      case TWEET:
		        count = database.delete(
		        		TweetsDatabaseHelper.TWEETS_TABLE, where, whereArgs);
		        break;
		      case TWEET_ID:
		        String segment = uri.getPathSegments().get(1);
		        count = database.delete(TweetsDatabaseHelper.TWEETS_TABLE, 
		                KEY_ID + "=" 
		                + segment
		                + (!TextUtils.isEmpty(where) ? " AND ("
		                + where + ')' : ""), whereArgs);
		        break;

		      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		    }

		    getContext().getContentResolver().notifyChange(uri, null);
		    return count;
		  }

		  @Override
		  public int update(Uri uri, ContentValues values, 
		             String where, String[] whereArgs) {
		    SQLiteDatabase database = dbHelper.getWritableDatabase();
		    
		    int count;
		    switch (uriMatcher.match(uri)) {
		      case TWEET: 
		        count = database.update(TweetsDatabaseHelper.TWEETS_TABLE, 
		                                values, where, whereArgs);
		        break;
		      case TWEET_ID: 
		        String segment = uri.getPathSegments().get(1);
		        count = database.update(TweetsDatabaseHelper.TWEETS_TABLE, 
		                                values, KEY_ID
		                                  + "=" + segment
		                                  + (!TextUtils.isEmpty(where) ? " AND ("
		                                  + where + ')' : ""), whereArgs);
		        break;
		      default: throw new IllegalArgumentException("Unknown URI " + uri);
		    }

		    getContext().getContentResolver().notifyChange(uri, null);
		    return count;
		  }
		  
		  //Helper class for opening, creating, and managing database version control
		  private static class TweetsDatabaseHelper extends SQLiteOpenHelper {
		  
		    private static final String TAG = "TweetsProvider";
		  
		    private static final String DATABASE_NAME = "tweets.db";
		    private static final int DATABASE_VERSION = 1;
		    private static final String TWEETS_TABLE = "tweets";
		  
		    private static final String DATABASE_CREATE =
		      "create table " + TWEETS_TABLE + " ("
		      + KEY_ID + " integer primary key autoincrement, "
		      + KEY_DATE + " INTEGER, "
		      + KEY_TEXT + " TEXT, "
		      + KEY_USER + " TEXT);";
		  
		    // The underlying database
		    private SQLiteDatabase earthquakeDB;
		  
		    public TweetsDatabaseHelper(Context context, String name,
		                                    CursorFactory factory, int version) {
		      super(context, name, factory, version);
		    }
		  
		    @Override
		    public void onCreate(SQLiteDatabase db) {
		      db.execSQL(DATABASE_CREATE);
		    }
		  
		    @Override
		    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		      Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
		                 + newVersion + ", which will destroy all old data");
		   
		      db.execSQL("DROP TABLE IF EXISTS " + TWEETS_TABLE);
		      onCreate(db);
		    }
		  }
}
