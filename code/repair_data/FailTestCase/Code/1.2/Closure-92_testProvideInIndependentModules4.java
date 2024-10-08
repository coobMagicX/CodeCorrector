  public void testProvideInIndependentModules4() {
    // Regression test for bug 261:
    // http://code.google.com/p/closure-compiler/issues/detail?id=261
    test(
        createModuleStar(
            "goog.provide('apps');",
            "goog.provide('apps.foo.bar.B');",
            "goog.provide('apps.foo.bar.C');"),
        new String[] {
            "var apps = {};apps.foo = {};apps.foo.bar = {}",
            "apps.foo.bar.B = {};",
            "apps.foo.bar.C = {};",
        });
  }