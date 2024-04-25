import java.util.*;

import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import static javafx.scene.paint.Color.rgb;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.image.ImageView;
import javafx.stage.WindowEvent;


import static javafx.scene.paint.Color.rgb;


public class Ship {
    Boolean isHorizontal;
    Boolean isVertical;
    int shipLength;
    int hitCount = 0;
    Boolean shipAlreadySunk = false;

    private ArrayList<String> coordinates = new ArrayList<>();

    Ship(int shipLength){
        this.shipLength = shipLength;
        isHorizontal = false;
        isVertical = false;
    }

    public void addCoordinates (String coords){
        coordinates.add(coords);
    }

    public ArrayList<String> getCoordinates(){
        return coordinates;
    }
}
