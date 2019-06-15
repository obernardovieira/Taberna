package bernardo.vieira.taberna;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static bernardo.vieira.taberna.ShoppingListContract.*;


public class ShoppingActivity extends Activity {

    private GridLayout mainGrid;
    private LinearLayout layoutBuyList;
    private Shopping shopping;
    private Dialog dialogFinishShopping;
    private ArrayList<Item> food;
    private ArrayList<Item> drinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        shopping = new Shopping();
        // get id's
        mainGrid = findViewById(R.id.mainGrid);
        layoutBuyList = findViewById(R.id.layout_listaCompra);

        // load content
        new Thread(() -> {
            food = randomDataFood();
            drinks = randomDataDrinks();
            runOnUiThread(()-> loadTabContent(0));
        }).start();

        // add listener
        TabLayout tabLayout = findViewById(R.id.item_type_tab);
        tabLayout.addOnTabSelectedListener(tabSelectListener);
    }

    /**
     * Setup listener to finish shopping and register to DB!
     * @param view android view!
     */
    public void finishShopping(View view) {
        // get id's
        RadioGroup groupPaymentMethod = dialogFinishShopping.findViewById(R.id.payment_method);
        RadioButton paymentMoney = dialogFinishShopping.findViewById(R.id.radio_payment_money);
        // if paying with money, get the difference
        if (groupPaymentMethod.getCheckedRadioButtonId() == paymentMoney.getId()) {
            EditText etReceivedMoney = dialogFinishShopping.findViewById(R.id.et_received_money_amount);
            float returnAmount = Float.parseFloat(etReceivedMoney.getText().toString()) - shopping.getTotalPrice();
        }
        // get items
        // save to database
        long insetResult = new ShoppingListDbHelper(this).insert();
        if (insetResult != -1) {
            dialogFinishShopping.dismiss();
        }
    }

    /**
     * Setup listener to open finish shopping
     * @param view android view!
     */
    public void openFinishShopping(View view) {
        // start creating dialog
        dialogFinishShopping = new Dialog(ShoppingActivity.this);
        dialogFinishShopping.setContentView(R.layout.finish_shopping);
        // get id's
        LinearLayout layoutPaymentMoney = dialogFinishShopping.findViewById(R.id.layout_payment_money);
        LinearLayout layoutPaymentTax = dialogFinishShopping.findViewById(R.id.layout_payment_tax);
        // program radio group actions
        RadioGroup rgPaymentMethod = dialogFinishShopping.findViewById(R.id.payment_method);
        rgPaymentMethod.setOnCheckedChangeListener((RadioGroup radioGroup, int i) -> {
            if (R.id.radio_payment_card == i) {
                layoutPaymentMoney.setVisibility(View.GONE);
            } else {
                layoutPaymentMoney.setVisibility(View.VISIBLE);
            }
        });
        // program checkbox actions
        CheckBox cbWithTaxNumber = dialogFinishShopping.findViewById(R.id.cb_payment_tax);
        cbWithTaxNumber.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            layoutPaymentTax.setVisibility((b) ? View.VISIBLE : View.GONE);
        });
        // set price
        ((TextView) dialogFinishShopping.findViewById(R.id.tv_total_price_shopping)).setText("Total " + shopping.getTotalPrice() + "€");
        ((EditText) dialogFinishShopping.findViewById(R.id.et_received_money_amount)).addTextChangedListener(textWatcherMoneyReturnAmount);
        // show dialog
        dialogFinishShopping.show();
    }

    /**
     * Watcher to text change.
     */
    private TextWatcher textWatcherMoneyReturnAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            EditText etReceivedMoney = dialogFinishShopping.findViewById(R.id.et_received_money_amount);
            TextView tvMoneyReturn = dialogFinishShopping.findViewById(R.id.tv_return_money_amount);
            if (etReceivedMoney.getText().length() == 0) {
                tvMoneyReturn.setText("Return: 0€");
            } else {
                float returnAmount = Float.parseFloat(etReceivedMoney.getText().toString()) - shopping.getTotalPrice();
                tvMoneyReturn.setText("Return: " + returnAmount + "€");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
        ArrayList<Item> content;
        if (tab == 0) {
            content = drinks;
        } else {
            content = food;
        }
        // remove all existing content
        mainGrid.removeAllViews();
        // iterate over each new one and add
        for (Item item: content)
        {
            // create card view
            LayoutInflater mainInflater = LayoutInflater.from(ShoppingActivity.this);
            CardView mainLayout = (CardView) mainInflater.inflate(R.layout.item_shoping_grid, null, false);
            // set image on card view
            ((ImageView) mainLayout.findViewById(R.id.item_image)).setImageBitmap(item.getBmp());
            // set text on card view
            ((TextView) mainLayout.findViewById(R.id.item_name)).setText(item.getName());
            // setup a click listener or the card view
            mainLayout.setOnClickListener(viewLayout -> {
                // when clicked, add a new item in buy list by...
                // creating a linear layout
                LayoutInflater inflater = LayoutInflater.from(ShoppingActivity.this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_buy_list, null, false);
                // setting the item text on the list
                ((ImageView) layout.findViewById(R.id.item_image)).setImageBitmap(item.getBmp());
                ((TextView) layout.findViewById(R.id.item_price)).setText(item.getPrice() + "€");
                ((TextView) layout.findViewById(R.id.item_name)).setText(item.getName());
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
    private ArrayList<Item> randomDataDrinks() {
        Bitmap bmpCerveja;
        Bitmap bmpAgua;
        ArrayList<Item> items = new ArrayList<>();
        try {
            bmpCerveja = BitmapFactory.decodeStream(
                    new URL("https://http2.mlstatic.com/cerveja-budweiser-550ml-D_NQ_NP_888656-MLB28446973474_102018-Q.jpg")
                            .openConnection().getInputStream()
            );
            bmpAgua = BitmapFactory.decodeStream(
                    new URL("https://www.decathlon.pt/media/836/8367345/big_459796.jpg")
                            .openConnection().getInputStream()
            );
            items.add(new Item("Cerveja", bmpCerveja,5.3f));
            items.add(new Item("Agua", bmpAgua, 2.1f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Get some random data
     * @return an array of random data
     */
    private ArrayList<Item> randomDataFood() {
        Bitmap bmpBifana;
        Bitmap bmpFatiaBolo;
        ArrayList<Item> items = new ArrayList<>();
        try {
            bmpBifana = BitmapFactory.decodeStream(
                    new URL("https://nit.pt/wp-content/uploads/2018/04/44c80d8aecbac8e27c30fbdf2f9daf2b-754x394.jpg")
                            .openConnection().getInputStream()
            );
            bmpFatiaBolo = BitmapFactory.decodeStream(
                    new URL("https://i.ytimg.com/vi/XrTjOTYGtM8/hqdefault.jpg")
                            .openConnection().getInputStream()
            );
            items.add(new Item("Bifana", bmpBifana,7.2f));
            items.add(new Item("Fatia Bolo",bmpFatiaBolo, 3.4f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
}
