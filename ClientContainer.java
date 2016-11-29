
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
      echoSocket.setSoTimeout(2000);
      String userInput;
      out.writeObject(new ChatPacket("Start"));
      out.flush();
      ChatPacket welcome = (ChatPacket)in.readObject();
      System.out.println(welcome);
      while(!state.equals("Exit")){
        if(state.equals("Menu")){
          if(!stdin.ready()){
            out.writeObject(new ChatPacket("KeepAlive"));
            ChatPacket KA=(ChatPacket)in.readObject();
            if(KA==null){
              System.out.println("connection Lost, exiting Client");
              state="Exit";
            }
            ClientProtocol.processProcedure(KA);
          }
          if((userInput = stdin.readLine()) != null){
            out.writeObject(new ChatPacket(state, userInput));
            ChatPacket message= (ChatPacket)in.readObject();
            ClientProtocol.processProcedure(message);
          }
        }
        if(state.equals("WaitingForLobby")){
          System.out.println("got into Waiting Section");
          out.writeObject(new ChatPacket(state));
          out.flush();
          ChatPacket message= (ChatPacket)in.readObject();
          ClientProtocol.processProcedure(message);
        }
        if(state.equals("BeginPlay")){
          out.writeObject(new ChatPacket(state, "ready"));
          out.flush();
          ChatPacket message= (ChatPacket)in.readObject();
          ClientProtocol.processProcedure(message);
        }
        if(state.equals("Playing")){
          if(!stdin.ready()){
            out.writeObject(new ChatPacket("KeepAlive","", gameID));
            out.flush();
            ChatPacket KA=(ChatPacket)in.readObject();
            if(KA==null){
              System.out.println("connection Lost, exiting Client");
              state="Exit";
            }
            ClientProtocol.processProcedure(KA);
          }
          if((userInput = stdin.readLine()) != null){
            out.writeObject(new ChatPacket(state, userInput, gameID));
            ChatPacket message= (ChatPacket)in.readObject();
            ClientProtocol.processProcedure(message);
          }
        }
        if(state.equals("WaitingForTurn")){
          if(stdin.ready()){
            String trash = stdin.readLine();
            System.out.println("wait your turn, threw away "+ trash);
          }
          out.writeObject(new ChatPacket(state));
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
