package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Tokenizer;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.IExercise2;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Exercise2 implements IExercise2 {

    public Map<Sentiment, Double> calculateClassProbabilities(Map<Path, Sentiment> trainingSet) throws IOException {
        double pos = 0;
        List<Sentiment> valueSet = new LinkedList<>(trainingSet.values());
        for (Sentiment s : valueSet) {
            if (s.equals(Sentiment.POSITIVE))
                pos++;
        }
        double p_pos = pos / valueSet.size();
        double p_neg = 1 - p_pos;

        Map<Sentiment, Double> classProbabilities = new HashMap<>();
        classProbabilities.put(Sentiment.POSITIVE, p_pos);
        classProbabilities.put(Sentiment.NEGATIVE, p_neg);

        return classProbabilities;
    }

    public Map<String, Map<Sentiment, Double>> calculateUnsmoothedLogProbs(Map<Path, Sentiment> trainingSet) throws IOException {
        Set<Path> pathSet = trainingSet.keySet();
        Tokenizer tokenizer = new Tokenizer();
        //Describes, for each word, the NUMBER of times the word appears
        //among all words in all documents, for each of positive and negative sentiments
        Map<String, Map<Sentiment, Double>> result = new HashMap<>();
        for (Path p : pathSet) {
            Sentiment currentPathSentiment = trainingSet.get(p);
            List<String> wordsInPath = tokenizer.tokenize(p);
            for (String word : wordsInPath) {
                //add word if it doesn't exit
                if (!result.containsKey(word)) {
                    //set count to 0 for +ve and -ve
                    Map<Sentiment, Double> posnegCount = new HashMap<>();
                    posnegCount.put(Sentiment.POSITIVE, 0.0);
                    posnegCount.put(Sentiment.NEGATIVE, 0.0);
                    result.put(word, posnegCount);
                }

                //Now, the current word must exist in the map, so we increment the relevant count
                Map<Sentiment, Double> currentWordSentimentMap = result.get(word);
                if (currentPathSentiment.equals(Sentiment.POSITIVE))
                    currentWordSentimentMap.put(Sentiment.POSITIVE, currentWordSentimentMap.get(Sentiment.POSITIVE) + 1);
                else
                    currentWordSentimentMap.put(Sentiment.NEGATIVE, currentWordSentimentMap.get(Sentiment.NEGATIVE) + 1);
            }

        }
        //Convert result such that it describes, for each word, the FRACTION of times the word appears
        //among all words in all documents, for each of positive and negative sentiments
        Set<String> wordsInLexicon = result.keySet();
        double totalPos = 0;
        double totalNeg = 0;
        for (String word : wordsInLexicon) {
            totalPos += result.get(word).get(Sentiment.POSITIVE);
            totalNeg += result.get(word).get(Sentiment.NEGATIVE);

        }
        for (String word : wordsInLexicon) {
            Map<Sentiment, Double> currentWorldSentimentMap2 = result.get(word);
            currentWorldSentimentMap2.put(Sentiment.POSITIVE, Math.log(currentWorldSentimentMap2.get(Sentiment.POSITIVE) / totalPos));
            currentWorldSentimentMap2.put(Sentiment.NEGATIVE, Math.log(currentWorldSentimentMap2.get(Sentiment.NEGATIVE) / totalNeg));
        }
        System.out.println(result.toString());
        return result;
    }

    public Map<String, Map<Sentiment, Double>> calculateSmoothedLogProbs(Map<Path, Sentiment> trainingSet) throws IOException {       Set<Path> pathSet = trainingSet.keySet();
        Tokenizer tokenizer = new Tokenizer();
        //Describes, for each word, the NUMBER of times the word appears
        //among all words in all documents, for each of positive and negative sentiments
        Map<String, Map<Sentiment, Double>> result = new HashMap<>();
        for (Path p : pathSet) {
            Sentiment currentPathSentiment = trainingSet.get(p);
            List<String> wordsInPath = tokenizer.tokenize(p);
            for (String word : wordsInPath) {
                //add word if it doesn't exit
                if (!result.containsKey(word)) {
                    //set count to 1 for +ve and -ve (plus one smoothing)
                    Map<Sentiment, Double> posnegCount = new HashMap<>();
                    posnegCount.put(Sentiment.POSITIVE, 1.0);
                    posnegCount.put(Sentiment.NEGATIVE, 1.0);
                    result.put(word, posnegCount);
                }

                //Now, the current word must exist in the map, so we increment the relevant count
                Map<Sentiment, Double> currentWordSentimentMap = result.get(word);
                if (currentPathSentiment.equals(Sentiment.POSITIVE))
                    currentWordSentimentMap.put(Sentiment.POSITIVE, currentWordSentimentMap.get(Sentiment.POSITIVE) + 1);
                else
                    currentWordSentimentMap.put(Sentiment.NEGATIVE, currentWordSentimentMap.get(Sentiment.NEGATIVE) + 1);
            }

        }
        //Convert result such that it describes, for each word, the FRACTION of times the word appears
        //among all words in all documents, for each of positive and negative sentiments
        Set<String> wordsInLexicon = result.keySet();
        double totalPos = 0;
        double totalNeg = 0;
        for (String word : wordsInLexicon) {
            totalPos += result.get(word).get(Sentiment.POSITIVE);
            totalNeg += result.get(word).get(Sentiment.NEGATIVE);

        }
        for (String word : wordsInLexicon) {
            Map<Sentiment, Double> currentWorldSentimentMap2 = result.get(word);
            currentWorldSentimentMap2.put(Sentiment.POSITIVE, Math.log(currentWorldSentimentMap2.get(Sentiment.POSITIVE) / totalPos));
            currentWorldSentimentMap2.put(Sentiment.NEGATIVE, Math.log(currentWorldSentimentMap2.get(Sentiment.NEGATIVE) / totalNeg));
        }
        System.out.println(result.toString());
        return result;
    }

    public Map<Path, Sentiment> naiveBayes(Set<Path> testSet, Map<String, Map<Sentiment, Double>> tokenLogProbs, Map<Sentiment, Double> classProbabilities) throws IOException {
        Map<Path, Sentiment> result = new HashMap<>();

        //Apply formula to each path
        Set<String> keySet = tokenLogProbs.keySet();
        for(Path p : testSet) {
            Tokenizer tokenizer = new Tokenizer();
            List<String> wordsInPath = tokenizer.tokenize(p);

            double positiveProbability = Math.log(classProbabilities.get(Sentiment.POSITIVE));
            double negativeProbability = Math.log(classProbabilities.get(Sentiment.NEGATIVE));
            for (String word : wordsInPath) {
                if (tokenLogProbs.containsKey(word)) {
                    positiveProbability += tokenLogProbs.get(word).get(Sentiment.POSITIVE);
                    negativeProbability+= tokenLogProbs.get(word).get(Sentiment.NEGATIVE);
                }
            }
            if (positiveProbability >= negativeProbability)
                result.put(p, Sentiment.POSITIVE);

            else
                result.put(p, Sentiment.NEGATIVE);
        }
        System.out.println("Naive Bayes Classifier prediction:\n" + result);
        return result;
    }
}
