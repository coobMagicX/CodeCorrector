if (covariances.length != dim) {
    throw new DimensionMismatchException("Number of covariance matrices must match the number of dimensions");
}

// Loop through each dimension and check something?
for (int i = 0; i < dim; i++) {
    if (covariances[i].length != dim) { // Check if each row has correct length
        throw new DimensionMismatchException("Number of rows in covariance matrices must match the number of dimensions");
    }
}