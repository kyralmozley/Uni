package uk.ac.cam.km687.fjava.tick3;

public class SafeMessageQueue<T> implements MessageQueue<T> {

    private static class Link<L> {
	      L val;
	      Link<L> next;
	      Link(L val) {
	          this.val = val;
	          this.next = null;
	      }
    }

    private Link<T> first = null;
    private Link<T> last = null;

    public synchronized void put(T val) {
  	//given a new "val", create a new Link<T>
  	//      element to contain it and update "first" and
  	//      "last" as appropriate
        Link<T> newLink = new Link<T>(val);
        if (first == null) {
            first = newLink;
            last = newLink;
        } else {
            last.next = newLink;
            last = newLink;
        }

        this.notify();
    }

    public synchronized T take() {

      	while(first == null) { //use a loop to block thread until data is available
      	    try {
      		      this.wait();
      	    } catch(InterruptedException ie) {
        		// Ignored exception
        		// TODO: what causes this exception to be thrown? and what should
        		//       you do with it ideally?

      	    }
      	}
        //TODO: retrieve "val" from "first", update "first" to refer
      	//      to next element in list (if any). Return "val"
        T val = first.val;
        first = first.next;
        if (first == null) {
            last = null;
        }

    	  return val;
    }
}
