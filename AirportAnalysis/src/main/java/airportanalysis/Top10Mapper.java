package airportanalysis;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Top10Mapper extends Mapper<LongWritable, Text, NullWritable, Text> {
	private TreeMap<Integer, Text> recMap;

	@Override
	protected void setup(Mapper<LongWritable, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {

		this.recMap = new TreeMap<Integer, Text>();

	}

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {

		String values[] = value.toString().split(",");
		try {

			String score = values[2];
			recMap.put(Integer.parseInt(score), new Text(value));

		} catch (Exception e) {
		}

		if (recMap.size() > 10) {
			recMap.remove(recMap.firstKey());
		}

	}

	@Override
	protected void cleanup(Mapper<LongWritable, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {

		for (Text t : recMap.values()) {
			context.write(NullWritable.get(), t);
		}
	}
}
