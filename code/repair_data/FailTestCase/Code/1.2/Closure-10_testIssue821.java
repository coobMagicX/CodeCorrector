  public void testIssue821() {
    foldSame("var a =(Math.random()>0.5? '1' : 2 ) + 3 + 4;");
    foldSame("var a = ((Math.random() ? 0 : 1) ||" +
             "(Math.random()>0.5? '1' : 2 )) + 3 + 4;");
  }