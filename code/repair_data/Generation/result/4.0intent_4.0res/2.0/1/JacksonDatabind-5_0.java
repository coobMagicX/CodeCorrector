protected void _addMethodMixIns(Class<?> targetClass, AnnotatedMethodMap methods,
        Class<?> mixInCls, AnnotatedMethodMap mixIns)
{
    List<Class<?>> parents = new ArrayList<Class<?>>();
    parents.add(mixInCls);
    ClassUtil.findSuperTypes(mixInCls, targetClass, parents);
    for (Class<?> mixin : parents) {
        for (Method m : mixin.getDeclaredMethods()) {
            if (!_isIncludableMemberMethod(m)) {
                continue;
            }
            AnnotatedMethod am = methods.find(m);
            if (am != null) {
                _addMixUnders(m, am);
            } else {
                AnnotatedMethod mixInMethod = _constructMethod(m);
                AnnotatedMethod existingMethod = methods.find(mixInMethod.getName(), mixInMethod.getParameterTypes());
                if (existingMethod != null) {
                    _addMixUnders(m, existingMethod);
                } else {
                    mixIns.add(mixInMethod);
                }
            }
        }
    }
}