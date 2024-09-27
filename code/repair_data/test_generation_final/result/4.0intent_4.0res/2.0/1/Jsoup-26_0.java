import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.List;

public class HtmlCleaner {
    private Whitelist whitelist;

    public HtmlCleaner(Whitelist whitelist) {
        this.whitelist = whitelist;
    }

    private int copySafeNodes(Element source, Element dest) {
        List<Node> sourceChildren = source.childNodes();
        int numDiscarded = 0;

        for (Node sourceChild : sourceChildren) {
            if (sourceChild instanceof Element) {
                Element sourceEl = (Element) sourceChild;

                if (whitelist.isSafeTag(sourceEl.tagName())) {
                    ElementMeta meta = createSafeElement(sourceEl);
                    Element destChild = meta.el;
                    dest.appendChild(destChild);

                    numDiscarded += meta.numAttribsDiscarded;
                    numDiscarded += copySafeNodes(sourceEl, destChild); // recurse into safe elements
                } else {
                    numDiscarded++; // discard the unsafe element
                    numDiscarded += copySafeNodes(sourceEl, dest); // recurse into children of unsafe elements
                }
            } else if (sourceChild instanceof TextNode) {
                TextNode sourceText = (TextNode) sourceChild;
                TextNode destText = new TextNode(sourceText.getWholeText(), sourceChild.baseUri());
                dest.appendChild(destText);
            }
            // ignore comments and other non-element, non-text nodes
        }
        return numDiscarded;
    }

    public Document clean(Document dirtyDocument) {
        Validate.notNull(dirtyDocument);

        Document clean = Document.createShell(dirtyDocument.baseUri());
        copySafeNodes(dirtyDocument.body(), clean.body());

        return clean;
    }

    private ElementMeta createSafeElement(Element sourceEl) {
        Element destEl = new Element(sourceEl.tagName(), sourceEl.baseUri());
        int numAttribsDiscarded = 0;

        sourceEl.attributes().forEach(attr -> {
            if (whitelist.isSafeAttribute(sourceEl.tagName(), attr.getKey())) {
                destEl.attributes().put(attr);
            } else {
                numAttribsDiscarded++;
            }
        });

        return new ElementMeta(destEl, numAttribsDiscarded);
    }

    private static class ElementMeta {
        Element el;
        int numAttribsDiscarded;

        ElementMeta(Element el, int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }

    private static class Whitelist {
        public boolean isSafeTag(String tagName) {
            // Implementation depends on specified safe tags
            return tagName.equals("div") || tagName.equals("span");
        }

        public boolean isSafeAttribute(String tagName, String attrName) {
            // Implementation depends on specified safe attributes
            return attrName.equals("id") || attrName.equals("class");
        }
    }
}