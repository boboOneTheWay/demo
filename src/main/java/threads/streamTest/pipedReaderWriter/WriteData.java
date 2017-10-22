package threads.streamTest.pipedReaderWriter;

import java.io.IOException;
import java.io.PipedWriter;

public class WriteData {

	public void writeMethod(PipedWriter out) throws IOException {
		System.out.println("write:");
		for(int i = 0; i<300; i++) {
			String outData = "" + (i+1);
			out.write(outData);
			System.out.println(outData);
		}
		
		out.close();
	}
}
