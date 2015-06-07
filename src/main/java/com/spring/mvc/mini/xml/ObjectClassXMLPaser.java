package com.spring.mvc.mini.xml;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.properties.Properties;
import com.spring.mvc.mini.svn.SVNHandler;

@Component
public class  ObjectClassXMLPaser {
	
	static Logger LOGGER = LoggerFactory.getLogger(ObjectClassXMLPaser.class);
	
    @Autowired
    private Properties properties;
    
    @Autowired
    private SVNHandler sh;
		
	public ArrayList<ObjectClass> objectClassMapping() throws Exception {
		
		LOGGER.info("Start to checkout");
		sh.svnCheckout();
		
		// Get the DOM Builder Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Get the DOM Builder
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Load and Parse the XML document
		// document contains the complete XML as a Tree.
		
		File objclsFile = new File(properties.getXmlPath());
	
		Document document = builder.parse(objclsFile);
		
		ArrayList<ObjectClass> objclsList = new ArrayList<>();

		// Iterating through the nodes and extracting the data.
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		
		ObjectClass objcls = null;
		int commentcount = 0;
		int elementcount = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Node node = nodeList.item(i);
			
			if(node instanceof Comment){
				objcls = new ObjectClass();
				Comment c = (Comment)node;
				objcls.setComment(c.getData());
				commentcount ++;
			}
			
			if (node instanceof Element) {
				
				objcls.setId(((Element) node).getAttribute("id").toString());
				objcls.setAbbrev(((Element) node).getAttribute("abbrev")
						.toString());
				objcls.setIntclass(Integer.parseInt(((Element) node).getAttribute("intclass")));
				objcls.setName(((Element) node).getAttribute("name").toString());
				objcls.setParents(((Element) node).getAttribute("parents")
						.toString());
				objcls.setAdaID(((Element) node).getAttribute("adaID")
						.toString());
				elementcount++;
				objclsList.add(objcls);
			}
		}
		
		LOGGER.debug("commentcount is:" + commentcount);
		LOGGER.debug("elementcount is:" + elementcount);
		
		return objclsList;
	}
	
	public void AddObjectClass(ObjectClass objcls){

		try {
			
			// Get the DOM Builder Factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// Get the DOM Builder
			DocumentBuilder builder = factory.newDocumentBuilder();;


		File objclsFile = new File(properties.getXmlPath());
		
		// Load and Parse the XML document
		// document contains the complete XML as a Tree.
		Document document = builder.parse(objclsFile);

		// Iterating through the nodes and extracting the data.
			
			Comment c = document.createComment(objcls.getComment());
			
			document.getDocumentElement().appendChild(c);
			
			Element objclselement = document.createElement("objclass");
			objclselement.setAttribute("id", objcls.getId());
			objclselement.setAttribute("intclass", String.valueOf(objcls.getIntclass()));
			objclselement.setAttribute("abbrev", objcls.getAbbrev());
			objclselement.setAttribute("adaID", objcls.getAdaID());
			objclselement.setAttribute("name", objcls.getName());
			objclselement.setAttribute("parents", objcls.getParents());
				
			document.getDocumentElement().appendChild(objclselement);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			
			StreamResult result = new StreamResult(new File(properties.getXmlPath()));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
			
		} catch (Exception e){
			LOGGER.error(e.toString());
		}
	}
	
	public byte[] getTextConent() throws Exception {
		
		LOGGER.info("Start to checkout");
		sh.svnCheckout();
			
		Path path = Paths.get(properties.getXmlPath());
		
		return Files.readAllBytes(path);
		
	}
	
}
