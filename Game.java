
public class Game{
  public String welcomeMessage(String[] players){
    String s = "Hello to "+players[0]+" and "+players[1]+". Let us Begin. The rules are as follows:\n";
    s+="This is simply a polite conversation. Take turns speaking, and wait until the other is complete. "+players[1]+" will go first.\n";
    return s;
  }
  public String move(String command){
    return command;
  }
}
