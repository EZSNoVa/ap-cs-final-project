
public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        System.out.println("Welcome to Blackjack!");
        System.out.println("How much would you like to bet?");
        switch (game.playerInput(new String[] { "1$", "1000$", "666$", "My life savings" })) {

            // 1
            case 1:
                System.out.println("You're a high roller, huh?");
                System.out.println("Minimum bet is 250$, if you can't afford it, get out.");

                switch (game.playerInput(new String[] { "All in", "I'm out" })) {
                    case 1:
                        game.playGame(10000);
                        return;

                    case 2:
                        System.out.println("Dealer: You sure?... Might be wrong, but today's your lucky day.");

                        switch (game.playerInput(new String[] { "..." })) {
                         case 1:
                                System.out.println("Dealer: Let's play then.");
                                game.playGame(250);
                                return;
                        }

                    break;
                }

                return;

            // 250
            case 2:
                System.err.println("Dealer: Wise or scared?\nw...will find out soon...");
                game.playGame(250);
                return;


            // 666
            case 3:
                System.out.println("Your phone starts ringing, it's your wife.");
                System.out.println("You answer, she says: 'I'm leaving you!!! can't believe you went to the casino again!, I'm taking the kids and the dog.'");
                System.out.println("You hang up, and look at the dealer, he's smiling.");

                switch (game.playerInput(new String[] { "I'm all in." })) {
                    case 1:
                        System.out.println("Dealer: Wishing you... Good luck.");
                        game.playGame(100000);
                        return;
                }
                break;

            // My life savings
            case 4:
                System.out.println("Dealer: You're not the first to say that.");
                System.out.println("Dealer: You're not the first to lose it all either.");
                System.out.println("Dealer: Let's play.");

                game.playGame(50000);
                return;
        }


    }
}
