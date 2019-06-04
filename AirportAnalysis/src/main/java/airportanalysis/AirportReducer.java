package airportanalysis;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AirportReducer extends Reducer<Text, FlightWritable, Text, FlightWritable>{

	@Override
	protected void reduce(Text key, Iterable<FlightWritable> values,
			Reducer<Text, FlightWritable, Text, FlightWritable>.Context context) throws IOException, InterruptedException {
		long total = 0;
		long delay = 0;
		double delayTime = 0.0;
		
		for(FlightWritable value : values) {
			total+=value.getTotal();
			delay+=value.getDelay();
			delayTime+=value.getAvgDelayTime();
		}
		FlightWritable out = new FlightWritable(total, delay);
		out.setDelayRate((double)delay/total);
		out.setAvgDelayTime(delayTime/delay);
		context.write(key, out);
	}

}
