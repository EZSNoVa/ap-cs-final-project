
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The `Game` class represents THE BLACKJACK game. It manages the deck of cards,
 * player's hand, and dealer's hand.
 * The game allows the player to draw cards, make decisions, and determine the
 * outcome of the game.
 */

public class Game {
    int[] cardValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 }; // Card values get "fixed" at Card constructor.
    String[] cardSuits = { "Spades", "Hearts", "Diamonds", "Clubs" };
    ArrayList<Card> deck = new ArrayList<>();
    Hand playerHand = new Hand();
    Hand dealerHand = new Hand();

    private ArrayList<Event> events = new ArrayList<>();


    public int bet;

    // * Flags
    public boolean will_bust = false; // Will absolutely lose
    public boolean lucky = false; // Might not lose
    public boolean god_intervention = false; // God is mad at, don't put it on god next time
    public boolean blind = false; // Can't see the cards
    public boolean dizzy = false; // A card might be replaced by a random one
    public boolean dealer_pass = false; // Dealer will pass

    public boolean game_over = false; // Game is over player gave up


    // * Game Flow control
    private boolean view_cards = false; // True when player wants to see his cards, used to not trigger events when player checks his cards

    /**
     * Constructs a new Game object.
     */
    public Game() {

        // Initialize the events to view
       for (Event ev : Events.events) {
            this.events.add(ev);
       }

       // Shuffle the events
        Collections.shuffle(this.events);

    }

    /**
     * Draws a card from the deck.
     *
     * @return The drawn card.
     */
    public Card drawCard() {
        return deck.remove(deck.size() - 1);
    }

    /**
     * Plays the game with the specified bet.
     *
     * @param bet The bet amount.
     */
    public void playGame(int bet) {
        this.bet = bet;

        reset();

        // Initial 2 cards
        playerHand.add(drawCard());
        playerHand.add(drawCard());

        dealerHand.add(drawCard());
        dealerHand.add(drawCard());

        System.out.println();
        System.out.println("Game starts, you take sit at the table. and remember your bet is " + bet + "$");
        System.out.println();

        while (true) {
            System.out.println();
            System.out.println("Dealer's Upcard: " + dealerHand.get(0));
            System.out.println();


            System.out.println("Your hand: " + (blind ? "...Cards are looking blurry..." : playerHand));
            System.out.println();


            // If not viewing cards, perform the next event
            if (!view_cards) {
                if (events.size() > 0) {
                    events.remove(0).perform(this);
                } else {
                    System.out.println("Dealer: This is the last round, make it count.");
                }

            // Otherwise, reset the view_cards flag
            } else {
                view_cards = false;
            }


            // Handler player giving up
            if (game_over) {
                System.out.println("Dealer: Giving up in a bet? That's a first.");
                System.out.println("Dealer: Should've played till the end.");
                System.out.println(dealerHand);

                afterGame(false);
                return;
            }


            switch (playerInput(new String[] { "Hit", "Stay", "Check my cards" })) {
                case 1:

                    if (god_intervention) {
                        System.out.println("Suddenly, as if it was magic, all your cards are turned into 7s");

                        int n = playerHand.size();
                        playerHand.clear();
                        for (int i = 0; i < n; i++) {
                            playerHand.add(new Card(7, "???"));
                        }
                        break;
                    }


                    playerHand.add(drawCard());

                    if (playerHand.value() > 21 && lucky) {
                        // Replace the last card with a random card
                        playerHand.set(playerHand.size() - 1, drawCard());
                        lucky = false;
                    }

                    if (playerHand.value() > 21) {

                        System.out.println("Your hand: " + playerHand);
                        System.out.println("Total: " + playerHand.value());
                        System.out.println("You busted! Dealer wins.");

                        if (will_bust) {
                            System.out.println("Dealer: I told you, you already won, but not this time. Ha, ha, ha");
                        }

                        afterGame(false);
                        return;
                    }
                    continue; // Continue player's turn

                case 2:
                    // Break player's turn
                    break;

                // Player checks his cards
                case 3:
                    System.out.println();
                    System.out.println("Your cards, haven't changed, yet.");
                    view_cards = true;
                    continue; // Continue player's turn
            }
            break; // End of player's turn
        }

        if (god_intervention) {
            System.out.println("Dealer: What even are those cards...?");
            System.out.println("Dealer: I plead you know, you can't bring your own deck...");
            System.out.println("Dealer: Furthermore, how?...");
            System.out.println("Dealer looks atonished, but continues the game.");
        }

        // Dealers turn
        while ((Math.random() < 0.35 && dealerHand.value() < 18) && !dealer_pass) {
            System.out.println("Dealer draws a card");
            dealerHand.add(drawCard());

            if (dealerHand.value() > 21) {
                System.out.println("Dealer's hand: " + dealerHand);
                System.out.println("\tDealer busted! You win.");

                System.out.println("Dealer: ... You're lucky, you know that?");

                // Revealing the hands
                revealHands();

                System.out.println("Dealer: Double or nothing?");
                switch (playerInput(new String[] { "Nah, I'd leave", "Double", "Double? go triple!" })) {
                    case 1:
                        System.out.println("Dealer: You sure?... You can win so much more~");
                        switch (playerInput(new String[] { "..." })) {
                            case 1:
                                System.out.println("Dealer: Let's play then.");
                                playGame(bet * 2);
                                return;
                        }

                    case 2:
                        System.out.println("Dealer: You're brave, I like that. Ha, ha, ha");
                        playGame(bet * 2);
                        return;

                    case 3:
                        System.out.println("Dealer: You're crazy, do you even have that much?");
                        System.out.println("Dealer: bluff or not, you're in. Ha, ha, ha");
                        playGame(bet * 3);
                        return;
                }

                return;
            }
        }

        if (dealerHand.value() > playerHand.value()) {
            System.out.println("Dealer wins, closer to 21!");

            revealHands();

        } else if (playerHand.value() > dealerHand.value()) {
            System.out.println("You win! You're closer to 21!");

            revealHands();

            System.out.println("Dealer: Bet you can't do that again.");

            switch (playerInput(new String[] { "I can", "I can't" })) {
                case 1:
                    System.out.println("Dealer: Ha, ha, ha, we'll see about that, double it.");
                    playGame(bet * 2);
                    return;

                case 2:
                    System.out.println("Dealer: That's the spirit, you're not a quitter.");
                    System.out.println("But... I said...");
                    playGame(bet* 2);
                    return;
                }

        } else {
            System.out.println("It's a tie!");
            System.out.println("Dealer: Was it skill or luck?");
            System.out.println("Dealer: Let's find out, double or nothing?");

            switch (playerInput(new String[] { "I'm off", "Get serious, triple it." })) {
                case 1:
                    System.out.println("Dealer: You sure?... You can win so much more~");
                    switch (playerInput(new String[] { "..." })) {
                        case 1:
                            System.out.println("Dealer: Let's play then.");
                            playGame(bet * 2);
                            return;
                    }

                case 2:
                    System.out.println("Dealer: My, my, you're brave, I like that. Ha, ha, ha");
                    System.out.println("Dealer: Hope your bank account is as brave.");
                    playGame(bet * 3);
                    return;
            }
        }
    }

    /**
     * Prompts the player for input and returns the selected option.
     *
     * @param options The available options.
     * @return The selected option.
     */
    public int playerInput(String[] options) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ": " + options[i]);
        }

        System.out.println();
        System.out.print(">> ");

        int choice;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("--- You're not playing with me, you're playing with yourself. ---");
            System.out.println("--- You can't just type anything, you know? ---");
            System.out.println("--- Be for real. ---");

            // Wait 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            // Clear console
            System.out.print("\033[H\033[2J");
            System.out.flush();

            return playerInput(options);
        }

        if (choice < 1 || choice > options.length) {
            System.out.println("(Can't say this...)");
            return playerInput(options);
        }


        // Clear console
        System.out.print("\033[H\033[2J");
        System.out.flush();

        return choice;
    }

    /**
     * Resets the game by clearing the deck and hands, and shuffling the deck.
     */
    void reset() {
        deck.clear();
        playerHand.clear();
        dealerHand.clear();
        for (String suit : cardSuits) {
            for (int value : cardValues) {
                deck.add(new Card(value, suit));
            }
        }
        Collections.shuffle(deck);
    }

    void revealHands() {
        System.out.println();
        System.out.println("Dealer's hand: " + dealerHand);
        System.out.println();
        System.out.println("Your hand: " + playerHand);
        System.out.println();
    }

    void afterGame(boolean player_won) {
        System.out.println("After leaving the casino you see...");

        // * Winsss Anita Max Wynn
        if (player_won) {
            // Very Low
            if (bet <= 500) {
                System.out.println("A bright sunshine but, you don't feel satisfied.");
                System.out.println("You feel like you could've won more.");
                System.out.println("You look at the casino, and think about going back...");
            }

            // Low
            else if (bet <= 5000) {
                System.out.println("A cold breeze, you feel the wind on your face. You feel alive, one with the dark night sky.");
                System.out.println("The cold breeze makes you feel restless, you feel like you could've won more.");
                System.out.println("in fact, you feel like you could've won it all.");
                System.out.println("There's a casino, you will go back, right?");

            }
            // Medium
            else if (bet <= 10000) {
                System.out.println("An dark abysmal night, you feel the solitude of the night.");
                System.out.println("You feel amazing, you  won.");
                System.out.println("You were predesigned to win, you feel like you could've won more.");
                System.out.println("You are the honored one, you feel like you could've won it all.");
                System.out.println("Are we a quitters?");


            // High Medium
            } else if (bet <= 50000) {
                System.out.println("An gentle, vivid and luminous night, you feel the warmth of the street lights.");
                System.out.println("Such a true satisfaction, you are, you'll be, and you will always be");
                System.out.println("Perhaps just another drop in the ocean, but you, you are different.");
                System.out.println("You are, the one, the only, the winner.");
                System.out.println("And you will always be, a true winner.");
                System.out.println("Or Are you? Winning is okay, but, winning it all is better.");
                System.out.println("Your heart starts racing...");
            }

            // High
            else if (bet <= 150000) {
                System.out.println("A solitary starry night which seems to be the start of a new day.");
                System.out.println("You feel the warmth of the night, the coldness of the stars.");
                System.out.println("You feel as big as the universe, yet, as small as a grain of sand.");
                System.out.println("What is life? You ask yourself.");
                System.out.println("Life is a game, and you, you are the winner.");
                System.out.println("Life might be short, but you, you are even shorter.");
                System.out.println("Not an issue! you are the one, you can achieve anything.");
                System.out.println("Life is short, enjoy it~");
            }
            // Very High
            else {
                System.out.println("A empty, dark, and cold night, you feel wasted.");
                System.out.println("You look up to the endless night, the beautiful stars, the cold moon.");
                System.out.println("You are certainly not a start, but, here you are, shining.");
                System.out.println("You are not as big as the universe, but, witht the universe, you are one.");
                System.out.println("You are not as small as a grain of sand, but, with the sand, you are one.");
                System.out.println("You are not the winner, but, you are the one.");
                System.out.println("You are not the one, but, you are the winner.");
                System.out.println("You are all and nothing");
                System.out.println("All and nothing");
                System.out.println("All or nothing");
                System.out.println("All");
                System.out.println("Nothing");
                System.out.println("You are all or nothing?");
            }


        // * Realities (lostsss)
        } else {
            // Very Low
            if (bet <= 500) {
                System.out.println("A cloudy sky reflecting your mood. You lost.");
                System.out.println("The faint sound of laughter comes from the casino, mocking you.");
                System.out.println("You reach into your pocket, hoping to find another dollar.");
                System.out.println("You know you can't end on a loss, right?");
                System.out.println("The casino doors seem to call your name...");
            }

            // Low
            else if (bet <= 5000) {
                System.out.println("A chilly wind bites at your face, much like the dealer's grin.");
                System.out.println("You lost, but the game isn't over.");
                System.out.println("You swear you heard the dealer say 'Better luck next time'.");
                System.out.println("The neon lights flicker invitingly. You can't resist their glow, can you?");
                System.out.println("Just one more round, you'll win it back.");
            }

            // Medium
            else if (bet <= 10000) {
                System.out.println("A foggy night, as if the world itself is mocking your loss.");
                System.out.println("You feel the sting of defeat, but also a burning desire for redemption.");
                System.out.println("The casino looms behind you, a siren's call you can't ignore.");
                System.out.println("Are you going to let this defeat stand?");
                System.out.println("You know you have to go back.");
            }

            // High Medium
            else if (bet <= 50000) {
                System.out.println("A quiet night, the kind that makes you question your choices.");
                System.out.println("Your wallet is lighter, but your spirit is heavy with determination.");
                System.out.println("You feel a sense of unfinished business.");
                System.out.println("The casino is there, just behind you. A second chance awaits.");
                System.out.println("You know you can't walk away now, right?");
            }

            // High
            else if (bet <= 150000) {
                System.out.println("An empty street, mirroring the void in your wallet.");
                System.out.println("You lost big, but you know it's just a temporary setback.");
                System.out.println("You hear the distant sounds of the casino, beckoning you back.");
                System.out.println("Quitting isn't in your nature. You were born to play.");
                System.out.println("The night is still young. Ready for another round?");
            }

            // Very High
            else {
                System.out.println("A vast, starless sky, as empty as your pockets.");
                System.out.println("The loss stings, but the allure of victory still burns within you.");
                System.out.println("You take a deep breath, the cool air filling you with resolve.");
                System.out.println("The casino stands tall, a beacon of second chances.");
                System.out.println("You're not one to give up. It's time to turn the tables.");
                System.out.println("All or nothing, the choice is clear.");
            }
        }
    }
}
