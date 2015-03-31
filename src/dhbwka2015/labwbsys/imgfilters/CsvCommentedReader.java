package dhbwka2015.labwbsys.imgfilters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CsvCommentedReader {
	
	private String filename = "";
	private ArrayList<String[]> fileContent= new ArrayList<String[]>();

	public CsvCommentedReader(String filename) {
		this.filename = filename;
	}

	public boolean readFile() {
		boolean res = true;

		try {
			BufferedReader br = new BufferedReader(
					new FileReader(this.filename));

			String line;
			while ((line = br.readLine()) != null) {
				if (line.charAt(0) == '#')
					continue;
				fileContent.add(parseLine(line));
			}
		} catch (Exception e) {
			res = false;
		}

		return res;
	}

	public ArrayList<String[]> getFileContent() {

		return fileContent;
	}

	private String[] parseLine(String line) {
		String[] tokens = line.split(",");

		return tokens;
	}

	public String toString() {
		StringBuilder res = new StringBuilder();

		for (int i=0; i<fileContent.size(); ++i){
			String[] line = fileContent.get(i);
			for (int j=0; j<line.length; ++j){
				res.append(line[j]);
				res.append("  ");
			}
			res.append("\n");
		}

		return res.toString();
	}

	
//	public static void main(String[] args) {
//
//		CsvCommentedReader reader = new CsvCommentedReader("test.csv");
//		reader.readFile();
//		
//		System.out.println(System.getProperty("user.dir"));
//		System.out.println(reader.toString());
//	}
	
}

