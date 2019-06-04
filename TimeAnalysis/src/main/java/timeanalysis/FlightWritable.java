package timeanalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class FlightWritable implements Writable {
	private long cancelled;
	private long delayed;
	private long total;
	private double avgTime;

	public FlightWritable() {
		super();
		this.cancelled = 0;
		this.delayed = 0;
		this.total = 0;
		this.avgTime=0.0;
	}

	public FlightWritable(long cancelled, long delayed, long total) {
		super();
		this.cancelled = cancelled;
		this.delayed = delayed;
		this.total = total;
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(cancelled);
		out.writeLong(delayed);
		out.writeLong(total);
		out.writeDouble(avgTime);
	}

	public void readFields(DataInput in) throws IOException {
		cancelled = in.readLong();
		delayed = in.readLong();
		total = in.readLong();
		avgTime = in.readDouble();
	}

	public long getDelayed() {
		return delayed;
	}

	public void setDelayed(long delayed) {
		this.delayed = delayed;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getCancelled() {
		return cancelled;
	}

	public void setCancelled(long cancelled) {
		this.cancelled = cancelled;
	}

	public double getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(double avgTime) {
		this.avgTime = avgTime;
	}

	@Override
	public String toString() {
		return "," + cancelled + "," + delayed + "," + total+","+avgTime;
	}

}
