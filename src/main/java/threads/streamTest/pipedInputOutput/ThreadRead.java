package threads.streamTest.pipedInputOutput;

import java.io.IOException;
import java.io.PipedInputStream;

public class ThreadRead extends Thread{

	private PipedInputStream in;
	private ReadData data;
	
	public ThreadRead(PipedInputStream in, ReadData data) {
		this.data = data;
		this.in = in;
	}
	
	public void run() {
		try {
			data.readMethod(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
