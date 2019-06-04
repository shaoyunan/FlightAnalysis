package airportanalysis;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class AirportJoinReducer extends Reducer<Text, Text, Text, Text> {
	private ArrayList<Text> flights = new ArrayList<Text>();
	private ArrayList<Text> names = new ArrayList<Text>();

	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {

		flights.clear();
		names.clear();

		for (Text value : values) {
			if (value.charAt(0) == 'N') {
				names.add(new Text(value.toString().substring(1)));
			} else if (value.charAt(0) == 'F') {
				flights.add(new Text(value.toString().substring(1)));
			}
		}

		if (!flights.isEmpty() && !names.isEmpty()) {
			for(Text n:names) {
				for(Text f:flights) {
					Text newKey = new Text(n.toString()+",");
					context.write(newKey, f);
				}
			}
		}
	}

}
