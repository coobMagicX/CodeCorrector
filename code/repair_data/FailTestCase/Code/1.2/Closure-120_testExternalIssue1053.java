  public void testExternalIssue1053() {
    testSame(
        "var u; function f() { u = Random(); var x = u; f(); alert(x===u)}");
  }