import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public Range getDataRange(ValueAxis axis) {
    Range result = null;
    List<XYDataset> mappedDatasets = new ArrayList<>();
    List<XYAnnotationBoundsInfo> includedAnnotations = new ArrayList<>();
    boolean isDomainAxis = true;

    // Check if it is a domain axis
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(domainIndex));
        if (domainIndex == 0) {
            // Collect annotations if it's the primary domain axis
            Iterator<XYAnnotation> iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add((XYAnnotationBoundsInfo) annotation);
                }
            }
        }
    }

    // Check if it is a range axis
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false;
        mappedDatasets.addAll(getDatasetsMappedToRangeAxis(rangeIndex));
        if (rangeIndex == 0) {
            // Collect annotations if it's the primary range axis
            Iterator<XYAnnotation> iterator = this.annotations.iterator();
            while (iterator.hasNext()) {
                XYAnnotation annotation = iterator.next();
                if (annotation instanceof XYAnnotationBoundsInfo) {
                    includedAnnotations.add((XYAnnotationBoundsInfo) annotation);
                }
            }
        }
    }

    // Process datasets mapped to the axis
    Iterator<XYDataset> datasetIterator = mappedDatasets.iterator();
    while (datasetIterator.hasNext()) {
        XYDataset dataset = datasetIterator.next();
        if (dataset != null) {
            XYItemRenderer renderer = getRendererForDataset(dataset);
            if (isDomainAxis) {
                if (renderer != null) {
                    result = Range.combine(result, renderer.findDomainBounds(dataset));
                } else {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(dataset));
                }
            } else {
                if (renderer != null) {
                    result = Range.combine(result, renderer.findRangeBounds(dataset));
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(dataset));
                }
            }

            // Include renderer-specific annotations
            if (renderer != null) {
                Collection<XYAnnotation> annotations = renderer.getAnnotations();
                for (XYAnnotation annotation : annotations) {
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add((XYAnnotationBoundsInfo) annotation);
                    }
                }
            }
        }
    }

    // Process included annotations to adjust the data range
    for (XYAnnotationBoundsInfo annotation : includedAnnotations) {
        if (annotation.getIncludeInDataBounds()) {
            if (isDomainAxis) {
                result = Range.combine(result, annotation.getXRange());
            } else {
                result = Range.combine(result, annotation.getYRange());
            }
        }
    }

    return result;
}

// Placeholder methods and classes
class XYDataset {}
class XYItemRenderer {
    public Range findDomainBounds(XYDataset dataset) { return null; }
    public Range findRangeBounds(XYDataset dataset) { return null; }
    public Collection<XYAnnotation> getAnnotations() { return null; }
}
class ValueAxis {}
class Range {
    public static Range combine(Range r1, Range r2) { return null; }
}
class XYAnnotation {}
interface XYAnnotationBoundsInfo extends XYAnnotation {
    Range getXRange();
    Range getYRange();
    boolean getIncludeInDataBounds();
}
class DatasetUtilities {
    public static Range findDomainBounds(XYDataset dataset) { return null; }
    public static Range findRangeBounds(XYDataset dataset) { return null; }
}