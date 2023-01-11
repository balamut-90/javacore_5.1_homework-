package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.models.Employee;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main( String[] args ) throws ParserConfigurationException, IOException, SAXException {

        String filename = "data.xml";
        String jsonFilename = "data.json";

        List<Employee> employeeList = parseXML(filename);
        String json = listToJson(employeeList);
        writeString(json, jsonFilename);

    }

    private static void writeString(String json, String jsonFilename) {
        try (FileWriter fileWriter = new FileWriter(jsonFilename)) {
            fileWriter.write(json);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> employeeList) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(employeeList, listType);
    }

    private static List<Employee> parseXML(String filename) throws ParserConfigurationException, IOException, SAXException {

        List<Employee> employeeList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(filename));

        Node root = document.getDocumentElement();

        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Employee employee = new Employee();
                Element element = (Element) node;

                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                String age = element.getElementsByTagName("age").item(0).getTextContent();

                employee.setId(Long.parseLong(id));
                employee.setFirstName(firstName);
                employee.setLastName(lastName);
                employee.setCountry(country);
                employee.setAge(Integer.parseInt(age));

                employeeList.add(employee);
            }
        }

        return employeeList;
    }
}
