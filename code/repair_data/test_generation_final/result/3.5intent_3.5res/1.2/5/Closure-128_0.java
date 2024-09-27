static String minifyObject(String obj) {
  StringBuilder sb = new StringBuilder();
  boolean insideString = false;
  boolean insideComment = false;
  boolean insideObject = false;
  boolean insideArray = false;

  for (int i = 0; i < obj.length(); i++) {
    char c = obj.charAt(i);

    if (insideComment) {
      if (c == '\n') {
        insideComment = false;
      }
      continue;
    }

    if (insideString) {
      if (c == '\\') {
        sb.append(c);
        i++;
      } else if (c == '"') {
        insideString = false;
      }
      sb.append(c);
      continue;
    }

    if (c == '"') {
      insideString = true;
      sb.append(c);
      continue;
    }

    if (insideObject) {
      if (c == '{') {
        insideObject = true;
      } else if (c == '}') {
        insideObject = false;
      }
      sb.append(c);
      continue;
    }

    if (insideArray) {
      if (c == '[') {
        insideArray = true;
      } else if (c == ']') {
        insideArray = false;
      }
      sb.append(c);
      continue;
    }

    if (c == '/') {
      if (i < obj.length() - 1 && obj.charAt(i + 1) == '/') {
        insideComment = true;
        continue;
      }
    }

    if (c == '{') {
      insideObject = true;
    } else if (c == '[') {
      insideArray = true;
    }

    sb.append(c);
  }

  return sb.toString();
}