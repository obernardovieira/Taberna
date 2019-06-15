package bernardo.vieira.taberna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.sql.Timestamp;
import java.util.ArrayList;

import static bernardo.vieira.taberna.ShoppingListContract.SQL_CREATE_ENTRIES;
import static bernardo.vieira.taberna.ShoppingListContract.SQL_DELETE_ENTRIES;
import static bernardo.vieira.taberna.ShoppingListContract.ShoppingListEntry.*;

public class ShoppingListDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "ShoppingList.db";

    ShoppingListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    long insert(int payedWith, float received, float returned, int taxNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PAYED_WITH, payedWith);
        values.put(COLUMN_NAME_RECEIVED, received);
        values.put(COLUMN_NAME_RETURN, returned);
        values.put(COLUMN_NAME_TAX_NUMBER, taxNumber);
        values.put(COLUMN_NAME_DATE, new Timestamp(System.currentTimeMillis()).getTime() / 1000);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    ArrayList<ShoppingListItem> getAll() {
        ArrayList<ShoppingListItem> shoppingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                COLUMN_NAME_PAYED_WITH,
                COLUMN_NAME_RECEIVED,
                COLUMN_NAME_RETURN,
                COLUMN_NAME_TAX_NUMBER,
                COLUMN_NAME_DATE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            shoppingList.add(
                    new ShoppingListItem(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_PAYED_WITH)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_RECEIVED)),
                            cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_NAME_RETURN)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_TAX_NUMBER)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE))
                    )
            );
        }
        cursor.close();
        db.close();
        return shoppingList;
    }
}
