package mergejoin;

import java.util.ArrayList;

public class AkaTitle implements DataProcessing {
	
	private String line;
	private ArrayList<String> titles;
	private ArrayList<String> regions;
	private String titleId;
	private final int TITLE_ID_INDEX = 0;
	private final int TITLE_INDEX = 2;
	private final int REGION_INDEX = 3;
	private String lastLine;
	
	public AkaTitle() {
		titles = new ArrayList<String>();
		regions = new ArrayList<String>();
	}
	
	public void setLine(String line) {
		this.line = line;
	}

	public void setData() {
		splitData();
	}
	
	public void splitData() {
		String[] tokens = line.split("\t");
		setTitleId(tokens[TITLE_ID_INDEX]);
		titles.add(tokens[TITLE_INDEX]);
		regions.add(tokens[REGION_INDEX]);
		setLastLine();
	}
	
	private void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	
	public String getTitleId() {
		return this.titleId;
	}
	
	private void setLastLine() {
		this.lastLine = line;
	}
	
	public String getLastLine() {
		return lastLine;
	}
	
	public String getTitle(int index) {
		return titles.get(index);
	}
	
	public String getRegion(int index) {
		return regions.get(index);
	}
	
	public boolean isEmpty() {
		return titles.isEmpty() ? true : false;
	}
	
	public void removeLastElements() {
		titles.remove(titles.size() - 1);
		regions.remove(regions.size() - 1);
	}
	
	public int checkElementOnList(String title) {
		return titles.contains(title) ? titles.indexOf(title) : -1;
	}
	
	public void removeElements(int index) {
		titles.remove(index);
		regions.remove(index);
	}
	
	public void printFields() {
		System.out.println("Aka Title Fields: ");
		for(int i = 0; i < titles.size(); i++) {
			System.out.println(getTitleId());
			System.out.println(titles.get(i) + "\t" + regions.get(i));
		}
		System.out.println("---");
	}
	
	public void clearLists() {
		titles.clear();
		regions.clear();
	}
}
