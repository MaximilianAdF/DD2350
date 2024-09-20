import java.util.LinkedList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<String> wordList = new LinkedList<String>();
        wordList.add("hej");
        wordList.add("hejjjjjjjjjj");

        //partdist 18ms
        //nya 0ms


        long t1 = System.currentTimeMillis();
        ClosestWords closestWords = new ClosestWords("hejjjjjj", wordList);
        long tottime = (System.currentTimeMillis() - t1);
        System.out.println("CPU time: " + tottime + " ms");
    }
    
    
}