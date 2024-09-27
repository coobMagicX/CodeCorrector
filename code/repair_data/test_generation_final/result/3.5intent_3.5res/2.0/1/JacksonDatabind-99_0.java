protected String buildCanonicalName()
{
    StringBuilder sb = new StringBuilder();
    sb.append(_class.getName());
    if (_referencedType instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) _referencedType;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        sb.append('<');
        for (int i = 0; i < typeArguments.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            if (typeArguments[i] instanceof Class) {
                sb.append(((Class) typeArguments[i]).getName());
            } else if (typeArguments[i] instanceof ParameterizedType) {
                sb.append(((ParameterizedType) typeArguments[i]).toCanonical());
            } else if (typeArguments[i] instanceof GenericArrayType) {
                sb.append(((GenericArrayType) typeArguments[i]).toCanonical());
            } else {
                sb.append(typeArguments[i].getTypeName());
            }
        }
        sb.append('>');
    } else {
        sb.append('<');
        sb.append(_referencedType.toCanonical());
        sb.append('>');
    }
    return sb.toString();
}