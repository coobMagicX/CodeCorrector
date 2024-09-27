  public void testParsePreserve() throws Exception {
    Node node = new Node(1);
    this.fileLevelJsDocBuilder = node.getJsDocBuilderForNode();
    String comment = "@preserve Foo\nBar\n\nBaz*/";
    parse(comment);
    assertEquals(" Foo\nBar\n\nBaz", node.getJSDocInfo().getLicense());
  }