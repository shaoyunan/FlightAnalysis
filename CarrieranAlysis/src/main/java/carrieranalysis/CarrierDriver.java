package carrieranalysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CarrierDriver {
	public static void main(String[] args) throws Exception {
		/*
		 * 0=seperate folder 1=aggregate all 2=aggregate by year 3=aggregate by month
		 */
		if (args[2].equals("0")) {
			Job job = Job.getInstance();
			Configuration config = job.getConfiguration();
			FileSystem local = FileSystem.get(config);
			Path inputDir = new Path(args[0]);
			// Path outputDir = new Path(args[1]);
			FileStatus[] inputFiles = local.listStatus(inputDir);
			int code = 0;

			for (int i = 0; i < inputFiles.length; i++) {
				job = Job.getInstance();
				config = job.getConfiguration();
				config.set("outformat", "seperate");
				job.setJarByClass(CarrierMapper.class);

				job.setInputFormatClass(TextInputFormat.class);
				job.setOutputFormatClass(TextOutputFormat.class);

				// set mapper
				job.setMapperClass(CarrierMapper.class);

				// set reducer
				job.setReducerClass(CarrierReducer.class);

				// Number of reducers
				job.setNumReduceTasks(1);

				// Specify key value
				job.setOutputKeyClass(Text.class);
				job.setOutputValueClass(CarrierWritable.class);

				// Specify input path
				FileInputFormat.addInputPath(job, inputFiles[i].getPath());
				// SPecify output path
				int startIndex = inputFiles[i].toString().lastIndexOf("/");
				int endIndex = inputFiles[i].toString().lastIndexOf(".");
				Path outputDir = new Path(
						args[1] + "/carrier/" + inputFiles[i].toString().substring(startIndex, endIndex));
				FileOutputFormat.setOutputPath(job, outputDir);

				FileSystem hdfs = FileSystem.get(job.getConfiguration());
				if (hdfs.exists(outputDir)) {
					hdfs.delete(outputDir, true);
				}

				code = job.waitForCompletion(true) ? 0 : 1;

				if (job.isSuccessful()) {

					System.out.println("Job was successful");

				} else if (!job.isSuccessful()) {

					System.out.println("Job was not successful");

				}
			}

			System.exit(code);
		}
		if (args[2].equals("1")) {
			Job job = Job.getInstance();
			Configuration config = job.getConfiguration();
			config.set("outformat", "combined");

			job.setJarByClass(CarrierMapper.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			// set mapper
			job.setMapperClass(CarrierMapper.class);

			// set reducer
			job.setReducerClass(CarrierReducer.class);

			// Number of reducers
			job.setNumReduceTasks(1);

			// Specify key value
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(CarrierWritable.class);

			// Specify input path
			FileInputFormat.addInputPath(job, new Path(args[0]));
			// SPecify output path
			Path outputDir = new Path(args[1] + "/carrier/all");
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
		}
		if (args[2].equals("2")) {
			Job job = Job.getInstance();
			Configuration config = job.getConfiguration();
			config.set("outformat", "byyear");
			job.setJarByClass(CarrierMapper.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			// set mapper
			job.setMapperClass(CarrierMapper.class);

			// set reducer
			job.setReducerClass(CarrierReducer.class);

			// Number of reducers
			job.setNumReduceTasks(1);

			// Specify key value
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(CarrierWritable.class);

			// Specify input path
			FileInputFormat.addInputPath(job, new Path(args[0]));
			// SPecify output path
			Path outputDir = new Path(args[1] + "/carrier/allbyyear");
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
		}

	}
}
