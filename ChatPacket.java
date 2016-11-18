
import java.io.*;

public class ChatPacket implements Serializable{
  String packetType;
  String packetMessage;
  public ChatPacket(String type, String message){
    packetType=type;
    packetMessage=message;
  }
  public ChatPacket(String type){
    packetType=type;
    packetMessage=null;
  }
  public String toString(){
    return packetType+": "+packetMessage;
  }
}
