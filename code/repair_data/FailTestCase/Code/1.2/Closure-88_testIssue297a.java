  public void testIssue297a() {
    testSame("function f(p) {" +
         " var x;" +
         " return ((x=p.id) && (x=parseInt(x.substr(1))) && x>0);" +
         "}; f('');");
  }