public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }  
    if (str.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear 
        // to be in specification of class. OS X Java parses it to 
        // a wrong value.
        return null;
    }
    if (str.startsWith("-") && str.endsWith("l")) {
        str = str.substring(0, str.length() - 1);
        try {
            return createLong(str);
        } catch (NumberFormatException nfe) { // NOPMD
            // Too big for a long
        }
        return createBigInteger(str);

    } else if (str.startsWith("-") && str.endsWith("L")) {
        str = str.substring(0, str.length() - 1);
        try {
            return createLong(str);
        } catch (NumberFormatException nfe) { // NOPMD
            // Too big for a long
        }
        return createBigInteger(str);

    } else if (str.startsWith("-") && str.endsWith("f")) {
        str = str.substring(0, str.length() - 1);
        try {
            Float f = NumberUtils.createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                return f;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("F")) {
        str = str.substring(0, str.length() - 1);
        try {
            Float f = NumberUtils.createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                return f;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("d")) {
        str = str.substring(0, str.length() - 1);
        try {
            Double d = NumberUtils.createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                return d;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("D")) {
        str = str.substring(0, str.length() - 1);
        try {
            Double d = NumberUtils.createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                return d;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("l")) {
        str = str.substring(0, str.length() - 1);
        try {
            return createLong(str);
        } catch (NumberFormatException nfe) { // NOPMD
            // Too big for a long
        }
        return createBigInteger(str);

    } else if (str.startsWith("-") && str.endsWith("L")) {
        str = str.substring(0, str.length() - 1);
        try {
            return createLong(str);
        } catch (NumberFormatException nfe) { // NOPMD
            // Too big for a long
        }
        return createBigInteger(str);

    } else if (str.startsWith("-") && str.endsWith("f")) {
        str = str.substring(0, str.length() - 1);
        try {
            Float f = NumberUtils.createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                return f;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("F")) {
        str = str.substring(0, str.length() - 1);
        try {
            Float f = NumberUtils.createFloat(str);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                return f;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("d")) {
        str = str.substring(0, str.length() - 1);
        try {
            Double d = NumberUtils.createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                return d;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else if (str.startsWith("-") && str.endsWith("D")) {
        str = str.substring(0, str.length() - 1);
        try {
            Double d = NumberUtils.createDouble(str);
            if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                return d;
            }
        } catch (NumberFormatException nfe) { // NOPMD
            // ignore the bad number
        }

    } else {
        str = str.substring(1); // remove leading minus sign if present
        try {
            return createBigInteger(str);
        } catch (NumberFormatException e) {
            try {
                return createLong(str);
            } catch (NumberFormatException ex) {
                return NumberUtils.createDouble(str);
            }
        }
    }

    throw new IllegalArgumentException("Unable to parse number: " + str);
}