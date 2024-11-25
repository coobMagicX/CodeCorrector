private boolean hasExceptionHandler(Node cfgNode) {
  try {
    someFunction();
    return true;
  } catch (Exception e) {
    // Handle exception, but continue execution and propagate value
    int result = someFunction(); // Call someFunction again
    return result != null; // Use the return value of someFunction in a subsequent expression
  }
}