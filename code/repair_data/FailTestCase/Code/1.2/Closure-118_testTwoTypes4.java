  public void testTwoTypes4() {
    String js = ""
        + "/** @constructor */ function Foo() {}\n"
        + "Foo.prototype = {a: 0};"
        + "/** @type Foo */\n"
        + "var F = new Foo;\n"
        + "F.a = 0;"
        + "/** @constructor */ function Bar() {}\n"
        + "Bar.prototype = {'a': 0};"
        + "/** @type Bar */\n"
        + "var B = new Bar;\n"
        + "B['a'] = 0;";

    String output = ""
        + "function Foo(){}"
        + "Foo.prototype = {a: 0};"
        + "var F=new Foo;"
        + "F.a=0;"
        + "function Bar(){}"
        + "Bar.prototype = {'a': 0};"
        + "var B=new Bar;"
        + "B['a']=0";

    testSets(false, js, output, "{a=[[Foo.prototype]]}");
    testSets(true, js, output, "{a=[[Foo.prototype]]}");
  }