case Token.FOR:
    if (!NodeUtil.isForIn(n)) {
        computeGenKill(NodeUtil.getConditionExpression(n), gen, kill, conditional);
    } else {
        // for(x in y) {...}
        Node lhs = n.getFirstChild();
        Node rhs = lhs.getNext();
        if (NodeUtil.isVar(lhs)) {
            // for(var x in y) {...}
            lhs = lhs.getFirstChild();
            computeGenKill(lhs.getFirstChild(), gen, kill, conditional);
        } else {
            computeGenKill(lhs, gen, kill, conditional);
        }
        addToSetIfLocal(lhs, kill);
        addToSetIfLocal(rhs, gen);
        computeGenKill(rhs, gen, kill, conditional);
    }
    return;
