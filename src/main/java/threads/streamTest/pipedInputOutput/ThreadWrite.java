package threads.streamTest.pipedInputOutput;

import java.io.IOException;
import java.io.PipedOutputStream;

public class ThreadWrite extends Thread{

	private PipedOutputStream out;
	private WriteData data;
	
	public ThreadWrite(WriteData data, PipedOutputStream out) {
		this.out = out;
		this.data = data;
	}
	
	public void run() {
		try {
			data.writeMethod(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
