package com.example.converter.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Controller
public class mainController {

    @GetMapping("/")
    public String home(Model model) throws ParserConfigurationException, IOException, SAXException {
        double value = 0;

        String url = "https://www.cbr.ru/scripts/XML_daily.asp";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("Valute");

        Map<String, Double> moneymap = new TreeMap<>();

        Map<String, Double> dollarandeuro = new HashMap<String, Double>();

        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);

            Element eElement = (Element) node;

            String strdollar = eElement.getElementsByTagName("Value").item(0).getTextContent().replace(',','.');
            value = Double.parseDouble(strdollar);
            strdollar = String.format("%.2f",value).replace(',','.');

            value = Double.parseDouble(strdollar);

            String name = eElement.getElementsByTagName("Name").item(0).getTextContent();

            if(eElement.getAttribute("ID").equals("R01235")||eElement.getAttribute("ID").equals("R01239")){
                dollarandeuro.put(name,value);
            }
            else {
                moneymap.put(name,value);
            }

        }
        model.addAttribute("dollarandeuro", dollarandeuro);
        model.addAttribute("moneymap", moneymap);

        return "main";
    }


}
