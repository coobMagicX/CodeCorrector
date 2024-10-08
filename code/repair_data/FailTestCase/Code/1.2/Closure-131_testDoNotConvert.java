  public void testDoNotConvert() {
    testSame("a[0]");
    testSame("a['']");
    testSame("a[' ']");
    testSame("a[',']");
    testSame("a[';']");
    testSame("a[':']");
    testSame("a['.']");
    testSame("a['0']");
    testSame("a['p ']");
    testSame("a['p' + '']");
    testSame("a[p]");
    testSame("a[P]");
    testSame("a[$]");
    testSame("a[p()]");
    testSame("a['default']");
    // Ignorable control characters are ok in Java identifiers, but not in JS.
    testSame("a['A\u0004']");
    // upper case lower half of o from phonetic extensions set.
    // valid in Safari, not in Firefox, IE.
    test("a['\u1d17A']", "a['\u1d17A']");
    // Latin capital N with tilde - nice if we handled it, but for now let's
    // only allow simple Latin (aka ASCII) to be converted.
    test("a['\u00d1StuffAfter']", "a['\u00d1StuffAfter']");
  }