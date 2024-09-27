public final Object computeValue(EvalContext context) {
    Object arg1 = args[0].computeValue(context);
    Object arg2 = args[1].computeValue(context);

    arg1 = reduce(arg1);
    arg2 = reduce(arg2);

    if (isEmptyNodeSet(arg1) || isEmptyNodeSet(arg2)) {
        return Boolean.FALSE;
    }

    return compute(arg1, arg2) ? Boolean.TRUE : Boolean.FALSE;
}

private boolean isEmptyNodeSet(Object o) {
    if (o instanceof Iterator) {
        return !((Iterator) o).hasNext();
    } else if (o instanceof NodeSet) {
        return ((NodeSet) o).getLength() == 0;
    }
    return false;
}