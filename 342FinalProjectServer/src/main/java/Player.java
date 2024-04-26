import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    static final long serialVersionUID = 42L;
    ArrayList<String> allCoordinates = new ArrayList<>();
    ArrayList<String> sentCoordinate = new ArrayList<>();
    int clientNumber = 1;

    Ship twoShip = new Ship(2);
    Ship threeShip = new Ship(3);
    Ship threeShip2 = new Ship(3);
    Ship fourShip = new Ship(4);
    Ship fiveShip = new Ship(5);
    int playerHealth = 17;
    Boolean currPlayerTurn = false;

}
