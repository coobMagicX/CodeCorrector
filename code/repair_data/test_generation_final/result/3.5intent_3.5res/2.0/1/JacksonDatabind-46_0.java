public StringBuilder getGenericSignature(StringBuilder sb) {
    sb = _classSignature(_class, sb, false);
    sb.append('<');
    sb = _referencedType.getGenericSignature(sb);
    sb.append(';');
    return sb;
}