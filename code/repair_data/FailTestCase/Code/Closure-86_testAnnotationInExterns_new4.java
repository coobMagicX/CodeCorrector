  public void testAnnotationInExterns_new4() throws Exception {
    // The entire expression containing "externObjSEThisMethod" is considered
    // side-effect free in this context.

    checkMarkedCalls("new externObjSEThis().externObjSEThisMethod('')",
        BROKEN_NEW ?
            ImmutableList.<String>of(
               "externObjSEThis") :
            ImmutableList.<String>of(
               "externObjSEThis", "NEW STRING externObjSEThisMethod"));
  }