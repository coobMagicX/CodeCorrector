  public void testRemoveUselessOps() {
    // There are four place where expression results are discarded:
    //  - a top level expression EXPR_RESULT
    //  - the LHS of a COMMA
    //  - the FOR init expression
    //  - the FOR increment expression


    // Known side-effect free functions calls are removed.
    fold("Math.random()", "");
    fold("Math.random(f() + g())", "f(),g();");
    fold("Math.random(f(),g(),h())", "f(),g(),h();");

    // Calls to functions with unknown side-effects are are left.
    foldSame("f();");
    foldSame("(function () {})();");

    // Uncalled function expressions are removed
    fold("(function () {});", "");
    fold("(function f() {});", "");
    // ... including any code they contain.
    fold("(function () {foo();});", "");

    // Useless operators are removed.
    fold("+f()", "f()");
    fold("a=(+f(),g())", "a=(f(),g())");
    fold("a=(true,g())", "a=g()");
    fold("f(),true", "f()");
    fold("f() + g()", "f(),g()");

    fold("for(;;+f()){}", "for(;;f()){}");
    fold("for(+f();;g()){}", "for(f();;g()){}");
    fold("for(;;Math.random(f(),g(),h())){}", "for(;;f(),g(),h()){}");

    // The optimization cascades into conditional expressions:
    fold("g() && +f()", "g() && f()");
    fold("g() || +f()", "g() || f()");
    fold("x ? g() : +f()", "x ? g() : f()");

    fold("+x()", "x()");
    fold("+x() * 2", "x()");
    fold("-(+x() * 2)", "x()");
    fold("2 -(+x() * 2)", "x()");
    fold("x().foo", "x()");
    foldSame("x().foo()");

    foldSame("x++");
    foldSame("++x");
    foldSame("x--");
    foldSame("--x");
    foldSame("x = 2");
    foldSame("x *= 2");

    // Sanity check, other expression are left alone.
    foldSame("function f() {}");
    foldSame("var x;");
  }