
import java.io.*;
import java.net.*;

public class ClientContainer{
  static String state = "Menu";
  static Integer gameID = 0;
  public static void main(String[] args) {
    if(args.length != 2){
      System.err.println(
      "Usage: needs two arguments, java ClientContainer <hostname> <portnumber>"
      );
      System.exit(1);
    }
    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);
    try(
    Socket echoSocket = new Socket(hostName,portNumber);
    ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))
    ){
      String userInput;
      out.writeObject(new ChatPacket("Start"));
      out.flush();
      ChatPacket welcome = (ChatPacket)in.readObject();
      System.out.println(welcome);
      while(!state.equals("Exit")){
        while(state.equals("Menu")){
          if((userInput = stdin.readLine()) != null){
            out.writeObject(new ChatPacket(state, userInput));
            ChatPacket message= (ChatPacket)in.readObject();
            ClientProtocol.processProcedure(message);
          }
        }
        while(state.equals("Waiting")){
          System.out.println("got into Waiting Section");
          out.writeObject(new ChatPacket(state, "isAlive"));
          out.flush();
          ChatPacket message= (ChatPacket)in.readObject();
          ClientProtocol.processProcedure(message);
        }
        while(state.equals("BeginPlay")){
          out.writeObject(new ChatPacket(state, "ready"));
          out.flush();
          ChatPacket message= (ChatPacket)in.readObject();
          ClientProtocol.processProcedure(message);
        }
        while(state.equals("Playing")){
          if((userInput = stdin.readLine()) != null){
            out.writeObject(new ChatPacket(state, userInput, gameID));
            ChatPacket message= (ChatPacket)in.readObject();
            ClientProtocol.processProcedure(message);
          }
        }
        while(state.equals("WaitingForTurn")){
          System.out.println("WaitingForOtherPlayer");
          out.writeObject(new ChatPacket(state, "isAlive"));
          out.flush();
          ChatPacket message= (ChatPacket)in.readObject();
          ClientProtocol.processProcedure(message);
          }
        }
    }catch (UnknownHostException e) {
      System.err.println("Don't know about host " + hostName);
      System.exit(1);
    }
    catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to " +
      hostName);
      System.exit(1);
    }
    catch (ClassNotFoundException e){
      System.err.println("Unexpected type of Object" + e);
      System.exit(1);

    }

  }
}
