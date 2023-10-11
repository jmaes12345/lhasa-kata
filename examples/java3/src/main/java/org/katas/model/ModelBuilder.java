package org.katas.model;

import java.io.IOException;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class ModelBuilder
{
	private static final int CLASSES_COUNT = 3;
	private static final int FEATURES_COUNT = 4;
	private static final int HIDDEN_LAYER_1_NODE_COUNT = 3;

	public void build() {
		var allData = readData();
		normaliseData(allData);

		SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);
		DataSet trainingData = testAndTrain.getTrain();
		DataSet testData = testAndTrain.getTest();

		MultiLayerConfiguration configuration
				= new NeuralNetConfiguration.Builder()
				.activation(Activation.TANH)
				.weightInit(WeightInit.XAVIER)
				.updater(new Sgd(0.1))
				.l2(0.0001)
				.list()
				.layer(0, new DenseLayer.Builder().nIn(FEATURES_COUNT).nOut(HIDDEN_LAYER_1_NODE_COUNT).build())
				.layer(1, new DenseLayer.Builder().nIn(HIDDEN_LAYER_1_NODE_COUNT).nOut(CLASSES_COUNT).build())
				.layer(2, new OutputLayer.Builder(
						LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.activation(Activation.SOFTMAX)
						.nIn(CLASSES_COUNT).nOut(CLASSES_COUNT).build())
				.build();

		MultiLayerNetwork model = new MultiLayerNetwork(configuration);
		model.init();
		model.fit(trainingData);

		INDArray output = model.output(testData.getFeatures());
		Evaluation eval = new Evaluation(3);
		eval.eval(testData.getLabels(), output);

		System.out.println(eval.stats());
	}

	public DataSet readData() {
		try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
			recordReader.initialize(new FileSplit(
					new ClassPathResource("iris.txt").getFile()));

			DataSetIterator iterator = new RecordReaderDataSetIterator(
					recordReader, 150, FEATURES_COUNT, CLASSES_COUNT);
			DataSet allData = iterator.next();
			allData.shuffle(42L);
			return allData;
		}
		catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Transform data into Normal distribution
	 */
	public void normaliseData(DataSet allData) {
		DataNormalization normalizer = new NormalizerStandardize();
		normalizer.fit(allData);
		normalizer.transform(allData);
	}
}
