public String getValue() {
    if (val.equals("true") || val.equals("false")) {
        return "";
    } else if (val.equals("")) {
        return val;
    }
    return val;
}