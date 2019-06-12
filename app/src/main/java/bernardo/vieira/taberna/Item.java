package bernardo.vieira.taberna;

import java.util.Objects;

public class Item {
    private String name;
    private String url;
    private float price;

    Item(String name, String url, float price) {
        this.name = name;
        this.url = url;
        this.price = price;
    }

    String getName() {
        return name;
    }

    String getUrl() {
        return url;
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
