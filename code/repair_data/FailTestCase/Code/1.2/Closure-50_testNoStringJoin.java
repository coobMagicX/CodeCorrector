  public void testNoStringJoin() {
    foldSame("x = [].join(',',2)");
    foldSame("x = [].join(f)");
  }