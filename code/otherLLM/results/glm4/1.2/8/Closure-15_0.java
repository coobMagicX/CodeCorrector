public boolean apply(Node n) {
  // When the node is null it means, we reached the implicit return
  // where the function returns (possibly without a return statement)
  if (n == null) {
    return false;
  }

  // Check for call or new expressions that might have side effects.
  if ((n.isCall() && NodeUtil.functionCallHasSideEffects(n)) || n.isNew()) {
    // If it's a constructor call, we also check for side effects on the constructor itself.
    if (NodeUtil.constructorCallHasSideEffects(n)) {
      return true;
    }
    
    // Check if the called function or new expression is dependent on any variable that
    // might be modified by the current context.
    Node parent = n.getParent();
    while (parent != null && !ControlFlowGraph.isEnteringNewCfgNode(parent)) {
      if (visit(parent, n)) {
        return true;
      }
      parent = parent.getParent();
    }
  }

  for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
    if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
      return true;
    }
  }
  
  // If we reach here, it means there are no side effects or inlining candidates.
  return false;
}

private boolean visit(Node parent, Node n) {
  if (parent == null || ControlFlowGraph.isEnteringNewCfgNode(parent)) {
    return false;
  }

  DiGraphNode<Node, Branch> graphNode = cfg.getDirectedGraphNode(n);
  if (graphNode == null) {
    // Not a CFG node.
    return false;
  }
  
  FlowState<MustDef> state = graphNode.getAnnotation();
  final MustDef defs = state.getIn();
  final Node cfgNode = n;

  AbstractCfgNodeTraversalCallback gatherCb = new AbstractCfgNodeTraversalCallback() {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isName()) {
        if (parent == null || n.getParent() != parent) {
          return;
        }

        // Check if the name node is purely a read.
        if ((NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n)
            || parent.isVar() || parent.isInc() || parent.isDec()
            || parent.isParamList() || parent.isCatch()) {
          return;
        }
        
        String name = n.getString();
        if (compiler.getCodingConvention().isExported(name)) {
          return;
        }

        Node defNode = reachingDef.getDef(name, cfgNode);
        // Check for definition outside of current context.
        if (defNode != null && !reachingDef.dependsOnOuterScopeVars(name, cfgNode)) {
          candidates.add(new Candidate(name, defNode, n, cfgNode));
        }
      }
    }
  };

  NodeTraversal.traverse(compiler, cfgNode, gatherCb);
  
  // If any candidate has been found, it means there's a side effect.
  return !candidates.isEmpty();
}