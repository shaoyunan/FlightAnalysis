package carrieroverview;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AirportFlightMapper extends Mapper<LongWritable, Text, Text, Text> {

	private Text outkey = new Text();
	private Text outvalue = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {

		String tokens[] = value.toString().split(",");

		outkey.set(tokens[0].trim());
		outvalue.set("F" + value.toString().trim());
		context.write(outkey, outvalue);

	}

}
