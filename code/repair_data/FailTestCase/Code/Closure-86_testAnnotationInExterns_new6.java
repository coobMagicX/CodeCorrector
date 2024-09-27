  public void testAnnotationInExterns_new6() throws Exception {
    // While "externObjSEThisMethod" has modifies "this"
    // it does not have global side-effects with "this" is
    // a known local value.
    checkMarkedCalls(
        "function f() {" +
        "  new externObjSEThis().externObjSEThisMethod('') " +
        "};" +
        "f();",
        BROKEN_NEW ?
            ImmutableList.<String>of(
                "externObjSEThis") :
           ImmutableList.<String>of(
               "externObjSEThis", "NEW STRING externObjSEThisMethod", "f"));
  }