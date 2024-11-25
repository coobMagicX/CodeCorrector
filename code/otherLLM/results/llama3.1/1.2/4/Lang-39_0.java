String input = "Hello world, hello again.";
String[] searchList = {"hello", "world"};
String[] replacementList = {"goodbye", "earth"};

StringUtils s = new StringUtils();
String result = s.replaceMultiple(input, searchList, replacementList, true, 5);
System.out.println(result); // Output: "Goodbye earth, goodbye again."