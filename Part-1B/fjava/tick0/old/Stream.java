/**
*
* Stream.java - Kyra Mozley 2018
*
**/

package uk.ac.cam.km687.fjava.tick0;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
Create the stream structure, and its getters
*/

public class Stream {

    private int head;
    private RandomAccessFile file;
    private DataInputStream data;
    private int count = 0;
    private int numberOfInts;
    private byte[] byteArray;
    private int[] intArray;
    private byte[] byteTemp = new byte[4];
    private int intPointer = 0;

    //Instantiate Stream
    public Stream(int startInt, int streamNumberOfInts, String fileName) throws IOException, EOFException{
        file = new RandomAccessFile(fileName, "r");
        data = new DataInputStream(new BufferedInputStream(new FileInputStream(file.getFD())));
        numberOfInts = streamNumberOfInts;

        data.skipBytes(startInt*4);

        next();
    }

    //Getter for head of stream without removing it
    public int getHead(){
        return this.head;
    }

    public int pop() throws EOFException, IOException, LastIntException{
        int val = getHead();

        if (count == numberOfInts){ //end
            throw new LastIntException(val);
        } else {
            next();
            return val;
        }

    }

    public void next() throws IOException, EOFException{
        if (count >= numberOfInts){
          //END OF FILE
            file.close();
            data.close();
            throw new EOFException();
        } else {

            if (intArray == null || intPointer >= intArray.length){
              
                byteArray = new byte[numberOfInts/4];
                if (data.read(byteArray, 0, numberOfInts/4) == -1) { //read returns number of bytes, -1 if EOF
                    throw new EOFException();
                }
                intPointer = 0;
                intArray = ExternalSort.byteArrayToIntArray(byteArray);
            }

            head = popIntFromBuffer();
            count++;

        }

    }

    private int popIntFromBuffer(){
        int val = intArray[intPointer];
        intPointer++;
        return val;
    }

}
