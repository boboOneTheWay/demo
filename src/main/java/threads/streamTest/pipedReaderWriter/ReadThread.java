package threads.streamTest.pipedReaderWriter;

import java.io.IOException;
import java.io.PipedReader;

public class ReadThread extends Thread{

	private ReadData readData;
	private PipedReader read;
	
	public ReadThread(ReadData readData, PipedReader read) {
		this.readData = readData;
		this.read = read;
	}
	
	public void run() {
		try {
			readData.readWrite(read);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
