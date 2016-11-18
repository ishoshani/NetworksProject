import java.net.*;
import java.io.*;

public class Room extends Thread{
  int portNumber;
  ServerSocket mySocket;
  int numClients;
  Socket[] clients;
  PrintWriter[] outs;
  BufferedReader[] ins;

  public Room(int portNumber){
    super("Room"+portNumber);
    int numClients=0;
    this.portNumber=portNumber;
    clients = new Socket[10];
    outs = new PrintWriter[10];
    ins = new BufferedReader[10];
  }
  public void addClient(Socket socket) throws IOException{
    try{
    clients[numClients]=socket;
    outs[numClients]=new PrintWriter(clients[numClients].getOutputStream(), true);
    ins[numClients]=new BufferedReader(new InputStreamReader(clients[numClients].getInputStream()));
  }catch(IOException e){
    System.err.println("error adding client"+ e);
  }
    numClients++;
  }
  public void run(){
    String inputLine, outputLine;
    try{
      while((inputLine =ins[numClients].readLine()) != null){
        System.out.println("inputLine");
      }
    }catch(IOException e){
      System.err.println("error reading inputLine"+ e);
    }
  }

}
