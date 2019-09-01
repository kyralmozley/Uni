package uk.ac.cam.cl.km687.exercises;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Map;
import java.lang.Math;
import java.util.Set;


import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.Sentiment;
import uk.ac.cam.cl.mlrd.exercises.sentiment_detection.IExercise4;

public class Exercise4 implements IExercise4                                                                                                                      {

    private Exercise1 ex1;

    /**
     * Modify the simple classifier from Exercise1 to include the information about the magnitude of a sentiment.
     * @param testSet
     *            {@link Set}<{@link Path}> Paths to reviews to classify
     * @param lexiconFile
     *            {@link Path} Path to the lexicon file
     * @return {@link Map}<{@link Path}, {@link Sentiment}> Map of calculated
     *         sentiment for each review
     * @throws IOException
     */
    public Map<Path, Sentiment> magnitudeClassifier(Set<Path> testSet, Path lexiconFile) throws IOException {
        if (ex1 == null) {
            ex1 = new Exercise1();
        }
        return ex1.improvedClassifier(testSet, lexiconFile);
    }


    /**
     * Implement the two-sided sign test algorithm to determine if one
     * classifier is significantly better or worse than another.
     * The sign for a result should be determined by which
     * classifier is more correct, or if they are equally correct should be 0.5
     * positive, 0.5 negative and the ceiling of the least common sign total
     * should be used to calculate the probability.
     *
     * @param actualSentiments
     *            {@link Map}<{@link Path}, {@link Sentiment}>
     * @param classificationA
     *            {@link Map}<{@link Path}, {@link Sentiment}>
     * @param classificationB
     *            {@link Map}<{@link Path}, {@link Sentiment}>
     * @return <code>double</code>
     */
    public double signTest(Map<Path, Sentiment> actualSentiments, Map<Path, Sentiment> classificationA,
                           Map<Path, Sentiment> classificationB) {
        int Plus =0;
        int Minus=0;
        int Null=0;

        for(Path p: actualSentiments.keySet()) {
            Sentiment trueSentiment = actualSentiments.get(p);
            if (classificationA.get(p).equals(classificationB.get(p))) Null++;
            else if (classificationA.get(p).equals(trueSentiment)) Plus++;
            else Minus++;
        }

        int n = 2*((int) Math.ceil(Null / 2)) + Plus + Minus;
        int k = (int) Math.ceil(Null/2) + Math.min(Plus, Minus);
        System.out.println("K equals: "+ k);

        double q = 0.5;

        BigDecimal prob = new BigDecimal(0.0);

        for(int i=0; i<=k; i++) {
            BigInteger nChooseI = factorial(n).divide(factorial(i).multiply(factorial(n-i)));

            BigDecimal prob1 = new BigDecimal(Math.pow(q, i));
            BigDecimal prob2 = new BigDecimal(Math.pow(1.0-q, n-i));
            prob = prob.add(prob1.multiply(prob2).multiply(new BigDecimal(nChooseI)));
        }

        prob = prob.multiply(new BigDecimal(2));

        return prob.doubleValue();

    }

    private BigInteger factorial(int n) {
        if (n == 0)
            return new BigInteger("1");
        else {
            BigInteger product = new BigInteger("1");
            for (int i = 2; i <= n; i++) {
                product = product.multiply(new BigInteger(String.valueOf(i)));
            }
            return product;
        }
    }


}