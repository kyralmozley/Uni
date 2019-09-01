package uk.ac.cam.km687.fjava.tick2;
//TODO: import required classes
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.MalformedURLException;
import java.net.URL;

class TestMessageReadWrite {

    static boolean writeMessage(String message, String filename) {
    	//TODO: Create an instance of "TestMessage" with "text" set
    	//      to "message" and serialise it into a file called "filename".
    	//      Return "true" if write was successful; "false" otherwise.
      try {
        TestMessage testmessage = new TestMessage();
        testmessage.setMessage(message);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(testmessage);
        out.close();
        return true;
      } catch (IOException e) {
        return false;
      }
    	//return false;
    }

    static String readMessage(String location) {
    	//TODO: If "location" begins with "http://" or "https://" then
    	// attempt to download and deserialise an instance of
    	// TestMessage; you should use the java.net.URL and
    	// java.net.URLConnection classes.  If "location" does not
    	// begin with "http://" or "https://" attempt to deserialise
    	// an instance of TestMessage by assuming that "location" is
    	// the name of a file in the filesystem.
    	//
    	// If deserialisation is successful, return a reference to the
    	// field "text" in the deserialised object. In case of error,
    	// return "null".
      try {
        if(location.startsWith("http://") || location.startsWith("https://")) {
          //using net
          ObjectInputStream in = new ObjectInputStream((new URL(location)).openConnection().getInputStream());
          TestMessage testmessage = (TestMessage) in.readObject();
          in.close();
          return testmessage.getMessage();
        } else {
          //assume its a file
          InputStream file = new FileInputStream(location);
          TestMessage testmessage = (TestMessage) new ObjectInputStream(file).readObject();
          file.close();
          return testmessage.getMessage();
        }
      } catch (MalformedURLException e) {
        return null;
      } catch (ClassNotFoundException e) {
        return null;
      } catch (IOException e) {
        return null;
      }
    	//return null;
    }

    public static void main(String args[]) {
    	//TODO: Implement suitable code to help you test your implementation
    	//      of "readMessage" and "writeMessage".

      String test = readMessage("https://www.cl.cam.ac.uk/teaching/current/FJava/testmessage-km687.jobj");
		  System.out.println(test);
    }
}
