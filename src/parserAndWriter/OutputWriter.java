package parserAndWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.Prediction;

public class OutputWriter {
	
	public void write(List<Prediction> predictions, String fileOutputName){
		FileWriter fw;
		try {
			fw = new FileWriter(fileOutputName);
			BufferedWriter bw=new BufferedWriter(fw);
//			bw.write("userID\tmovieID\trating"+"\n");
			for (Prediction p: predictions){
				bw.write(p.getRating()+"\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
