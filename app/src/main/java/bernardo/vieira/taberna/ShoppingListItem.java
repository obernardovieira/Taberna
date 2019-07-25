package bernardo.vieira.taberna;

public class ShoppingListItem {
    private int payedWith;
    private float received;
    private float returned;
    private int taxNumber;
    private int date;

    public ShoppingListItem(int payedWith, float received, float returned, int taxNumber, int date) {
        this.payedWith = payedWith;
        this.received = received;
        this.returned = returned;
        this.taxNumber = taxNumber;
        this.date = date;
    }

    public int getPayedWith() {
        return payedWith;
    }

    public float getReceived() {
        return received;
    }

    public float getReturned() {
        return returned;
    }

    public int getTaxNumber() {
        return taxNumber;
    }

    public int getDate() {
        return date;
    }
}
