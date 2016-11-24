import java.net.*;
import java.io.*;
import java.util.Hashtable;

public class ServerContainer{
  public static ServerState servState = new ServerState();
  public static Hashtable<Integer,Room> roomList = new Hashtable<Integer,Room>();
  public static Hashtable<String, Room> privateRooms = new Hashtable<String,Room>();


  public static void main(String[] args) throws IOException{
    if (args.length!=1){
            System.err.println("Usage: java ServerContainer <port number>");
            System.exit(1);
          }
    int portNumber = Integer.parseInt(args[0]);
    startServer(portNumber);
  }

  public static void startServer(int portNumber){
      int userNumber=0;
      boolean listening = true;
    try(
      ServerSocket serverSocket = new ServerSocket(portNumber);
        ){
          while(listening){
            Socket newClient=serverSocket.accept();
            userNumber++;
            servState.currentUsers++;
            new Connector(newClient,userNumber).start();

          }
        }catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
      }
  }
