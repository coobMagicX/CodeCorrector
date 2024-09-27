public final Object computeValue(EvalContext context) {
    Object left = args[0].computeValue(context);
    Object right = args[1].computeValue(context);
    
    // Handling cases where one or both operands might be non-existent nodes or empty node sets
    if (left == null || right == null || (left instanceof Collection && ((Collection) left).isEmpty()) || (right instanceof Collection && ((Collection) right).isEmpty())) {
        return Boolean.FALSE;
    }
    
    return compute(left, right) ? Boolean.TRUE : Boolean.FALSE;
}