if (js.indexOf('\n', pos) == -1) {
    if (pos >= js.length()) {
        return null;
    } else {
        return js.substring(pos, js.length());
    }
} else {
    return js.substring(pos, js.indexOf('\n', pos));
}