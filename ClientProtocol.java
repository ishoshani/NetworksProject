import java.io.*;
import java.net.*;

public class ClientProtocol{
  public static void processProcedure(ChatPacket input){
    if(input.packetType.equals("message")){
      System.out.println(input);
      return;
    }
    if(input.packetType.equals("NoLobby")){
      System.out.println("NoLobby : waiting for lobby with name of"+input.packetMessage);
      ClientContainer.state="Waiting";
      return;
    }
    System.out.println("cound not process packet "+ input);
    return;
  }
}
