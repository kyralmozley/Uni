package uk.ac.cam.cl.km687.exercises;

import edu.stanford.nlp.ling.Word;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Tokenizer;
import uk.ac.cam.cl.mlrd.utils.BestFit;
import uk.ac.cam.cl.mlrd.utils.BestFit.*;
import uk.ac.cam.cl.mlrd.utils.ChartPlotter;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Exercise3 {

    static final Path dataset = Paths.get("data/large_dataset");
    private Line bestFit;

    //Tast 1: 1. Find frequencies of all unique words in dataset
    private List<WordFreq> findFreqTokens() {
        Set<Path> dataset = readInData();
        Tokenizer tokenizer = new Tokenizer();
        Map<String, Integer> wordFrequencies = new HashMap<>();

        try {
            for(Path p : dataset) {
                List<String> words = tokenizer.tokenize(p);
                for(String word: words) {
                    if(wordFrequencies.containsKey(word)) {
                        wordFrequencies.put(word, wordFrequencies.get(word) + 1);
                    } else {
                        wordFrequencies.put(word, 1);
                    }
                }
            }
        } catch (IOException e) {
            //do something?
        }

        //rank words
        List<String> orderedFreq = new ArrayList<>(wordFrequencies.keySet());
        Collections.sort(orderedFreq, (word1, word2) -> wordFrequencies.get(word2) - wordFrequencies.get(word1));

        List<WordFreq> rankFreq = new ArrayList<>();
        for(int i=0; i < orderedFreq.size(); i++) {
            String word = String.valueOf(orderedFreq.get(i));
            rankFreq.add(new WordFreq(word, wordFrequencies.get(word), i));
        }
        return rankFreq;

    }

    private Set<Path> readInData() {
        HashSet<Path> dataset = new HashSet<>();
        try {
            Path dataDirectory = Paths.get("data");
            Path large_dataset = dataDirectory.resolve("large_dataset");
            DirectoryStream<Path> files = Files.newDirectoryStream(large_dataset);
            for(Path item: files) {
                dataset.add(item);
            }

        } catch (IOException e) {
            //do something
        }
        return dataset;
    }

    //1.2 Plot a frequency vs rank graph for 10,000 highest ranked words
    private void plotFreqRank() {
        List <WordFreq> wordrank = findFreqTokens();
        List<Point> points = new ArrayList<>();
        for(int i=0; i < 10000; i++) {
            WordFreq wordFreq = wordrank.get(i);
            points.add(new Point(wordFreq.getRank(), wordFreq.getFrequency()));
        }
        ChartPlotter.plotLines(points);
    }

    //1.3 Plot task1 words
    private void plotTenWords() {
        String tenWords[] = {
                "awesome", "awful", "bland", "unfortunately", "lovely", "engaging", "painful", "boring", "fun", "bad"
        };
        List<WordFreq> rankWords = findFreqTokens();
        List<Point> points = new LinkedList<>();
        for(String wordToPlot: tenWords) {
            for(WordFreq rankfreq: rankWords) {
                if(wordToPlot.equals(rankfreq.getWord())) {
                    points.add(new Point(rankfreq.getRank(), rankfreq.getFrequency()));
                    break;
                }
            }
        }
        ChartPlotter.plotLines(points);
    }

    //1.4 plot log scale
    private void plotFreqRankLog() {
        List<Point> points = getLogCurvePoints(findFreqTokens());
        ChartPlotter.plotLines(points);
    }

    private List<Point> getLogCurvePoints(List<WordFreq> wordRank) {
        List<Point> points = new ArrayList<>();
        for(int i=0; i<10000; i++) {
            WordFreq wordFreq = wordRank.get(i);
            points.add(new Point(Math.log(wordFreq.getRank() +1), Math.log(wordFreq.getFrequency() + 1)));
        }
        return points;
    }

    //1.5 add best fit
    private void plotBestFitLog() {
        List<WordFreq> wordRank = findFreqTokens();
        getBestFit(wordRank);
        List<Point> bestFitPoints = new LinkedList<>();
        bestFitPoints.add(new Point(Math.log(1), bestFit.gradient*Math.log(1) + bestFit.yIntercept));
        bestFitPoints.add(new Point(Math.log(100000), bestFit.gradient*Math.log(100000) + bestFit.yIntercept));

        List<Point> points = getLogCurvePoints(wordRank);
        ChartPlotter.plotLines(points, bestFitPoints);
    }

    private void getBestFit(List <WordFreq> wordrank) {
        Map<Point, Double> points = new HashMap<>();
        for(WordFreq wordFreq : wordrank) {
            points.put(new Point(Math.log(wordFreq.getRank()+1), Math.log(wordFreq.getFrequency()+1)), (double) wordFreq.getFrequency());
        }
        this.bestFit = BestFit.leastSquares(points);
    }

    //1.6 function that takes a rank, outputs expected frequency
    private int predictedFreq(int rank) {
        List<WordFreq> wordRank = findFreqTokens();
        getBestFit(wordRank);
        double logY = Math.log(rank)*bestFit.gradient + bestFit.yIntercept;
        return (int) Math.rint(Math.exp(logY));
    }

    private void predictedFreqVSActual() {
        String tenWords[] = {
                "awesome", "awful", "bland", "unfortunately", "lovely", "engaging", "painful", "boring", "fun", "bad"
        };
        List<WordFreq> wordrank = findFreqTokens();
        for(WordFreq wordFreq: wordrank) {
            for(String word: tenWords) {
                if(wordFreq.getWord().equals(word)) {
                    System.out.println("Word: " + word + " Predicted Frequency: " + predictedFreq(wordFreq.getRank()) +
                            " Actual Frequency: " + wordFreq.getFrequency());
                }
            }
        }
    }

    //1.7 use best fit line to estimate zipfs parameters
    private void getZipfsConst() {
        getBestFit(findFreqTokens());
        double k = Math.exp(bestFit.yIntercept);
        double alpha = -bestFit.gradient;
        System.out.println("K = " + k);
        System.out.println("Alpha = " + alpha);
    }

    //2 count hoow many unique words system finds
    private Map<Integer, Integer> countUniqueWords() {
        Set<Path> dataset = readInData();
        Map<Integer, Integer> numTokens = new HashMap<>();
        Set<String> seenWords = new HashSet<>();

        try {
            int wordCount = 0;
            for(Path path: dataset) {
                Tokenizer tokenizer = new Tokenizer();
                List<String> words = tokenizer.tokenize(path);
                for(String word: words) {
                    seenWords.add(word);
                    if(Integer.bitCount(wordCount) == 1 || wordCount == 0) {
                        //collect datapoint since this is a power of two
                        numTokens.put(seenWords.size(), wordCount+1);
                    }
                    wordCount++;
                }
            }
            numTokens.put(seenWords.size(), wordCount);
        }
        catch(IOException e) {
            //do something
        }
        return numTokens;
    }

    private void plotHeap() {
        Map<Integer, Integer> numTokens = countUniqueWords();
        List<Point> points = new LinkedList<>();
        for(int typecount: numTokens.keySet()) {
            points.add(new Point(Math.log(numTokens.get(typecount)), Math.log(typecount)));
        }
        ChartPlotter.plotLines(points);
    }

    public static void main(String[] args) {
        Exercise3 exercise3 = new Exercise3();
        exercise3.plotHeap();
    }
}

