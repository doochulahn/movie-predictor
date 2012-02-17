package model.knn;

public class DoubleFeature implements Feature{
	private double value;

	public DoubleFeature(double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public Double getDistanceThan(Feature minuendo){
		return this.value-(Double) minuendo.getValue();
	}

}
