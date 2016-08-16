package com.spring.mvc.mini.xml;

import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.svn.SVNHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Component
public class ObjectClassXMLPaser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectClassXMLPaser.class);

    @Value("${path.xml}")
    private String xmlPath;

    @Autowired
    private SVNHandler svnHandler;

    public ArrayList<ObjectClass> objectClassMapping() throws Exception {

        LOGGER.info("Start to checkout");
        svnHandler.svnCheckout();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File objectClassesFile = new File(xmlPath);

        Document document = builder.parse(objectClassesFile);

        ArrayList<ObjectClass> objectClasses = new ArrayList<>();

        NodeList nodeList = document.getDocumentElement().getChildNodes();

        ObjectClass objectClass = null;
        int commentcount = 0;
        int elementcount = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node instanceof Comment) {
                objectClass = setCommentToObject((Comment) node);
                commentcount++;
            }

            if (node instanceof Element) {

                setElementToObject(objectClass, (Element) node);
                elementcount++;
                objectClasses.add(objectClass);
            }
        }

        LOGGER.debug("commentcount is:" + commentcount);
        LOGGER.debug("elementcount is:" + elementcount);

        return objectClasses;
    }

    private ObjectClass setCommentToObject(Comment node) {
        ObjectClass objectClass;
        objectClass = new ObjectClass();
        objectClass.setComment(node.getData());
        return objectClass;
    }

    private void setElementToObject(ObjectClass objectClass, Element node) {
        objectClass.setId(node.getAttribute("id"));
        objectClass.setAbbreviation(node.getAttribute("abbrev"));
        objectClass.setIntclass(Integer.parseInt(node.getAttribute("intclass")));
        objectClass.setName(node.getAttribute("name"));
        objectClass.setParents(node.getAttribute("parents"));
        objectClass.setPackageName(node.getAttribute("adaID"));
    }

    public void AddObjectClass(ObjectClass objectClass) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            File objectClassesFile = new File(xmlPath);

            Document document = builder.parse(objectClassesFile);

            Comment c = document.createComment(objectClass.getComment());

            document.getDocumentElement().appendChild(c);
            document.getDocumentElement().appendChild(getElementOfObjectClass(objectClass, document));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);

            StreamResult result = new StreamResult(new File(xmlPath));

            transformer.transform(source, result);

        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

    private Element getElementOfObjectClass(ObjectClass objectClass, Document document) {
        Element e = document.createElement("objclass");
        e.setAttribute("id", objectClass.getId());
        e.setAttribute("intclass", String.valueOf(objectClass.getIntclass()));
        e.setAttribute("abbrev", objectClass.getAbbreviation());
        e.setAttribute("adaID", objectClass.getPackageName());
        e.setAttribute("name", objectClass.getName());
        e.setAttribute("parents", objectClass.getParents());
        return e;
    }

    public byte[] getTextConent() throws Exception {

        LOGGER.info("Start to checkout");
        svnHandler.svnCheckout();

        Path path = Paths.get(xmlPath);

        return Files.readAllBytes(path);

    }

}
