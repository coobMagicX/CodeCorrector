public static Date parse(String date, ParsePosition pos) throws ParseException {
    Exception fail = null;
    try {
        int offset = pos.getIndex();

        // extract year
        int year = parseInt(date, offset, offset += 4);
        if (checkOffset(date, offset, '-')) {
            offset += 1;
        }

        // extract month
        int month = parseInt(date, offset, offset += 2);
        if (checkOffset(date, offset, '-')) {
            offset += 1;
        }

        // extract day
        int day = parseInt(date, offset, offset += 2);
        // default time value
        int hour = 0;
        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0; // always use 0 otherwise returned date will include millis of current time

        // if the value has no time component (and no time zone), we are done
        boolean hasT = checkOffset(date, offset, 'T');
        
        if (!hasT && (date.length() <= offset)) {
            Calendar calendar = new GregorianCalendar(year, month - 1, day);

            pos.setIndex(offset);
            return calendar.getTime();
        }

        if (hasT) {

            // extract hours, minutes, seconds and milliseconds
            hour = parseInt(date, offset += 1, offset += 2);
            if (checkOffset(date, offset, ':')) {
                offset += 1;
            }

            minutes = parseInt(date, offset, offset += 2);
            if (checkOffset(date, offset, ':')) {
                offset += 1;
            }
            // second and milliseconds can be optional
            if (date.length() > offset) {
                char c = date.charAt(offset);
                if (c != 'Z' && c != '+' && c != '-') {
                    seconds = parseInt(date, offset, offset += 2);
                    if (seconds > 59 && seconds < 63) seconds = 59; // truncate up to 3 leap seconds
                    // milliseconds can be optional in the format
                    if (checkOffset(date, offset, '.')) {
                        offset += 1;
                        int endOffset = indexOfNonDigit(date, offset + 1); // assume at least one digit
                        int parseEndOffset = Math.min(endOffset, offset + 3); // parse up to 3 digits
                        int fraction = parseInt(date, offset, parseEndOffset);
                        // compensate for "missing" digits
                        switch (parseEndOffset - offset) { // number of digits parsed
                        case 2:
                            milliseconds = fraction * 10;
                            break;
                        case 1:
                            milliseconds = fraction * 100;
                            break;
                        default:
                            milliseconds = fraction;
                        }
                        offset = endOffset;
                    }
                }
            }
        }

        // extract timezone
        if (date.length() <= offset) {
            throw new IllegalArgumentException("No time zone indicator");
        }

        TimeZone timezone = null;
        char timezoneIndicator = date.charAt(offset);

        if (timezoneIndicator == 'Z') {
            timezone = TIMEZONE_UTC;
            offset += 1;
        } else if (timezoneIndicator == '+' || timezoneIndicator == '-') {
            String timezoneOffset = date.substring(offset);

            // Append ":00" if timezone has only hour part
            if (timezoneOffset.matches("[+-]\\d\\d")) {
                timezoneOffset += ":00";
            }

            // Now process the full timezone string
            offset += timezoneOffset.length();
            timezone = TimeZone.getTimeZone("GMT" + timezoneOffset);

            String act = timezone.getID();
            if (!act.equals("GMT" + timezoneOffset)) {
                String cleaned = act.replace(":", "");
                if (!cleaned.equals("GMT" + timezoneOffset)) {
                    throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + "GMT" + timezoneOffset + " given, resolves to "
                            + timezone.getID());
                }
            }
        } else {
            throw new IndexOutOfBoundsException("Invalid time zone indicator '" + timezoneIndicator + "'");
        }

        Calendar calendar = new GregorianCalendar(timezone);
        calendar.setLenient(false);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milliseconds);

        pos.setIndex(offset);
        return calendar.getTime();
    } catch (IndexOutOfBoundsException e) {
        fail = e;
    } catch (NumberFormatException e) {
        fail = e;
    } catch (IllegalArgumentException e) {
        fail = e;
    }
    String input = (date == null) ? null : ('"' + date + '"');
    String msg = fail.getMessage();
    if (msg == null || msg.isEmpty()) {
        msg = "(" + fail.getClass().getName() + ")";
    }
    ParseException ex = new ParseException("Failed to parse date [" + input + "]: " + msg, pos.getIndex());
    ex.initCause(fail);
    throw ex;
}