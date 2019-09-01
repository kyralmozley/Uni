package uk.ac.cam.rkh23.Algorithms.Tick2;

public abstract class LCSFinder {

    protected final String mString1;
    protected final String mString2;
    protected int mTable[][];

    public LCSFinder(String s1, String s2) {
        mString1=s1;
        mString2=s2;
    }

    public abstract int getLCSLength();
    public abstract String getLCSString();

    public int[][] getLCSLengthTable() {return mTable;}
}
