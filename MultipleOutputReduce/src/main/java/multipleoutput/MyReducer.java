package multipleoutput;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class MyReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
	private MultipleOutputs<Text, NullWritable> out;

	@Override
	protected void setup(Reducer<Text, NullWritable, Text, NullWritable>.Context context) throws IOException, InterruptedException {
		out = new MultipleOutputs<Text, NullWritable>(context);
	}

	@Override
	protected void reduce(Text key, Iterable<NullWritable> values, Reducer<Text, NullWritable, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		if (!key.toString().contains("Year")) {
			String tokens[] = key.toString().split(",");
			if (!tokens[14].equals("NA")) {
				if (Integer.parseInt(tokens[14]) >= 15) {
					out.write(key, NullWritable.get(), "Delay");
				}
			}
			if (!tokens[14].equals("NA")) {
				if (tokens[23].equals("1")) {
					out.write(key, NullWritable.get(), "Diverted");
				}
			}
			if (!tokens[21].equals("NA")) {
				if (tokens[22].equals("A")) {

					out.write(key, NullWritable.get(), "CarrierCancel");
				} else if (tokens[22].equals("B")) {

					out.write(key, NullWritable.get(), "WeatherCancel");
				} else if (tokens[22].equals("C")) {

					out.write(key, NullWritable.get(), "NASCancel");
				} else if (tokens[22].equals("D")) {

					out.write(key, NullWritable.get(), "SecurityCancel");
				}
			}
		}
	}

	@Override
	protected void cleanup(Reducer<Text, NullWritable, Text, NullWritable>.Context context) throws IOException, InterruptedException {
		out.close();
	}
}
