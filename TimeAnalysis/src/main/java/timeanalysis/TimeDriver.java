package timeanalysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/*
 * args[2]->hour, timerange, dayofweek, month
 */

public class TimeDriver {
	public static void main(String[] args) throws Exception {

		if (args[2] != null) {
			Job job = Job.getInstance();
			Configuration config = job.getConfiguration();
			config.set("keyfield", args[2]);
			job.setJarByClass(TimeMapper.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			job.setMapperClass(TimeMapper.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(FlightWritable.class);

			job.setCombinerClass(TimeReducer.class);
			
			job.setReducerClass(TimeReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(FlightWritable.class);

			job.setNumReduceTasks(1);

			FileInputFormat.addInputPath(job, new Path(args[0]));
			Path outputDir = new Path(args[1] + "/timeanalysis/" + args[2]);
			FileOutputFormat.setOutputPath(job, outputDir);

			FileSystem hdfs = FileSystem.get(job.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			int code = job.waitForCompletion(true) ? 0 : 1;

			if (job.isSuccessful()) {
				System.out.println("Job was successful");
			} else if (!job.isSuccessful()) {
				System.out.println("Job was not successful");
			}

			System.exit(code);
		}else {
			System.out.println("No key field selected");
			System.exit(1);
		}

	}
}
