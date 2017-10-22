package threads.streamTest.pipedInputOutput;

import java.io.IOException;
import java.io.PipedOutputStream;

public class WriteData {

	public void writeMethod(PipedOutputStream out) throws IOException {
		System.out.println("write :");
		for(int i = 0; i< 300; i++) {
			String outData = "" + (i +1);
			try {
				out.write(outData.getBytes());
				System.out.println(outData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		out.close();
	}
}
