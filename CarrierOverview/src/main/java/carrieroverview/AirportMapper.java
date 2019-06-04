package carrieroverview;

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
			
			if (!tokens[16].equals("NA")) {
				FlightWritable out = new FlightWritable();
				
				String year = tokens[0];
				String month = tokens[1];
				String day = tokens[2];
				if(month.length()==1) {
					month = "0"+tokens[1];
				}
				if(day.length() == 1) {
					day = "0"+tokens[2];
				}
				keyout.set(tokens[8]+","+year+"-"+month);
				out.setTotal(1);
				context.write(keyout, out);
			}
			
		}

	}

}
