package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;
import java.util.function.Function;
//
//public class Comparator<Map<String, Object>> mapComparator = new Comparator<Map<String, Object>>() {
//    public int compare(Map<String, String> m1, Map<String, String> m2) {
//        return m1.get("name").compareTo(m2.get("name"));
//    }
//}

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
	    // Read the request
        URL url = new URL("http://localhost:8080/api/news");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        con.disconnect();

        // Convert Response to JSONArray
        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(String.valueOf(response));
//        System.out.print(json);

        // Iterating through the json response
        List<Map<String, String>> reportList = new ArrayList<>();
        Iterator i = json.iterator();

        while (i.hasNext()) {
            JSONObject article = (JSONObject) i.next();
            String id = (String)article.get("id");
            String category = (String)article.get("category");
            String title = (String)article.get("title");
            JSONArray tags = (JSONArray) article.get("tags");
            Long weight = (Long) article.get("weight");

            // Ignore if Category is Social
            if(category!="SOCIAL"){
                Map<String, String> mapObject = new HashMap<>();
                mapObject.put("id", id.substring(id.length()-10));
                mapObject.put("category", category);
                mapObject.put("title", title);
                mapObject.put("tags", String.valueOf(tags.size()));
                mapObject.put("weight", String.valueOf(weight));
                reportList.add(mapObject);
            }
        }

        // Sort the Report list
        Comparator<Map<String, String>> sortByWeight = Comparator.comparing(x -> -1 * Integer.valueOf(x.get("weight")));
        reportList.sort(sortByWeight);

//        for(Map<String, String> mapObject : reportList)
//        {
//            System.out.println(mapObject.get("id"));
//            System.out.println(mapObject.get("category"));
//            System.out.println(mapObject.get("title"));
//            System.out.println(mapObject.get("tags"));
//            System.out.println(mapObject.get("weight"));
//            System.out.println();
//        }

        // Write List to File
        FileWriter myWriter = new FileWriter("edited_articles.txt");
        for(Map<String, String> mapObject : reportList)
        {
            myWriter.write(mapObject.get("id"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("category"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("title"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("tags"));
            myWriter.write("\t");

            myWriter.write(mapObject.get("weight"));
            myWriter.write("\n");

        }

        myWriter.close();
    }
}
