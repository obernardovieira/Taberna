package bernardo.vieira.taberna;

import java.util.ArrayList;

class Shopping {

    private ArrayList<Item> items = new ArrayList<>();

    void addItem(Item item) {
        items.add(item);
    }

    void removeItem(Item item) {
        items.remove(item);
    }

    float getTotalPrice() {
        float total = .0f;
        for(Item item: items) {
            total += item.getPrice();
        }
        return total;
    }
}
