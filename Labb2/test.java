import java.util.LinkedList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<String> wordList = new LinkedList<String>();
        wordList.add("hejj");
        wordList.add("hejz");
        wordList.add("hejsan");     
        wordList.add("hejsanhej");
        wordList.add("hejsanhejsan");
        wordList.add("hejsanhejsanhej");
        wordList.add("hejzzzzzzzzzzzzzzzzzzzzzzdad");




        long t1 = System.currentTimeMillis();
        ClosestWords closestWords = new ClosestWords("hej", wordList);
        long tottime = (System.currentTimeMillis() - t1);
        System.out.println("CPU time: " + tottime + " ms");
    }
    
    
}