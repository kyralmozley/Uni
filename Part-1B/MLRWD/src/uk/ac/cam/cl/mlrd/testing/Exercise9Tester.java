package uk.ac.cam.cl.mlrd.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//TODO: Replace with your package.
import uk.ac.cam.cl.km687.exercises.Exercise9;
import uk.ac.cam.cl.km687.exercises.Exercise7;
import uk.ac.cam.cl.mlrd.exercises.markov_models.IExercise7;

import uk.ac.cam.cl.mlrd.exercises.markov_models.AminoAcid;
import uk.ac.cam.cl.mlrd.exercises.markov_models.Feature;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HMMDataStore;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrd.exercises.markov_models.IExercise9;

public class Exercise9Tester {

    static final Path dataFile = Paths.get("data/bio_dataset.txt");

   /* public static void main(String[] args) throws IOException {

        List<HMMDataStore<AminoAcid, Feature>> sequencePairs = HMMDataStore.loadBioFile(dataFile);

        // Use for testing the code
        Collections.shuffle(sequencePairs, new Random((long) 9));
        int testSize = sequencePairs.size() / 10;
        List<HMMDataStore<AminoAcid, Feature>> devSet = sequencePairs.subList(0, testSize);
        List<HMMDataStore<AminoAcid, Feature>> testSet = sequencePairs.subList(testSize, 2 * testSize);
        List<HMMDataStore<AminoAcid, Feature>> trainingSet = sequencePairs.subList(testSize * 2, sequencePairs.size());
        // But:
        // TODO: Replace with cross-validation for the tick.

        IExercise9 implementation = (IExercise9) new Exercise9();

        HiddenMarkovModel<AminoAcid, Feature> model = implementation.estimateHMM(trainingSet);
        System.out.println("Predicted transitions:");
        System.out.println(model.getTransitionMatrix());
        System.out.println();
        System.out.println("Predicted emissions:");
        System.out.println(model.getEmissionMatrix());
        System.out.println();

        HMMDataStore<AminoAcid, Feature> data = devSet.get(0);
        List<Feature> predicted = implementation.viterbi(model, data.observedSequence);
        System.out.println("True hidden sequence:");
        System.out.println(data.hiddenSequence);
        System.out.println();

        System.out.println("Predicted hidden sequence:");
        System.out.println(predicted);
        System.out.println();

        Map<List<Feature>, List<Feature>> true2PredictedSequences = implementation.predictAll(model, devSet);
        double accuracy = implementation.precision(true2PredictedSequences);
        System.out.println("Prediction precision:");
        System.out.println(accuracy);
        System.out.println();

        double recall = implementation.recall(true2PredictedSequences);
        System.out.println("Prediction recall:");
        System.out.println(recall);
        System.out.println();

        double f1Score = implementation.fOneMeasure(true2PredictedSequences);
        System.out.println("Prediction F1 score:");
        System.out.println(f1Score);
        System.out.println();
    }*/
   public static List<List<HMMDataStore<AminoAcid, Feature>>> splitIntoTenFolds(List<HMMDataStore<AminoAcid, Feature>> dataSet, int seed) {
       //Create 10 lists (folds) for each of the paths to be added to
       List<List<HMMDataStore<AminoAcid, Feature>>> result = new LinkedList<>();
       for (int i = 0; i < 10; i++) {
           List<HMMDataStore<AminoAcid, Feature>> newList = new LinkedList<>();
           result.add(newList);
       }

       //Generates a number between 0 and 1
       Random rng = new Random(seed);
       int initialListSize = dataSet.size() - 1;
       for (int i = initialListSize; i >= 0; i--) {
           int indexOfChosenPath = (int) rng.nextDouble() * i;
           int fold = i % 10;
           result.get(fold).add(dataSet.get(indexOfChosenPath));
           dataSet.remove(indexOfChosenPath);
       }

       return result;
   }

    public static void crossValidate(List<List<HMMDataStore<AminoAcid, Feature>>> folds) throws IOException {
        double[] accuracies = new double[10];
        IExercise7 implementation7 = (IExercise7) new Exercise7();
        IExercise9 implementation = (IExercise9) new Exercise9();
        double precision = 0;
        double recall = 0;
        double fOneMeasure = 0;
        for (int i = 0; i < accuracies.length; i++) {
            //Create test set for current accuracy
            List<HMMDataStore<AminoAcid, Feature>> trainingData = new LinkedList<>();
            List<HMMDataStore<AminoAcid, Feature>> testingData = new LinkedList<>();
            for (int j = 0; j < folds.size(); j++) {
                if (j != i)
                    trainingData.addAll(folds.get(j));
                else
                    testingData = folds.get(j);
            }
            //Perform training on training data i.e. generate transitions and emissions
            HiddenMarkovModel<AminoAcid, Feature> model = implementation.estimateHMM(testingData);

            //Test
            HMMDataStore<AminoAcid, Feature> data = trainingData.get(0);
            List<Feature> predicted = implementation.viterbi(model, data.observedSequence);
            Map<List<Feature>, List<Feature>> true2PredictedSequences = implementation.predictAll(model, testingData);

            precision += implementation.precision(true2PredictedSequences);
            recall += implementation.recall(true2PredictedSequences);
            fOneMeasure += implementation.fOneMeasure(true2PredictedSequences);
        }
        System.out.println("Prediction precision:");
        System.out.println(precision / 10.0);
        System.out.println();

        System.out.println("Prediction recall:");
        System.out.println(recall / 10.0);
        System.out.println();

        System.out.println("Prediction fOneMeasure:");
        System.out.println(fOneMeasure / 10.0);
        System.out.println();
    }

    public static void main(String[] args) throws IOException {

        List<HMMDataStore<AminoAcid, Feature>> sequencePairs = HMMDataStore.loadBioFile(dataFile);

        // Use for testing the code
        Collections.shuffle(sequencePairs, new Random(0));

        IExercise9 implementation = (IExercise9) new Exercise9();
        List<List<HMMDataStore<AminoAcid, Feature>>> tenFolds = splitIntoTenFolds(sequencePairs, 0);
        crossValidate(tenFolds);
    }

}