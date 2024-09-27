  public void testIssue1101b() {
    helperCanInlineReferenceToFunction(CanInlineResult.NO,
        "function foo(a){return (x.prop = 2),a;} foo(x.prop);", "foo",
        INLINE_DIRECT);
  }