package uk.ac.cam.km687.Algorithms.Tick2;
import uk.ac.cam.rkh23.Algorithms.Tick2.LCSFinder;
import java.util.Arrays;

public class LCSBottomUp extends LCSFinder {
//need to create m and n based on lengths
  int m;
  int n;
  int[][] table;


  public LCSBottomUp(String s1, String s2) {
    super(s1,s2);
    m = s1.length();
    n = s2.length();
    if(m == 0 || n == 0) {
      table = null;
    } else {
      table = new int[m+1][n+1];
    }
    super.mTable = new int[m][n];
  }

  public int getLCSLength() {
    if(m == 0 || n==0) {
      return 0;
    }

    for (int i=0; i<=m; i++)
        {
            for (int j=0; j<=n; j++)
            {
                if (i == 0 || j == 0)
                    table[i][j] = 0;
                else if (mString1.charAt(i-1) == mString2.charAt(j-1))
                    table[i][j] = table[i-1][j-1] + 1;
                else
                    table[i][j] = Math.max(table[i-1][j], table[i][j-1]);
            }
        }
    //super.mTable = table;
    for(int a=1; a<=m; a++) {
      for(int b=1; b<=n; b++) {
        super.mTable[a-1][b-1] = table[a][b];
      }
    }
    return table[m][n];
  }

  public String getLCSString() {
    if (m == 0 || n == 0) {
      return "";
    }
    int index = table[m][n];
    int temp = index;

    char[] lcs = new char[index];

    int i = m, j = n;
    while (i > 0 && j > 0)
    {

        if (mString1.charAt(i-1) == mString2.charAt(j-1))
        {
            // Put current character in result
            lcs[index-1] = mString1.charAt(i-1);

            // reduce values of i, j and index
            i--;
            j--;
            index--;
        }

        else if (table[i-1][j] > table[i][j-1])
            i--;
        else
            j--;
    }
    String lcsstring = new String(lcs);
    return lcsstring;
  }

  public static void main(String[] args) {
    String s1;
    String s2;
    if (args.length == 0) {
      s1 = "";
      s2 = "";
    } else {
      s1 = args[0];
      s2 = args[1];
    }
    LCSBottomUp lcs = new LCSBottomUp(s1, s2);
    System.out.println(lcs.getLCSLength());
    System.out.println(lcs.getLCSString());
  }
}
