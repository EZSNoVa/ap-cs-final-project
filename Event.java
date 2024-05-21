/// Original idea of this module was to be a parser for this script language
/// ----------------
/// EVENT Luck:              <- Event definition starts with EVENT
/// byte String, String, ... ;  <- Argument format
///
///  |     |       |         |
///  Action, a map of byte-to-function would resolve the action
///        |       |         |
///        Argument 1 & 2    |
///                          End of event line.
/// END                      <- Event definition ends with END

/// But for time constraints, I'll just implement the events as classes
/// Each class extends from base class Event, and implements the perform function
/// That way events can directly interact with the game context


import java.util.ArrayList;

/// List of all events
class Events  {
    static Event[] events = new Event[] {
        new Luck(),
        new TryPeak(),
        new Passing(),
        new DealerPeak(),
        new DealerShow(),
        new Blind(),
        new ReligiousTalk(),
        new GodIntervention()
    };
}

/// Base class for all events
public abstract class Event {
    static ArrayList<Event> events = new ArrayList<Event>();
    public abstract void perform(Game ctx);

}

class Luck extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: I'm feeling lucky, are you?");

        switch (ctx.playerInput(new String[] { "I already won!", "I'm not sure..." })) {
            case 1:
                System.out.println("Dealer: You're right, you already won. Ha, ha, ha");
                ctx.will_bust = true;
                break;

            case 2:
                System.out.println("Dealer: You should be.");
                ctx.lucky = true;
                break;
        }
    }
}

class TryPeak extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: Trying to peak at my cards, huh?");

        switch (ctx.playerInput(new String[] { "...", "No way!" })) {
            case 1:
                System.out.println("Dealer: At very least you're honest..");
                System.out.println("Dealer shows a " + ctx.dealerHand.get(0));
                break;

            case 2:
                System.out.println("Dealer: It's fair I guess...");

                switch (ctx.playerInput(new String[] { "Is this game fair?", "Fair... for who?"})) {
                    case 1:
                        System.out.println("Dealer: Well, you could say that.");
                        System.out.println("Dealer: There's only a 50% chance of losing.");
                        System.out.println("Dealer: Good for you, right?");
                        break;

                    case 2:
                        System.out.println("Dealer: It's fair for me.");
                        System.out.println("Dealer: I'm the dealer after all.");
                        break;
                }

                break;
        }
    }
}

class Passing extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: don't bluff, just pass... or maybe I will have to...");

        switch (ctx.playerInput(new String[] { "I'll pass if you do, deal?", "Bluff" })) {
            case 1:
                System.out.println("Dealer: Fine, fine, it's a deal.");
                ctx.dealer_pass = true;
                break;

            case 2:
                System.out.println("Dealer: I see...");
                System.out.println("Dealer: I'll have to pass too.");
                break;
        }
    }
}

class DealerPeak extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: Trying to hide that " + ctx.playerHand.get(0) + "?");

        switch (ctx.playerInput(new String[] { "How?...", "Not fair!" })) {
            case 1:
                System.out.println("Dealer: Isn't it obvious?");
                System.out.println("Dealer: Want to give up?");

                switch (ctx.playerInput(new String[] { "No...!", "I give up." })) {
                    case 1:
                        System.out.println("Dealer: Good choice.");
                        System.out.println("Dealer: You won't regret it. Ha, ha, ha");
                        break;

                    case 2:
                        System.out.println("Dealer: Ha, ha, ha... See you next time.");
                        System.out.println("Your phone starts ringing ominously...");
                        ctx.game_over = true;
                        return;
                }

                break;

            case 2:
                System.out.println("Dealer: I'm just good at reading people. Ha, ha, ha");
                break;
        }
    }
}


class DealerShow extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: Want to know what I have?");
        switch (ctx.playerInput(new String[] { "Sure, let's hear the bluff", "No need." })) {
            case 1:
                System.out.println("I have a " + ctx.dealerHand.get(0));
                ctx.dealerHand.get(0).print_display();
                break;

            case 2:
                System.out.println("Dealer: You're right, it's not important. Ha, ha, ha");
                System.out.println("Dealer: Or... is it?");
                break;
        }
    }
}

class Blind extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: You're putting quite some thought into the game.");
        System.out.println("Dealer: like the cards you see?");

        switch (ctx.playerInput(new String[] { "Not really, you?", "Don't think I'll have to see them no more~" })) {
            case 1:
                System.out.println("Dealer: Oh, Ho, ho, ho...");
                System.out.println("Dealer: I'm sure you'll like them.");

                switch (ctx.playerInput(new String[] { "Bluff", "I agree, I'll like them." })) {
                    case 1:
                        System.out.println("Dealer: Ha, ha, ha... I see.");
                        System.out.println("Dealer: Don't worry, you'll see it soon too. Ha, ha, ha");
                        ctx.blind = true;
                        break;

                    case 2:
                        System.out.println("Dealer: Oh, ho, ho, ho...");
                        System.out.println("Dealer: You're a funny one.");
                        break;
                }

                break;

            case 2:
                System.out.println("Dealer: Ha, ha, ha... I see.");
                System.out.println("Dealer: Don't worry, you'll see it soon too. Ha, ha, ha");
                ctx.blind = true;
                break;
        }


    }
}

class ReligiousTalk extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: Winning is such a matter of luck, isn't it?");
        System.out.println("Dealer: By the way, do you believe in God?");

        switch (ctx.playerInput(new String[] { "Yes", "No..." })) {
            case 1:
                System.out.println("Dealer: Why don't you ask him for help?");
                System.out.println("Dealer: I'm sure he'll understand.");
                System.out.println("Dealer: Ha, ha, ha...");
                break;

            case 2:
                System.out.println("Dealer: Then, who do you believe in?");

                switch (ctx.playerInput(new String[] { "Myself", "No one" })) {
                    case 1:
                        System.out.println("Dealer: I also believe in myself.");
                        System.out.println("Dealer: But you see... I'm the dealer.");
                        System.out.println("Dealer: Ha, ha, ha...");
                        break;

                    case 2:
                        System.out.println("Dealer: Pretty smart, I see.");
                        System.out.println("Dealer: If you don't believe in anyone, you can't be disappointed.");
                        System.out.println("Dealer: But you can't be saved either. Ha, ha, ha");
                        break;
                }

                break;
        }

    }
}


class GodIntervention extends Event {
    public void perform(Game ctx) {
        System.out.println("Dealer: If I were to stay right now...");
        System.out.println("Dealer: Would you lose?");

        switch (ctx.playerInput(new String[] { "Nah, I'd win", "I'd lose" })) {
            case 1:
                System.out.println("Dealer: Oh, pretty confident, aren't you?");
                System.out.println("Dealer: Would you... put your life on it?");
                System.out.println("Dealer: Ha, ha, ha...");
                System.out.println("Dealer: Just kidding. But, aren't bets more fun when they're high?");

                switch (ctx.playerInput(new String[] { "Not betting my life but, on God I'll win", "I'll pass, risk management is important after all..." })) {
                    case 1:
                        System.out.println("Dealer looks at you with a smile.");

                        // Fill console with random text to simulate god's intervention
                        for (int i = 0; i < 1000; i++) {
                            // Print random characters
                            for (int j = 0; j < 200; j++)
                                System.out.print((char) (Math.random() * 255));

                            System.out.println();
                        }

                        // Clear console
                        System.out.print("\033[H\033[2J");
                        System.out.flush();


                        // God's intervention
                        System.out.println();
                        System.out.println();

                        System.out.println("┌-------------------┐");
                        System.out.println("|                   |");
                        System.out.println("| Nah, you'll lose. |");
                        System.out.println("|                   |");
                        System.out.println("└------------------┘");


                        System.out.println();
                        System.out.println();

                        System.out.println("Dealer: ...");
                        System.out.println("Dealer: Ha, ha, ha...");
                        System.out.println("The dealer keeps his smile, acting as if nothing happened.");
                        System.out.println("The game continues.");

                        ctx.god_intervention = true;
                        break;

                    case 2:
                        System.out.println("Dealer: Of course it is~");
                        System.out.println("Dealer: But... so is the thrill of the game.");
                        System.out.println("Dealer: Ha, ha, ha...");
                        break;

                }

        }
    }
}
