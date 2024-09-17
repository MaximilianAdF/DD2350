/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
  LinkedList<String> closestWords = null;

  int closestDistance = -1;

  int partDist(String w1, String w2, int w1len, int w2len) {
    if (w1len == 0)
      return w2len;
    if (w2len == 0)
      return w1len;
    int res = partDist(w1, w2, w1len - 1, w2len - 1) +        
	  (w1.charAt(w1len - 1) == w2.charAt(w2len - 1) ? 0 : 1);     // Checks if the last letters are the same
    int addLetter = partDist(w1, w2, w1len - 1, w2len) + 1;     // Checks if it is better to add a letter to w1 (less distance)
    if (addLetter < res)                                        // If it is better to add a letter, then res is updated
      res = addLetter;
    int deleteLetter = partDist(w1, w2, w1len, w2len - 1) + 1;  // Checks if it is better to delete a letter from w1 (less distance)
    if (deleteLetter < res)                                     // If it is better to delete a letter, then res is updated
      res = deleteLetter;
    return res;
  }

  int impDist(String w1, String w2, int w1len, int w2len) {
    int[][] dp = new int[w1len + 1][w2len + 1];
    for (int i = 0; i <= w1len; i++) {
      for (int j = 0; j <= w2len; j++) {
        if (i == 0) {
          dp[0][j] = j;
        } else if (j == 0) {
          dp[i][0] = i;
        } else {
          dp[i][j] = Math.min(dp[i-1][j-1] + (w1.charAt(i-1) != w2.charAt(j-1) ? 1 : 0), 
                     Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1));
        }
      }
    }
    return dp[w1len][w2len];
  } 

  int distance(String w1, String w2) {
    return impDist(w1, w2, w1.length(), w2.length());
    //return partDist(w1, w2, w1.length(), w2.length());
  }

  public ClosestWords(String w, List<String> wordList) {
    for (String s : wordList) {
      int dist = distance(w, s);
      // System.out.println("d(" + w + "," + s + ")=" + dist);
      if (dist < closestDistance || closestDistance == -1) {
        closestDistance = dist;
        closestWords = new LinkedList<String>();
        closestWords.add(s);
      }
      else if (dist == closestDistance)
        closestWords.add(s);
    }
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
}
