package uk.ac.cam.cl.km687.exercises;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import uk.ac.cam.cl.mlrd.exercises.markov_models.*;

public class Exercise8 implements IExercise8 {


    public List<DiceType> viterbi(HiddenMarkovModel<DiceRoll, DiceType> model, List<DiceRoll> observedSequence) {
        LinkedList<DiceType> result = new LinkedList<>();

        List<Map<DiceType, Double>> pathProbabilities = new ArrayList<>(); //index of time stamp, current state, prob of current hidden
        List<Map<DiceType, DiceType>> prevHidden = new ArrayList<>(); //index of time stamp, current hidden state, most probable previous hidden

        Map<DiceType, Map<DiceType, Double>> transitions = model.getTransitionMatrix();
        Map<DiceType, Map<DiceRoll, Double>> emissions = model.getEmissionMatrix();

        for(int i=0; i< observedSequence.size(); i++) {
            Map<DiceType, Double> newPathProbability = new HashMap<>();
            Map<DiceType, DiceType> newPrevHidden = new HashMap<>();
            DiceRoll currentRoll = observedSequence.get(i);

            //get values for all possible current type
            for (DiceType currentType : DiceType.values()) {
                if (i == 0) {
                    newPathProbability.put(currentType, Math.log(emissions.get(currentType).get(currentRoll)));
                    newPrevHidden.put(currentType, null);
                } else {
                    //get prev state which has greatest prob
                    DiceType maxPrev = null;
                    double maxProbability = Double.NEGATIVE_INFINITY;
                    for (DiceType previous : DiceType.values()) {
                        double currentProbability = pathProbabilities.get(i - 1).get(previous) + Math.log(transitions.get(previous).get(currentType)) + Math.log(emissions.get(currentType).get(currentRoll));
                        if (currentProbability > maxProbability || maxPrev == null) {
                            maxPrev = previous;
                            maxProbability = currentProbability;
                        }
                    }
                    newPathProbability.put(currentType, maxProbability);
                    newPrevHidden.put(currentType, maxPrev);
                }
            }

            pathProbabilities.add(newPathProbability);
            prevHidden.add(newPrevHidden);
        }

        //backtrack to get max
        DiceType previousAdded = DiceType.END;
        result.add(previousAdded);
        for(int i= prevHidden.size()-1; i>0; i--) {
            result.addFirst(prevHidden.get(i).get(previousAdded));
            previousAdded = prevHidden.get(i).get(previousAdded);
        }

        return result;
    }


    public Map<List<DiceType>, List<DiceType>> predictAll(HiddenMarkovModel<DiceRoll, DiceType> model,
                                                          List<Path> testFiles) throws IOException {
        List<HMMDataStore<DiceRoll, DiceType>> sequences = HMMDataStore.loadDiceFiles(testFiles);
        Map<List<DiceType>, List<DiceType>> result = new HashMap<>();
        for(HMMDataStore hmm: sequences) {
            List<DiceType> predicted = viterbi(model, hmm.observedSequence);
            result.put(hmm.hiddenSequence, predicted);
        }
        return result;
    }


    public double precision(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        double correct = 0;
        double weighted = 0;
        for(List<DiceType> correctSequence: true2PredictedMap.keySet()) {
            List<DiceType> predictedSequence = true2PredictedMap.getOrDefault(correctSequence, new ArrayList<>());
            for(int i=0; i<correctSequence.size(); i++) {
                if(predictedSequence.get(i).equals(DiceType.WEIGHTED)) {
                    weighted++;
                    if (correctSequence.get(i).equals(predictedSequence.get(i)))
                        correct++;

                }
            }
        }
        return correct/weighted;
    }


    public double recall(Map<List<DiceType>, List<DiceType>> true2PredictedMap) {
        double correct = 0;
        double weighted = 0;
        for(List<DiceType> correctSequence: true2PredictedMap.keySet()) {
            List<DiceType> predictedSequence = true2PredictedMap.get(correctSequence);
            for(int i=0; i<correctSequence.size(); i++) {
                if(correctSequence.get(i).equals(DiceType.WEIGHTED)) {
                    weighted++;
                    if(correctSequence.get(i).equals(predictedSequence.get(i)))
                        correct++;
                }
            }
        }
        return correct/weighted;
    }


    public double fOneMeasure(Map<List<DiceType>, List<DiceType>> true2PredictedMap){
        double precision = precision(true2PredictedMap);
        double recall = recall(true2PredictedMap);
        double f_1 = 2*(precision*recall) / (precision+recall);

        return f_1;
    }

}