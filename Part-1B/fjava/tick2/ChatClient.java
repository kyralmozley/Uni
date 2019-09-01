package uk.ac.cam.km687.fjava.tick2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.ac.cam.cl.fjava.messages.ChangeNickMessage;
import uk.ac.cam.cl.fjava.messages.ChatMessage;
import uk.ac.cam.cl.fjava.messages.DynamicObjectInputStream;
import uk.ac.cam.cl.fjava.messages.Execute;
import uk.ac.cam.cl.fjava.messages.Message;
import uk.ac.cam.cl.fjava.messages.NewMessageType;
import uk.ac.cam.cl.fjava.messages.RelayMessage;
import uk.ac.cam.cl.fjava.messages.StatusMessage;

import uk.ac.cam.km687.fjava.tick2.FurtherJavaPreamble;



@FurtherJavaPreamble(
		author = "Kyra Mozley",
		crsid = "km687",
		date = "2nd November 2018",
		summary = "ChatClient",
    ticker = FurtherJavaPreamble.Ticker.B
		)

class ChatClient {


  public static void main(String[] args) throws IOException, InterruptedException {
    String server = null;
    int port = 0;
    final Socket s;

    final SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
    final Date dateTime = new Date();

    if(args.length != 2) {
      System.err.println("This application requires two arguments: <machine> <port>");
      return;
    }
    server = args[0];

    try {
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("This application requires two arguments: <machine> <port>");
      return;
    } try {
      s = new Socket(server,port);
    } catch (ConnectException e) {
      System.err.println("Cannot connect to " + server + " on port " +  port);
      return;
    }
    System.out.println(date.format(dateTime) + " [Client] Connected to " + server + " on port " + port + ".");

    DynamicObjectInputStream input = new DynamicObjectInputStream(s.getInputStream());
    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

    Thread output = new Thread() {
      @Override
      public void run() {
        try {
          while(true) {
            Object messageIn = input.readObject();
            if(messageIn instanceof StatusMessage) {
              StatusMessage message = (StatusMessage) messageIn;
              System.out.println(date.format(message.getCreationTime()) + " [Server] " + message.getMessage());

            } else if (messageIn instanceof RelayMessage) {

              RelayMessage message = (RelayMessage) messageIn;
              System.out.println(date.format(message.getCreationTime()) + " [" + message.getFrom() + "] " + message.getMessage());

            } else if (messageIn instanceof NewMessageType) {

              NewMessageType message = (NewMessageType) messageIn;
              input.addClass(message.getName(), message.getClassData());
              System.out.println(date.format(message.getCreationTime()) + " [Client] NewMessageClass: field1(" + message.getName() + "), field2(" + message.getName() + ")");

            } else {
              //dont know the type of messageIn
              Class<?> someClass = messageIn.getClass();
              System.out.print(date.format(dateTime) + " [Client] " + someClass.getSimpleName()+ ": ");
              String fields = new String();
              for (Field i: someClass.getFields()) {
                i.setAccessible(true);
                fields = fields + i.getName() + "(" + i.get(messageIn) + "), ";
              }
              System.out.println(fields.substring(0, fields.length()-2));
              for(Method i: someClass.getMethods()) {
                if(i.getParameterTypes().length == 0 && i.isAnnotationPresent(Execute.class)) {
                  i.invoke(messageIn, (Object []) null);
                }
              }
            }
          }

        } catch (IOException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    };
    output.setDaemon(true);
    output.start();

    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    String userInput = "";

    while(!userInput.equals("\\quit")) {
      userInput = r.readLine();

      if(userInput.startsWith("\\")) { // backslash is an escape char, so have to use two
        String[] command = userInput.split(" ");
        if(command[0].equalsIgnoreCase("\\quit")) {
          System.out.println(date.format(dateTime) + " [Client] Connection Terminated.");
          return;
        }
        else if (command[0].equalsIgnoreCase("\\nick")) {
          if(command.length != 2) {
            System.out.println(date.format(dateTime) + " [Client] Incorrect command. \\nick [new nickname]");
            continue;
          }
          ChangeNickMessage message = new ChangeNickMessage(command[1]);
          out.writeObject(message);
          continue;
        } else {
          System.out.println(date.format(dateTime) + " [Client] Unknown command \"" + command[0].substring(1) + "\"");
          continue;
        }
      }
      out.writeObject(new ChatMessage(userInput));
    }
      /*char[] chars = userInput.toCharArray();
      byte[] bytes = new byte[chars.length];
      for(int i=0; i<chars.length; i++) {
        bytes[i] = (byte) chars[i];
      }
      out.write(bytes);
    }
    s.close();*/

  }
}
