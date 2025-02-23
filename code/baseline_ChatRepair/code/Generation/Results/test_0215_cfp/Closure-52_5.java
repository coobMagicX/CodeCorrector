static boolean isSimpleNumber(String s) {
  int len = s.length();
  
  
  if (len == 0 || (s.charAt(0) == '0' && !s.equals("0"))) {
    return false;
  }
  
  