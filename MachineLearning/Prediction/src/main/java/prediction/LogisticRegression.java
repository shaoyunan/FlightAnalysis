package prediction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.L2;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

public class LogisticRegression {
	public static void main(String[] args) {
		LogisticRegression logisticRegression = new LogisticRegression();

		// Load the input data
		List<Observation> trainingData = logisticRegression.parseInputFile(args[0] + "/part-r-00000.csv");

		// Train a model
		OnlineLogisticRegression olr = logisticRegression.train(trainingData);

		// Test the model
		logisticRegression.testModel(olr);
	}

	public List<Observation> parseInputFile(String inputFile) {
		List<Observation> result = new ArrayList<Observation>();
		BufferedReader br = null;
		String line = "";
		try {
			// Load the file which contains training data
			System.out.println("Start Importing");
			br = new BufferedReader(new FileReader(new File(inputFile)));
			// Skip the first line which contains the header values
			// line = br.readLine();
			// Prepare the observation data
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				result.add(new Observation(values));
				// System.out.println(result.size());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public OnlineLogisticRegression train(List<Observation> trainData) {
		// System.out.println(trainData.size());
		System.out.println("Start Training");
		OnlineLogisticRegression olr = new OnlineLogisticRegression(2, 7, new L1());
		// Train the model
		for (int pass = 0; pass < 5; pass++) {
			for (Observation observation : trainData) {
				olr.train(observation.getActual(), observation.getVector());
			}

			if (pass % 1 == 0) {
				Auc eval = new Auc(0.5);
				for (Observation observation : trainData) {
					eval.add(observation.getActual(), olr.classifyScalar(observation.getVector()));
				}

				System.out.format("Pass: %2d, Accuracy: %2.4f\n", pass + 1, eval.auc());
			}
		}
		return olr;
	}

	void testModel(OnlineLogisticRegression olr) {
		Observation newObservation = new Observation(new String[] { "12", "5", "19", "EV", "LAS", "PHX", "0" });
		Vector result = olr.classifyFull(newObservation.getVector());

		System.out.println("------------- Testing -------------");
		System.out.format("Probability of not Delay (0) = %.3f\n", result.get(0));
		System.out.format("Probability of Delay (1)     = %.3f\n", result.get(1));
	}

	class Observation {
		private DenseVector vector = new DenseVector(7);
		private int actual;

		public Observation(String[] values) {
			ConstantValueEncoder bias = new ConstantValueEncoder("intercept");
			FeatureVectorEncoder monthEncoder = new StaticWordValueEncoder("month");
			FeatureVectorEncoder dowEncoder = new StaticWordValueEncoder("dayofweek");
			FeatureVectorEncoder hourEncoder = new StaticWordValueEncoder("hour");
			FeatureVectorEncoder carrierEncoder = new StaticWordValueEncoder("carrier");
			FeatureVectorEncoder originEncoder = new StaticWordValueEncoder("origin");
			FeatureVectorEncoder destEncoder = new StaticWordValueEncoder("dest");
			// FeatureVectorEncoder fligthnEncoder = new
			// StaticWordValueEncoder("flightnumber");

			bias.addToVector("1", vector);

			monthEncoder.addToVector(values[0], vector);
			dowEncoder.addToVector(values[1], vector);
			hourEncoder.addToVector(values[2], vector);
			carrierEncoder.addToVector(values[3], vector);
			// fligthnEncoder.addToVector(values[4], vector);
			originEncoder.addToVector(values[4], vector);
			destEncoder.addToVector(values[5], vector);

			this.actual = Integer.valueOf(values[6]);
		}

		public Vector getVector() {
			return vector;
		}

		public int getActual() {
			return actual;
		}
	}
}
