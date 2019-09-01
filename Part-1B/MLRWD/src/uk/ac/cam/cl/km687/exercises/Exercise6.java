package uk.ac.cam.cl.km687.exercises;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.*;

import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.NuancedSentiment;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.IExercise6;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Tokenizer;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.DataPreparation1;

import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.DataPreparation6;

public class Exercise6 implements IExercise6 {

    public Map<NuancedSentiment, Double> calculateClassProbabilities(Map<Path, NuancedSentiment> trainingSet) throws IOException {
        double pos = 0;
        double neg = 0;
        List<NuancedSentiment> valueSet = new LinkedList<>(trainingSet.values());
        for(NuancedSentiment s: valueSet) {
            if(s.equals(NuancedSentiment.POSITIVE))
                pos++;
            else if (s.equals(NuancedSentiment.NEGATIVE))
                neg++;
        }
        double p_pos = pos/valueSet.size();
        double p_neg = neg/valueSet.size();
        double p_neut = 1- p_neg - p_pos;

        Map<NuancedSentiment, Double> classProbabilties = new HashMap<>();
        classProbabilties.put(NuancedSentiment.POSITIVE, p_pos);
        classProbabilties.put(NuancedSentiment.NEGATIVE, p_neg);
        classProbabilties.put(NuancedSentiment.NEUTRAL, p_neut);

        return classProbabilties;
    }

    public Map<String, Map<NuancedSentiment, Double>> calculateNuancedLogProbs(Map<Path, NuancedSentiment> trainingSet) throws IOException {
        Set<Path> pathSet = trainingSet.keySet();
        Tokenizer tokenizer = new Tokenizer();

        Map<String, Map<NuancedSentiment, Double>> result = new HashMap<>();
        for(Path p: pathSet) {
            NuancedSentiment currentPathSentiment = trainingSet.get(p);
            List<String> wordsInPath = tokenizer.tokenize(p);
            for(String word: wordsInPath) {
                if(!result.containsKey(word)) {
                    Map<NuancedSentiment, Double> nuancedCount = new HashMap<>();
                    nuancedCount.put(NuancedSentiment.NEGATIVE, 1.0);
                    nuancedCount.put(NuancedSentiment.POSITIVE, 1.0);
                    nuancedCount.put(NuancedSentiment.NEUTRAL, 1.0);
                    result.put(word, nuancedCount);
                }
                Map<NuancedSentiment, Double> currentWordSentimentMap = result.get(word);
                if(currentPathSentiment.equals(NuancedSentiment.POSITIVE))
                    currentWordSentimentMap.put(NuancedSentiment.POSITIVE, currentWordSentimentMap.get(NuancedSentiment.POSITIVE) + 1);
                else if(currentPathSentiment.equals(NuancedSentiment.NEGATIVE))
                    currentWordSentimentMap.put(NuancedSentiment.NEGATIVE, currentWordSentimentMap.get(NuancedSentiment.NEGATIVE) + 1);
                else
                    currentWordSentimentMap.put(NuancedSentiment.NEUTRAL, currentWordSentimentMap.get(NuancedSentiment.NEUTRAL) +1);
            }
        }

        Set<String> wordsInLexicon = result.keySet();
        double totalPos = 0;
        double totalNeg = 0;
        double totalNeut = 0;

        for(String word: wordsInLexicon) {
            totalPos += result.get(word).get(NuancedSentiment.POSITIVE);
            totalNeg += result.get(word).get(NuancedSentiment.NEGATIVE);
            totalNeut += result.get(word).get(NuancedSentiment.NEUTRAL);
        }
        for(String word: wordsInLexicon) {
            Map<NuancedSentiment, Double> currentWordSentimentMap2 = result.get(word);
            currentWordSentimentMap2.put(NuancedSentiment.POSITIVE, Math.log(currentWordSentimentMap2.get(NuancedSentiment.POSITIVE) / totalPos));
            currentWordSentimentMap2.put(NuancedSentiment.NEGATIVE, Math.log(currentWordSentimentMap2.get(NuancedSentiment.NEGATIVE) / totalNeg));
            currentWordSentimentMap2.put(NuancedSentiment.NEUTRAL, Math.log(currentWordSentimentMap2.get(NuancedSentiment.NEUTRAL) / totalNeut));
        }
        return result;
    }


    public 	Map<Path, NuancedSentiment> nuancedClassifier(Set<Path> testSet, Map<String, Map<NuancedSentiment, Double>> tokenLogProbs, Map<NuancedSentiment, Double> classProbabilities)
            throws IOException{
        Map<Path, NuancedSentiment> result = new HashMap<>();

        Set<String> keySet = tokenLogProbs.keySet();
        for(Path p: testSet) {
            Tokenizer tokenizer = new Tokenizer();
            List<String> wordsInPath = tokenizer.tokenize(p);

            double posProb = Math.log(classProbabilities.get(NuancedSentiment.POSITIVE));
            double negProb = Math.log(classProbabilities.get(NuancedSentiment.NEGATIVE));
            double neutProb = Math.log(classProbabilities.get(NuancedSentiment.NEUTRAL));

            for (String word: wordsInPath) {
                if(tokenLogProbs.containsKey(word)) {
                    posProb += tokenLogProbs.get(word).get(NuancedSentiment.POSITIVE);
                    negProb += tokenLogProbs.get(word).get(NuancedSentiment.NEGATIVE);
                    neutProb += tokenLogProbs.get(word).get(NuancedSentiment.NEUTRAL);
                }
            }
            if(neutProb >= posProb && neutProb >= negProb)
                result.put(p, NuancedSentiment.NEUTRAL);
            else if(posProb >= neutProb && posProb >= negProb)
                result.put(p, NuancedSentiment.POSITIVE);
            else
                result.put(p, NuancedSentiment.NEGATIVE);
        }
        return result;
    }


    public double nuancedAccuracy(Map<Path, NuancedSentiment> trueSentiments,
                                  Map<Path, NuancedSentiment> predictedSentiments){
        double correct = 0;
        double incorrect = 0;
        for(Path p : trueSentiments.keySet()) {
            if(predictedSentiments.containsKey(p)) {
                if(predictedSentiments.get(p).equals(trueSentiments.get(p)))
                    correct++;
                else
                    incorrect++;
            }
        }
        double accuracy = correct / (correct + incorrect);
        return accuracy;
    }


    public Map<Integer, Map<Sentiment, Integer>> agreementTable(Collection<Map<Integer, Sentiment>> predictedSentiments){
        Map<Integer, Map<Sentiment, Integer>> result = new HashMap<>();
        List<Map<Integer, Sentiment>> personPrediction = new LinkedList<>(predictedSentiments);
        for(Map<Integer, Sentiment> currentPrediction: personPrediction) {
            for(int review : currentPrediction.keySet()) {
                Sentiment currentSentiment = currentPrediction.get(review);
                Map<Sentiment, Integer> count = result.getOrDefault(review, new HashMap<>());
                count.put(currentSentiment, count.getOrDefault(currentSentiment, 0) + 1);
                result.put(review, count);

            }
        }
        return result;
    }


    public double kappa(Map<Integer, Map<Sentiment, Integer>> agreementTable){
        double N = agreementTable.size();
        //n_ij is the number of predictions that item i belongs to class j
        Map<Integer, Map<Sentiment, Double>> n_ij = new HashMap<>();
        for(Integer currentReview: agreementTable.keySet()) {
            Map<Sentiment, Double> value = new HashMap<>();
            value.put(Sentiment.POSITIVE, (double) agreementTable.get(currentReview).getOrDefault(Sentiment.POSITIVE, 0));
            value.put(Sentiment.NEGATIVE, (double) agreementTable.get(currentReview).getOrDefault(Sentiment.NEGATIVE, 0));

            n_ij.put(currentReview, value);
        }
        System.out.println(n_ij);

        //calculating n_i, the total number of predictions for item i
        Map<Integer, Double> n_i = new HashMap<>();
        for(Integer review: agreementTable.keySet()) {
            n_i.put(review, n_ij.get(review).get(Sentiment.POSITIVE) + n_ij.get(review).get(Sentiment.NEGATIVE));
        }
        System.out.println(n_i);

        //calculate P_a, the mean of the proportions of prediction pairs which are in agreement
        //for all items

        double P_a = 0.0;
        for(Integer i: agreementTable.keySet()) {
            double P_a_summation = 0.0;
            for(Sentiment j : n_ij.get(i).keySet()) {
                P_a_summation += n_ij.get(i).get(j) * (n_ij.get(i).get(j) -1 );
            }
            P_a += P_a_summation /( n_i.get(i) * (n_i.get(i) -1));
        }
        P_a = P_a /N;

        //calcuatee P_e, the mean of the observed proportions of assignments to each class squared:
        double P_e = 0;
        for(Sentiment j: Sentiment.values()) {
            double P_e_summation = 0;
            for(Integer review: agreementTable.keySet()) {
                P_e_summation += n_ij.get(review).get(j) / n_i.get(review);
            }
            P_e += Math.pow(P_e_summation / N, 2);
        }
        double kappa = (P_a - P_e)/(1-P_e);
        return kappa;
    }
}