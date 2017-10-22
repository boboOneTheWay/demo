package threads.streamTest.pipedInputOutput;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
/**
 * 
 * @author gaobo
 * 线程间通信，字节流
 * out.connect(in) 或 in.connect(out); 建立通信
 *
 */
public class MainTest {

	public static void main(String[] args) throws InterruptedException, IOException {
		WriteData writeData = new WriteData();
		ReadData readData = new ReadData();
		PipedInputStream in = new PipedInputStream();
		PipedOutputStream out = new PipedOutputStream();
		
		out.connect(in);
		
		ThreadRead read = new ThreadRead(in, readData);
		read.start();
		Thread.sleep(2000);
		
		ThreadWrite write = new ThreadWrite(writeData,out);
		write.start();
	}
}
