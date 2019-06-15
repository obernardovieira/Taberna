package bernardo.vieira.taberna;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ListShoppingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shoppings);

        renderShoppings();
    }

    private void renderShoppings() {
        ArrayList<ShoppingListItem> shoppingListItems = new ShoppingListDbHelper(this).getAll();
        //
    }
}
