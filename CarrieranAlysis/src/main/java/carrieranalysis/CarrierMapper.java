package carrieranalysis;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CarrierMapper extends Mapper<LongWritable, Text, Text, CarrierWritable> {

	private Text carrier = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, CarrierWritable>.Context context)
			throws IOException, InterruptedException {

		String tokens[] = value.toString().split(",");
		int depDelay = 0;
		int delayCount = 0;
		int cancelCount = 0;
		if (!value.toString().contains("Year")) {
			if (!tokens[24].contains("NA")) {
				int carrierDelay = Integer.parseInt(tokens[24]);
				if (carrierDelay > 0) {
					delayCount = 1;
					depDelay = Integer.parseInt(tokens[14]);
				}
			}
			if (tokens[22].equals("A")) {
				cancelCount = 1;
			}
			if (context.getConfiguration().get("outformat").equals("byyear")) {
				carrier.set(tokens[8] + "," + tokens[0] + ",");
			} else {
				carrier.set(tokens[8] + ",");
			}

			CarrierWritable out = new CarrierWritable(depDelay, depDelay, delayCount, 1, cancelCount);
			context.write(carrier, out);
		}

	}

}
