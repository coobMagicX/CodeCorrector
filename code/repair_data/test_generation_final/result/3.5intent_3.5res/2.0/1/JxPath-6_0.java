protected boolean equal(
    EvalContext context,
    Expression left,
    Expression right) {
    Object l = left.compute(context);
    Object r = right.compute(context);

    System.err.println("COMPARING: " +
        (l == null ? "null" : l.getClass().getName()) + " " +
        (r == null ? "null" : r.getClass().getName()));

    if (l instanceof InitialContext || l instanceof SelfContext) {
        l = ((EvalContext) l).getSingleNodePointer();
    }

    if (r instanceof InitialContext || r instanceof SelfContext) {
        r = ((EvalContext) r).getSingleNodePointer();
    }

    if (l instanceof Collection) {
        l = ((Collection) l).iterator();
    }

    if (r instanceof Collection) {
        r = ((Collection) r).iterator();
    }

    if ((l instanceof Iterator) && !(r instanceof Iterator)) {
        return contains((Iterator) l, r);
    }
    if (!(l instanceof Iterator) && (r instanceof Iterator)) {
        return contains((Iterator) r, l);
    }
    if (l instanceof Iterator && r instanceof Iterator) {
        return findMatch((Iterator) l, (Iterator) r);
    }
    return equal(l, r);
}

protected boolean equal(Object l, Object r) {
    if (l instanceof Pointer && r instanceof Pointer) {
        if (l.equals(r)) {
            return true;
        }
    }

    if (l instanceof Pointer) {
        l = ((Pointer) l).getValue();
    }

    if (r instanceof Pointer) {
        r = ((Pointer) r).getValue();
    }

    if (l == r) {
        return true;
    }

    // Add the comparison between XPath value of variable and string
    if (l instanceof Pointer && r instanceof String) {
        return InfoSetUtil.stringValue(l).equals(r);
    }
    if (l instanceof String && r instanceof Pointer) {
        return l.equals(InfoSetUtil.stringValue(r));
    }

    if (l instanceof Boolean || r instanceof Boolean) {
        return (InfoSetUtil.booleanValue(l) == InfoSetUtil.booleanValue(r));
    }
    if (l instanceof Number || r instanceof Number) {
        return (InfoSetUtil.doubleValue(l) == InfoSetUtil.doubleValue(r));
    }
    if (l instanceof String || r instanceof String) {
        return (
            InfoSetUtil.stringValue(l).equals(InfoSetUtil.stringValue(r)));
    }
    return l != null && l.equals(r);
}