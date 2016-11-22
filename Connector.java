import java.io.*;
import java.net.*;
import java.util.Hashtable;

public class Connector extends Thread{
  Socket client;
  String username;
  ServerState server;
  Hashtable roomList;
  CommandProtocol commandProtocol;
  public Connector(Socket socket, Integer userNumber, Hashtable roomList, ServerState server){
    super("Connection"+socket);
    username = "newUser"+userNumber;
    this.server = server;
    this.roomList=roomList;
    client = socket;
  }

  public void run(){
    try(
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(client.getInputStream());
        ){
          ChatPacket inputPacket, outputPacket;
          while ((inputPacket = ((ChatPacket)in.readObject())) != null) {
            outputPacket=process(inputPacket);
            out.writeObject(outputPacket);
            }
        }catch(IOException e){
          System.err.println(e);
        }catch(ClassNotFoundException e){
          System.err.println(e);
        }
    }

  public ChatPacket process(ChatPacket input){
    commandProtocol= new CommandProtocol(server,username);
    ChatPacket c;
    if(input.packetType.equals("Start")){
      c = new ChatPacket("Message", "Welcome to IRC!");
      return c;
    }
    if(input.packetType.equals("Command")){
      String command = input.packetMessage;
      return commandProtocol.process(command);
    }
      c = new ChatPacket("Message", "sorry didnt get that");
      return c;
  }

}
