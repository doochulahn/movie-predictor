package parserAndWriter;

import java.util.List;


public interface Parser {
	
	public void updateParser(String record);
	
	public List<? extends Object> getResults();
	
}
