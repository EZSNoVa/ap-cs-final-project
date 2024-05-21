
public class Card implements CardDisplay {
    private int value;
    private String suit;
    private String name;

    public Card(int value, String suit) {
        this.value = value;
        this.suit = suit;

        name = Integer.toString(value);
        if (value == 1 || value == 14) {
            name = "Ace";
        } else if (value == 11) {
            name = "Jack";
        } else if (value == 12) {
            name = "Queen";
        } else if (value == 13) {
            name = "King";
        }
    }

    @Override
    public String toString() {
        return name + " of " + suit;
    }

    public void print_display() {
        String[][] display = display(this);

        for (int i = 0; i < display.length; i++) {
            for (int j = 0; j < display[i].length; j++) {
                System.out.println(display[i][j]);
            }
        }
    }

    // Getters
    public int getValue() { return value; }
    public String getSuit() { return suit; }
    public String getName() { return name; }



}
