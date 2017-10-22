package threads.streamTest.pipedInputOutput;

import java.io.IOException;
import java.io.PipedInputStream;

public class ReadData {

	public void readMethod(PipedInputStream input) throws IOException {
		System.out.println("read :");
		byte[] byteArray = new byte[20];
		//没有数据写入
		int read = input.read(byteArray);
		while(read != -1) {
			String newData = new String(byteArray, 0, read);
			System.out.println(newData);
			read = input.read(byteArray);
		}
		input.close();
	}
}
