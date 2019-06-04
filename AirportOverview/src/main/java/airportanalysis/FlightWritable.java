package airportanalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class FlightWritable implements Writable {
	private long total;

	public FlightWritable() {

	}

	public FlightWritable(long total, String date) {
		super();
		this.total = total;

	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(total);
	}

	public void readFields(DataInput in) throws IOException {
		total = in.readLong();

	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}


	@Override
	public String toString() {
		return ",airport," + total;
	}

}
