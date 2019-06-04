package multipleoutput;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class MyMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
	
	private MultipleOutputs<Text, NullWritable> out;

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		if (!value.toString().contains("Year")) {
			String tokens[] = value.toString().split(",");
			if (!tokens[14].equals("NA")) {
				if (Integer.parseInt(tokens[14]) >= 15) {
					out.write(value, NullWritable.get(), "Delay");
				}
			}
			if (!tokens[14].equals("NA")) {
				if (tokens[23].equals("1")) {
					out.write(value, NullWritable.get(), "Diverted");
				}
			}
			if (!tokens[21].equals("NA")) {
				if (tokens[22].equals("A")) {

					out.write(value, NullWritable.get(), "CarrierCancel");
				} else if (tokens[22].equals("B")) {

					out.write(value, NullWritable.get(), "WeatherCancel");
				} else if (tokens[22].equals("C")) {

					out.write(value, NullWritable.get(), "NASCancel");
				} else if (tokens[22].equals("D")) {

					out.write(value, NullWritable.get(), "SecurityCancel");
				}
			}
		}
	}

	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		out.close();
	}

	@Override
	protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		out = new MultipleOutputs<Text, NullWritable>(context);
	}

}
