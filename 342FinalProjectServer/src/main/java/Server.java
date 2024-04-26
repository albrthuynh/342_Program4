import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;

	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				
				count++;
			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			BattleShipGame serverGame;

			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;
				serverGame = new BattleShipGame();
			}
			
			public void updateClients(BattleShipGame message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}

			public void debugPrintClients(){
				for(int i = 0; i < clients.size(); i++){
					ClientThread t = clients.get(i);
					//System.out.println("index at " + i + " is this client: " + clients.get(i).toString());
				}
			}
			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);	
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				//updateClients("new client on server: client #"+count);
					
				 while (true) {
					    try {

					    	BattleShipGame data = (BattleShipGame) in.readObject();
							System.out.println("data first connect boolean: " + data.firstConnect+"\n\n");
							System.out.println("position user chose: " + data.currentPlayer.sentCoordinate+"\n\n");


							////////////
//							synchronized (data) {

								////////////

								System.out.println("DATA ALLCOORDINATES FOR CURRENT PLAYER: " + data.currentPlayer.allCoordinates + "\n\n");
								System.out.println("data's first connect boolean: #1" + data.firstConnect);
								System.out.println("This is data.otherPlayerConnected: " + data.otherPlayerConnected);
								System.out.println("This is data.firstConnect: " + data.firstConnect);

//							if(clients.size() ==1){
//								clients.get(0).serverGame = data;
//								//clients.get(0).out.writeObject(data);
//							}
								if (clients.size() == 2 && !data.otherPlayerConnected) {
									System.out.println("data's first connect boolean: #2" + data.firstConnect);
									debugPrintClients();
									System.out.println("Going inside the count == 2 and !data.otherPlayerConnected");
									clients.get(0).serverGame.firstConnect = true;
									clients.get(0).serverGame.otherPlayerConnected = true;
									clients.get(0).serverGame.currentPlayer.currPlayerTurn = true; //the first client attacks first
									clients.get(0).serverGame.opponent = data.currentPlayer;
									clients.get(0).serverGame.currentPlayer.clientNumber = 1; //differentiating between player 1 and 2

									//clients.get(0).serverGame.opponent.allCoordinates = data.currentPlayer.allCoordinates;

									clients.get(0).out.reset();
									clients.get(0).out.writeObject(clients.get(0).serverGame);

									clients.get(1).serverGame.currentPlayer.allCoordinates = data.currentPlayer.allCoordinates;

									clients.get(1).serverGame.opponent = clients.get(0).serverGame.currentPlayer;
									//data.opponent = clients.get(0).serverGame.currentPlayer;
									clients.get(1).serverGame.firstConnect = true;
									//data.firstConnect = true;
									clients.get(1).serverGame.currentPlayer.currPlayerTurn = false;
									//data.currentPlayer.currPlayerTurn = false;
									clients.get(1).serverGame.otherPlayerConnected = true;
									//data.otherPlayerConnected = true;

									clients.get(1).serverGame.currentPlayer.clientNumber = 2;

									clients.get(1).out.reset();
									clients.get(1).out.writeObject(clients.get(1).serverGame);
								}
								//rest of game
								else if (clients.size() == 2 && !data.firstConnect) {

									System.out.println("ELSE IF ALL COORDINATES FOR DATA" + data.currentPlayer.allCoordinates);
									System.out.println("data's first connect boolean before change: #3" + data.firstConnect);
									System.out.println("Going inside the else if statement");
									clients.get(0).serverGame.firstConnect = false;
									clients.get(1).serverGame.firstConnect = false;
									System.out.println("data's first connect boolean after change: #4" + data.firstConnect);
									System.out.println("client's 0 first connect boolean: #5" + clients.get(0).serverGame.firstConnect);
									System.out.println("client's 1 first connect boolean: #6" + clients.get(1).serverGame.firstConnect);

									//if it is now the second clients turn, update its opponent and send to that
									// client to indicate it is now their turn
									//the if statement condition indicates that it was previously that clients turn
									if (clients.get(0).serverGame.currentPlayer.currPlayerTurn) {
										System.out.println("inside the clients.get(0) if statement");
										clients.get(0).serverGame.currentPlayer.currPlayerTurn = false;
										clients.get(1).serverGame.currentPlayer.currPlayerTurn = true;

										// change to 0 if something wonky happens
										clients.get(0).serverGame.opponent = data.opponent;
										clients.get(1).serverGame.opponent = data.currentPlayer;
										//
										System.out.println("this is clients.get(1).opponentsHits\n" + clients.get(1).serverGame.opponent.sentCoordinate);
										clients.get(1).out.reset();
										clients.get(1).out.writeObject(clients.get(1).serverGame);

										clients.get(0).out.reset();
										clients.get(0).out.writeObject(clients.get(0).serverGame);
									}

									//if it is now the first clients turn, update its opponent and send to that
									// client to indicate it is now their turn
									else {
										clients.get(0).serverGame.firstConnect = false;
										clients.get(1).serverGame.firstConnect = false;
										System.out.println("inside the clinets.get(1) else statement");
										clients.get(1).serverGame.currentPlayer.currPlayerTurn = false;
										clients.get(0).serverGame.currentPlayer.currPlayerTurn = true;

										// test this
										clients.get(1).serverGame.opponent = data.opponent;
										clients.get(0).serverGame.opponent = data.currentPlayer;
										//

										clients.get(0).out.reset();
										clients.get(0).out.writeObject(clients.get(0).serverGame);
										clients.get(1).out.reset();
										clients.get(1).out.writeObject(clients.get(1).serverGame);
									}
								}
//							}

					    	//updateClients("client #"+count+" said: " + data);
					    	
					    	}
					    catch (Exception e) {
							e.printStackTrace();
					    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
					    	//updateClients("Client #"+count+" has left the server!");
					    	clients.remove(this);
					    	break;
					    }
					}
				}//end of run

			public void send(BattleShipGame data) {
				try {
					out.writeObject(data);
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}//end of client thread

}


	
	

	
