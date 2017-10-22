package threads.streamTest.pipedReaderWriter;

import java.io.IOException;
import java.io.PipedReader;

public class ReadData {

	public void readWrite(PipedReader in) throws IOException {
		System.out.println("read");
		char[] byteArray = new char[20];
		int read = in.read(byteArray);
		while(read != -1) {
			String newData = new String(byteArray, 0, read);
			System.out.println(newData);
			read = in.read(byteArray);
		}
		
		in.close();
		
	}
}
