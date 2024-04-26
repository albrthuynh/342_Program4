import java.io.Serializable;

public class BattleShipGame implements Serializable {
    static final long serialVersionUID = 42L;
    Boolean versusComputer = false;
    Player currentPlayer = new Player();
    Player opponent = new Player();
    Computer computerPlayer = new Computer();
    int countShipSize;
    boolean isShipFilled;
    boolean firstConnect = false;

    boolean otherPlayerConnected = false;


    Boolean above = true; //this means that they can choose spots above the coordinate they chose
    Boolean below = true; //this means that they can choose spots below the coordinate they chose
    Boolean right = true; //this means that they can choose spots to the right of the coordinate they chose
    Boolean left = true; //this means that they can choose spots to the left of the coordinate they chose

    BattleShipGame(){
        countShipSize = 0;
        isShipFilled = false;
    }

    public String checkWinner(){
        if (versusComputer){
            if(computerPlayer.computerHealth == 0){
                return "currentPlayer";
            }
            else if(currentPlayer.playerHealth == 0){
                return "otherPlayer";
            }
        }
        else {
            if (opponent.playerHealth == 0) {
                return "currentPlayer";
            }
            else if (currentPlayer.playerHealth == 0) {
                return "otherPlayer";
            }
        }
        return "none";
    }

    public Boolean checkAttack(String coordinate, Boolean isComputerTurn){
        //computer is attacking the player
        if (!versusComputer) {
            if (opponent.allCoordinates.contains(coordinate)) {
                if (opponent.twoShip.getCoordinates().contains(coordinate)) {
                    opponent.twoShip.hitCount++;
                }
                else if (opponent.threeShip.getCoordinates().contains(coordinate)) {
                    opponent.threeShip.hitCount++;
                }
                else if (opponent.threeShip2.getCoordinates().contains(coordinate)) {
                    opponent.threeShip2.hitCount++;
                }
                else if (opponent.fourShip.getCoordinates().contains(coordinate)) {
                    opponent.fourShip.hitCount++;
                }
                else if (opponent.fiveShip.getCoordinates().contains(coordinate)) {
                    opponent.fiveShip.hitCount++;
                }
                opponent.playerHealth--;
                return true;
            }

        }
        else {
            if (isComputerTurn) {
                //check the player's coordinates to check if the computer hit any of their ships
                if (currentPlayer.allCoordinates.contains(coordinate)) {
                    //System.out.println("INSIDE FOUND COORDINATE IN ALL COORDINATES");
                    if (currentPlayer.twoShip.getCoordinates().contains(coordinate)) {
                        currentPlayer.twoShip.hitCount++;
                    }
                    else if (currentPlayer.threeShip.getCoordinates().contains(coordinate)) {
                        currentPlayer.threeShip.hitCount++;
                    }
                    else if (currentPlayer.threeShip2.getCoordinates().contains(coordinate)) {
                        currentPlayer.threeShip2.hitCount++;
                    }
                    else if (currentPlayer.fourShip.getCoordinates().contains(coordinate)) {
                        currentPlayer.fourShip.hitCount++;
                    }
                    else if (currentPlayer.fiveShip.getCoordinates().contains(coordinate)) {
                        currentPlayer.fiveShip.hitCount++;
                    }
                    currentPlayer.playerHealth--;
                    return true;
                }
            }
            //player is attacking the computer
            else {
                if (computerPlayer.allCoordinates.contains(coordinate)) {
                    //System.out.println("This is the boolean value to see if the .getText() is inside the computer's allCoordinates: " + computerPlayer.allCoordinates.contains(coordinate));
                    if (computerPlayer.twoShip.getCoordinates().contains(coordinate)) {
                        computerPlayer.twoShip.hitCount++;
                    } else if (computerPlayer.threeShip.getCoordinates().contains(coordinate)) {
                        computerPlayer.threeShip.hitCount++;
                    } else if (computerPlayer.threeShip2.getCoordinates().contains(coordinate)) {
                        computerPlayer.threeShip2.hitCount++;
                    } else if (computerPlayer.fourShip.getCoordinates().contains(coordinate)) {
                        computerPlayer.fourShip.hitCount++;
                    } else if (computerPlayer.fiveShip.getCoordinates().contains(coordinate)) {
                        computerPlayer.fiveShip.hitCount++;
                    }
                    computerPlayer.computerHealth--;
                    return true;
                }
            }
        }

        return false;
    }
    public void randomizeCoords(Ship ship){
        ship.isHorizontal = false;
        ship.isVertical = false;
        String randCoord = "";
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] orientation = {"Horizontal", "Vertical"};
        int rangeForNums = 10; //range for choosing random numbers for coord (upperBound - lowerBound)
        int rangeForLetters = 10; //range for choosing for random letters(index in array)
        int rangeForOrientation = 2;
        int randomOrientation = 0;
//        //generate random click
        //keep looping generating random coordinate until one works where we can fill all spots for ship
        randomOrientation = (int)(Math.random() * rangeForOrientation);
        if (orientation[randomOrientation].equals("Horizontal")) {
            ship.isHorizontal = true;
        }
        else {
            ship.isVertical = true;
        }

        while (ship.getCoordinates().size() != ship.shipLength-1) {
            int randomNum = (int)(Math.random() * rangeForNums) + 1;
            int randomLetter = (int)(Math.random() * rangeForLetters);
            //System.out.println("RANDOM ORIENTATION IN WHILE LOOP: " +orientation[randomOrientation]);
//            randomOrientation = (int)(Math.random() * rangeForOrientation);
//            if (orientation[randomOrientation].equals("Horizontal")) {
//                ship.isHorizontal = true;
//            }
//            else{
//                ship.isVertical = true;
//            }
            randCoord = letters[randomLetter] + randomNum;
            checkFirstClick(randCoord, ship, versusComputer);

            //keep generating random first click until twoFilled is true
        }
        //System.out.println("RANDOM ORIENTATION AFTER WHILE LOOP: " +orientation[randomOrientation]);

        ship.addCoordinates(randCoord); //add first initital coord if valid
        computerPlayer.allCoordinates.add(randCoord);
//        System.out.println("COMPUTER TWO SHIP COORDINATES: " + ship.getCoordinates());
//        System.out.println("COMPUTER ALL COORDINATES: " + computerPlayer.allCoordinates);
    }
    public void createBoardForComputer() {
        randomizeCoords(computerPlayer.twoShip);
        randomizeCoords(computerPlayer.threeShip);
        randomizeCoords(computerPlayer.threeShip2);
        randomizeCoords(computerPlayer.fourShip);
        randomizeCoords(computerPlayer.fiveShip);
    }

    public void checkAbove(Ship ship, char rowChar, Integer col, Boolean isComputer){
        countShipSize = 0;
//        System.out.println("inside checkAbove method");
        for (int i = 0; i < ship.shipLength - 1; i++){
            char nextCoord = (char)(rowChar - (i+1));
            //System.out.println("Above - next coord: " + nextCoord);
            String fullCoord = String.valueOf(nextCoord) + String.valueOf(col);
            //System.out.println("Above - next fullCoord: " + fullCoord);

            //check if this coordinate exists in the array
            if (!isComputer){
                if (currentPlayer.allCoordinates.contains(fullCoord)) {
                   // System.out.println("inside if statement ABOVE allCoordinates contains\n");
                    //ship cannot be placed above
                    above = false;
                    break;
                }
            }
            else{
                if (computerPlayer.allCoordinates.contains(fullCoord)) {
                    //System.out.println("inside if statement ABOVE allCoordinates contains\n");
                    //ship cannot be placed above
                    above = false;
                    break;
                }
            }

            //if we have not filled up all the spots for the ship, add next coordinate
            //System.out.println("countShipSize count: "+ countShipSize);
            if (countShipSize < ship.shipLength - 1){
                countShipSize++;
                //System.out.println("adding fullcoord in the checkabove method" + fullCoord);
                ship.addCoordinates(fullCoord);
            }
        }
    }

    public void checkBelow(Ship ship, char rowChar, Integer col, Boolean isComputer){
       // System.out.println("inside checkbelow method");
        for (int i = 0; i < ship.shipLength - 1; i++) {
            char nextCoord = (char)(rowChar + (i+1));
            //System.out.println("Below - next coord: " + nextCoord);
            String fullCoord = String.valueOf(nextCoord) + String.valueOf(col);
           // System.out.println("Below - next fullCoord: " + fullCoord);

            //check if this coordinate exists in the array
            if (!isComputer){
                if (currentPlayer.allCoordinates.contains(fullCoord)) {
                    //System.out.println("inside if statement BELOW allCoordinates contains\n " );
                    //ship cannot be placed below
                    below = false;
                    break;
                }
            }
            else {
                if (computerPlayer.allCoordinates.contains(fullCoord)) {
                   // System.out.println("inside if statement BELOW allCoordinates contains\n " );
                    //ship cannot be placed below
                    below = false;
                    break;
                }
            }

            //if we have not filled up all the spots for the ship, add next coordinate
           // System.out.println("countShipSize count: "+ countShipSize);
            if (countShipSize < ship.shipLength - 1) {
                countShipSize++;
                //System.out.println("adding fullcoord in the check below method" + fullCoord);
                ship.addCoordinates(fullCoord);
            }
        }
    }

    public void checkLeft(Ship ship, char rowChar, Integer col, Boolean isComputer){
        for(int i = 0; i < ship.shipLength -1; i++){
            int nextInt = col - (i+1);
            //System.out.println("Left - next Int: " + nextInt);
            String fullCoord = String.valueOf(rowChar) + String.valueOf(nextInt);
           // System.out.println("Left - next fullCoord: " + fullCoord);

            //check if this coordinate exists in the array
            if (!isComputer){
                if(currentPlayer.allCoordinates.contains(fullCoord)){
                   // System.out.println("inside if statement LEFT allCoordinates contains\n");
                    left = false;
                    break;
                }
            }
            else{
                if(computerPlayer.allCoordinates.contains(fullCoord)){
                   // System.out.println("inside if statement LEFT allCoordinates contains\n");
                    left = false;
                    break;
                }
            }

            //if we have not filled up all the spots for the ship, add next coordinate
           // System.out.println("countShipSize count: "+ countShipSize);
            if(countShipSize < ship.shipLength - 1){
                countShipSize++;
                //System.out.println("adding fullcoord in the check left method: " + fullCoord);
                ship.addCoordinates(fullCoord);
            }
        }
    }

    public void checkRight(Ship ship, char rowChar, Integer col, Boolean isComputer){
        //System.out.println("inside checkright method");
        for (int i = 0; i < ship.shipLength -1; i++) {
            int nextInt = col +(1+i);
            //System.out.println("Right - next Int: " + nextInt);
            String fullCoord = String.valueOf(rowChar) + String.valueOf(nextInt);
            //System.out.println("Right - next fullCoord: " + fullCoord);

            //check if this coordinate exists in the array
            if (!isComputer) {
                if (currentPlayer.allCoordinates.contains(fullCoord)) {
                    //System.out.println("inside if statement RIGHT allCoordinates contains\n");
                    right = false;
                    break;
                }
            }
            else {
                if (computerPlayer.allCoordinates.contains(fullCoord)) {
                    //System.out.println("inside if statement RIGHT allCoordinates contains\n");
                    right = false;
                    break;
                }
            }

            //if we have not filled up all the spots for the ship, add next coordinate
            //System.out.println("countShipSize count: "+ countShipSize);
            if(countShipSize < ship.shipLength - 1){
                countShipSize++;
                //System.out.println("adding fullcoord in the check right method" + fullCoord);
                ship.addCoordinates(fullCoord);
            }
        }
    }

    //check users first click for ship to make sure they have room to fill in the rest of their ship
    public void checkFirstClick(String coord, Ship ship, Boolean isComputer) {
        Integer col = Integer.parseInt(coord.substring(1)); //corresponding coordinate number (col)
        char rowChar = coord.charAt(0); //convert string to char

//        Boolean above = true;
//        Boolean below = true;
//        Boolean right = true;
//        Boolean left = true;

        //checking out of bounds for vertical
        //if going below leads to it being out of bounds
        if ((ship.shipLength - 1) + rowChar > 'J') {
            below = false;
        }
        else if (rowChar - (ship.shipLength - 1) < 'A') {
            above = false;
        }

        //checking out of bounds for horizontal
        //if going below leads to it being out of bounds
        if (col + (ship.shipLength - 1) > 10) {
            right = false;
        }
        else if(col - (ship.shipLength - 1) < 1) {
            left = false;
        }

        //check based off the letters
        if (ship.isVertical) {
            //there is no available space above the coordinate

            if (above.equals(false)) {
                //System.out.println("inside above false if statement\n");
               //they are both out of bounds, so ship will not fit
                if (below.equals(false)) {
                    //System.out.println("inside if ship doesn't fit\n");
                    above = true;
                    below = true;
                    isShipFilled = false;
                    countShipSize = 0;
                    ship.getCoordinates().clear();
                    ship.isHorizontal = false;
                    ship.isVertical = false;
                    return;
                }
                //there is available space only below the coordinate
                else {
                    //System.out.println("inside else only below\n");
                    // go below
                    checkBelow(ship, rowChar, col, isComputer);
                }

            }
            //there is available space above the coordinate
            else {
               // System.out.println("inside space above if statement\n");

                //only available space above
                if (below.equals(false)) {
                    //checking for space above
                   // System.out.println("inside if only above\n");
                    checkAbove(ship,rowChar, col, isComputer);
                }
                //available space both above and below
                else {
                   // System.out.println("inside checking both\n");
                    checkBelow(ship, rowChar, col, isComputer);
                    //System.out.println("AFTER CHECK BELOW - COUNTSHIPSIZE: " + countShipSize);
                    if (countShipSize != ship.shipLength - 1) {
                       // System.out.println("no more coords below so we go above");
                        checkAbove(ship,rowChar, col, isComputer);
                    }
                }
            }
        }
        //check based off numbers if horizontal
        else {
            //System.out.println("inside horizontal else\n");
            //there is no available space to the left of the coordinate
            if (left.equals(false)) {
                //they are both out of bounds, so ship will not fit
                if (right.equals(false)) {
                   // System.out.println("inside right and left are false\n");
                    left = true;
                    right = true;
                    isShipFilled = false;
                    countShipSize = 0;
                    ship.getCoordinates().clear();
                    ship.isHorizontal = false;
                    ship.isVertical = false;
                    return;
                }
                //there is available space only to the right the coordinate
                else {
                    ///System.out.println("inside space only available to the right\n");
                    // go right
                    checkRight(ship, rowChar, col, isComputer);
                }
            }
            //there is available space to the left the coordinate
            else {
                //System.out.println("inside else horizontal else\n");
                //available space only to the left
                if (right.equals(false)) {
                   // System.out.println("inside space only available to the left\n");
                    //checking for space to the left
                    checkLeft(ship,rowChar, col, isComputer);
                }
                //available space both left and right
                else {
                   /// System.out.println("inside space available for both\n");
                    checkLeft(ship, rowChar, col, isComputer);
                    if (countShipSize != ship.shipLength - 1) {
                        checkRight(ship,rowChar, col, isComputer);
                    }
                }
            }
        }

        //System.out.println("this is before Line 236, countShipSize is: " + countShipSize);
        if (countShipSize == ship.shipLength - 1) {
            isShipFilled = true;

            if (!isComputer || !versusComputer) {
                currentPlayer.allCoordinates.addAll(ship.getCoordinates());
            }
            else {
                computerPlayer.allCoordinates.addAll(ship.getCoordinates());
            }

            //System.out.println("This is currentplayer.allCoordinates inside the if statement as above\n " + currentPlayer.allCoordinates);
        }
        else {
            ship.getCoordinates().clear();
            isShipFilled = false;
        }

        above = true;
        below = true;
        left = true;
        right = true;
        countShipSize = 0;
        ship.isHorizontal = false;
        ship.isVertical = false;
//        isShipFilled = false;
    }
}
