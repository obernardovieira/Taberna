package bernardo.vieira.taberna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class ListShoppingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shoppings);

        renderShoppings();
    }

    private void renderShoppings() {
        ArrayList<ShoppingListItem> shoppingListItems = new ShoppingListDbHelper(this).getAll();
        LinearLayout layoutShoppingsList = findViewById(R.id.linear_shoppings_list);
        for (ShoppingListItem item: shoppingListItems) {
            // creating a linear layout
            LayoutInflater inflater = LayoutInflater.from(ListShoppingsActivity.this);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_shoppings_list, null, false);
            // setting the item text on the list
            ((TextView) layout.findViewById(R.id.tv_payed_with)).setText(String.valueOf(item.getPayedWith()));
            ((TextView) layout.findViewById(R.id.tv_received)).setText(String.valueOf(item.getReceived()));
            ((TextView) layout.findViewById(R.id.tv_date)).setText(
                    new Date(Long.parseLong(String.valueOf((long)item.getDate() * 1000))).toString()
            );
            // add to view
            layoutShoppingsList.addView(layout);
        }
    }
}
