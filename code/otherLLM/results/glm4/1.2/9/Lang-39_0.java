public class Main {
    public static void main(String[] args) {
        String text = "The quick brown fox jumps over the lazy dog";
        String[] searchList = {"quick", "lazy", "fox"};
        String[] replacementList = {"slow", "active", "cat"};
        
        String result = replaceEach(text, searchList, replacementList, true, 2);
        System.out.println(result); // Output: The slow brown cat jumps over the active dog
    }
}