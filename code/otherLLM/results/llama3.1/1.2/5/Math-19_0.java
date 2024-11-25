if (min(diagC) != 0) {
    double conditionNumber = max(diagD)/min(diagC);
    if (conditionNumber > 1e7) {
        break;
    }
}