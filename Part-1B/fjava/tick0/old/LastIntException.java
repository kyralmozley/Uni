/**
*
* LastIntException.java - Kyra Mozley 2018
*
**/

package uk.ac.cam.km687.fjava.tick0;

public class LastIntException extends Exception {

    int lastInt;

    public LastIntException(int lastInt){
        this.lastInt = lastInt;
    }
}
