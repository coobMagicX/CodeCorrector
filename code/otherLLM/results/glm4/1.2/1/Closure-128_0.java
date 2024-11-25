static boolean isSimpleNumber(String s) {
    int len = s.length();
    // Trim leading spaces to handle numbers with leading spaces
    while (len > 0 && s.charAt(0) == ' ') {
        s = s.substring(1);
        len--;
    }
    
    if (len == 0 || s.charAt(0) == '0') {
        return false;
    }

    for (int index = 0; index < len; index++) {
        char c = s.charAt(index);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    
    // After the loop, check again in case the number starts with a '0' after trimming spaces
    if (s.length() > 1 && s.charAt(0) == '0') {
        return false;
    }

    return true;
}