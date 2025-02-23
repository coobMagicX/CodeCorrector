static boolean isSimpleNumber(String s) {
  int len = s.length();
  
  if (len == 0) {
    return false; 
  }
  
  if (len > 1 && s.charAt(0) == '0') {
    return false; 
  }
  
