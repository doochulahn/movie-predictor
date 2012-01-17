package model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import parserAndWriter.Parser;

public class Dataset {
	private int firstRowNum;
	private int lastRowNum;
	private String filename;
	

	
	public Dataset(String filename, int i, int j) {
		super();
		this.firstRowNum = i;
		this.lastRowNum = j;
		this.filename = filename;
	}
	
	public Dataset(String filename, int i) {
		this.firstRowNum = i;
		this.lastRowNum = 0;
		this.filename = filename;
	}

	
	public Dataset(String filename) {
		this.filename=filename;
		this.firstRowNum=0;
		this.lastRowNum=0;
	}

	public int getFirstRowNum() {
		return firstRowNum;
	}
	public void setI(int i) {
		this.firstRowNum = i;
	}
	public int getLastRowNum() {
		return lastRowNum;
	}
	public void setJ(int j) {
		this.lastRowNum = j;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
	public static void visit(Dataset dataset, Parser p){
		File file=new File(dataset.getFilename());
		FileInputStream f;
		int i=0;
		try {
			f = new FileInputStream(file);
			DataInputStream dis=new DataInputStream(f);
			InputStreamReader isr=new InputStreamReader(dis,"iso-8859-1");
			BufferedReader br=new BufferedReader(isr);
			String line;
			
			while ((line=br.readLine())!=null && ( i<=dataset.getLastRowNum() || dataset.getLastRowNum()==0) ){
				if (i>=dataset.getFirstRowNum()){
					p.updateParser(line);
				}
				i++;
			}
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
