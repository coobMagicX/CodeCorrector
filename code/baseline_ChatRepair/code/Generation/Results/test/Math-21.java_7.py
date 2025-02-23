for (int i = r + 1; i < order; ++i) {
    int ii = index[i];
    double e = c[ii][ir] / sqrt;
    b[i][r] = e;
    for (int j = r + 1; j <= i; ++j) {
        int jj = index[j];
        // Correctly update the elements of C and maintain symmetry
        double updatedValue = c[ii][jj] - e * b[j][r];
        c[ii][jj] = updatedValue;
        c[jj][ii] = updatedValue; // explicitly maintain symmetry
    }
}
