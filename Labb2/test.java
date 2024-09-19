import java.util.LinkedList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<String> wordList = new LinkedList<String>();
        wordList.add("aaaaaaaa");
        wordList.add("aaaaaaa");
        wordList.add("word");
        wordList.add("ward");
        wordList.add("world");
        wordList.add("sword");
        wordList.add("whale");




        long t1 = System.currentTimeMillis();
        ClosestWords closestWords = new ClosestWords("aaaaa", wordList);
        long tottime = (System.currentTimeMillis() - t1);
        System.out.println("CPU time: " + tottime + " ms");
    }
    
    
}