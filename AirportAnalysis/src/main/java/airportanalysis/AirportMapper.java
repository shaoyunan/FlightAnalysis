package airportanalysis;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class AirportMapper extends Mapper<LongWritable, Text, Text, FlightWritable> {
	Text keyout = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlightWritable>.Context context)
			throws IOException, InterruptedException {
		String ap = context.getConfiguration().get("ap");

		if (!value.toString().contains("Year")) {
			String tokens[] = value.toString().split(",");
			FlightWritable out = new FlightWritable(1, 0);
			if (!tokens[14].equals("NA")) {
				if (Integer.parseInt(tokens[14]) >= 15 || Integer.parseInt(tokens[15]) >= 15) {
					out.setDelay(1);
					out.setAvgDelayTime(Double.parseDouble(tokens[14]));
				}
			}
			if (ap.equals("origin")) {
				keyout.set(tokens[16]);
			} else {
				keyout.set(tokens[17]);
			}
			context.write(keyout, out);
		}

	}

}
