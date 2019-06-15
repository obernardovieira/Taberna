package bernardo.vieira.taberna;

import android.graphics.Bitmap;

import java.util.Objects;

public class Item {
    private String name;
    private Bitmap bmp;
    private float price;

    Item(String name, Bitmap bmp, float price) {
        this.name = name;
        this.bmp = bmp;
        this.price = price;
    }

    String getName() {
        return name;
    }

    Bitmap getBmp() {
        return bmp;
    }

    float getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Float.compare(item.price, price) == 0 &&
                name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
