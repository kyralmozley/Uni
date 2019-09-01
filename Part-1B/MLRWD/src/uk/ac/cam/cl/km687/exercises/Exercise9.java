package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.markov_models.IExercise9;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import uk.ac.cam.cl.mlrd.exercises.markov_models.AminoAcid;
import uk.ac.cam.cl.mlrd.exercises.markov_models.Feature;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HMMDataStore;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrd.exercises.markov_models.DiceRoll;
import uk.ac.cam.cl.mlrd.exercises.markov_models.DiceType;

public class Exercise9 implements IExercise9 {

    public HiddenMarkovModel<AminoAcid, Feature> estimateHMM(List<HMMDataStore<AminoAcid, Feature>> sequencePairs)
            throws IOException {
        BioEmissionTransitionTables bioEmissionTransitionTables = new BioEmissionTransitionTables();
        HiddenMarkovModel<AminoAcid, Feature> result =bioEmissionTransitionTables.estimateHMM(sequencePairs);
        return result;

    }

    /**
     * Uses the Viterbi algorithm to calculate the most likely single sequence
     * of hidden states given the observed sequence.
     *
     * @param model
     *            A pre-trained HMM.
     * @param observedSequence
     *            {@link List}<{@link AminoAcid}> A sequence of observed amino
     *            acids
     * @return {@link List}<{@link Feature}> The most likely single sequence of
     *         hidden states
     */
    public List<Feature> viterbi(HiddenMarkovModel<AminoAcid, Feature> model, List<AminoAcid> observedSequence) {
        List <Map<Feature, Double>> pathProbability = new ArrayList<>();
        List<Map<Feature, Feature>> helperVars = new ArrayList<>();

        Map<Feature, Map<Feature, Double>> transistions = model.getTransitionMatrix();
        Map<Feature, Map<AminoAcid, Double>> emissions = model.getEmissionMatrix();

        for(int i=0; i<observedSequence.size(); i++) {
            Map<Feature, Double> newPathProbability = new HashMap<>();
            Map<Feature, Feature> newHelperVar = new HashMap<>();
            AminoAcid currentAcid = observedSequence.get(i);

            //generate values for all possible current types
            for(Feature current : Feature.values()) {
                //for first observations, there is no prev type, so prob is emission prob
                if(i==0) {
                    newPathProbability.put(current, Math.log(emissions.get(current).get(currentAcid)));
                    newHelperVar.put(current, null);
                }
                else {
                    Feature maxPrev = null;
                    double maxProbability = Double.NEGATIVE_INFINITY;
                    for(Feature prev : Feature.values()) {
                        double currentprobability = pathProbability.get(i-1).get(prev) + Math.log(transistions.get(prev).get(current))
                                + Math.log(emissions.get(current).get(currentAcid));
                        if(currentprobability > maxProbability || maxPrev == null) {
                            maxPrev = prev;
                            maxProbability = currentprobability;
                        }
                    }

                    newPathProbability.put(current, maxProbability);
                    newHelperVar.put(current, maxPrev);
                }
            }

            pathProbability.add(newPathProbability);
            helperVars.add(newHelperVar);
        }

        //backtrack
        LinkedList<Feature> result = new LinkedList<>();
        Feature prevAdded = Feature.END;
        result.add(prevAdded);
        for(int i=helperVars.size() -1 ; i>0; i--) {
            result.addFirst(helperVars.get(i).get(prevAdded));
            prevAdded = helperVars.get(i).get(prevAdded);
        }

        return result;
    }


    /**
     * Uses the Viterbi algorithm to predict hidden sequences of all observed
     * sequences in testSequencePairs.
     *
     * @param model
     *            The HMM model.
     * @param testSequencePair
     *            A list of {@link HMMDataStore}s with observed and true hidden
     *            sequences.
     * @return {@link Map}<{@link List}<{@link Feature}>,
     *         {@link Feature}<{@link Feature}>> A map from a real hidden
     *         sequence to the equivalent estimated hidden sequence.
     * @throws IOException
     */
    public Map<List<Feature>, List<Feature>> predictAll(HiddenMarkovModel<AminoAcid, Feature> model,
                                                 List<HMMDataStore<AminoAcid, Feature>> testSequencePairs) throws IOException{
        Map<List<Feature>, List<Feature>> result = new HashMap<>();
        for(HMMDataStore hmm: testSequencePairs) {
            List<Feature> predicted = viterbi(model, hmm.observedSequence);
            result.put(hmm.hiddenSequence, predicted);
        }
        return result;
    }


    /**
     * Calculates the precision of the estimated sequence with respect to the
     * membrane state, i.e. the proportion of predicted membrane states that
     * were actually in the membrane.
     *
     * @param true2PredictedMap
     *            {@link Map}<{@link List}<{@link Feature}>,
     *            {@link List}<{@link Feature}>> A map from a real hidden
     *            sequence to the equivalent estimated hidden sequence.
     * @return <code>double</code> The precision of the estimated sequence with
     *         respect to the membrane state averaged over all the test
     *         sequences.
     */
    public double precision(Map<List<Feature>, List<Feature>> true2PredictedMap) {
        double correct = 0, membrane = 0;
        for (List<Feature> correctSeq : true2PredictedMap.keySet()) {
            List<Feature> predicted = true2PredictedMap.getOrDefault(correctSeq, new ArrayList<>());
            for(int i=0; i < correctSeq.size(); i++) {
                if(predicted.get(i).equals(Feature.MEMBRANE)) {
                    membrane++;
                    if(correctSeq.get(i).equals(predicted.get(i))) {
                        correct++;
                    }
                }
            }
        }
        return correct/membrane;
    }


    /**
     * Calculate the recall for the membrane state.
     *
     * @param true2PredictedMap
     *            {@link Map}<{@link List}<{@link Feature}>,
     *            {@link List}<{@link Feature}>> A map from a real hidden
     *            sequence to the equivalent estimated hidden sequence.
     * @return The recall for the membrane state.
     */
    public double recall(Map<List<Feature>, List<Feature>> true2PredictedMap){
        double correct = 0;
        double membrane = 0;
        for(List<Feature> correctSequence: true2PredictedMap.keySet()) {
            List<Feature> predictedSequence = true2PredictedMap.get(correctSequence);
            for(int i=0; i<correctSequence.size(); i++) {
                if(correctSequence.get(i).equals(Feature.MEMBRANE)) {
                    membrane++;
                    if(correctSequence.get(i).equals(predictedSequence.get(i)))
                        correct++;
                }
            }
        }
        return correct/membrane;

    }


    /**
     * Calculate the F1 score for the membrane state.
     *
     * @param true2PredictedMap
     */
    public double fOneMeasure(Map<List<Feature>, List<Feature>> true2PredictedMap){
        double precision = precision(true2PredictedMap);
        double recall = recall(true2PredictedMap);
        double f_1 = 2*(precision*recall) / (precision+recall);

        return f_1;
    }


}