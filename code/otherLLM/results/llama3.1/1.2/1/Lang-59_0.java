public static void main(String[] args) {
    StrBuilder strBuilder = new StrBuilder();
    
    // Add characters to the string builder
    strBuilder.append('A');
    strBuilder.append('B');
    strBuilder.append('C');
    System.out.println(strBuilder.toString());  // prints "ABC"
    
    // Use a tokenizer to split the string into substrings
    Tokenizer tokenizer = new StrBuilderTokenizer();
    String[] substrings = tokenizer.tokenize(strBuilder.toCharArray(), 0, strBuilder.length());
    for (String substring : substrings) {
        System.out.println(substring);
    }
}