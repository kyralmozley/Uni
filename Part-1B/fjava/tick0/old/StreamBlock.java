/**
*
* StreamBlock.java - Kyra Mozley 2018
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
Create
*/

public class StreamBlock {

    private int head;
    private RandomAccessFile file;
    private DataInputStream data;
    private int count = 0;
    private int numberOfInts;
    private byte[] byteArray;
    private int[] intArray;
    private byte[] byteTemp = new byte[4];
    private int intPointer = 0;

    //Instantiate StreamBlock
    public StreamBlock(int startIntOfBlock, int numOfIntsInBlock, String fileName) throws IOException, EOFException{
        file = new RandomAccessFile(fileName, "r");
        data = new DataInputStream(new BufferedInputStream(new FileInputStream(file.getFD())));
        numberOfInts = numOfIntsInBlock;

        data.skipBytes(startIntOfBlock*4);

        advance();
    }

    //Getter for head of stream without removing it
    public int getHead(){
        return this.head;
    }

    //Get the value of head and increase pointer value
    private int popIntFromBuffer(){
        int val = intArray[intPointer];
        intPointer++;
        return val;
    }


    public int pop() throws EOFException, IOException, LastIntException{
        int val = getHead();

        if (count == numberOfInts){
            throw new LastIntException(val);
        } else {
            advance();
            return val;
        }

    }

    public void advance() throws IOException, EOFException{
        if (count >= numberOfInts){
            file.close();
            data.close();
            throw new EOFException();
        } else {
            if (intArray == null || intPointer >= intArray.length){
                byteArray = new byte[numberOfInts/4];
                int test = data.read(byteArray, 0, numberOfInts/4);
                if (test == -1){
                    throw new EOFException();
                }
                intPointer = 0;
                intArray = ExternalSort.byteArrayToIntArray(byteArray);
            }

            head = popIntFromBuffer();
            count++;

        }

    }

}
