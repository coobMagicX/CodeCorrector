// Adjust the step size limit based on the maximum growth factor
final double adjustedStepSizeLimit = stepSize * maxGrowth;
if (hNew > adjustedStepSizeLimit) {
    hNew = adjustedStepSizeLimit;
}

final double  scaledH    = hNew;
final double  nextT      = stepStart + scaledH;
final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
hNew = filterStep(scaledH, forward, nextIsLast);

final double  filteredNextT      = stepStart + hNew;
final boolean filteredNextIsLast = forward ? (filteredNextT >= t) : (filteredNextT <= t);
if (filteredNextIsLast) {
    hNew = t - stepStart;
}