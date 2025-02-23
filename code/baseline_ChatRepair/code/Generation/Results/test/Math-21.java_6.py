for (int i = r + 1; i < order; ++i) {
    int ii = index[i];
    double e = c[ii][ir] / sqrt;
    b[i][r] = e;
    for (int j = r + 1; j <= i; ++j) {
        int jj = index[j];
        c[ii][jj] -= e * b[j][r];
        if (i != j) {
            c[jj][ii] = c[ii][jj];  // maintain symmetry
        }
    }
}
