package mergejoin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MergeJoin {
	
	private BufferedReader basicTitleReader;
	private BufferedReader akaTitleReader;
	private BufferedWriter outputWriter;
	private String basicTitleLine;
	private String akaTitleLine;
	private BasicTitle basicTitle;
	private AkaTitle akaTitle;
	private final int BASIC = 0;
	private final int AKA = 1;
	private final int T_CONST_LT_TITLED_ID = 0;
	private final int T_CONST_GTE_TITLE_ID = 1;
	private boolean basicEOF;
	private boolean akaEOF;
	private boolean sameIds;
	private boolean lastLineAkaFile;
	private int differentIdsInARow;
	
	public MergeJoin() {
		basicEOF = false;
		akaEOF = false;
		sameIds = false;
		lastLineAkaFile = false;
		differentIdsInARow = 0;
	}
	
	public void readFiles() throws IOException{
		File basicTitleFile = new File(System.getProperty("user.dir") +
											"/data/title.basics.tsv/data.tsv");
		File akaTitleFile = new File(System.getProperty("user.dir") +
											"/data/title.akas.tsv/data.tsv");
		outputWriter = new BufferedWriter(new FileWriter("dataoutput.tsv"));
		basicTitle = new BasicTitle();
		akaTitle = new AkaTitle();
		basicTitleReader = getBufferedReader(basicTitleFile);
		akaTitleReader = getBufferedReader(akaTitleFile);
		
		writeOutputFirstLine();
		readFirstTwoLines();
		
		while(true) {
			switch(findWhichFileMoveNext()) {
				case 0:
					if((basicTitleLine = basicTitleReader.readLine())
							== null) {
						System.out.println("EOF");
						if(!akaTitle.isEmpty()) writeOutputFile(AKA);
						basicEOF = true;
						break;
					}
					setBasicOrAkaFields(BASIC);
					break;
				case 1:
					if((akaTitleLine = akaTitleReader.readLine()) == null) {
						lastLineAkaFile = true;
						if(!akaTitle.isEmpty()) writeOutputFile(AKA);
						System.out.println("EOF");
						akaEOF = true;
						break;
					}
					setBasicOrAkaFields(AKA);
					break;
				case -1:
					System.out.println("Something goes wrong with "
							+ "method findWhichFileMoveNext!");
					break;
				default:
					System.out.println("Something goes "
							+ "wrong with switch/case!");
					break;
			}
			if(basicEOF == true || akaEOF == true) break;
		}
		outputWriter.close();
	}
	
	private BufferedReader getBufferedReader(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void writeOutputFirstLine() throws IOException {
		outputWriter.write("titleId\t");
		outputWriter.write("primaryTitle\t");
		outputWriter.write("title\t");
		outputWriter.write("regions\t");
		outputWriter.write("\n");
	}
	
	private void readFirstTwoLines() throws IOException {
		int firstTwoLines = 2;
		for(int i = 0; i < firstTwoLines; i++) {
			basicTitleLine = basicTitleReader.readLine();
			setBasicOrAkaFields(BASIC);
			if(i == 1) akaTitle.clearLists();
			akaTitleLine = akaTitleReader.readLine();
			setBasicOrAkaFields(AKA);
		}
	}
	
	private void setBasicOrAkaFields(int whichFile) throws IOException {
		if(whichFile == BASIC) {
			basicTitle.setLine(basicTitleLine);
			basicTitle.setData();
		} else if(whichFile == AKA) {
			akaTitle.setLine(akaTitleLine);
			akaTitle.setData();
		}
	}
	
	private int getTitleAsInt(String title) {
		char[] titleAsChar = title.toCharArray();
		String titleAsString = "";
		for(int i = 2; i < title.length(); i++) {
			titleAsString += Character.toString(titleAsChar[i]);
		}
		return Integer.parseInt(titleAsString);
	}
	
	private int findWhichFileMoveNext() throws IOException {
		if(getTitleAsInt(basicTitle.getTConst())
				== getTitleAsInt(akaTitle.getTitleId())) {
			if(sameIds == false) writeOutputFile(BASIC);
			sameIds = true;
			return T_CONST_GTE_TITLE_ID;
		} else {
			if(getTitleAsInt(basicTitle.getTConst())
					> getTitleAsInt(akaTitle.getTitleId()))
								differentIdsInARow++;
			if(sameIds == true) {
				writeOutputFile(AKA);
				setLastLine();
			}
			if(differentIdsInARow > 1) {
				akaTitle.clearLists();
				differentIdsInARow = 0;
			}
			sameIds = false;
			if(getTitleAsInt(basicTitle.getTConst())
					> getTitleAsInt(akaTitle.getTitleId())) {
				return T_CONST_GTE_TITLE_ID;
			} else if(getTitleAsInt(basicTitle.getTConst())
					< getTitleAsInt(akaTitle.getTitleId())) {
				return T_CONST_LT_TITLED_ID;
			}
		}
		return -1;
	}
	
	private void setLastLine() {
		akaTitle.clearLists();
		akaTitle.setLine(akaTitle.getLastLine());
		akaTitle.setData();
	}
	
	private void writeOutputFile(int whichFile) throws IOException {
		int index = 0;
		int i = 0;
		String title;
		if(whichFile == BASIC) {
			outputWriter.write(basicTitle.getTConst() + "\t");
			outputWriter.write(basicTitle.getPrimaryTitle() + "\t");
		} else if(whichFile == AKA) {
			if(lastLineAkaFile == false) akaTitle.removeLastElements();
			while(!akaTitle.isEmpty()) {
				i = 0;
				outputWriter.write(akaTitle.getTitle(i) + " (" + akaTitle.getRegion(i));
				title = akaTitle.getTitle(i);
				akaTitle.removeElements(i);
				while((index = akaTitle.checkElementOnList(title)) != -1) {
					outputWriter.write(", " + akaTitle.getRegion(index));
					akaTitle.removeElements(index);
				}
				outputWriter.write(")\t");
			}
			outputWriter.write("\n");
		}
	}
}
