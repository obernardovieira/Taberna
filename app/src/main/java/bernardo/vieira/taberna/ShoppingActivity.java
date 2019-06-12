package bernardo.vieira.taberna;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class ShoppingActivity extends Activity {

    private GridLayout mainGrid;
    private LinearLayout layoutBuyList;
    private Shopping shopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        shopping = new Shopping();
        // get id's
        mainGrid = findViewById(R.id.mainGrid);
        layoutBuyList = findViewById(R.id.layout_listaCompra);

        // load content
        loadTabContent(0);

        // add listener
        TabLayout tabLayout = findViewById(R.id.item_type_tab);
        tabLayout.addOnTabSelectedListener(tabSelectListener);
    }

    /**
     * Setup listener to finish shopping
     * @param view android view!
     */
    public void finishShopping(View view) {
        // start creating dialog
        Dialog dialog = new Dialog(ShoppingActivity.this);
        dialog.setContentView(R.layout.finish_shopping);
        // get id's
        LinearLayout layoutPaymentMoney = dialog.findViewById(R.id.layout_payment_money);
        LinearLayout layoutPaymentTax = dialog.findViewById(R.id.layout_payment_tax);
        // program radio group actions
        RadioGroup rgPaymentMethod = dialog.findViewById(R.id.payment_method);
        rgPaymentMethod.setOnCheckedChangeListener((RadioGroup radioGroup, int i) -> {
            if (R.id.radio_payment_card == i) {
                layoutPaymentMoney.setVisibility(View.GONE);
            } else {
                layoutPaymentMoney.setVisibility(View.VISIBLE);
            }
        });
        // program checkbox actions
        CheckBox cbWithTaxNumber = dialog.findViewById(R.id.cb_payment_tax);
        cbWithTaxNumber.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            layoutPaymentTax.setVisibility((b) ? View.VISIBLE : View.GONE);
        });
        // set price
        ((TextView) dialog.findViewById(R.id.tv_total_price_shopping)).setText("Total " + shopping.getTotalPrice() + "€");
        // show dialog
        dialog.show();
    }

    /**
     * Setup listener or tab change
     */
    private TabLayout.OnTabSelectedListener tabSelectListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            loadTabContent(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    /**
     * Load content when changing tab
     * @param tab tab id
     */
    private void loadTabContent(int tab) {
        Item[] content;
        if (tab == 0) {
            content = randomDataDrinks();
        } else {
            content = randomDataFood();
        }
        // remove all existing content
        mainGrid.removeAllViews();
        // iterate over each new one and add
        for (Item item: content)
        {
            // create card view
            LayoutInflater mainInflater = LayoutInflater.from(ShoppingActivity.this);
            CardView mainLayout = (CardView) mainInflater.inflate(R.layout.item_shoping_grid, null, false);
            // set text on card view
            ((TextView) mainLayout.findViewById(R.id.cardText)).setText(item.getName());
            // setup a click listener or the card view
            mainLayout.setOnClickListener(viewLayout -> {
                // when clicked, add a new item in buy list by...
                // creating a linear layout
                LayoutInflater inflater = LayoutInflater.from(ShoppingActivity.this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_buy_list, null, false);
                // setting the item text on the list
                ((TextView)layout.findViewById(R.id.item_price)).setText(item.getPrice() + "€");
                ((TextView)layout.findViewById(R.id.item_name)).setText(item.getName());
                // setup a click listener
                layout.setOnClickListener(viewItem -> {
                    // if clicked, remove from view
                    shopping.removeItem(item);
                    layoutBuyList.removeView(viewItem);
                });
                // add to view
                shopping.addItem(item);
                layoutBuyList.addView(layout);
                // Toast.makeText(ShoppingActivity.this, "Clicked " + s, Toast.LENGTH_SHORT).show();
            });
            // add to view
            mainGrid.addView(mainLayout);
        }
    }

    /**
     * Get some random data
     * @return an array of random data
     */
    private Item[] randomDataDrinks() {
        return new Item[] {
                new Item("Cerveja", 5.3f),
                new Item("Agua", 2.1f),
        };
    }

    /**
     * Get some random data
     * @return an array of random data
     */
    private Item[] randomDataFood() {
        return new Item[] {
                new Item("Bifana", 7.2f),
                new Item("Fatia Bolo", 3.4f),
        };
    }
}
