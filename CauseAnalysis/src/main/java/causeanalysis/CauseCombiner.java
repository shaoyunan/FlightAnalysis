package causeanalysis;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SortedMapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Reducer;

public class CauseCombiner extends Reducer<Text, SortedMapWritable, Text, SortedMapWritable> {

	@Override
	protected void reduce(Text key, Iterable<SortedMapWritable> values,
			Reducer<Text, SortedMapWritable, Text, SortedMapWritable>.Context context)
			throws IOException, InterruptedException {

		SortedMapWritable out = new SortedMapWritable();

		for (SortedMapWritable value : values) {

			for (Entry<WritableComparable, Writable> entry : value.entrySet()) {

				IntWritable count = (IntWritable) out.get(entry.getKey());

				if (count != null) {
					count.set(count.get() + ((IntWritable) entry.getValue()).get());
				} else {
					out.put(entry.getKey(), new IntWritable(((IntWritable) entry.getValue()).get()));
				}
			}
			value.clear();
		}
		context.write(key, out);
	}

}
