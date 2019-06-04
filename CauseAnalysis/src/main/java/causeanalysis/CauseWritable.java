package causeanalysis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class CauseWritable implements Writable{
	private String type;
	private long count;

	public CauseWritable() {
		super();
	}

	public CauseWritable(String type, long count) {
		super();
		this.type = type;
		this.count = count;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(type);
		out.writeLong(count);
	}

	public void readFields(DataInput in) throws IOException {
		type = in.readUTF();
		count = in.readLong();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}


	@Override
	public String toString() {
		return "," + type + "," + count;
	}

}
