public static void main(String[] args) {
    String root = "a";
    String prop = "b";
    String alias = appendPropForAlias(root, prop);
    System.out.println(alias); // prints "a$b"
}