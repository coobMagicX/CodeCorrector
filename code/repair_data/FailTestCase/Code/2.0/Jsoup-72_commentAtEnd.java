  @Test public void commentAtEnd() throws Exception {
      Document doc = Jsoup.parse("<!");
      assertTrue(doc.childNode(0) instanceof Comment);
  }