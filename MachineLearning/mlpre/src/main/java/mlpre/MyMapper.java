package mlpre;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<LongWritable, Text, NullWritable, Text>{

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, NullWritable, Text>.Context context)
			throws IOException, InterruptedException {
		String delayed = "0";

		if(!value.toString().contains("Year")) {
			String tokens[] = value.toString().split(",");
			if(!tokens[14].equals("NA")) {
				if(Integer.parseInt(tokens[14])>=15 || Integer.parseInt(tokens[15])>=15) {
					delayed="1";
				}
			}
			if(tokens[23].equals("0") && tokens[21].equals("0")) {
				int time = 0;
				if (tokens[5].length() >= 4) {
					time = Integer.parseInt(tokens[5].substring(0, 2));
					if (tokens[5].substring(0, 2).equals("24")) {
						time = 0;
					}
				} else {
					time=Integer.parseInt(tokens[5].substring(0, 1));
				}
				
				StringBuilder sb = new StringBuilder();
				
				sb.append(tokens[1]+",");
				sb.append(tokens[3]+",");
				sb.append(String.valueOf(time)+",");
				sb.append(tokens[8]+",");
				sb.append(tokens[16]+",");
				sb.append(tokens[17]+",");
				sb.append(delayed);
				String keyout = sb.toString();
				context.write(NullWritable.get(), new Text(keyout));
			}
		}
	}

}
