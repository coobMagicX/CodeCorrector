static boolean isSimpleNumber(String s) {
    if (s == null || s.isEmpty()) {
        return false;
    }
    String regex = "^(0x[0-9a-fA-F]+|0[0-7]*|[1-9]\\d*|0)([.]\\d+)?$";
    return s.matches(regex);
}