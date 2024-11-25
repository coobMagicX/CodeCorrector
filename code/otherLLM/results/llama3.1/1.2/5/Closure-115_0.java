private CanInlineResult inlineFunction(NodeTraversal t, Node callNode, final Node fnNode) {
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

  CanInlineResult result;
  if (fnContainsVars) {
    // Check if the caller contains functions or evals
    Predicate<Node> match = new Predicate<Node>() {
      @Override
      public boolean apply(Node n) {
        if (n.isName()) {
          return n.getString().equals("eval");
        }
        if (!t.inGlobalScope() && n.isFunction()) {
          return true;
        }
        return false;
      }
    };

    Node fnCallerBody = t.getScopeRoot().getLastChild();
    boolean forbidTemps = NodeUtil.has(fnCallerBody, match);

    // If the caller contains functions or evals and we have VAR declarations
    // in the function being inlined, verify we aren't adding any additional
    // VAR declarations because aliasing is needed.
    if (forbidTemps) {
      Map<String, Node> args =
          FunctionArgumentInjector.getFunctionCallParameterMap(
              fnNode, callNode, this.safeNameIdSupplier);
      boolean hasArgs = !args.isEmpty();
      if (hasArgs) {
        // Limit the inlining
        Set<String> namesToAlias = Sets.newHashSet();
        FunctionArgumentInjector.maybeAddTempsForCallArguments(
            fnNode, args, namesToAlias, compiler.getCodingConvention());
        if (!namesToAlias.isEmpty()) {
          return CanInlineResult.DO_NOT_INLINE;
        }
      }
    }

    // Check if the called function meets block inlining requirements
    boolean callMeetsBlockInliningRequirements = 
        callMeetsBlockInliningRequirements(t, callNode, fnNode, new HashSet<String>());

    result = callMeetsBlockInliningRequirements ?
             CanInlineResult.CAN_INLINE :
             CanInlineResult.DO_NOT_INLINE;
  } else {
    // Check if the called function meets block inlining requirements
    boolean callMeetsBlockInliningRequirements = 
        callMeetsBlockInliningRequirements(t, callNode, fnNode, new HashSet<String>());

    result = callMeetsBlockInliningRequirements ?
             CanInlineResult.CAN_INLINE :
             CanInlineResult.DO_NOT_INLINE;
  }

  return result;
}