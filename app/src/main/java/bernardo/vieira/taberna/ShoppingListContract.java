package bernardo.vieira.taberna;

import android.provider.BaseColumns;

class ShoppingListContract {
    private ShoppingListContract() {
        //
    }

    /* Inner class that defines the table contents */
    public static class ShoppingListEntry implements BaseColumns {
        static final String TABLE_NAME = "shopping";
        static final String COLUMN_NAME_PAYED_WITH = "payed_with";
        static final String COLUMN_NAME_RECEIVED = "received";
        static final String COLUMN_NAME_RETURN = "return";
        static final String COLUMN_NAME_TAX_NUMBER= "tax_number";
        static final String COLUMN_NAME_DATE= "date";
    }

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ShoppingListEntry.TABLE_NAME + " (" +
                    ShoppingListEntry._ID + " INTEGER PRIMARY KEY," +
                    ShoppingListEntry.COLUMN_NAME_PAYED_WITH + " INTEGER," +
                    ShoppingListEntry.COLUMN_NAME_RECEIVED + " REAL," +
                    ShoppingListEntry.COLUMN_NAME_RETURN + " REAL," +
                    ShoppingListEntry.COLUMN_NAME_TAX_NUMBER + " INTEGER," +
                    ShoppingListEntry.COLUMN_NAME_DATE + " INTEGER)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ShoppingListEntry.TABLE_NAME;
}
