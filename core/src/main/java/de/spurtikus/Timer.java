package de.spurtikus;

public class Timer {

	long start;
	long stop;
	
	public void start() {
		start = System.currentTimeMillis();
	}
	
	public long stopAndRead() {
		stop = System.currentTimeMillis();
		return stop-start;
	}

	public void stopAndPrintln() {
		stop = System.currentTimeMillis();
		System.out.println("Timer: " + (stop-start) + " ms");
	}

}
