package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.IExercise1;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Exercise1 implements IExercise1 {
    public Map<Path, Sentiment> simpleClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        Map<String, Sentiment> lexicon = lexiconFileToMap(lexiconFile);
        Map<Path, Sentiment> result = new HashMap<>();
        Tokenizer tonkenizer = new Tokenizer();

        for(Path p : testSet) {
            List<String> review = tonkenizer.tokenize(p);
            int pos =0, neg=0;

            for(String word : review) {
                word = word.toLowerCase();
                if(lexicon.containsKey(word)) {
                    if(lexicon.get(word).equals(Sentiment.POSITIVE)) {
                        pos++;
                    } else {
                        neg++;
                    }
                }
            }

            if(pos >= neg) {
                result.put(p, Sentiment.POSITIVE);
            } else {
                result.put(p, Sentiment.NEGATIVE);
            }
        }
        return result;
    }

    private Map<String, Sentiment> lexiconFileToMap(Path lexiconFile) {
        Map<String, Sentiment> lexicon = new HashMap<>();
        try {
            //format: "word=foul intensity=weak polarity=negative"
            BufferedReader br = new BufferedReader(new FileReader(lexiconFile.toFile()));

            String line;
            while ((line = br.readLine()) != null) {

                String[] lexiconLine = line.split(" ");
                String[] word = lexiconLine[0].split("=");
                String[] polarity = lexiconLine[2].split("=");
                if (polarity[1].equals("positive"))
                    lexicon.put(word[1], Sentiment.POSITIVE);
                else if (polarity[1].equals("negative"))
                    lexicon.put(word[1], Sentiment.NEGATIVE);
            }

            return lexicon;
        }
        catch(Exception e) {
            //do something
        }
        return lexicon;
    }

    private Map<String, String> lexiconFilePolarity(Path lexiconFile) {
        Map<String, String> lexicon = new HashMap<>();
        try {
            //format: "word=foul intensity=weak polarity=negative"
            BufferedReader br = new BufferedReader(new FileReader(lexiconFile.toFile()));

            String line;
            while ((line = br.readLine()) != null) {

                String[] lexiconLine = line.split(" ");
                String[] word = lexiconLine[0].split("=");
                String[] intensity = lexiconLine[1].split("=");
                lexicon.put(word[1], intensity[1]);
            }

            return lexicon;
        }
        catch(Exception e) {
            //do something
        }
        return lexicon;
    }


    public double calculateAccuracy(Map<Path, Sentiment> trueSentiments, Map<Path, Sentiment> predictedSentiments) {
        List<Sentiment> trueSent = new LinkedList<>(trueSentiments.values());
        List<Sentiment> predSent = new LinkedList<>(predictedSentiments.values());

        double right = 0, wrong = 0;

        for(int i=0; i<trueSent.size(); i++) {
            if(trueSent.get(i).equals(predSent.get(i))) right++;
            else wrong++;
        }

        return (right / (wrong+right));
    }

    public Map<Path, Sentiment> improvedClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        Map<String, Sentiment>lexicon = lexiconFileToMap(lexiconFile);
        Map<String, String> lexiconPolarity = lexiconFilePolarity(lexiconFile);
        Map<Path, Sentiment> result = new HashMap<>();
        Tokenizer tokenizer = new Tokenizer();

        /* ORIGINAL TICK 1
        for (Path p : testSet) {
            List<String> review = tokenizer.tokenize(p);

            int pos=0, neg=0;
            int strongWeighting = 3;
            int weakWeighting = 1;
            int threshold = 19;

            for (String word: review) {
                word = word.toLowerCase();

                if (lexicon.containsKey(word)) {
                    if(lexicon.get(word).equals(Sentiment.POSITIVE)) {
                        if(lexiconPolarity.get(word).equals("strong")) {
                            pos += strongWeighting;
                        } else {
                            pos += weakWeighting;
                        }
                    } else {
                        if(lexiconPolarity.get(word).equals("strong")) {
                            neg += strongWeighting;
                        } else {
                            neg += weakWeighting;
                        }
                    }
                }
            }

            if (pos - neg > threshold) {
                result.put(p, Sentiment.POSITIVE);
            } else {
                result.put(p, Sentiment.NEGATIVE);
            }
        } */


        //FOR TICK 4
        for (Path p : testSet) {
            List<String> review = tokenizer.tokenize(p);

            int pos=0, neg=0;
            int strongWeighting = 2;
            int weakWeighting = 1;

            for (String word: review) {
                word = word.toLowerCase();

                if (lexicon.containsKey(word)) {
                    if(lexicon.get(word).equals(Sentiment.POSITIVE)) {
                        if(lexiconPolarity.get(word).equals("strong")) {
                            pos += strongWeighting;
                        } else {
                            pos += weakWeighting;
                        }
                    } else {
                        if(lexiconPolarity.get(word).equals("strong")) {
                            neg += strongWeighting;
                        } else {
                            neg += weakWeighting;
                        }
                    }
                }
            }

            if (pos >= neg) {
                result.put(p, Sentiment.POSITIVE);
            } else {
                result.put(p, Sentiment.NEGATIVE);
            }
        }


        return result;
    }


}