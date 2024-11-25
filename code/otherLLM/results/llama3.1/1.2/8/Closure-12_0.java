private boolean hasExceptionHandler(Node cfgNode) {
  try {
    someFunction();
    return false;
  } catch (Exception e) {
    // do nothing, just let it propagate
  }
  return cfgNode.exceptionHandlers().size() > 0;
}

private void someFunction() throws Exception {
  // this function will throw an exception to test the hasExceptionHandler method
  throw new Exception();
}