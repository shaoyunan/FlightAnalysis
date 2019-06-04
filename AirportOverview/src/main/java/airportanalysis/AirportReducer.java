package airportanalysis;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AirportReducer extends Reducer<Text, FlightWritable, Text, FlightWritable>{

	@Override
	protected void reduce(Text key, Iterable<FlightWritable> values,
			Reducer<Text, FlightWritable, Text, FlightWritable>.Context context) throws IOException, InterruptedException {
		long total = 0;
		
		for(FlightWritable value : values) {
			total+=value.getTotal();
		}
		FlightWritable out = new FlightWritable();
		out.setTotal(total);
		context.write(key, out);
	}

}
