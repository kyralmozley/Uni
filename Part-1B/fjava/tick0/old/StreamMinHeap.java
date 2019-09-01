/**
*
* StreamMinHeap.java - Kyra Mozley 2018
*
**/

package uk.ac.cam.km687.fjava.tick0;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public class StreamMinHeap {

    private int nodeCount = 0;
    private Stream[] streams;

    public StreamMinHeap(int size){
        streams = new StreamBlock[size];
    }

    public int size(){
        return nodeCount;
    }

    public boolean isEmpty(){
        return nodeCount == 0;
    }

    private void swapStream(int a, int b){
        StreamBlock temp = streams[a];
        streams[a] = streams[b];
        streams[b] = temp;
    }

    private void heapifyUp(int index){
        if (index > 0){
            //  children at 2i+1 and 2i+2
            int parentIndex = (index-1)/2;

            if (streams[parentIndex].getHead() > streams[index].getHead()){
                swapStream(index, parentIndex);
                heapifyUp(parentIndex);
            }
        }
    }

    public void insert(StreamBlock streamBlock){
        if (nodeCount == streams.length){
            throw new RuntimeException("Heap is full");
        }

        streams[nodeCount] = streamBlock;
        heapifyUp(nodeCount);
        nodeCount++;
    }

    private void heapify(int index){
        int leftChildIndex = 2*index + 1;
        int rightChildIndex = 2*index + 2;

        if (leftChildIndex >= nodeCount && rightChildIndex >= nodeCount){
            return;
        }

        int smallestChildIndex = (streams[leftChildIndex].getHead() <= streams[rightChildIndex].getHead()) ? leftChildIndex : rightChildIndex;

        if (streams[index].getHead() > streams[smallestChildIndex].getHead()){
            swapStream(index, smallestChildIndex);
            //swapInt(dataReadCount, index, smallestChildIndex);
            // Potentially misplaced element is now at index = smallestChildIndex
            heapify(smallestChildIndex);
        }
    }

    public StreamBlock removeMin(){
        if (isEmpty()){
            throw new RuntimeException("Heap is currently empty");
        }

        StreamBlock min = streams[0];
        streams[0] = streams[nodeCount-1];


        if (--nodeCount > 0){
            heapify(0);
        }

        return min;
    }

}
