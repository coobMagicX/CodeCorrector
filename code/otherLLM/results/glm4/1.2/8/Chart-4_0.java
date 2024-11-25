public Range getDataRange(ValueAxis axis) {

    Range result = null;
    List mappedDatasets = new ArrayList();
    boolean isDomainAxis = false;

    // is it a domain axis?
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
    }

    // or is it a range axis?
    else {
        int rangeIndex = getRangeAxisIndex(axis);
        if (rangeIndex >= 0) {
            isDomainAxis = false;
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        }
    }

    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = (XYDataset) iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (isDomainAxis) {
                if (r != null) {
                    result = Range.combine(result, r.findDomainBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                }
            } else {
                if (r != null) {
                    result = Range.combine(result, r.findRangeBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
        }
    }

    // Include annotations in the data bounds
    List includedAnnotations = new ArrayList();
    Collection c = getRendererForDataset(mappedDatasets.get(0)).getAnnotations(); // Assuming only one dataset
    Iterator i = c.iterator();
    while (i.hasNext()) {
        XYAnnotation a = (XYAnnotation) i.next();
        if (a instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo) a).getIncludeInDataBounds()) {
            includedAnnotations.add(a);
        }
    }

    // Combine annotation bounds with the data range
    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        if (isDomainAxis) {
            result = Range.combine(result, xyabi.getXRange());
        } else {
            result = Range.combine(result, xyabi.getYRange());
        }
    }

    return result;
}