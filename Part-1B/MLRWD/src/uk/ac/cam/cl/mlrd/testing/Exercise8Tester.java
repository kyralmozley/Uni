package uk.ac.cam.cl.mlrd.testing;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//TODO: Replace with your package.
import uk.ac.cam.cl.km687.exercises.Exercise7;
import uk.ac.cam.cl.km687.exercises.Exercise8;
import uk.ac.cam.cl.mlrd.exercises.markov_models.DiceRoll;
import uk.ac.cam.cl.mlrd.exercises.markov_models.DiceType;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HMMDataStore;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrd.exercises.markov_models.IExercise7;
import uk.ac.cam.cl.mlrd.exercises.markov_models.IExercise8;

public class Exercise8Tester {

    static final Path dataDirectory = Paths.get("data/dice_dataset");

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        List<Path> sequenceFiles = new ArrayList<>();
        try (DirectoryStream<Path> files = Files.newDirectoryStream(dataDirectory)) {
            for (Path item : files) {
                sequenceFiles.add(item);
            }
        } catch (IOException e) {
            throw new IOException("Cant access the dataset.", e);
        }

        // Use for testing the code
        Collections.shuffle(sequenceFiles, new Random(0));
        int testSize = sequenceFiles.size() / 10;
        List<Path> devSet = sequenceFiles.subList(0, testSize);
        List<Path> testSet = sequenceFiles.subList(testSize, 2 * testSize);
        List<Path> trainingSet = sequenceFiles.subList(testSize * 2, sequenceFiles.size());
        // But:
        // TODO: Replace with cross-validation for the tick.


        IExercise7 implementation7 = (IExercise7) new Exercise7();
        HiddenMarkovModel<DiceRoll, DiceType> model = implementation7.estimateHMM(trainingSet);

        IExercise8 implementation = (IExercise8) new Exercise8();

        HMMDataStore<DiceRoll, DiceType> data = HMMDataStore.loadDiceFile(devSet.get(0));
        List<DiceType> predicted = implementation.viterbi(model, data.observedSequence);
        System.out.println("True hidden sequence:");
        System.out.println(data.hiddenSequence);
        System.out.println();

        System.out.println("Predicted hidden sequence:");
        System.out.println(predicted);
        System.out.println();

        Map<List<DiceType>, List<DiceType>> true2PredictedMap = implementation.predictAll(model, devSet);
        double precision = implementation.precision(true2PredictedMap);
        System.out.println("Prediction precision:");
        System.out.println(precision);
        System.out.println();

        double recall = implementation.recall(true2PredictedMap);
        System.out.println("Prediction recall:");
        System.out.println(recall);
        System.out.println();

        double fOneMeasure = implementation.fOneMeasure(true2PredictedMap);
        System.out.println("Prediction fOneMeasure:");
        System.out.println(fOneMeasure);
        System.out.println();
        //List<List<Path>> tenFolds = splitIntoTenFolds(sequenceFiles, 0);
        //crossValidate(tenFolds);
    }

    private static List<List<Path>> splitIntoTenFolds(List<Path> dataSet, int seed) {
        List<List<Path>> result = new LinkedList<>();
        for(int i=0; i<10; i++) {
            List<Path> newList = new LinkedList<>();
            result.add(newList);
        }

        Random ran = new Random(seed);
        int initailListSize = dataSet.size() - 1;
        for(int i= initailListSize; i>=0; i--) {
            int index = (int) ran.nextDouble()*i;
            int fold = i%10;
            result.get(fold).add(dataSet.get(index));
            dataSet.remove(index);
        }
        return result;
    }

    public static void crossValidate(List<List<Path>> folds) throws IOException {
        double[] accuracies = new double[10];
        IExercise7 implementation7 = (IExercise7) new Exercise7();
        IExercise8 implementation = (IExercise8) new Exercise8();
        double precision = 0;
        double recall = 0;
        double fOneMeasure = 0;
        for (int i = 0; i < accuracies.length; i++) {
            //Create test set for current accuracy
            List<Path> trainingData = new LinkedList<>();
            List<Path> testingData = new LinkedList<>();
            for (int j = 0; j < folds.size(); j++) {
                if (j != i)
                    trainingData.addAll(folds.get(j));
                else
                    testingData = folds.get(j);
            }
            //Perform training on training data - generate transitions and emissions
            HiddenMarkovModel<DiceRoll, DiceType> model = implementation7.estimateHMM(testingData);
            HMMDataStore<DiceRoll, DiceType> data = HMMDataStore.loadDiceFile(trainingData.get(0));

            Map<List<DiceType>, List<DiceType>> true2PredictedMap = implementation.predictAll(model, trainingData);

            precision += implementation.precision(true2PredictedMap);
            recall += implementation.recall(true2PredictedMap);
            fOneMeasure += implementation.fOneMeasure(true2PredictedMap);
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
}