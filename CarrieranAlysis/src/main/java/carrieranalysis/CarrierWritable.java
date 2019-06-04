package carrieranalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class CarrierWritable implements Writable {

	private int depDelayMin;
	private int depDelayMax;
	private long delayCount;
	private long totalCount;
	private double delayRate;
	private double avgDelayTime;
	private double median;
	private double stdDev;
	private long cancellationCount;
	private double cancelRate;
	
	public CarrierWritable() {
		super();
	}

	public CarrierWritable(int depDelayMin, int depDelayMax, long delayCount, long totalCount, long cancellationCount) {
		super();

		this.depDelayMin = depDelayMin;
		this.depDelayMax = depDelayMax;
		this.delayCount = delayCount;
		this.totalCount = totalCount;
		this.cancellationCount = cancellationCount;
	}

	public void write(DataOutput out) throws IOException {

		out.writeInt(depDelayMin);
		out.writeInt(depDelayMax);
		out.writeLong(delayCount);
		out.writeLong(totalCount);
		out.writeDouble(delayRate);
		out.writeDouble(avgDelayTime);
		out.writeLong(cancellationCount);
		out.writeDouble(cancelRate);
	}

	public void readFields(DataInput in) throws IOException {

		depDelayMin = in.readInt();
		depDelayMax = in.readInt();
		delayCount = in.readLong();
		totalCount = in.readLong();
		delayRate = in.readDouble();
		avgDelayTime = in.readDouble();
		cancellationCount = in.readLong();
		cancelRate = in.readDouble();
	}

	public int getDepDelayMin() {
		return depDelayMin;
	}

	public void setDepDelayMin(int depDelayMin) {
		this.depDelayMin = depDelayMin;
	}

	public int getDepDelayMax() {
		return depDelayMax;
	}

	public void setDepDelayMax(int depDelayMax) {
		this.depDelayMax = depDelayMax;
	}

	public long getDelayCount() {
		return delayCount;
	}

	public void setDelayCount(long delayCount) {
		this.delayCount = delayCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
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

	public double getMedian() {
		return median;
	}

	public void setMedian(double median) {
		this.median = median;
	}

	public double getStdDev() {
		return stdDev;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	public long getCancellationCount() {
		return cancellationCount;
	}

	public void setCancellationCount(long cancellationCount) {
		this.cancellationCount = cancellationCount;
	}

	public double getCancelRate() {
		return cancelRate;
	}

	public void setCancelRate(double cancelRate) {
		this.cancelRate = cancelRate;
	}

	@Override
	public String toString() {
		return (delayRate + "," + avgDelayTime + "," + depDelayMin + "," + depDelayMax + "," + delayCount + ","
				+ totalCount + "," + cancellationCount+","+cancelRate);
	}
}
