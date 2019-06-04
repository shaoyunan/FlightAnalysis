package carrieranalysis;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CarrierReducer extends Reducer<Text, CarrierWritable, Text, CarrierWritable> {

	@Override
	protected void reduce(Text key, Iterable<CarrierWritable> values,
			Reducer<Text, CarrierWritable, Text, CarrierWritable>.Context context)
			throws IOException, InterruptedException {

		long totalCount = 0;
		long delayCount = 0;
		long ddTotal = 0;
		long cancelCount = 0;
		int ddMin = Integer.MAX_VALUE;
		int ddMax = Integer.MIN_VALUE;

		for (CarrierWritable value : values) {
			if (value.getDelayCount() == 1) {

				if (value.getDepDelayMin() < ddMin) {
					ddMin = value.getDepDelayMin();
				}
				if (value.getDepDelayMin() > ddMax) {
					ddMax = value.getDepDelayMax();
				}
				ddTotal += value.getDepDelayMax();
				delayCount += 1;
			}
			if (value.getCancellationCount() == 1) {
				cancelCount += 1;
			}

			totalCount += 1;
		}
		CarrierWritable out = new CarrierWritable(ddMin, ddMax, delayCount, totalCount, cancelCount);

		double rate = (double) delayCount / totalCount;
		double rateResult = BigDecimal.valueOf(rate).setScale(3, RoundingMode.HALF_UP).doubleValue();
		double avgDelay = (double) ddTotal / delayCount;
		double avgResult = BigDecimal.valueOf(avgDelay).setScale(3, RoundingMode.HALF_UP).doubleValue();
		double avgCancel = (double) cancelCount / totalCount;
		double cancelResult = BigDecimal.valueOf(avgCancel).setScale(3, RoundingMode.HALF_UP).doubleValue();
		out.setDelayRate(rateResult);
		out.setAvgDelayTime(avgResult);
		out.setCancelRate(cancelResult);
		context.write(key, out);
	}

}
