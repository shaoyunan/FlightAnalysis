package timeanalysis;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TimeMapper extends Mapper<LongWritable, Text, Text, FlightWritable> {
	private Text keyout = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlightWritable>.Context context)
			throws IOException, InterruptedException {

		if (!value.toString().contains("Year")) {
			FlightWritable out = new FlightWritable();
			out.setTotal(1);
			String tokens[] = value.toString().split(",");
			if (!tokens[14].equals("NA")) {
				if (Integer.parseInt(tokens[14]) >= 15 || Integer.parseInt(tokens[15]) >= 15) {
					out.setDelayed(1);
					out.setAvgTime(Integer.parseInt(tokens[14]));
				}
			}
			if (tokens[21].equals("1")) {
				out.setCancelled(1);
			}
			Configuration config = context.getConfiguration();
			if (config.get("keyfield").equals("month")) {
				keyout.set(tokens[1]);
			} else if (config.get("keyfield").equals("dayofweek")) {
				keyout.set(tokens[3]);
			} else if (config.get("keyfield").equals("hour")) {
				if (tokens[5].length() >= 4) {
					keyout.set(tokens[5].substring(0, 2));
					if (tokens[5].substring(0, 2).equals("24")) {
						keyout.set("0");
					}
				} else {
					keyout.set(tokens[5].substring(0, 1));
				}
			} else if (config.get("keyfield").equals("timerange")) {
				int time = 0;
				if (tokens[5].length() >= 4) {
					time = Integer.parseInt(tokens[5].substring(0, 2));
					if (tokens[5].substring(0, 2).equals("24")) {
						time = 0;
					}
				} else {
					time=Integer.parseInt(tokens[5].substring(0, 1));
				}
				if(time>=6 && time<12) {
					keyout.set("Morning");
				}
				if(time>=12 && time <17) {
					keyout.set("Afternoon");
				}
				if(time>=17 && time < 20) {
					keyout.set("Evening");
				}
				if(time>=20 || time <6) {
					keyout.set("Night");
				}
			}
//			if(keyout.toString().equals("0")&&out.getCancelled()==1) {
//				System.out.println(tokens[22]);
//			}
			
			//keyout.set(tokens[16]+","+keyout.toString());
			context.write(keyout, out);
			
			
		}

	}

}
