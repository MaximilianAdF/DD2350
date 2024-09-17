public class partDist {
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

    public static void main(String[] args) {
        partDist pd = new partDist();
        System.out.println(pd.partDist("labd", "blad", 4, 3));
    }
}
