package XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Logik.Controller;
import Logik.Pause;
import Logik.Tag;

public class XMLparser {
	
	public ArrayList<Tag> getTagListFromActricity(String filepath) {
		HashMap<String, ActionReturn> arMap = new HashMap<String, ActionReturn>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(filepath));

			for (Element arNode : getAllChildren(document.getDocumentElement())) {
				// --- Datum
				Calendar date = Calendar.getInstance();

				String dateString = getTextFromSubElementByName(arNode, "Datum").replaceAll("[.]", "_");

				if (dateString == null) {
					throw new NullPointerException(
							"Mehrere oder kein Knoten mit Namen Datum vorhanden.");
				}

				String[] dmy = dateString.split("_");
				if (dmy.length != 3) {
					throw new IndexOutOfBoundsException("Datum fehlerhaft");
				}

				date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dmy[0]));
				date.set(Calendar.MONTH, Integer.parseInt(dmy[1])-1);
				date.set(Calendar.YEAR, Integer.parseInt(dmy[2]));
				
				if(arMap.containsKey(dateString)) {
					arMap.get(dateString).addTime(getStartEnd(arNode));
				} else {
					ActionReturn ar = new ActionReturn(date);
					ar.addTime(getStartEnd(arNode));
					arMap.put(dateString, ar);
				}
			}

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.err.println(ex.getStackTrace());
		}
		
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		
		for(String key : arMap.keySet()) {
			ActionReturn ar = arMap.get(key);
			ArrayList<StartEndTime> seList = ar.getCalList();
			
			if(seList == null || seList.size() <= 0) {
				return null;
			}
			
			Tag t = new Tag();
			
			t.setTagAnfang(seList.get(0).getAnfang());
			t.setTagEnde(seList.get(seList.size()-1).getEnde());
			
			if(seList.size() > 1) {
				for(int x = 0; x < seList.size()-1; x++) {
					t.setPausenAnfang(seList.get(x).getEnde());
					t.setPausenEnde(seList.get(x+1).getAnfang());
				}
			}
			
			tagList.add(t);
		}
		
		return tagList;
	}

	private StartEndTime getStartEnd(Element ar) throws IndexOutOfBoundsException{
		StartEndTime set = new StartEndTime();
		
		set.setAnfang(getTimeFromElement(getChildrenByName(ar, "Anfangszeit").get(0))); //FEHLERBEHANDLUNG
		set.setEnde(getTimeFromElement(getChildrenByName(ar, "Endzeit").get(0)));
		
		return set;
	}
	
	private Calendar getTimeFromElement(Element time) 
			throws IndexOutOfBoundsException {
		String zeit = time.getTextContent();
		String[] zeitArr = zeit.split(":");
		if(zeitArr.length != 2) {
			throw new IndexOutOfBoundsException("Zeit falsch angegeben");
		}
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(zeitArr[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(zeitArr[1]));
		cal.set(Calendar.SECOND, 0);
		
		return cal;
	}
	
	private String getTextFromSubElementByName(Element el, String name) {
		ArrayList<Element> childList = getChildrenByName(el, name);
		if (childList.size() > 1) {
			return null;
		}
		for (Element e : childList) {
			return e.getTextContent();
		}
		return null;
	}

	private ArrayList<Element> getAllChildren(Element el) {
		return getChildrenByName(el, "");
	}

	private ArrayList<Element> getChildrenByName(Element el, String nodeName) {
		ArrayList<Element> list = new ArrayList<Element>();

		for (int x = 0; x < el.getChildNodes().getLength(); x++) {
			Node n = el.getChildNodes().item(x);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				if (e.getNodeName().equals(nodeName) || nodeName.equals("")) {
					list.add(e);
				}
			}
		}

		return list;
	}
}
