package Lab5;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.api.writable.Writable;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoundaryFollowingAgent {
    public static void main(String[] args) throws Exception {
        int numInputs = 8;
        int numOutputs = 2; // Left and right motor speeds
        int numHiddenNodes = 64;
        int numEpochs = 1000;
        int miniBatchSize = 32;
        double learningRate = 0.001;

        // incarca si prepare training data-ul (csv file cu sensos inputs & motor outputs)
        File dataFile = new File("training_data.csv");
        InputSplit dataSplit = new FileSplit(dataFile);
        RecordReader recordReader = new CSVRecordReader();
        recordReader.initialize(dataSplit);
        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader, miniBatchSize, 0, 2);

        // definire rnn
        MultiLayerNetwork network = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(12345)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(learningRate))
                .l2(1e-4)
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue)
                .gradientNormalizationThreshold(1.0)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numInputs)
                        .nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MEAN_SQUARED_LOGARITHMIC_ERROR)
                        .nIn(numHiddenNodes)
                        .nOut(numOutputs)
                        .activation(Activation.IDENTITY)
                        .build())
                .backpropType(BackpropType.Standard)
                .pretrain(false)
                .backprop(true)
                .build());

        network.init();

        // training rnn
        for (int epoch = 0; epoch < numEpochs; epoch++) {
            while (dataSetIterator.hasNext()) {
                DataSet dataSet = dataSetIterator.next();
                network.fit(dataSet);
            }
            dataSetIterator.reset();
        }

        // eval model antrenat
        Evaluation evaluation = new Evaluation(numOutputs);
        while (dataSetIterator.hasNext()) {
            DataSet dataSet = dataSetIterator.next();
            INDArray features = dataSet.getFeatures();
            INDArray labels = dataSet.getLabels();
            INDArray predicted = network.output(features, false);

            evaluation.eval(labels, predicted);
        }

        // rezultate eval
        System.out.println("Evaluation Results:");
        System.out.println(evaluation.stats());

        // exemplu de input si output
        INDArray input = Nd4j.create(new double[]{s1, s2, s3, s4, s5, s6, s7, s8}); // Sensor inputs
        INDArray output = network.output(input, false); // Motor control signals 
        System.out.println("Motor Control Output:");
        System.out.println(output);
    }
}
