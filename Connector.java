import java.io.*;
import java.net.*;

public class Connector extends Thread{
  Socket client;

  public Connector(Socket socket){
    super("Connection"+socket);
    client = socket;
  }

  public void run(){
    try(
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ){
          String inputLine, outputLine;
          while ((inputLine = in.readLine()) != null) {
            outputLine=process(inputLine);
            out.println(outputLine);
            }
        }catch(IOException e){
          System.err.println(e);
        }
    }

  public String process(String input){
    if(input == "hello"){
      return "hello";
    }else{
      return "sorry, didn't get that";
    }
  }

}
