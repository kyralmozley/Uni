package uk.ac.cam.cl.km687.exercises;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.*;

import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.IExercise5;
import uk.ac.cam.cl.km687.exercises.Exercise2;
import uk.ac.cam.cl.km687.exercises.Exercise1;

public class Exercise5 implements IExercise5 {

    private Exercise2 ex2 = new Exercise2();
    private Exercise1 ex1 = new Exercise1();

    public List<Map<Path, Sentiment>> splitCVRandom(Map<Path, Sentiment> dataSet, int seed) {
        //select randomly from list
        List <Path> pathList = new LinkedList<>(dataSet.keySet());
        return splitTo10Folds(dataSet, seed, pathList);
    }

    private List<Map<Path, Sentiment>> splitTo10Folds(Map<Path, Sentiment> dataset, int seed, List<Path> pathList) {
        //create 10 folds for each path
        List<Map<Path, Sentiment>> result = new LinkedList<>();
        for(int i=0; i<10; i++) {
            Map<Path, Sentiment> newMap = new HashMap<>();
            result.add(newMap);
        }

        Random ran = new Random(seed);
        int size = pathList.size() -1;
        for(int i=size; i>=0; i--) {
            int index = (int) ran.nextDouble()*i;
            int fold = i%10;
            result.get(fold).put(pathList.get(index), dataset.get(pathList.get(index)));
            pathList.remove(index);
        }

        return result;
    }



    public List<Map<Path, Sentiment>> splitCVStratifiedRandom(Map<Path, Sentiment> dataSet, int seed) {
        //split +ve -ve into 10 equal
        List<Path> pos = new LinkedList<>();
        List<Path> neg = new LinkedList<>();
        for(Path p: dataSet.keySet()) {
            if(dataSet.get(p).equals(Sentiment.POSITIVE)) pos.add(p);
            if(dataSet.get(p).equals(Sentiment.NEGATIVE)) neg.add(p);
        }

        List<Map<Path, Sentiment>> posFold = splitTo10Folds(dataSet, seed, pos);
        List<Map<Path, Sentiment>> negFold = splitTo10Folds(dataSet, seed, neg);
        List<Map<Path, Sentiment>> result = new LinkedList<>();

        for(int i=0; i<10; i++) {
            Map<Path, Sentiment> fold = new HashMap<>();
            fold.putAll(posFold.get(i));
            fold.putAll(negFold.get(i));
            result.add(fold);
        }
        return result;
    }


    public double[] crossValidate(List<Map<Path, Sentiment>> folds) throws IOException {
        double[] accuracies = new double[10];
        for(int i=0; i<accuracies.length; i++) {
            Map<Path, Sentiment> trainingData = new HashMap<>();
            Map<Path, Sentiment> testData = new HashMap<>();
            for(int j=0; j<folds.size(); j++) {
                if(j != i) {
                    trainingData.putAll(folds.get(j));
                } else {
                    testData = folds.get(j);
                }
            }
            Path dataDirectory = Paths.get("data/sentiment_dataset");
            Path lexiconFile = Paths.get("data/sentiment_lexicon");
            Path sentimentFile = dataDirectory.resolve("review_sentiment");

            // NB
            Map<String, Map<Sentiment, Double>> smoothLogProb = ex2.calculateSmoothedLogProbs(trainingData);
            Map<Sentiment, Double> classProb = ex2.calculateClassProbabilities(trainingData);
            Map<Path, Sentiment> smoothNB = ex2.naiveBayes(testData.keySet(), smoothLogProb, classProb);
            double smoothNBAccuracy = ex1.calculateAccuracy(testData, smoothNB);
            accuracies[i] = smoothNBAccuracy;
        }

        return accuracies;
    }


    public double cvAccuracy(double[] scores) {
        double sum = 0;
        for(double accuracy: scores) {
            sum += accuracy;
        }
        return sum/scores.length;
    }


    public double cvVariance(double[] scores) {
        double av = cvAccuracy(scores);
        double sum = 0;
        for(double accuracy: scores) {
            sum += Math.pow(accuracy-av, 2);
        }
        return (1.0 / scores.length)*sum;
    }
}