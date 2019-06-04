package causeanalysis;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Reducer;

public class CauseReducer extends Reducer<Text, SortedMapWritable, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<SortedMapWritable> values,
			Reducer<Text, SortedMapWritable, Text, Text>.Context context) throws IOException, InterruptedException {
		int ontimeCount = 0;
		int cancelCount = 0;
		int divertedCount = 0;
		int carrierDelay = 0;
		int weatherDelay = 0;
		int NASDelay = 0;
		int securityDelay = 0;
		int lateairDelay = 0;
		for(SortedMapWritable value : values) {
			for(Entry<WritableComparable, Writable> entry : value.entrySet()) {
				String type = ((Text)entry.getKey()).toString();
				int count = ((IntWritable) entry.getValue()).get();
				if(type.equals("ontime")) {
					ontimeCount+=count;
				}else if(type.equals("cancelled")) {
					cancelCount+=count;
				}else if(type.equals("diverted")) {
					divertedCount+=count;
				}else if(type.equals("carrierdelay")) {
					carrierDelay+=count;
				}else if(type.equals("weatherdelay")) {
					weatherDelay+=count;
				}else if(type.equals("nasdelay")) {
					NASDelay+=count;
				}else if(type.equals("securitydelay")) {
					securityDelay+=count;
				}else if(type.equals("lateaircraftdelay")) {
					lateairDelay+=count;
				}
			}
			value.clear();
		}
		int total = ontimeCount+cancelCount+divertedCount+carrierDelay+weatherDelay+NASDelay+securityDelay+lateairDelay;
		context.write(key, new Text(","+ontimeCount+","+cancelCount+","+divertedCount+","+carrierDelay+","+weatherDelay+","+NASDelay+","+securityDelay+","+lateairDelay+","+total));
	}

}
