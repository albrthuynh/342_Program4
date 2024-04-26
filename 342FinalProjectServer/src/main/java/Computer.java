import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

public class Computer implements Serializable {
    static final long serialVersionUID = 42L;
    ArrayList<String> allCoordinates = new ArrayList<>();
    Ship twoShip = new Ship(2);
    Ship threeShip = new Ship(3);
    Ship threeShip2 = new Ship(3);
    Ship fourShip = new Ship(4);
    Ship fiveShip = new Ship(5);
    int computerHealth = 17;

    public String computerHit(){
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        int columns = 10;

        Random rand = new Random();

        return "insert coordinate here";
    }
}