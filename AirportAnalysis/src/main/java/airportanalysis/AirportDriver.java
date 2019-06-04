package airportanalysis;

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

		if (mapReduceOrigin && !top10Origin) {
			Job job1 = Job.getInstance();

			job1.setJarByClass(Top10Mapper.class);

			job1.setInputFormatClass(TextInputFormat.class);
			job1.setOutputFormatClass(TextOutputFormat.class);

			job1.setMapperClass(Top10Mapper.class);
			job1.setMapOutputKeyClass(NullWritable.class);
			job1.setMapOutputValueClass(Text.class);

			job1.setReducerClass(Top10Reducer.class);
			job1.setOutputKeyClass(NullWritable.class);
			job1.setOutputValueClass(Text.class);

			job1.setNumReduceTasks(1);

			FileInputFormat.addInputPath(job1, new Path(mrOriginPath+"/part-r-00000"));
			Path outputDir = new Path(top10OriginPath);
			FileOutputFormat.setOutputPath(job1, outputDir);

			FileSystem hdfs = FileSystem.get(job1.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			top10Origin = job1.waitForCompletion(true);
		}
		
		if (mapReduceOrigin && top10Origin) {
			Job job2 = Job.getInstance();
			job2.setJarByClass(AirportDriver.class);

			MultipleInputs.addInputPath(job2, new Path(top10OriginPath+"/part-r-00000"), TextInputFormat.class,
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

		
		if (!mapReduceDest && originFinal) {
			Job job3 = Job.getInstance();
			Configuration config = job3.getConfiguration();
			config.set("ap", "dest");
			job3.setJarByClass(AirportMapper.class);

			job3.setInputFormatClass(TextInputFormat.class);
			job3.setOutputFormatClass(TextOutputFormat.class);

			job3.setMapperClass(AirportMapper.class);
			job3.setMapOutputKeyClass(Text.class);
			job3.setMapOutputValueClass(FlightWritable.class);

			job3.setReducerClass(AirportReducer.class);
			job3.setOutputKeyClass(Text.class);
			job3.setOutputValueClass(FlightWritable.class);

			job3.setNumReduceTasks(1);

			FileInputFormat.addInputPath(job3, new Path(args[0]));
			Path outputDir = new Path(mrDestPath);
			FileOutputFormat.setOutputPath(job3, outputDir);

			FileSystem hdfs = FileSystem.get(job3.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			mapReduceDest = job3.waitForCompletion(true);
		}

		if (mapReduceDest && !top10Dest) {
			Job job4 = Job.getInstance();

			job4.setJarByClass(Top10Mapper.class);

			job4.setInputFormatClass(TextInputFormat.class);
			job4.setOutputFormatClass(TextOutputFormat.class);

			job4.setMapperClass(Top10Mapper.class);
			job4.setMapOutputKeyClass(NullWritable.class);
			job4.setMapOutputValueClass(Text.class);

			job4.setReducerClass(Top10Reducer.class);
			job4.setOutputKeyClass(NullWritable.class);
			job4.setOutputValueClass(Text.class);

			job4.setNumReduceTasks(1);

			FileInputFormat.addInputPath(job4, new Path(mrDestPath+"/part-r-00000"));
			Path outputDir = new Path(top10DestPath);
			FileOutputFormat.setOutputPath(job4, outputDir);

			FileSystem hdfs = FileSystem.get(job4.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			top10Dest = job4.waitForCompletion(true);
		}
		
		if (mapReduceDest && top10Dest) {
			Job job5 = Job.getInstance();
			job5.setJarByClass(AirportDriver.class);

			MultipleInputs.addInputPath(job5, new Path(top10DestPath+"/part-r-00000"), TextInputFormat.class,
					AirportFlightMapper.class);
			MultipleInputs.addInputPath(job5, new Path(args[1]), TextInputFormat.class, AirportNameMapper.class);

			job5.setReducerClass(AirportJoinReducer.class);

			job5.setOutputFormatClass(TextOutputFormat.class);
			job5.setOutputKeyClass(Text.class);
			job5.setOutputValueClass(Text.class);

			job5.setNumReduceTasks(1);

			Path outputDir = new Path(destFinalPath);
			FileOutputFormat.setOutputPath(job5, outputDir);

			FileSystem hdfs = FileSystem.get(job5.getConfiguration());
			if (hdfs.exists(outputDir)) {
				hdfs.delete(outputDir, true);
			}

			int code = job5.waitForCompletion(true) ? 0 : 1;

			if (job5.isSuccessful()) {
				System.out.println("Job was successful");
			} else if (!job5.isSuccessful()) {
				System.out.println("Job was not successful");
			}

			System.exit(code);
		}
	}
}
