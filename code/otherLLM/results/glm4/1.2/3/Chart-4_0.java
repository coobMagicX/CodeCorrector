public Range getDataRange(ValueAxis axis) {

    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>();
    boolean isDomainAxis;

    // is it a domain axis?
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(domainIndex));
    }
    // or is it a range axis?
    else {
        isDomainAxis = false;
        domainIndex = getRangeAxisIndex(axis); // Corrected this line to reflect the axis index
        if (domainIndex >= 0) {
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(domainIndex));
        }
    }

    List<XYAnnotation> includedAnnotations = new ArrayList<>();
    Iterator<XYAnnotation> iteratorAnnotations = getAnnotations().iterator();
    while (iteratorAnnotations.hasNext()) {
        XYAnnotation annotation = iteratorAnnotations.next();
        if (annotation instanceof XYAnnotationBoundsInfo) {
            includedAnnotations.add(annotation);
        }
    }

    // iterate through the datasets that map to the axis and get the union
    // of the ranges.
    Iterator<XYDataset> iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            Range bounds;
            if (isDomainAxis) {
                if (r != null) {
                    bounds = r.findDomainBounds(d);
                } else {
                    bounds = DatasetUtilities.findDomainBounds(d);
                }
            } else {
                if (r != null) {
                    bounds = r.findRangeBounds(d);
                } else {
                    bounds = DatasetUtilities.findRangeBounds(d);
                }
            }

            result = Range.combine(result, bounds);

            // Add annotation bounds to the result range
            Collection<XYAnnotation> annotations = r.getAnnotations();
            if (annotations != null) {
                for (XYAnnotation a : annotations) {
                    if (a instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo) a).getIncludeInDataBounds()) {
                        if (isDomainAxis) {
                            result = Range.combine(result, ((XYAnnotationBoundsInfo) a).getXRange());
                        } else {
                            result = Range.combine(result, ((XYAnnotationBoundsInfo) a).getYRange());
                        }
                    }
                }
            }
        }
    }

    // Combine the bounds from annotations
    Iterator<XYAnnotationBoundsInfo> iteratorBounds = includedAnnotations.stream()
            .map(a -> (XYAnnotationBoundsInfo) a)
            .iterator();
    while (iteratorBounds.hasNext()) {
        XYAnnotationBoundsInfo xyabi = iteratorBounds.next();
        if (xyabi.getIncludeInDataBounds()) {
            if (isDomainAxis) {
                result = Range.combine(result, xyabi.getXRange());
            } else {
                result = Range.combine(result, xyabi.getYRange());
            }
        }
    }

    return result;
}