// Before returning null, check the requirement for return statement at the end of function.
if (node.isFunction()) {
  JSType requiredReturnType = node.getFunctionReturnType();
  if (requiredReturnType != null && !requiredReturnType.isVoidType()) {
    // Log an error or manage it according to the specific requirements
    throw new RuntimeException("JSC_MISSING_RETURN_STATEMENT. Missing return statement. Function expected to return " + requiredReturnType + ".");
  }
}
return null;
