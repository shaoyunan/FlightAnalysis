package multipleoutput;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MyDriver {
	public static void main(String[] args) throws Exception {
		Job job = Job.getInstance();
		job.setJarByClass(MyDriver.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		
		
//		MultipleOutputs.addNamedOutput(job, "Delay", TextOutputFormat.class, Text.class, NullWritable.class);
//		MultipleOutputs.addNamedOutput(job, "Diverted", TextOutputFormat.class, Text.class, NullWritable.class);
//		MultipleOutputs.addNamedOutput(job, "CarrierCancel", TextOutputFormat.class, Text.class, NullWritable.class);
//		MultipleOutputs.addNamedOutput(job, "WeatherCancel", TextOutputFormat.class, Text.class, NullWritable.class);
//		MultipleOutputs.addNamedOutput(job, "NASCancel", TextOutputFormat.class, Text.class, NullWritable.class);
//		MultipleOutputs.addNamedOutput(job, "SecurityCancel", TextOutputFormat.class, Text.class, NullWritable.class);
		//job.setNumReduceTasks(1);
		LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);  
		
		Path outputDir = new Path(args[1]+"/preprocess/reduce");
		FileOutputFormat.setOutputPath(job,outputDir);
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
