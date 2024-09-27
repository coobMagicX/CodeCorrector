  public void testParseLicenseAscii() throws Exception {
    Node node = new Node(1);
    this.fileLevelJsDocBuilder = node.getJsDocBuilderForNode();
    String comment = "@license Foo\n *   Bar\n\n  Baz*/";
    parse(comment);
    assertEquals(" Foo\n   Bar\n\n  Baz", node.getJSDocInfo().getLicense());
  }