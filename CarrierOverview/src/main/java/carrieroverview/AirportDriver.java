package carrieroverview;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class AirportDriver {
	public static void main(String[] args) throws Exception {
		boolean mapReduceOrigin = false;
		String mrOriginPath = args[2] + "/airport/origin/mr";
		boolean mapReduceDest = false;
		String mrDestPath = args[2] + "/airport/dest/mr";
		boolean top10Origin = false;
		String top10OriginPath = args[2] + "/airport/origin/top10";
		boolean top10Dest = false;
		String top10DestPath = args[2] + "/airport/dest/top10";
		boolean originFinal = false;
		String originFinalPath = args[2] + "/airport/origin/final";
		String destFinalPath = args[2] + "/airport/dest/final";

		if (!mapReduceOrigin) {
			Job job = Job.getInstance();
			Configuration config = job.getConfiguration();
			config.set("ap", "origin");
			job.setJarByClass(AirportMapper.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			job.setMapperClass(AirportMapper.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(FlightWritable.class);

			job.setReducerClass(AirportReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(FlightWritable.class);

			job.setNumReduceTasks(1);

			FileInputFormat.addInputPath(job, new Path(args[0]));
			Path outputDir = new Path(mrOriginPath);
			FileOutputFormat.setOutputPath(job, outputDir);

			FileSystem hdfs = FileSystem.get(job.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			mapReduceOrigin = job.waitForCompletion(true);
		}

		if (mapReduceOrigin) {
			Job job2 = Job.getInstance();
			job2.setJarByClass(AirportDriver.class);

			MultipleInputs.addInputPath(job2, new Path(mrOriginPath + "/part-r-00000"), TextInputFormat.class,
					AirportFlightMapper.class);
			MultipleInputs.addInputPath(job2, new Path(args[1]), TextInputFormat.class, AirportNameMapper.class);

			job2.setReducerClass(AirportJoinReducer.class);

			job2.setOutputFormatClass(TextOutputFormat.class);
			job2.setOutputKeyClass(Text.class);
			job2.setOutputValueClass(Text.class);

			job2.setNumReduceTasks(1);

			Path outputDir = new Path(originFinalPath);
			FileOutputFormat.setOutputPath(job2, outputDir);

			FileSystem hdfs = FileSystem.get(job2.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			originFinal = job2.waitForCompletion(true);

		}

	}
}
