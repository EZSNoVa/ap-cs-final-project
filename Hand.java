
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a hand of cards in a card game.
 */
public class Hand {
    private ArrayList<Card> cards = new ArrayList<>(); // Use diamond operator for type inference

    /**
     * Adds a card to the hand.
     *
     * @param card The card to be added.
     */
    public void add(Card card) {
        cards.add(card);
    }

    /**
     * Calculates the value of the hand.
     * Aces are counted as 11 if it doesn't bust the hand.
     *
     * @return The value of the hand.
     */
    public int value() {
        int value = 0;
        int aces = 0;

        for (Card card : cards) {
            value += card.getValue();
            if (card.getName().equals("Ace")) {
                aces++;
            }
        }

        // Use aces as 11 if it doesn't bust the hand
        while (value < 12 && aces > 0) {
            value += 10; // 10 = 11 (Ace value now) - 1 (Counted value before)
            aces--;
        }

        return value;
    }
    /**
     * @return Card at index
     */
    public Card get(int index) {
        return cards.get(index);
    }

    public void set(int index, Card card) {
        cards.set(index, card);
    }

    public int size() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
    }

    /**
     * @return The string representation of the hand.
     */
    @Override
    public String toString() {
        String[][][] displays = new String[cards.size()][][];

        for (int i = 0; i < cards.size(); i++) {
            displays[i] = cards.get(i).display(cards.get(i));
        }

        // Display the cards in a row
        StringBuilder[] rows = new StringBuilder[5];
        for (int i = 0; i < 5; i++) {
            rows[i] = new StringBuilder();
            for (int j = 0; j < cards.size(); j++) {
                rows[i].append(displays[j][i][0]);
                rows[i].append(" ");
            }
        }

        for (StringBuilder row : rows) {
            System.out.println(row);
        }


        return cards.subList(0, cards.size() - 1)
                .stream()
                .map(Card::toString)
                .collect(Collectors.joining(", ")) + " and " + cards.get(cards.size() - 1);
    }
}
