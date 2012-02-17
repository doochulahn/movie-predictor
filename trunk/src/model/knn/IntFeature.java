package model.knn;

public class IntFeature implements Feature {
	private int value;

	public IntFeature(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public Integer getDistanceThan(Feature minuendo){
		return this.value-(Integer) minuendo.getValue();
	}
	

}
