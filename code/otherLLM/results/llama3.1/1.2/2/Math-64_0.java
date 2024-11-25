private double computeScaledPredictedReduction() {
    double coeff1 = 0;
    for (int j = 0; j < solvedCols; ++j) {
        coeff1 += work1[j] * work1[j];
    }
    double pc2 = previousCost * previousCost;
    coeff1 = coeff1 / pc2;
    double coeff2 = lmPar * lmNorm * lmNorm / pc2;
    return coeff1 + 2 * coeff2;
}