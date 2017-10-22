package threads.streamTest.pipedReaderWriter;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * 
 * @author gaobo
 * 字符流
 *
 */
public class MainTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		WriteData writeData = new WriteData();
		ReadData readData = new ReadData();
		
		PipedReader pipedReader = new PipedReader();
		PipedWriter pipedWriter = new PipedWriter();
		
		pipedReader.connect(pipedWriter);
		
		ReadThread r = new ReadThread(readData, pipedReader);
		WriteThread w = new WriteThread(writeData, pipedWriter);
		r.start();
		
		Thread.sleep(2000);
		
		w.start();
		
	}
	
	
}
