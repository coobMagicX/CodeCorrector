  public void testUselessCode() {
    test("function f(x) { if(x) return; }", ok);
    test("function f(x) { if(x); }", "function f(x) { if(x); }", e);

    test("if(x) x = y;", ok);
    test("if(x) x == bar();", "if(x) JSCOMPILER_PRESERVE(x == bar());", e);

    test("x = 3;", ok);
    test("x == 3;", "JSCOMPILER_PRESERVE(x == 3);", e);

    test("var x = 'test'", ok);
    test("var x = 'test'\n'str'",
         "var x = 'test'\nJSCOMPILER_PRESERVE('str')", e);

    test("", ok);
    test("foo();;;;bar();;;;", ok);

    test("var a, b; a = 5, b = 6", ok);
    test("var a, b; a = 5, b == 6",
         "var a, b; a = 5, JSCOMPILER_PRESERVE(b == 6)", e);
    test("var a, b; a = (5, 6)",
         "var a, b; a = (JSCOMPILER_PRESERVE(5), 6)", e);
    test("var a, b; a = (bar(), 6, 7)",
         "var a, b; a = (bar(), JSCOMPILER_PRESERVE(6), 7)", e);
    test("var a, b; a = (bar(), bar(), 7, 8)",
         "var a, b; a = (bar(), bar(), JSCOMPILER_PRESERVE(7), 8)", e);
    test("var a, b; a = (b = 7, 6)", ok);
    test("function x(){}\nfunction f(a, b){}\nf(1,(x(), 2));", ok);
    test("function x(){}\nfunction f(a, b){}\nf(1,(2, 3));",
         "function x(){}\nfunction f(a, b){}\n" +
         "f(1,(JSCOMPILER_PRESERVE(2), 3));", e);
  }