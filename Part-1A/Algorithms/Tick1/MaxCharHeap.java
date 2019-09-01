package uk.ac.cam.km687.Algorithms.Tick1;
import uk.ac.cam.rkh23.Algorithms.Tick1.EmptyHeapException;
import uk.ac.cam.rkh23.Algorithms.Tick1.MaxCharHeapInterface;

public class MaxCharHeap implements MaxCharHeapInterface {

  private char heapName;
  private char[] heapData;
  private int length;

//FINE
  public MaxCharHeap(String s) {
    // make lower case
    //change from sstring to char
    String str = s.toLowerCase();
    //heapData = str.toCharArray();
    heapData = new char[31];
    length = 0;
    for(char i:str.toCharArray()) {
      insert(i);
    }
  }

  public char getMax() throws EmptyHeapException {
    if (length==0) throw new EmptyHeapException();
    swap(0, length-1);
    length--;
    heapDown(heapData, length-1);
    return heapData[length];
  }

  private void heapDown(char[] str, int end) {
    int parent = 0;
    while(parent < end) {
      int leftChild = 2*parent+1;
      int rightChild = leftChild+1;
      if (leftChild==end) {
        if (heapData[parent] < heapData[leftChild]) {
          swap(parent, leftChild);
          parent = leftChild;
        }
        else{break;}
      }
      else if (rightChild<= end) {
        if (heapData[parent] < heapData[leftChild] && heapData[rightChild] <= heapData[leftChild]) {
          swap(parent, leftChild);
          parent = leftChild;
        }
        else if(heapData[parent] < heapData[rightChild] && heapData[leftChild] <= heapData[rightChild]) {
          swap(parent, rightChild);
          parent = rightChild;
        }
        else {break;}
      }
      else {break;}
    }
  }

//FINE
  private void swap(int val1, int val2) {
    char tmp = heapData[val1];
    heapData[val1] = heapData[val2];
    heapData[val2] = tmp;
  }

  // Insert a new value into the heap
  public void insert(char i) {
    if (length==heapData.length) increaseLength();
    heapData[length]=Character.toLowerCase(i);
    length++;
    int charlocation = length-1;
    while(heapData[charlocation]>heapData[(charlocation-1)/2]) {
      swap((charlocation-1)/2, charlocation);
      charlocation = (charlocation-1)/2;
      if (charlocation == 0)
      return;
    }

  }

//FINE
  public void increaseLength() {
    char[] heap2 = new char[2*heapData.length-1];
    for(int i = 0; i<heapData.length; i++) {
      heap2[i] = heapData[i];
    }
    heapData = heap2;
  }

//FINE
  // Get the number of items in the heap
  public int getLength() {
    return length;
  }


  public static void main(String[] args) {
  //run it
		MaxCharHeap h = new MaxCharHeap(args[0]);

	}

}
