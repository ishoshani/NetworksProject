
public class Game{
  Room myRoom;
  public Game(Room myRoom){
    this.myRoom = myRoom;
  }
  public String welcomeMessage(){
    String s = "Hello to "+myRoom.players[0]+" and "+myRoom.players[1]+". Let us Begin. The rules are as follows:\n";
    s+="This is simply a polite conversation. Take turns speaking, and wait until the other is complete. "+myRoom.players[1]+" will go first.\n";
    return s;
  }
  public String move(String command){
    if(command.equals("goodbye")){
      myRoom.state=Room.DONE;
      return "";
    }
    return command;
  }
  public String finish(){
    String s = "thank you both for playing. "+myRoom.players[myRoom.turn]+ " said goodbye";
    return s;
  }
}
