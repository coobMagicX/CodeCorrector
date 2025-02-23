private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        
        int j = 4 * (n - 1);  
        for (int i = 0; i < j; i += 4) {
            int endIdx = j + 3; 
            for (int k = 0; k < 4; k++) {
                
                
                if (i + k < endIdx - k) {
                    double tmp = work[i + k];
                    work[i + k] = work[endIdx - k];
                    work[endIdx - k] = tmp;
                }
            }
