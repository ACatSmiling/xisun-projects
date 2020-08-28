package cn.xisun.kafka.producer.utility;

import nu.xom.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author XiSun
 * @Date 2020/8/24 9:48
 */
public class XomUtils {
    /**
     * Gets the next sibling of a given node.
     *
     * @param node The reference node.
     * @return The next Sibling, or null.
     */
    public static Node getNextSibling(Node node) {
        ParentNode parent = node.getParent();
        int i = parent.indexOf(node);
        if (i + 1 >= parent.getChildCount()) {
            return null;
        }
        return parent.getChild(i + 1);
    }

    /**
     * Gets the first next sibling of a given node whose tagname matches the given string.
     *
     * @param current The reference node.
     * @param tagName The tagname of a node to look for
     * @return The matched next Sibling, or null.
     */
    public static Node getNextSibling(Node current, String tagName) {
        Element next = (Element) getNextSibling(current);
        while (next != null) {
            if (next.getLocalName().equals(tagName)) {
                return next;
            }
            next = (Element) getNextSibling(next);
        }
        return null;
    }


    /**
     * Gets the previous sibling of a given node.
     *
     * @param node The reference node.
     * @return The previous Sibling, or null.
     */
    public static Node getPreviousSibling(Node node) {
        ParentNode parent = node.getParent();
        int i = parent.indexOf(node);
        if (i == 0) {
            return null;
        }
        return parent.getChild(i - 1);
    }


    /**
     * Gets the first previous sibling of a given node whose tagname matches the given string.
     *
     * @param current The reference node.
     * @param tagName The tagname of a node to look for
     * @return The matched previous Sibling, or null.
     */
    public static Node getPreviousSibling(Node current, String tagName) {
        Element prev = (Element) getPreviousSibling(current);
        while (prev != null) {
            if (prev.getLocalName().equals(tagName)) {
                return prev;
            }
            prev = (Element) getPreviousSibling(prev);
        }
        return null;
    }

    /**
     * Inserts a node so that it occurs before a reference node. The new node
     * must not currently have a parent.
     *
     * @param node    The reference node.
     * @param newNode The new node to insert.
     */
    public static void insertBefore(Node node, Node newNode) {
        ParentNode parent = node.getParent();
        int i = parent.indexOf(node);
        parent.insertChild(newNode, i);
    }

    /**
     * Inserts a node so that it occurs after a reference node. The new node
     * must not currently have a parent.
     *
     * @param node    The reference node.
     * @param newNode The new node to insert.
     */
    public static void insertAfter(Node node, Node newNode) {
        ParentNode parent = node.getParent();
        int i = parent.indexOf(node);
        parent.insertChild(newNode, i + 1);
    }

    /**
     * Gets the next node. This element need not be a sibling
     *
     * @param node: starting node
     * @return
     */
    public static Node getNext(Node node) {
        Element parent = (Element) node.getParent();
        if (parent == null) {
            return null;
        }
        int index = parent.indexOf(node);
        if (index + 1 >= parent.getChildCount()) {
            return getNext(parent);//reached end of element
        }
        Element next = (Element) parent.getChild(index + 1);
        Elements children = next.getChildElements();
        while (children.size() != 0) {
            next = children.get(0);
            children = next.getChildElements();
        }
        return next;
    }

    /**
     * Gets the previous node. This element need not be a sibling
     *
     * @param node: starting node
     * @return
     */
    public static Node getPrevious(Node node) {
        Element parent = (Element) node.getParent();
        if (parent == null) {
            return null;
        }
        int index = parent.indexOf(node);
        if (index == 0) {
            return getPrevious(parent);//reached beginning of element
        }
        Element previous = (Element) parent.getChild(index - 1);
        Elements children = previous.getChildElements();
        while (children.size() != 0) {
            previous = children.get(children.size() - 1);
            children = previous.getChildElements();
        }
        return previous;
    }

    /**
     * Sets the first text child of the group to the newName
     * Throws an exception if the first child is not a Text node
     *
     * @param group
     * @param newName
     */
    public static void setTextChild(Element group, String newName) {
        Node textNode = group.getChild(0);
        if (textNode instanceof Text) {
            ((Text) textNode).setValue(newName);
        } else {
            throw new IllegalArgumentException("No Text Child Found!");
        }
    }

    /**
     * Returns an arrayList containing sibling elements of the given type after the given element.
     * These elements need not be continuous
     *
     * @param currentElem: the element to look for following siblings of
     * @param type:        the "localname" of the element type desired
     * @return
     */
    public static List<Element> getNextSiblingsOfType(Element currentElem, String type) {
        List<Element> laterSiblingElementsOfType = new ArrayList<Element>();
        Element parent = (Element) currentElem.getParent();
        if (parent == null) {
            return laterSiblingElementsOfType;
        }
        Elements potentialMatches = parent.getChildElements(type);
        int indexOfCurrentElem = parent.indexOf(currentElem);
        for (int i = 0; i < potentialMatches.size(); i++) {
            if (parent.indexOf(potentialMatches.get(i)) > indexOfCurrentElem) {
                laterSiblingElementsOfType.add(potentialMatches.get(i));
            }
        }
        return laterSiblingElementsOfType;
    }

    /**
     * Returns an arrayList containing sibling elements of the given type after the given element.
     *
     * @param currentElem: the element to look for following siblings of
     * @param type:        the "localname" of the element type desired
     * @return
     */
    public static List<Element> getNextAdjacentSiblingsOfType(Element currentElem, String type) {
        List<Element> siblingElementsOfType = new ArrayList<Element>();
        Element parent = (Element) currentElem.getParent();
        if (parent == null) {
            return siblingElementsOfType;
        }
        Element nextSibling = (Element) getNextSibling(currentElem);
        while (nextSibling != null && nextSibling.getLocalName().equals(type)) {
            siblingElementsOfType.add(nextSibling);
            nextSibling = (Element) getNextSibling(nextSibling);
        }

        return siblingElementsOfType;
    }

    /**
     * Returns an arrayList containing sibling elements of the given types after the given element.
     * These elements need not be continuous and are returned in the order encountered
     *
     * @param currentElem: the element to look for following siblings of
     * @param types:       An array of the "localname"s of the element types desired
     * @return
     */
    public static List<Element> getNextSiblingsOfTypes(Element currentElem, String[] types) {
        List<Element> laterSiblingElementsOfTypes = new ArrayList<Element>();
        currentElem = (Element) getNextSibling(currentElem);
        while (currentElem != null) {
            String name = currentElem.getLocalName();
            for (String type : types) {
                if (name.equals(type)) {
                    laterSiblingElementsOfTypes.add(currentElem);
                    break;
                }
            }
            currentElem = (Element) getNextSibling(currentElem);
        }
        return laterSiblingElementsOfTypes;
    }

    /**
     * Returns an arrayList containing sibling elements of the given type before the given element.
     * These elements need not be continuous
     *
     * @param currentElem: the element to look for previous siblings of
     * @param type:        the "localname" of the element type desired
     * @return
     */
    public static List<Element> getPreviousSiblingsOfType(Element currentElem, String type) {
        List<Element> earlierSiblingElementsOfType = new ArrayList<Element>();
        Element parent = (Element) currentElem.getParent();
        if (parent == null) {
            return earlierSiblingElementsOfType;
        }
        Elements potentialMatches = parent.getChildElements(type);
        int indexOfCurrentElem = parent.indexOf(currentElem);
        for (int i = 0; i < potentialMatches.size(); i++) {
            if (parent.indexOf(potentialMatches.get(i)) < indexOfCurrentElem) {
                earlierSiblingElementsOfType.add(potentialMatches.get(i));
            }
        }
        return earlierSiblingElementsOfType;
    }

    /**
     * Gets the next sibling element of the given element. If this element's name is within the elementsToIgnore array this is repeated
     * If no appropriate element can be found null is returned
     *
     * @param startingEl
     * @param elementsToIgnore
     * @return
     */
    public static Element getNextSiblingIgnoringCertainElements(Element startingEl, String[] elementsToIgnore) {
        ParentNode parent = startingEl.getParent();
        if (parent == null) {
            return null;
        }
        int i = parent.indexOf(startingEl);
        if (i + 1 >= parent.getChildCount()) {
            return null;
        }
        Element next = (Element) parent.getChild(i + 1);
        String elName = next.getLocalName();
        for (String namesToIgnore : elementsToIgnore) {
            if (elName.equals(namesToIgnore)) {
                return getNextSiblingIgnoringCertainElements(next, elementsToIgnore);
            }
        }
        return next;
    }


    /**
     * Gets the previous sibling element of the given element. If this element's name is within the elementsToIgnore array this is repeated
     * If no appropriate element can be found null is returned
     *
     * @param startingEl
     * @param elementsToIgnore
     * @return
     */
    public static Element getPreviousSiblingIgnoringCertainElements(Element startingEl, String[] elementsToIgnore) {
        ParentNode parent = startingEl.getParent();
        if (parent == null) {
            return null;
        }
        int i = parent.indexOf(startingEl);
        if (i == 0) {
            return null;
        }
        Element previous = (Element) parent.getChild(i - 1);
        String elName = previous.getLocalName();
        for (String namesToIgnore : elementsToIgnore) {
            if (elName.equals(namesToIgnore)) {
                return getPreviousSiblingIgnoringCertainElements(previous, elementsToIgnore);
            }
        }
        return previous;
    }

    /**
     * Finds all descendant elements whose localname matches the given elementName
     * Equivalent to an xpath of type .//*[local-name() = 'elementName'] from the startingElement
     *
     * @param startingElement 起始节点对象
     * @param elementName     给定的name
     * @return 返回的是Document节点下所有elementName对应的节点对应的Element对象的List集合
     */
    public static List<Element> getDescendantElementsWithTagName(Element startingElement, String elementName) {
        List<Element> matchingElements = new ArrayList<Element>();
        Deque<Element> stack = new ArrayDeque<Element>();
        Elements children = startingElement.getChildElements();// 获得Document节点的子节点，并将子节点对应的Element对象的List集合
        for (int i = children.size() - 1; i >= 0; i--) {// 将每一个子节点对应的Element对象，添加到双端队列stack中
            stack.add(children.get(i));
        }
        /**
         * 1.双端队列stack，含有的起始元素是Document节点的子节点对象
         * 2.循环开始时，弹出stack队列的尾部元素，并判断该元素是不是elementName对应的节点
         * 3.是elementName对应的节点，则添加到matchingElements列表中
         * 4.不是elementName对应的节点，则再获取该节点的子节点对象，并添加到stack队列中
         * 5.继续循环，直到将Document节点下的全部子节点(含子节点的子节点)进行判断，把所有elementName对应的节点对象添加到matchingElements列表中
         */
        while (stack.size() > 0) {
            Element currentElement = stack.removeLast();// 弹出stack队列尾部元素
            if (currentElement.getLocalName().equals(elementName)) {
                // 若当前Element对象对应的是MOLECULE节点，则直接添加到matchingElements列表中
                matchingElements.add(currentElement);
            }
            children = currentElement.getChildElements();// 否则，获取该节点的子节点
            for (int i = children.size() - 1; i >= 0; i--) {// 将每个子节点对应的Element对象添加到双端队列stack中
                Element child = children.get(i);
                stack.add(child);
            }
        }
        return matchingElements;
    }

    /**
     * Finds all descendant elements whose localname matches one of the strings in elementNames
     * Equivalent to an xpath of type .//*[local-name() = 'elementName1']|.//*[local-name() = 'elementName2']|.//*[local-name() = 'elementName3'] from the startingElement
     *
     * @param startingElement
     * @param elementNames
     * @return
     */
    public static List<Element> getDescendantElementsWithTagNames(Element startingElement, String[] elementNames) {
        List<Element> matchingElements = new ArrayList<Element>();
        Deque<Element> stack = new ArrayDeque<Element>();
        Elements children = startingElement.getChildElements();
        for (int i = children.size() - 1; i >= 0; i--) {
            stack.add(children.get(i));
        }
        while (stack.size() > 0) {
            Element currentElement = stack.removeLast();
            String currentElName = currentElement.getLocalName();
            for (String targetTagName : elementNames) {
                if (currentElName.equals(targetTagName)) {
                    matchingElements.add(currentElement);
                    break;
                }
            }
            children = currentElement.getChildElements();
            for (int i = children.size() - 1; i >= 0; i--) {
                Element child = children.get(i);
                stack.add(child);
            }
        }
        return matchingElements;
    }

    /**
     * Finds all child elements whose localname matches one of the strings in elementNames
     * Equivalent to an xpath of type ./*[local-name() = 'elementName1']|./*[local-name() = 'elementName2']|./*[local-name() = 'elementName3'] from the startingElement
     *
     * @param startingElement
     * @param elementNames
     * @return
     */
    public static List<Element> getChildElementsWithTagNames(Element startingElement, String[] elementNames) {
        if (startingElement != null) {
            List<Element> matchingElements = new ArrayList<Element>();
            Elements children = startingElement.getChildElements();
            int childCount = children.size();
            for (int i = 0; i < childCount; i++) {
                Element child = children.get(i);
                String currentElName = child.getLocalName();
                for (String targetTagName : elementNames) {
                    if (currentElName.equals(targetTagName)) {
                        matchingElements.add(child);
                        break;
                    }
                }
            }
            return matchingElements;
        }
        return null;
    }

    /**
     * 获得所有节点镀锡
     *
     * @param startingElement
     * @return
     */
    public static List<Element> getChildElements(Element startingElement) {
        List<Element> matchingElements = new ArrayList<Element>();
        Elements children = startingElement.getChildElements();
        int childCount = children.size();
        for (int i = 0; i < childCount; i++) {
            Element child = children.get(i);
            String currentElName = child.getLocalName();
            matchingElements.add(child);
        }
        return matchingElements;
    }

    /**
     * Finds all child elements whose localname matches one of the strings in elementNames
     * Equivalent to an xpath of type ./*[local-name() = 'elementName'] from the startingElement
     * This is equivalent to XOM's getChildElements(String) other than returning an arrayList
     *
     * @param startingElement
     * @param elementName
     * @return
     */
    public static List<Element> getChildElementsWithTagName(Element startingElement, String elementName) {
        List<Element> matchingElements = new ArrayList<Element>();
        Elements children = startingElement.getChildElements();
        int childCount = children.size();
        for (int i = 0; i < childCount; i++) {
            Element child = children.get(i);
            String currentElName = child.getLocalName();
            if (currentElName.equals(elementName)) {
                matchingElements.add(child);
            }
        }
        return matchingElements;
    }

    /**
     * Finds all descendant elements whose localname matches the given elementName
     * Additionally the element must have the specified attribute and the value of the attribute must be as specified
     * Equivalent to an xpath of type .//*[local-name() = 'elementName'][@attribute="attributevalue"] from the startingElement
     *
     * @param startingElement
     * @param elementName
     * @return
     */
    public static List<Element> getDescendantElementsWithTagNameAndAttribute(Element startingElement, String elementName, String attributeName, String attributeValue) {
        List<Element> matchingElements = new ArrayList<Element>();
        Deque<Element> stack = new ArrayDeque<Element>();
        Elements children = startingElement.getChildElements();
        for (int i = children.size() - 1; i >= 0; i--) {
            stack.add(children.get(i));
        }
        while (stack.size() > 0) {
            Element currentElement = stack.removeLast();
            if (currentElement.getLocalName().equals(elementName)) {
                if (attributeValue.equals(currentElement.getAttributeValue(attributeName))) {
                    matchingElements.add(currentElement);
                }
            }
            children = currentElement.getChildElements();
            for (int i = children.size() - 1; i >= 0; i--) {
                Element child = children.get(i);
                stack.add(child);
            }
        }
        return matchingElements;
    }

    /**
     * Finds all child elements whose localname matches the given elementName
     * Additionally the element must have the specified attribute and the value of the attribute must be as specified
     * Equivalent to an xpath of type ./*[local-name() = 'elementName'][@attribute="attributevalue"] from the startingElement
     *
     * @param startingElement
     * @param elementName
     * @return
     */
    public static List<Element> getChildElementsWithTagNameAndAttribute(Element startingElement, String elementName, String attributeName, String attributeValue) {
        List<Element> matchingElements = new ArrayList<Element>();
        Elements children = startingElement.getChildElements();
        for (int i = 0; i < children.size(); i++) {
            Element child = children.get(i);
            if (child.getLocalName().equals(elementName)) {
                if (attributeValue.equals(child.getAttributeValue(attributeName))) {
                    matchingElements.add(child);
                }
            }
        }
        return matchingElements;
    }

    /**
     * Find all the later siblings of startingElement with the search terminating at the element with string tagName
     * or if there are not more siblings
     *
     * @param startingEl
     * @param tagName
     * @return
     */
    public static List<Element> getSiblingsUpToElementWithTagName(Element startingEl, String tagName) {
        List<Element> laterSiblings = new ArrayList<Element>();
        Element nextEl = (Element) getNextSibling(startingEl);
        while (nextEl != null && !nextEl.getLocalName().equals(tagName)) {
            laterSiblings.add(nextEl);
            nextEl = (Element) getNextSibling(nextEl);
        }
        return laterSiblings;
    }
}
