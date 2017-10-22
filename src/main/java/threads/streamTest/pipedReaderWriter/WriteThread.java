package threads.streamTest.pipedReaderWriter;

import java.io.IOException;
import java.io.PipedWriter;

public class WriteThread extends Thread{

	private WriteData writeData;
	private PipedWriter write;
	
	public WriteThread(WriteData writeData, PipedWriter write) {
		this.writeData = writeData;
		this.write = write;
	}
	
	public void run() {
		try {
			writeData.writeMethod(write);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
