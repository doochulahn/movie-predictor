package util;


public class PerformanceMeter {
	private long start;
	private long stop;
	
	public void start(){
		this.start=System.nanoTime();
	}
	
	public void stop(){
		this.stop=System.nanoTime();
		System.out.println("Execution time: "+(this.stop-this.start)*0.000000001+" second");
	}
	
	
	

}
