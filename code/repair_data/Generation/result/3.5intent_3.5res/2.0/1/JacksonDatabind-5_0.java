protected void _addMethodMixIns(Class<?> targetClass, AnnotatedMethodMap methods,
    Class<?> mixInCls, AnnotatedMethodMap mixIns) {
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
                mixIns.add(_constructMethod(m));
            }
        }
    }
}

protected void _addMixUnders(Method mixin, AnnotatedMethod target) {
    _addMixOvers(mixin, target, false);
}

protected void _addMixOvers(Method mixin, AnnotatedMethod target,
    boolean addParamAnnotations) {
    _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
    if (addParamAnnotations) {
        Annotation[][] pa = mixin.getParameterAnnotations();
        for (int i = 0, len = pa.length; i < len; ++i) {
            for (Annotation a : pa[i]) {
                target.addOrOverrideParam(i, a);
            }
        }
    }
}