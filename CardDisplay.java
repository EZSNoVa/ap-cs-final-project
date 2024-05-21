
/// * Ascii Card Display Interface
/// Should look like this:
///  _________
/// |A        |
/// |         |
/// |    ♠    |
/// |         |
/// |        A|
/// |_________|

import java.util.HashMap;

public interface CardDisplay {
    public static final HashMap<String, String> suits = new HashMap<String, String>() {
        {
            put("Spades", "♠");
            put("Hearts", "♥");
            put("Diamonds", "♦");
            put("Clubs", "♣");
            put("???", "†");
        }
    };

    public default String[][] display(Card card) {
        String[][] cardDisplay = new String[5][1];

        String name = card.getName();

        if (name.length() > 2) {
            name = name.substring(0, 1);
        }

        int len = name.length();

        cardDisplay[0][0] = "┌───────┐";
        cardDisplay[1][0] = "|" + name + (" ".repeat(7 - len)) + "|";
        cardDisplay[2][0] = "|   " + suits.get(card.getSuit()) + "   |";
        cardDisplay[3][0] = "|" + (" ".repeat(7 - len)) + name + "|";
        cardDisplay[4][0] = "└───────┘";

        return cardDisplay;
    }
}
