import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;

	BattleShipGame clientGame = new BattleShipGame();
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call){
	
		callback = call;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			 
			try {
				BattleShipGame gameFromServer = (BattleShipGame) in.readObject();
				System.out.println("INSIDE CLIENT.JAVA GAMEFROMSERVER ALL COORDINATES " + gameFromServer.currentPlayer.allCoordinates+"\n\n");
				System.out.println("gameFromServer's firstConnect boolean: " + gameFromServer.firstConnect);
				System.out.println("gameFromServer's otherPlayerConnected boolean: " + gameFromServer.otherPlayerConnected);
				clientGame.currentPlayer.currPlayerTurn = gameFromServer.currentPlayer.currPlayerTurn;
				clientGame.opponent = gameFromServer.opponent;
				clientGame.firstConnect = gameFromServer.firstConnect;
				clientGame.otherPlayerConnected = gameFromServer.otherPlayerConnected;
				clientGame.currentPlayer.clientNumber = gameFromServer.currentPlayer.clientNumber;
				System.out.println("clientGame's firstConnect boolean inside client.java: " + clientGame.firstConnect);

				callback.accept(clientGame);
			}
			catch(Exception e) {}
		}
    }
	
	public void send(BattleShipGame data) {
		
		try {
			out.reset();
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
