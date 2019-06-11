package bernardo.vieira.taberna;

import java.util.ArrayList;

class Shopping {

    private ArrayList<String> items = new ArrayList<>();

    void addItem(String name) {
        items.add(name);
    }

    void removeItem(String name) {
        items.remove(name);
    }

    ArrayList<String> getItems() {
        return items;
    }
}
