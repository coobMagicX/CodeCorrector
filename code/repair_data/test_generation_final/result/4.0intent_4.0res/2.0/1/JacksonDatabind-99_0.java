protected String buildCanonicalName() {
    StringBuilder sb = new StringBuilder();
    sb.append(_class.getName());
    if (_typeParameters != null && _typeParameters.size() > 0) {
        sb.append('<');
        for (int i = 0; i < _typeParameters.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(_typeParameters.get(i).toCanonical());
        }
        sb.append('>');
    }
    return sb.toString();
}