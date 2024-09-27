public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(value);
    secondMoment.increment(value);

    // If mean, variance or geomean have been overridden,
    // need to increment these
    if (meanImpl != null) {
        meanImpl.increment(value);
    } else {
        sumImpl.increment(value);
        n++;
    }

    if (varianceImpl != null) {
        varianceImpl.increment(value);
    } else {
        sumsqImpl.increment(value * value);
    }

    if (geoMeanImpl != null) {
        geoMeanImpl.increment(value);
    } else {
        sumLogImpl.increment(Math.log(value));
    }

    n++;
}

public void setMeanImpl(StorelessUnivariateStatistic meanImpl) {
    checkEmpty();
    this.meanImpl = meanImpl;
    if (meanImpl != null) {
        meanImpl.clear();
        for (double value : values) {
            meanImpl.increment(value);
        }
    }
}

public void setVarianceImpl(StorelessUnivariateStatistic varianceImpl) {
    checkEmpty();
    this.varianceImpl = varianceImpl;
    if (varianceImpl != null) {
        varianceImpl.clear();
        for (double value : values) {
            varianceImpl.increment(value);
        }
    }
}

public void setGeoMeanImpl(StorelessUnivariateStatistic geoMeanImpl) {
    checkEmpty();
    this.geoMeanImpl = geoMeanImpl;
    if (geoMeanImpl != null) {
        geoMeanImpl.clear();
        for (double value : values) {
            geoMeanImpl.increment(value);
        }
    }
}