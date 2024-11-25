public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    // Remove leading whitespace
    String trimmedStr = str.replaceAll("^\\s+", "");
    
    char[] chars = trimmedStr.toCharArray();
    int sz = chars.length;
    boolean hasExp = false;
    boolean hasDecPoint = false;
    boolean allowSigns = false;
    boolean foundDigit = false;

    // deal with any possible sign up front
    int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
    
    if (sz > start + 1) {
        if (toLowerCase(chars[start]) == '0' && toLowerCase(chars[start + 1]) == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false; // str == "0x"
            }
            // checking hex (it can't be anything else)
            for (; i < chars.length; i++) {
                if (!isHexChar(chars[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    sz--; // don't want to loop to the last char, check it afterwards
          // for type qualifiers

    int i = start;
    
    // loop to the next to last char or to the last char if we need another digit to 
    // make a valid number (e.g. chars[0..5] = "1234E")
    while (i < sz || (i == sz && allowSigns && !foundDigit)) {
        if (chars[i] >= '0' && chars[i] <= '9') {
            foundDigit = true;
            allowSigns = false;

        } else if (chars[i] == '.') {
            if (hasDecPoint || hasExp) {
                // two decimal points or dec in exponent   
                return false;
            }
            hasDecPoint = true;
        } else if (chars[i] == 'e' || chars[i] == 'E') {
            // we've already taken care of hex.
            if (hasExp) {
                // two E's
                return false;
            }
            if (!foundDigit) {
                return false;
            }
            hasExp = true;
            allowSigns = true;
        } else if (chars[i] == '+' || chars[i] == '-') {
            if (!allowSigns) {
                return false;
            }
            allowSigns = false;
            foundDigit = false; // we need a digit after the E
        } else if ((sz > i + 1 && toLowerCase(chars[i]) == 'l' 
                    || chars[i] == 'L')) { 
            if (hasExp || hasDecPoint) {
                return foundDigit && !hasExp;
            }
        } else {
            return false;
        }

        i++;
    }
    
    // allowSigns is true iff the val ends in 'E'
    // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
    if (allowSigns) {
        return false; 
    }
    
    return !StringUtils.isEmpty(trimmedStr) && foundDigit;
}

private static boolean isHexChar(char c) {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
}

private static String toLowerCase(String str) {
    if (str == null) {
        return null;
    }
    
    int len = str.length();
    StringBuilder lowerCaseStr = new StringBuilder(len);
    
    for (int i = 0; i < len; ++i) {
        char c = str.charAt(i);
        
        switch(c) {
            case 'A': 
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                lowerCaseStr.append((char)(c - 32));
                break;
            
            default:
                lowerCaseStr.append(c);
        }
    }
    
    return lowerCaseStr.toString();
}