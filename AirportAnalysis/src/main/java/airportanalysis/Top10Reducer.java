package airportanalysis;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Top10Reducer extends Reducer<NullWritable, Text, NullWritable, Text> {
	private TreeMap<Integer, Text> recMap;

	@Override
	protected void setup(Reducer<NullWritable, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		recMap = new TreeMap<Integer, Text>();
	}

	@Override
	protected void reduce(NullWritable key, Iterable<Text> value,
			Reducer<NullWritable, Text, NullWritable, Text>.Context context) throws IOException, InterruptedException {

		for (Text t : value) {
			String values[] = t.toString().split(",");
			String score = values[2];
			recMap.put(Integer.parseInt(score), new Text(t));
		}
		if (recMap.size() > 10) {
			recMap.remove(recMap.firstKey());
		}

	}

	@Override
	protected void cleanup(Reducer<NullWritable, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {

		for (Text t : recMap.values()) {
			context.write(NullWritable.get(), t);
		}

	}
}
