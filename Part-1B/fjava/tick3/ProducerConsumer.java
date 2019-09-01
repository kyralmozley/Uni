package uk.ac.cam.km687.fjava.tick3;

class ProducerConsumer {
    private MessageQueue<Character> m = new UnsafeMessageQueue<Character>();

    private class Producer implements Runnable {
	char[] cl = "Department of Computer Science and Technology".toCharArray();
	public void run() {
	    for(char c : cl) {
		m.put(c);
		try {
		    Thread.sleep(500);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private class Consumer implements Runnable {
	public void run() {
	    while (true) {
		System.out.print(m.take());
		System.out.flush();
	    }
	}
    }

    void execute() {
	new Thread(new Producer()).start();
	new Thread(new Consumer()).start();
    }

    public static void main(String[] args) {
	new ProducerConsumer().execute();
    }
}