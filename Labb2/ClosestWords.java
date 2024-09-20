/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
  LinkedList<String> closestWords = null;
  String dpWord = null;
  int closestDistance = -1;
  int[][] Dp = new int[41][41];

  //temp
  public static void printMatrix(int[][] matrix) {
    for (int[] row : matrix) {
        for (int value : row) {
            System.out.print(value + " ");
        }
        System.out.println(); // Move to the next line after printing a row
    }
  }

  int partDist(String w1, String w2) { //w1 är felstavade ordet, w2 är ordet vi jämför med
    int w1len = w1.length();
    int w2len = w2.length();

		int cskip = 1;
		
    if(dpWord != null ) {
      for(int i = 0; i <= Math.min(w2len, dpWord.length())-1; i++){
        if (w2.charAt(i) == dpWord.charAt(i))
          cskip++;
        else
          break;
      }
    }

    
    int lowestRowDistance = Integer.MAX_VALUE; 
    for (int r = 1; r <= w1len; r++) {
      lowestRowDistance = Integer.MAX_VALUE; // Reset for each row
      for (int c = cskip; c <= w2len; c++) {
          int t = (w1.charAt(r - 1) != w2.charAt(c - 1)) ? 1 : 0;
          Dp[r][c] = Math.min(Math.min(Dp[r - 1][c] + 1, Dp[r][c - 1] + 1), Dp[r - 1][c - 1] + t);
          // Track the lowest distance in this row
      }
      //cheack current row before cskip
      for (int c = 1 ; c<= w2len;c++){
        if (Dp[r][c] < lowestRowDistance) {
          lowestRowDistance = Dp[r][c];
        }
      }

      if (lowestRowDistance > closestDistance && closestDistance != -1) { //check if the lowest amount of steps are larger than previus min dist
          dpWord = w2;
          return 100; // Set an impossibly high value to skip further comparison
      }
  }
		dpWord = w2;
		return Dp[w1len][w2len];
	}




  public ClosestWords(String w, List<String> wordList) {
    init();
    for (String s : wordList) {
      //int dist = distance(w, s);
      int dist = partDist(w, s);
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance) {
        closestWords.add(s);
      }
    }
    //System.err.println("Closest words: " + closestWords);
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
  private void init() {
		for(int i=0; i<=40; i++)	Dp[i][0] =Dp[0][i]  = i;

	}
}
