public class Main {
    public static void main(String[] args) {
//        BattleShipGame game = new BattleShipGame();
//
//        game.currentPlayer.allCoordinates.add("D10");
//        //game.currentPlayer.allCoordinates.add("E8");
//        //game.currentPlayer.allCoordinates.add("D5");
//        //game.currentPlayer.allCoordinates.add("I4");
//
//        game.currentPlayer.fiveShip.isVertical = true;
//
//        game.checkFirstClick("H10", game.currentPlayer.fiveShip);
////        System.out.println("checking right boolean: " + game.currentPlayer.right); //false
////        System.out.println("checking left boolean: " + game.currentPlayer.left); //true
//
//        System.out.println("checking above boolean: " + game.currentPlayer.above); //false
//        System.out.println("checking below boolean: " + game.currentPlayer.below); //false

        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        int rangeForNums = 10; //range for choosing random numbers for coord (upperBound - lowerBound)
        int rangeForLetters = 10; //range for choosing for random letters(index in array)
        Computer computerPlayer = new Computer();
        String randCoord = "";

        for(int i = 0; i < 3; i++){
            int randomNum = (int)(Math.random() * rangeForNums) + 1;
            int randomLetter = (int)(Math.random() * rangeForLetters);
            randCoord = letters[randomLetter] + randomNum;
            System.out.println("random coord: " + randCoord);
        }

        computerPlayer.twoShip.addCoordinates(randCoord);
        System.out.println("two ship coords: " +  computerPlayer.twoShip.getCoordinates());
    }
}
