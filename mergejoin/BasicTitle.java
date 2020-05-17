package mergejoin;

public class BasicTitle implements DataProcessing {
	
	private String line;
	private String tConst;
	private String primaryTitle;
	private final int T_CONST_INDEX = 0;
	private final int PRIMARY_TITLE_INDEX = 2;
	
	public void setLine(String line) {
		this.line = line;
	}

	public void setData() {
		splitData();
	}
	
	public void splitData() {
		String[] tokens = line.split("\t");
		setTConst(tokens[T_CONST_INDEX]);
		setPrimaryTitle(tokens[PRIMARY_TITLE_INDEX]);
	}
	
	private void setTConst(String token) {
		tConst = token;
	}
	
	public String getTConst() {
		return this.tConst;
	}
	
	private void setPrimaryTitle(String token) {
		primaryTitle = token;
	}
	
	public String getPrimaryTitle() {
		return this.primaryTitle;
	}
	
	public void printFields() {
		System.out.print("Basic title fields: \t");
		System.out.print("tconst = " + tConst + "\t");
		System.out.print("primaryTitle = " + primaryTitle + "\n");
	}
}
