package com.nimble.json.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParse {
    private String jsonStr;
    private JSONObject jsonObject;
    private JSONArray nodes;
    private Iterator iter = null;
    public JsonParse(String jsonStr) throws ParseException {
        this.jsonStr=jsonStr;
        jsonObject=getJsonObject();
        if (jsonObject==null) {
            throw new IllegalArgumentException("Couldn't parse argument as a JSON string");
        }
        nodes=getNodes();
        resetNodeIndex();
        if (nodes == null)
            System.out.println("Current object doesn't have any nodes");
    }
    JSONObject getJsonObject() throws ParseException {
        Object obj = getObject();
        if (obj instanceof JSONObject)
            return (JSONObject) obj;
        else throw new ClassCastException("Object is not a JSONObject");
    }
    Object getObject() throws ParseException {
        JSONParser parser = new JSONParser();
        return parser.parse(jsonStr);
    }
    JSONArray getNodes() {
        return (JSONArray) jsonObject.get("nodes");
    }
    JSONObject nextNode() {
        if (!hasNextNode())
            return null;
        return (JSONObject) iter.next();
    }
    public boolean hasNextNode() {
        if (iter ==null) return false;
        return iter.hasNext();
    }
    public void resetNodeIndex() {
        if (nodes==null)
            iter =null;
        else
            iter =nodes.iterator();
    }

    public List<String> getOutcomes() {
        resetNodeIndex();
        List<String> outcomes=new ArrayList<>();

        JSONObject node;
        String typeVal;
        Object obj;
        while (hasNextNode()) {
            node=nextNode();
            obj=node.get("type");
            if (obj instanceof String)
                typeVal=(String) obj;
            else {
                System.err.println("Error: Node value for 'type' is of an unexpected type ("+obj?.getClass()+")");
                System.err.flush();
                continue;
            }
            if (typeVal.equalsIgnoreCase("outcome")) {
                obj=node.get("caption");
                if (obj instanceof String)
                    outcomes.add((String) obj);
                else {
                    System.err.println("Error: Node value for 'outcome' is of an unexpected type ("+obj?.getClass()+")");
                    System.err.flush();
                }
            }
        }

        return outcomes;
    }

    public static void main(String[] args) {
        try {
            String str = new String("{\"caption\":\"Aspirin\",\"connections\":[{\"from\":0,\"to\":1},{\"from\":1,\"to\":2},{\"from\":0,\"to\":3},{\"from\":3,\"to\":4},{\"from\":0,\"to\":5},{\"from\":5,\"to\":6},{\"from\":0,\"to\":7},{\"from\":7,\"to\":8},{\"from\":0,\"to\":9},{\"from\":9,\"to\":10}],\"name\":\"ASPIRIN_Router\",\"nodes\":[{\"caption\":\"What is the reason for you taking aspirin?\",\"componentType\":\"checkboxes\",\"editing\":false,\"helpText\":\"undefined\",\"name\":\"reason-aspirin\",\"originX\":\"100px\",\"originY\":\"100px\",\"type\":\"Question\"},{\"caption\":\"Low dose, preventative only\",\"editing\":false,\"originX\":\"21.9908px\",\"originY\":\"321px\",\"type\":\"Answer\"},{\"caption\":\"Standard\",\"editing\":false,\"originX\":\"19px\",\"originY\":\"389px\",\"type\":\"Outcome\"},{\"caption\":\"Part of my treatment for diabetes\",\"editing\":false,\"originX\":278,\"originY\":327,\"type\":\"Answer\"},{\"caption\":\"Standard\",\"editing\":false,\"originX\":284,\"originY\":392,\"type\":\"Outcome\"},{\"caption\":\"Stroke, mini-stroke, angina or heart attack\",\"editing\":false,\"originX\":526,\"originY\":300,\"type\":\"Answer\"},{\"caption\":\"Decline\",\"editing\":false,\"originX\":543,\"originY\":370,\"type\":\"Outcome\"},{\"caption\":\"Heart disease or coronary artery bypass operation\",\"editing\":false,\"originX\":838,\"originY\":238,\"type\":\"Answer\"},{\"caption\":\"Decline\",\"editing\":false,\"originX\":898,\"originY\":311,\"type\":\"Outcome\"},{\"caption\":\"Another medical condition\",\"editing\":false,\"originX\":755,\"originY\":144,\"type\":\"Answer\"},{\"caption\":\"Search box full condition list\",\"editing\":false,\"originX\":1060,\"originY\":162,\"type\":\"Outcome\"}],\"notes\":[{\"caption\":\"Untitled Note\",\"originX\":-139,\"originY\":41},{\"caption\":\"Untitled Note\",\"originX\":-223,\"originY\":43}]}");
            JsonParse myParser = new JsonParse(str);

            System.out.println("Outcomes:");
//            myParser.getOutcomes().forEach(System.out::println);
            myParser.getOutcomes().each {println it}
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void printValue(String name, Object value) { // for testing
        System.out.println(name + " --> "+value);
    }
}
