  public void testOneType4() {
    String js = ""
        + "/** @constructor */ function Foo() {}\n"
        + "Foo.prototype = {'a': 0};\n"
        + "/** @type Foo */\n"
        + "var F = new Foo;\n"
        + "F['a'] = 0;";
    String expected = "{}";
    testSets(false, js, js, expected);
    testSets(true, js, js, expected);
  }