package uk.ac.cam.cl.km687.exercises;

import uk.ac.cam.cl.mlrd.exercises.markov_models.Feature;
import uk.ac.cam.cl.mlrd.exercises.markov_models.AminoAcid;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HiddenMarkovModel;
import uk.ac.cam.cl.mlrd.exercises.markov_models.HMMDataStore;


import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BioEmissionTransitionTables {
    private Map<Feature, Map<Feature, Double>> transitionTable = new HashMap<>();
    private Map<Feature, Map<AminoAcid, Double>> emissionsTable = new HashMap<>();

    public HiddenMarkovModel<AminoAcid, Feature> estimateHMM(List<HMMDataStore<AminoAcid, Feature>> sequences) throws IOException {

        //This is used to count the *total* of times a dice is rolled from a particular state/type
        Map<Feature, Integer> fromTypeCount = new HashMap<>();

        //Add 0s for end state since this is never actually encountered
        addZerosForEndState();

        //Start counting
        for (HMMDataStore<AminoAcid, Feature> currSequence : sequences) {
            Feature prevType = currSequence.hiddenSequence.get(0);

            for (int i = 0 ; i < currSequence.observedSequence.size(); i++) {
                AminoAcid currRoll = currSequence.observedSequence.get(i);
                Feature currType = currSequence.hiddenSequence.get(i);

                /* Counts for transition table */
                //Increment count specific: FROM I TO J
                if (i > 0) {
                    Map<Feature, Double> thisTransMap = transitionTable.getOrDefault(prevType, new HashMap<>());
                    thisTransMap.put(currType, thisTransMap.getOrDefault(currType, 0.0) +1.0);
                    transitionTable.put(prevType, thisTransMap);
                }

                /* Counts for emissions table */
                Map<AminoAcid, Double> thisEmsMap = emissionsTable.getOrDefault(currType, new HashMap<>());
                thisEmsMap.put(currRoll, thisEmsMap.getOrDefault(currRoll, 0.0) +1.0);
                emissionsTable.put(currType, thisEmsMap);

                /* Increment totals count: FROM J */
                fromTypeCount.put(currType, fromTypeCount.getOrDefault(currType, 0) +1);

                //Set prev values
                prevType = currType;
            }
        }
        System.out.println("trans : " + transitionTable.toString());
        System.out.println("ot: " + transitionTable.toString());

        // Calculate transition probability by dividing by total
        for (Feature fromType : transitionTable.keySet()) {
            Map<Feature, Double> currInnerMap = transitionTable.get(fromType);
            for (Feature toType : currInnerMap.keySet()) {
                if (fromTypeCount.containsKey(fromType))
                    currInnerMap.put(toType, currInnerMap.get(toType)/ (double) fromTypeCount.get(fromType));
                else
                    currInnerMap.put(toType, 0.0);
            }
        }

        // Calculate emission probability by dividing by total
        for (Feature fromType : emissionsTable.keySet()) {
            Map<AminoAcid, Double> currInnerMap = emissionsTable.get(fromType);
            for (AminoAcid toRoll : currInnerMap.keySet()) {
                if (fromTypeCount.containsKey(fromType)) {
                    currInnerMap.put(toRoll, currInnerMap.get(toRoll)/ (double) fromTypeCount.get(fromType));
                }
                else {
                    currInnerMap.put(toRoll, 0.0);
                }
            }
        }

        //Add zeros if there is no count
        addZerosForStatesWithoutCountsInTransitions();
        addZerosForStatesWithoutCountsInEmissions();

        return new HiddenMarkovModel<>(transitionTable, emissionsTable);
    }

    private void addZerosForEndState() {
        Map<Feature, Double> endStateTypeMap = new HashMap<>();
        Map<AminoAcid, Double> endStateRollMap = new HashMap<>();
        for (Feature type : Feature.values()) {
            endStateTypeMap.put(type, 0.0);
        }
        for (AminoAcid roll : AminoAcid.values()) {
            endStateRollMap.put(roll, 0.0);
        }
        transitionTable.put(Feature.END, endStateTypeMap);
        emissionsTable.put(Feature.END, endStateRollMap);
    }

    //Nothing ever goes from some state to a start state so within hashmap, all S values should be 0
    private void addZerosForStatesWithoutCountsInTransitions() {
        for (Feature fromType : transitionTable.keySet()) {
            Map<Feature, Double> innerMap = transitionTable.get(fromType);
            for (Feature toType : Feature.values()) {
                if (!innerMap.containsKey(toType))
                    innerMap.put(toType, 0.0);
            }
        }
    }

    private void addZerosForStatesWithoutCountsInEmissions() {
        for (Feature fromType : emissionsTable.keySet()) {
            Map<AminoAcid, Double> innerMap = emissionsTable.get(fromType);
            for (AminoAcid toRoll : AminoAcid.values()) {
                if (!innerMap.containsKey(toRoll))
                    innerMap.put(toRoll, 0.0);
            }
        }
    }
}
