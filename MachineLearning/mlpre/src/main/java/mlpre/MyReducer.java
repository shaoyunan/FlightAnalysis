package mlpre;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MyReducer extends Reducer<NullWritable, Text, Text, NullWritable>{

	@Override
	protected void reduce(NullWritable key, Iterable<Text> values,
			Reducer<NullWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
		for(Text value:values) {
			context.write(value, key);
		}
	}

}
