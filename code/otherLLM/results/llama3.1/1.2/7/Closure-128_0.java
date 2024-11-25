static boolean isSimpleNumber(String s) {
  if (!(s instanceof String)) { // Validate input format: check if s is a string
    return false; // Return False for non-string inputs
  }
  
  int len = s.length();
  for (int index = 0; index < len; index++) {
    char c = s.charAt(index);
    if (c < '0' || c > '9') { // Correctly parse numeric values: check for decimal points and scientific notation
      if (c == '.' || c == 'e' || c == 'E') { 
        continue;
      } else {
        return false; // Return False for non-numeric characters other than decimal point or scientific notation
      }
    }
  }
  
  return len > 0 && s.charAt(0) != '0'; // Check for leading zeros
}