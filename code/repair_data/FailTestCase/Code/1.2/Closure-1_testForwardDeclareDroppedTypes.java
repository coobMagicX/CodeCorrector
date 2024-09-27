  public void testForwardDeclareDroppedTypes() {
    args.add("--manage_closure_dependencies=true");

    args.add("--warning_level=VERBOSE");
    test(new String[] {
          "goog.require('beer');",
          "goog.provide('beer'); /** @param {Scotch} x */ function f(x) {}",
          "goog.provide('Scotch'); var x = 3;"
         },
         new String[] {
           "var beer = {}; function f(a) {}",
           ""
         });

    test(new String[] {
          "goog.require('beer');",
          "goog.provide('beer'); /** @param {Scotch} x */ function f(x) {}"
         },
         new String[] {
           "var beer = {}; function f(a) {}",
           ""
         },
         RhinoErrorReporter.TYPE_PARSE_ERROR);
  }