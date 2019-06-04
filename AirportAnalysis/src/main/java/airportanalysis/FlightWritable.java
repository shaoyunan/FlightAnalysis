package airportanalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class FlightWritable implements Writable {
	private long total;
	private long delay;
	private double delayRate;
	private double avgDelayTime;

	public FlightWritable() {

	}

	public FlightWritable(long total, long delay) {
		super();
		this.total = total;
		this.delay = delay;
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(total);
		out.writeLong(delay);
		out.writeDouble(delayRate);
		out.writeDouble(avgDelayTime);
	}

	public void readFields(DataInput in) throws IOException {
		total = in.readLong();
		delay = in.readLong();
		delayRate = in.readDouble();
		avgDelayTime = in.readDouble();
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public double getDelayRate() {
		return delayRate;
	}

	public void setDelayRate(double delayRate) {
		this.delayRate = delayRate;
	}

	public double getAvgDelayTime() {
		return avgDelayTime;
	}

	public void setAvgDelayTime(double avgDelayTime) {
		this.avgDelayTime = avgDelayTime;
	}

	@Override
	public String toString() {
		return "," + total + "," + delay + "," + delayRate + "," + avgDelayTime;
	}

}
