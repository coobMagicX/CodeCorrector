public StringBuilder getGenericSignature(StringBuilder sb) {
    _classSignature(_class, sb, false);
    if (_referencedType != null) {
        sb.append('<');
        sb = _referencedType.getGenericSignature(sb);
        sb.append('>');
    }
    sb.append(';');
    return sb;
}