/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

<<<<<<< HEAD
// Optimering 0: Borttagning av rekursiva partDist med tidskomplexitet av O(3^n)

// Optimering 1: Användning av dynamisk programmering för att reducera antalet "redundant" beräkningar.
// Nya metoden som sparar beräkningar i en matris och återanvänder dem för att minska antalet beräkningar
// har tidskomplexitet av O(n*m) där n är längden av ordet w1 och m är längden av ordet w2.

// Optimering 2: Om två ord efter varandra i ordlistan börjar med x antal samma bokstäver börja man med
// nästa ord från rad x i föregående ords matris. Detta för att ytterliggare minska antalet beräkningar som görs.
// Har tids komplexitet av O(n*m) i värsta fall. Kan ge en förbättring av tidkomplexitet ner till O(n) i bästa fall om
// föregående och nästa ord har i närheten av m första bokstäver som är samma.


public class ClosestWords {
  LinkedList<String> closestWords = new LinkedList<>();
  int closestDistance = Integer.MAX_VALUE;
  int[][] dp = new int[41][41];
  String dpWord = null;
  

  private int partDist(String w1, String w2) {
    int w1len = w1.length();
    int w2len = w2.length();
		int x = 1;
		

    // Kollar hur många bokstäver som är lika i början av orden
    if (dpWord != null) {
      while (x <= Math.min(w2len, dpWord.length()) && w2.charAt(x - 1) == dpWord.charAt(x - 1)) {
        x++;
      }
    }


    for (int i = x; i <= w2len; i++) {
      for (int j = 1; j <= w1len; j++) {
          dp[i][j] = Math.min(dp[i-1][j-1] + (w2.charAt(i-1) != w1.charAt(j-1) ? 1 : 0), 
                     Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1));
      }
    }

		dpWord = w2;
		return dp[w2len][w1len];
	}


  public ClosestWords(String w, List<String> wordList) {
    // Initiera matrisen
    for(int i = 0; i <= 40; i++)	dp[i][0] = dp[0][i]  = i;

    // Kollar igenom alla ord i ordlistan och sparar de ord som har minst distans till w
    for (String s : wordList) {
      int dist = partDist(w, s);
      if (dist < closestDistance) {
        closestDistance = dist;
        closestWords.clear();
        closestWords.add(s);
      } else if (dist == closestDistance) {
        closestWords.add(s);
      }
    }
  }


  public int getMinDistance() {
    return closestDistance;
  }

  public List<String> getClosestWords() {
=======
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
>>>>>>> main
    return closestWords;
  }
}
