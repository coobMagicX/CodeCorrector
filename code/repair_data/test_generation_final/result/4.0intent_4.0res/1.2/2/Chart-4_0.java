public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>();
    List<XYAnnotation> includedAnnotations = new ArrayList<>();
    boolean isDomainAxis = true;

    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(domainIndex)));
        if (domainIndex == 0) {
            Iterator<XYAnnotation> iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }
    }

    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(rangeIndex)));
        if (rangeIndex == 0) {
            Iterator<XYAnnotation> iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add(annotation);
                }
            }
        }

    }

    for (XYDataset d : mappedDatasets) {
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                if (isDomainAxis) {
                    result = Range.combine(result, r.findDomainBounds(d));
                } else {
                    result = Range.combine(result, r.findRangeBounds(d));
                }

                for (Object a : r.getAnnotations()) {
                    if (a instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add((XYAnnotation) a);
                    }
                }
            }
        }
    }

    for (XYAnnotation a : includedAnnotations) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) a;
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