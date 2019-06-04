package mlpre;

import java.io.IOException;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class MyDriver {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

		Job job = Job.getInstance();
		job.setJarByClass(MyMapper.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Text.class);


		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		job.setNumReduceTasks(1);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		Path outputDir = new Path(args[1] + "/mlpre");
		FileOutputFormat.setOutputPath(job, outputDir);

		FileSystem hdfs = FileSystem.get(job.getConfiguration());
		if (hdfs.exists(outputDir)) {
			hdfs.delete(outputDir, true);
		}

		boolean succeed = job.waitForCompletion(true);

		if (job.isSuccessful()) {
			System.out.println("Job was successful, Change to CSV");
		} else if (!job.isSuccessful()) {
			System.out.println("Job was not successful");
		}

		if(succeed) {

		    FileStatus fs[] = hdfs.listStatus(outputDir);
		    if (fs != null){ 
		        for (FileStatus aFile : fs) {
		            if (!aFile.isDir()) {
		                hdfs.rename(aFile.getPath(), new Path(aFile.getPath().toString()+".csv"));
		            }
		        }
		    }
		}
		
		System.exit(1);

	}

}
