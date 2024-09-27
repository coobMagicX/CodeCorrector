private int peekNumber() throws IOException {
  char[] buffer = this.buffer;
  int p = pos;
  int l = limit;

  long value = 0; // Negative to accommodate Long.MIN_VALUE more easily.
  boolean negative = false;
  boolean fitsInLong = true;
  int last = NUMBER_CHAR_NONE;

  int i = 0;

  charactersOfNumber:
  for (; true; i++) {
    if (p + i == l) {
      if (i == buffer.length) {
        return PEEKED_NONE;
      }
      if (!fillBuffer(i + 1)) {
        break;
      }
      p = pos;
      l = limit;
    }

    char c = buffer[p + i];
    switch (c) {
      case '-':
        if (last == NUMBER_CHAR_NONE) {
          negative = true;
          last = NUMBER_CHAR_SIGN;
          continue;
        } else if (last == NUMBER_CHAR_EXP_E) {
          last = NUMBER_CHAR_EXP_SIGN;
          continue;
        }
        return PEEKED_NONE;

      case '+':
        if (last == NUMBER_CHAR_EXP_E) {
          last = NUMBER_CHAR_EXP_SIGN;
          continue;
        }
        return PEEKED_NONE;

      case 'e':
      case 'E':
        if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT) {
          last = NUMBER_CHAR_EXP_E;
          continue;
        }
        return PEEKED_NONE;

      case '.':
        if (last == NUMBER_CHAR_DIGIT) {
          last = NUMBER_CHAR_DECIMAL;
          continue;
        }
        return PEEKED_NONE;

      default:
        if (c < '0' || c > '9') {
          if (!isLiteral(c)) {
            break charactersOfNumber;
          }
          return PEEKED_NONE;
        }
        if (last == NUMBER_CHAR_SIGN || last == NUMBER_CHAR_NONE) {
          value = -(c - '0');
          last = NUMBER_CHAR_DIGIT;
        } else if (last == NUMBER_CHAR_DIGIT) {
          // Handling negative zero specifically
          if (value == 0 && c == '0' && negative) { // Check for "-0"
            fitsInLong = false; // Ensure "-0" is treated specifically
          } else if (value == 0 && !negative) {
            return PEEKED_NONE; // Prevent "0" prefix in any non-negative number
          }
          long newValue = value * 10 - (c - '0');
          fitsInLong &= value > MIN_INCOMPLETE_INTEGER
              || (value == MIN_INCOMPLETE_INTEGER && newValue < value);
          value = newValue;
        } else if (last == NUMBER_CHAR_DECIMAL) {
          last = NUMBER_CHAR_FRACTION_DIGIT;
        } else if (last == NUMBER_CHAR_EXP_E || last == NUMBER_CHAR_EXP_SIGN) {
          last = NUMBER_CHAR_EXP_DIGIT;
        }
    }
  }

  if (last == NUMBER_CHAR_DIGIT && fitsInLong && (value != Long.MIN_VALUE || negative)) {
    peekedLong = negative ? value : -value;
    pos += i;
    return peeked = PEEKED_LONG;
  } else if (last == NUMBER_CHAR_DIGIT || last == NUMBER_CHAR_FRACTION_DIGIT
      || last == NUMBER_CHAR_EXP_DIGIT) {
    peekedNumberLength = i;
    return peeked = PEEKED_NUMBER;
  } else {
    return PEEKED_NONE;
  }
}