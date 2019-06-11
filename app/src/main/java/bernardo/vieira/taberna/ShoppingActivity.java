package bernardo.vieira.taberna;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ShoppingActivity extends Activity {

    private GridLayout mainGrid;
    private LinearLayout layoutBuyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        mainGrid = findViewById(R.id.mainGrid);
        layoutBuyList = findViewById(R.id.layout_listaCompra);

        loadTabContent(0);


        TabLayout tabLayout = findViewById(R.id.item_type_tab);
        tabLayout.addOnTabSelectedListener(tabSelectListener);
    }

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

    private void loadTabContent(int tab) {
        String[] content;
        if (tab == 0) {
            content = randomDataDrinks();
        } else {
            content = randomDataFood();
        }
        mainGrid.removeAllViews();
        for (String s: content)
        {
            LayoutInflater mainInflater = LayoutInflater.from(this);
            CardView mainLayout = (CardView) mainInflater.inflate(R.layout.item_shoping_grid, null, false);
            ((TextView) mainLayout .findViewById(R.id.cardText)).setText(s);
            mainLayout.setOnClickListener(view -> {
                LayoutInflater inflater = LayoutInflater.from(ShoppingActivity.this);
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_buy_list, null, false);
                ((TextView)layout.findViewById(R.id.item_name)).setText(s);
                layoutBuyList.addView(layout);
                Toast.makeText(ShoppingActivity.this, "Clicked " + s, Toast.LENGTH_SHORT).show();
            });
            mainGrid.addView(mainLayout);
        }
    }

    private String[] randomDataDrinks() {
        return new String[] {"Cerveja", "Agua"};
    }

    private String[] randomDataFood() {
        return new String[] {"Bifana", "Fatia Bolo"};
    }
}
