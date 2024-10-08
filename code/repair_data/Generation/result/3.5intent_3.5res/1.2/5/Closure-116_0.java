private boolean callMeetsBlockInliningRequirements(
    NodeTraversal t, Node callNode, final Node fnNode, Set<String> namesToAlias) {
  final boolean assumeMinimumCapture = this.assumeMinimumCapture;

  // Note: functions that contain function definitions are filtered out
  // in isCandidateFunction.

  // TODO(johnlenz): Determining if the called function contains VARs
  // or if the caller contains inner functions accounts for 20% of the
  // run-time cost of this pass.

  // Don't inline functions with var declarations into a scope with inner
  // functions as the new vars would leak into the inner function and
  // cause memory leaks.
  boolean fnContainsVars = NodeUtil.has(
      NodeUtil.getFunctionBody(fnNode),
      new NodeUtil.MatchDeclaration(),
      new NodeUtil.MatchShallowStatement());
  boolean forbidTemps = false;
  if (!t.inGlobalScope()) {
    Node fnCaller = t.getScopeRoot();
    Node fnCallerBody = fnCaller.getLastChild();

    // Don't allow any new vars into a scope that contains eval or one
    // that contains functions (excluding the function being inlined).
    Predicate<Node> match = new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        if (n.isName()) {
          return n.getString().equals("eval");
        }
        if (!assumeMinimumCapture && n.isFunction()) {
          return n != fnNode;
        }
        return false;
      }
    };
    forbidTemps = NodeUtil.has(fnCallerBody, match, NodeUtil.MATCH_NOT_FUNCTION);
  }

  if (fnContainsVars && forbidTemps) {
    return false;
  }

  // If the caller contains functions or evals, verify we aren't adding any
  // additional VAR declarations because aliasing is needed.
  if (forbidTemps && !namesToAlias.isEmpty()) {
    return false;
  }

  return true;
}