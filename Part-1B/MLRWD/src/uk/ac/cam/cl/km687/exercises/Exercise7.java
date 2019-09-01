package uk.ac.cam.cl.km687.exercises;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.*;

import uk.ac.cam.cl.mlrd.exercises.markov_models.DiceRoll;
import uk.ac.cam.cl.mlrd.exercises.markov_models.DiceType;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HMMDataStore;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrd.exercises.markov_models.IExercise7;

public class Exercise7 implements IExercise7 {

    private Map<DiceType, Map<DiceType, Double>> transitionTable = new HashMap<>();
    private Map<DiceType, Map<DiceRoll, Double>> emissionTable = new HashMap<>();


    /**
     * Loads the sequences of visible and hidden states from the sequence files
     * (visible dice rolls on first line and hidden dice types on second) and uses
     * them to estimate the parameters of the Hidden Markov Model that generated
     * them.
     *
     * @param sequenceFiles
     *            {@link Collection}<{@link Path}> The files containing dice roll
     *            sequences
     * @return {@link HiddenMarkovModel}<{@link DiceRoll}, {@link DiceType}> The
     *         estimated model
     * @throws IOException
     */
    public HiddenMarkovModel<DiceRoll, DiceType> estimateHMM(Collection<Path> sequenceFiles) throws IOException {
       List<HMMDataStore<DiceRoll, DiceType>> sequences = HMMDataStore.loadDiceFiles(sequenceFiles);

       //count total times a die is rolled from a state
        Map<DiceType, Integer> fromTypeCount = new HashMap<>();

        //add 0s for end state since we can not get there
        addZerosToEndState();

        //start counting
        for(HMMDataStore<DiceRoll, DiceType> currentSequence: sequences) {
            DiceType prev = currentSequence.hiddenSequence.get(0);

            for(int i=0; i< currentSequence.observedSequence.size(); i++) {
                DiceRoll currentRoll = currentSequence.observedSequence.get(i);
                DiceType currentType = currentSequence.hiddenSequence.get(i);

                //counts for transition table
                if(i>0) {
                    Map<DiceType, Double> thisMap = transitionTable.getOrDefault(prev, new HashMap<>());
                    thisMap.put(currentType, thisMap.getOrDefault(currentType, 0.0) + 1);
                    transitionTable.put(prev, thisMap);
                }

                //count emissions
                Map<DiceRoll, Double> thisEMap = emissionTable.getOrDefault(currentType, new HashMap<>());
                thisEMap.put(currentRoll, thisEMap.getOrDefault(currentRoll, 0.0) + 1);
                emissionTable.put(currentType, thisEMap);

                fromTypeCount.put(currentType, fromTypeCount.getOrDefault(currentType, 0) + 1);

                prev = currentType;
            }
        }

        System.out.println("trans: " + transitionTable.toString());

        //calculate transistion probability
        for(DiceType fromType: transitionTable.keySet()) {
            Map<DiceType, Double> currentMap = transitionTable.get(fromType);
            for(DiceType toType: currentMap.keySet()) {
                if(fromTypeCount.containsKey(fromType))
                    currentMap.put(toType, currentMap.get(toType) / (double) fromTypeCount.get(fromType));
                else
                    currentMap.put(toType, 0.0);
            }
        }

        //emission prob
        for(DiceType fromType: emissionTable.keySet()) {
            Map<DiceRoll, Double> currentMap = emissionTable.get(fromType);
            for(DiceRoll toRoll: currentMap.keySet()) {
                if(fromTypeCount.containsKey(fromType))
                    currentMap.put(toRoll, currentMap.get(toRoll) / (double) fromTypeCount.get(fromType));
                else
                    currentMap.put(toRoll, 0.0);

            }
        }

        //add zero if no count
        addZeroToTransition();
        addZerosToEmission();

        return new HiddenMarkovModel<>(transitionTable, emissionTable);
    }

    private void addZerosToEndState() {
        Map<DiceType, Double> typeMap = new HashMap<>();
        Map<DiceRoll, Double> rollMap = new HashMap<>();
        for(DiceType type: DiceType.values()) {
            typeMap.put(type, 0.0);
        }
        for(DiceRoll roll : DiceRoll.values()) {
            rollMap.put(roll, 0.0);
        }
        transitionTable.put(DiceType.END, typeMap);
        emissionTable.put(DiceType.END, rollMap);
    }

    private void addZeroToTransition() {
        for(DiceType fromType: transitionTable.keySet()) {
            Map<DiceType, Double> map = transitionTable.get(fromType);
            for(DiceType toType: DiceType.values()) {
                if(!map.containsKey(toType))
                    map.put(toType, 0.0);
            }
        }

    }
    private void addZerosToEmission() {
        for(DiceType fromType: emissionTable.keySet()) {
            Map<DiceRoll, Double> map = emissionTable.get(fromType);
            for(DiceRoll toRoll: DiceRoll.values()) {
                if(!map.containsKey(toRoll))
                    map.put(toRoll, 0.0);
            }
        }
    }
}