package parserAndWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.Dataset;
import model.Prediction;

public class OutputWriter {
	
	public void write(List<Prediction> predictions, Dataset outputDataset){
		FileWriter fw;
		try {
			fw = new FileWriter(outputDataset.getFilename());
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write("userID\tmovieID\trating"+"\n");
			for (Prediction p: predictions){
				bw.write(p.toString()+"\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
