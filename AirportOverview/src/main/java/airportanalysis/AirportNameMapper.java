package airportanalysis;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AirportNameMapper extends Mapper<LongWritable, Text, Text, Text> {
	private Text outkey = new Text();
	private Text outvalue = new Text();
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		if(!value.toString().contains("iata")) {
			String tokens[] = value.toString().split(",");
			outkey.set(tokens[0].substring(1, tokens[0].length()-1));
			outvalue.set("N"+tokens[1].substring(1, tokens[1].length()-1));
			context.write(outkey, outvalue);
		}
	}

}
