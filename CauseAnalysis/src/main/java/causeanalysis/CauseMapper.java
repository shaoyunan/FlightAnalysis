package causeanalysis;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CauseMapper extends Mapper<LongWritable, Text, Text, SortedMapWritable> {

	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, SortedMapWritable>.Context context)
			throws IOException, InterruptedException {
		int ontimeCount = 1;
		int cancelCount = 0;
		int divertedCount = 0;
		int carrierDelay = 0;
		int weatherDelay = 0;
		int NASDelay = 0;
		int securityDelay = 0;
		int lateairDelay = 0;
		
		SortedMapWritable result = new SortedMapWritable();
		
		if (!value.toString().contains("Year")) {
			String tokens[] = value.toString().split(",");
			if (!tokens[21].equals(String.valueOf(0))) {
				cancelCount=1;
				ontimeCount=0;
			}
			if (tokens[23].equals(String.valueOf(1))) {
				divertedCount=1;
				ontimeCount=0;
			}
			if(!tokens[14].equals("NA")) {
				if(Integer.parseInt(tokens[14])>=15 || Integer.parseInt(tokens[15])>=15) {
					if (!tokens[24].equals("NA") && !tokens[25].equals("NA") && !tokens[26].equals("NA")
							&& !tokens[27].equals("NA") && !tokens[28].equals("NA")) {
						ontimeCount=0;
						int arr[] = new int[5];
						arr[0] = Integer.parseInt(tokens[24]);
						arr[1] = Integer.parseInt(tokens[25]);
						arr[2] = Integer.parseInt(tokens[26]);
						arr[3] = Integer.parseInt(tokens[27]);
						arr[4] = Integer.parseInt(tokens[28]);
						int max = -1;
						int maxIndex = -1;
						for (int i = 0; i < arr.length; i++) {
							if (arr[i] > max) {
								max = arr[i];
								maxIndex = i;
							}
						}
						if (maxIndex == 0) {
							carrierDelay=1;
						} else if (maxIndex == 1) {
							weatherDelay=1;
						} else if (maxIndex == 2) {
							NASDelay=1;
						} else if (maxIndex == 3) {
							securityDelay=1;
						} else if (maxIndex == 4) {
							lateairDelay=1;
						}
					}
				}
				
			}
			result.put(new Text("ontime"), new IntWritable(ontimeCount));
			result.put(new Text("cancelled"), new IntWritable(cancelCount));
			result.put(new Text("diverted"), new IntWritable(divertedCount));
			result.put(new Text("carrierdelay"), new IntWritable(carrierDelay));
			result.put(new Text("weatherdelay"), new IntWritable(weatherDelay));
			result.put(new Text("nasdelay"), new IntWritable(NASDelay));
			result.put(new Text("securitydelay"), new IntWritable(securityDelay));
			result.put(new Text("lateaircraftdelay"), new IntWritable(lateairDelay));
			
			context.write(new Text(tokens[0]), result);
		}
	}

}
