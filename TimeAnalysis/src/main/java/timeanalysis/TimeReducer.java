package timeanalysis;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TimeReducer extends Reducer<Text, FlightWritable, Text, FlightWritable>{

	@Override
	protected void reduce(Text key, Iterable<FlightWritable> values,
			Reducer<Text, FlightWritable, Text, FlightWritable>.Context context) throws IOException, InterruptedException {
		long cancelCount=0;
		long delayCount=0;
		long totalCount = 0;
		double totalTime = 0.0;
		
		for(FlightWritable value : values) {
			cancelCount+=value.getCancelled();
			delayCount+=value.getDelayed();
			totalCount+=value.getTotal();
			totalTime+=value.getAvgTime()*value.getDelayed();
		}
		FlightWritable out = new FlightWritable(cancelCount, delayCount, totalCount);
		out.setAvgTime(totalTime/delayCount);
		context.write(key, out);
	}

}
