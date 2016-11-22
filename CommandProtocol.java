import java.io.*;
public class CommandProtocol{
  ServerState server;
  String username;
  public CommandProtocol(ServerState serve, String user){
    server=serve;
    username=user;
  }
  public ChatPacket process(String command){
    ChatPacket c;
    if(command.matches("username (.*)")){
      username=command.split(" ")[1];
      c = new ChatPacket("Message", "Username is now "+username);
      return c;
    }if(command.equals("hello")){
      c = new ChatPacket("Message", "hello "+username);
      return c;
    }if(command.matches("status")){
      synchronized(server){
        c = new ChatPacket("Message", "there are currently"+server.currentUsers);
      }
      return c;
    }
    return new ChatPacket("Message", "command Not understood");
  }
}
