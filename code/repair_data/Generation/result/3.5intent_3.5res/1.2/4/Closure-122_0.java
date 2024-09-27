private void handleBlockComment(Comment comment) {
  if (comment.getValue().matches("\\/\\* @") || comment.getValue().matches("\\n \\* @")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}

private Node transform(AstNode node) {
  Node irNode = justTransform(node);
  JSDocInfo jsDocInfo = handleJsDoc(node, irNode);
  if (jsDocInfo != null) {
    irNode = maybeInjectCastNode(node, jsDocInfo, irNode);
    irNode.setJSDocInfo(jsDocInfo);
  }
  setSourceInfo(irNode, node);
  return irNode;
}