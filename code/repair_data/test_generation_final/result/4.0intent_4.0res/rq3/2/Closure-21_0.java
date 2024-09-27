import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.ArrayList;

public class NodeAnalyzer {
    private final List<Node> problemNodes = new ArrayList<>();
    private final DiagnosticLevel level = DiagnosticLevel.WARNING;
    private static final String USELESS_CODE_ERROR = "USELESS_CODE_ERROR";

    public void visit(NodeTraversal t, Node n, Node parent) {
        // VOID nodes appear when there are extra semicolons at the BLOCK level.
        if (n.isEmpty() || n.isComma()) {
            return;
        }

        if (parent == null) {
            return;
        }

        // Do not try to remove a block or an expr result.
        if (n.isExprResult()) {
            return;
        }

        // This no-op statement was there so that JSDoc information could be attached to the name.
        if (n.isQualifiedName() && n.getJSDocInfo() != null) {
            return;
        }

        boolean isResultUsed = NodeUtil.isExpressionResultUsed(n);
        boolean isSimpleOp = NodeUtil.isSimpleOperatorType(n.getType());
        if (parent.getType() == Token.COMMA) {
            if (isResultUsed) {
                return;
            }
            if (n == parent.getLastChild()) {
                for (Node ancestor : parent.getAncestors()) {
                    int ancestorType = ancestor.getType();
                    if (ancestorType == Token.COMMA) continue;
                    if (ancestorType != Token.EXPR_RESULT && ancestorType != Token.BLOCK) return;
                }
            }
        } else if (parent.getType() != Token.EXPR_RESULT && parent.getType() != Token.BLOCK) {
            if (!(parent.getType() == Token.FOR && parent.getChildCount() == 4 &&
                    (n == parent.getFirstChild() || n == parent.getFirstChild().getNext().getNext()))) {
                return;
            }
        }

        if (isSimpleOp || !NodeUtil.mayHaveSideEffects(n, t.getCompiler())) {
            String msg = "This code lacks side-effects. Is there a bug?";
            if (n.isString()) {
                msg = "Is there a missing '+' on the previous line?";
            } else if (isSimpleOp) {
                msg = "The result of the '" + Token.name(n.getType()).toLowerCase() +
                        "' operator is not being used.";
            }

            t.getCompiler().report(t.makeError(n, level, USELESS_CODE_ERROR, msg));
            if (!NodeUtil.isStatement(n)) {
                problemNodes.add(n);
            }
        }
    }
}