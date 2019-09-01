package uk.ac.cam.cl.km687.exercises;


public class WordFreq {
    private String word;
    private int frequency;
    private int rank;

    public WordFreq(String word, int frequency, int rank) {
        this.word = word;
        this.frequency = frequency;
        this.rank = rank;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "WordFreq{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                ", rank=" + rank +
                '}';
    }
}
