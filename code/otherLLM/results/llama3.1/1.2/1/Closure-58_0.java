private void computeGenKill(Node n, BitSet gen, BitSet kill,
    boolean conditional) {

  switch (n.getType()) {
    case ASSIGNMENT:
      // Add variable on the left side of assignment to GEN set.
      addToSetIfLocal(((AssignmentNode)n).getLeft(), gen);
      break;
    case FUNCTION_CALL:
      // If function call is not conditionally executed, add all parameters
      // to KILL set.
      if (!conditional) {
        for (Var param : ((FunctionCallNode)n).getParameters()) {
          addToSetIfLocal(param, kill);
        }
      } else {
        // If function call is conditionally executed, do nothing.
      }
      break;
    case VARIABLE_READ:
      // Add variable being read to KILL set if it's not conditionally
      // executed.
      if (!conditional) {
        addToSetIfLocal(((VariableReadNode)n).getVariable(), kill);
      } else {
        // If variable being read is conditionally executed, do nothing.
      }
      break;
    case VARIABLE_WRITE:
      // Add variable being written to GEN set if it's not
      // conditionally executed.
      if (!conditional) {
        addToSetIfLocal(((VariableWriteNode)n).getVariable(), gen);
      } else {
        // If variable being written is conditionally executed, do nothing.
      }
      break;
    case FOR_LOOP:
      // In a for loop, the index of the loop is in KILL set.
      addToSetIfLocal(((ForLoopNode)n).getIndex(), kill);
      break;
  }

  // If the node is not an assignment, add all variables to KILL set
  if (n.getType() != NodeType.ASSIGNMENT) {
    for (Var var : n.getVariables()) {
      addToSetIfLocal(var, kill);
    }
  }

  // Add any local variables that are live at this point in the program
  // to GEN set.
  for (Var var : jsScope.getLocalVariables()) {
    if (liveVariablesAnalysis.isLive(var)) {
      addToSetIfLocal(var, gen);
    }
  }
}