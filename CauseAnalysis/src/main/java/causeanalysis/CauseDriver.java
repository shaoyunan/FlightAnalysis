package causeanalysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class CauseDriver {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Job job = Job.getInstance();
		job.setJarByClass(CauseMapper.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// set mapper
		job.setMapperClass(CauseMapper.class);

		job.setCombinerClass(CauseCombiner.class);
		// set reducer
		job.setReducerClass(CauseReducer.class);

		// Number of reducers
		job.setNumReduceTasks(1);

		// Specify key value
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(SortedMapWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// Specify input path
		FileInputFormat.addInputPath(job, new Path(args[0]));
		// SPecify output path
		Path outputDir = new Path(args[1]+"/cause");
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
