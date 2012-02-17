package parserAndWriter;

import java.util.ArrayList;
import java.util.List;

import model.InputFileRow;

public class InputFileParser implements Parser{
	List<InputFileRow> inputfileRows;
	
	public InputFileParser() {
		this.inputfileRows=new ArrayList<InputFileRow>();
	}


	@Override
	public void updateParser(String record) {
		InputFileRow row=new InputFileRow(record);
		this.inputfileRows.add(row);
		
	}

	@Override
	public List<InputFileRow> getResults() {
		return this.inputfileRows;
	}

}
