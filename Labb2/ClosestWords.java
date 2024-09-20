/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

//optimering 0: inget har tidskomplexitet av O(3^n)


//optimering 1: gjorde om programmet till dynamiskt istället för recursivt
// har tids komplexitet av O(n*m) n är det felstavade ordet

//optimering 2: om två ord efter varandra började med samma x bokstäver börja räkna från column x
// har tids komplexitet av O(n*m)  potencielt inget eller mycket
 


public class ClosestWords {
  LinkedList<String> closestWords = null;
  String dpWord = null;
  int closestDistance = -1;
  int[][] Dp = new int[41][41];

  int partDist(String w1, String w2) { //w1 är felstavade ordet, w2 är ordet vi jämför med
    int w1len = w1.length();
    int w2len = w2.length();
		int cskip = 1;
		
    if(dpWord != null ) { //kollar antalet bokstäver som är samma som förra ordet
      for(int i = 0; i <= Math.min(w2len, dpWord.length())-1; i++){
        if (w2.charAt(i) == dpWord.charAt(i))
          cskip++;
        else
          break;
      }
    }

    for (int r = 1; r <= w1len; r++) { //bygger matrisen
      for (int c = cskip; c <= w2len; c++) {
          int t = (w1.charAt(r - 1) != w2.charAt(c - 1)) ? 1 : 0;
          Dp[r][c] = Math.min(Math.min(Dp[r - 1][c] + 1, Dp[r][c - 1] + 1), Dp[r - 1][c - 1] + t);
      }

  }
		dpWord = w2;
		return Dp[w1len][w2len];
	}


  public ClosestWords(String w, List<String> wordList) {
    init();
    for (String s : wordList) {
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
  }

  int getMinDistance() {
    return closestDistance;
  }

  List<String> getClosestWords() {
    return closestWords;
  }
  private void init() {
		for(int i = 0; i <= 40; i++)	Dp[i][0] = Dp[0][i]  = i;

	}
}
