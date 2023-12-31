import java.util.Scanner;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all of the things a Hunter can do in town.
 */
public class Town
{
    //instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private static int treasureTotal;

    private static int treasure;

    private static int firstTreasure;
    private static int secondTreasure;
    private static int thirdTreasure;
    public static int luck;



    //Constructor
    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     * @param s The town's shoppe.
     * @param t The surrounding terrain.
     */
    public Town(Shop shop, double toughness)
    {
        this.shop = shop;
        this.terrain = getNewTerrain();


        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews()
    {
        return printMessage;
    }

    public int getTreasureTotal(){
        return treasureTotal;
    }

    /**
     * Assigns an object to the Hunter in town.
     * @param h The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter)
    {
        treasure = 1;
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown)
        {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        }
        else
        {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown()
    {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown)
        {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak())
            {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, your " + item + " broke.";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    public void enterShop(String choice)
    {
        shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble()
    {
        double noTroubleChance;
        if (toughTown)
        {
            noTroubleChance = 0.66;
        }
        else
        {
            noTroubleChance = 0.33;
        }

        if (Math.random() > noTroubleChance)
        {
            printMessage = "You couldn't find any trouble";
        }
        else
        {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int)(Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance)
            {
                if(TreasureHunter.cheatMode){
                    int cheatGoldDiff = 100;
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " +  cheatGoldDiff + " gold.";
                    hunter.changeGold(cheatGoldDiff);
                }else{;
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
                    hunter.changeGold(goldDiff);
                }
            }
            else
            {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
                hunter.changeGold(-1 * goldDiff);
                if(hunter.getGold() == 0){
                    System.out.println("Game over, you lost your gold");
                    System.exit(0);
                }
            }
        }

    }

    public void huntForTreasure() throws InterruptedException {
        int noTreasureChance = (int) (Math.random() * 4) + 1;
        if ((luck > 10) && noTreasureChance == 4){
            noTreasureChance-=1;
        }
        if (noTreasureChance == 1 && treasure == 1 && treasureTotal < 3){

            if (firstTreasure == 2 || firstTreasure == 3) {
                printMessage = "You have duplicate treasures, you must discard one armor";
                firstTreasure = firstTreasure - 1;
            }else{
                firstTreasure = firstTreasure + 1;
                treasureTotal = treasureTotal + 1;
                treasure = treasure - 1;
                printMessage = "You got Armor! you have " + treasureTotal + " treasure";
            }
            if (treasureTotal == 3){
                System.out.print("You have found armor and now all 3 treasure! You have won the game!");
                System.exit(0);
            }

        }
        else if (noTreasureChance == 2 && treasure == 1 && treasureTotal < 3){



            if (secondTreasure == 3 || secondTreasure == 1) {
                printMessage = "You have duplicate treasures, you must discard one book";
                secondTreasure = secondTreasure - 1;
            }else{
                secondTreasure = secondTreasure + 1;
                treasureTotal = treasureTotal + 1;
                treasure = treasure - 1;
                printMessage = "You got a Book! you have " + treasureTotal + " treasure";
            }
            if (treasureTotal == 3){
                System.out.print("You have now found a book and found all 3 treasure! You have won the game!");
                System.exit(0);
            }
        }
        else if (noTreasureChance == 3 && treasure == 1 && treasureTotal < 3){



            if (thirdTreasure == 1 || thirdTreasure == 2) {
                printMessage = "You have duplicate treasures, you must discard one diamond ring";
                thirdTreasure = thirdTreasure - 1;
            }else{
                thirdTreasure = thirdTreasure + 1;
                treasureTotal = treasureTotal + 1;
                treasure = treasure - 1;
                printMessage = "You got a Diamond Ring! you have " + treasureTotal + " treasure";
            }
            if (treasureTotal == 3){
                System.out.print("Congrats! You got the diamond ring all 3 Treasure! Game over.");
                //printMessage = "You have found all 3 treasure! You have won the game!";
                Thread.sleep(1000);
                System.exit(1);
            }
        }
        else if (noTreasureChance == 4 && treasure == 1 && treasureTotal < 3){
            printMessage = "You couldn't find any treasure";
        }
        else if(treasure == 0){
            printMessage = "You must leave and go to the next town!";
        }

    }

    public void goToCasino() {
        if (hunter.getGold() > 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("How much gold do you want to wager? ");
            int wager = Integer.parseInt(scanner.nextLine());
            int wagerStart = wager;
            if (wager <= hunter.getGold()) {
                int rollOne = (int) (Math.random() * 6) - 1;
                int rollTwo = (int) (Math.random() * 6) - 1;
                int number = rollOne + rollTwo;
                System.out.print("Pick a number between 1 and 12: ");
                int guess = Integer.parseInt(scanner.nextLine());
                int checkNumGreater = number - guess;
                int checkGuessGreater = guess - number;
                if (guess == number) {
                    System.out.println("Congratulations! your wager gold has doubled and been added!!");
                    wager = wager * 2;
                    hunter.addGold(wager);
                    if ((wager / 2) >= 10){
                        int luckAdded = ((wager / 2) / 10) * 2;
                        System.out.println("Your luck has went up by " + luckAdded + "%");
                        luck = luck + luckAdded;
                    }
                } else if ((checkNumGreater <= 2) && (checkGuessGreater <= 2)) {
                    System.out.println("Since your guesses were within two or the number, your gold is untouched");
                } else if ((checkNumGreater > 2) || (checkGuessGreater > 2)) {
                    System.out.println("Your gold is more than two numbers away, you loose all the gold you wagered!");
                    hunter.looseGold(wager);
                    if (wager >= 10) {
                        int luckLost = (wager / 10) * 2;
                        System.out.println("Your luck has went down by " + luckLost + "%");
                        luck = luck - luckLost;
                    }
                } else {
                    System.out.println("Invalid input please put a number between 1 and 12");
                }
            }
            else{
                System.out.println("You cannot wager more than you have!");
            }
            }
        else{
            System.out.println("You can only enter if you have gold!");
        }

        }

    public String toString()
    {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain()
    {
        double rnd = Math.random();
        if (rnd < .2)
        {
            return new Terrain("Mountains", "Rope");
        }
        else if (rnd < .4)
        {
            return new Terrain("Ocean", "Boat");
        }
        else if (rnd < .6)
        {
            return new Terrain("Plains", "Horse");
        }
        else if (rnd < .8)
        {
            return new Terrain("Desert", "Water");
        }
        else
        {
            return new Terrain("Jungle", "Machete");
        }
    }

    /**
     * Determines whether or not a used item has broken.
     * @return true if the item broke.
     */
    private boolean checkItemBreak()
    {
        double rand = Math.random();
        return (rand < 0.5);
    }

}