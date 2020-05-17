package mergejoin;

import java.io.IOException;

public class Client {

	public static void main(String[] args) throws IOException {
		MergeJoin mergeJoin = new MergeJoin();
		mergeJoin.readFiles();
	}
}
