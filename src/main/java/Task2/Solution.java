package Task2;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;


public class Solution {
    static final String KEY1 = "parse";
    static final String KEY2 = "text";
    static  final String DEFAULT_SEARCH_TERM = "pizza";

    TopicSource topicSource;


    public int getOccurrenceCount(String topic) throws Exception {
        topicSource = new TopicSource(topic,SearchURL.WIKI);
        InputStream is = getInputStream(topicSource);
        JSONObject jsonResponse = inputStreamToJsonObjText(is);
        return countKeyword(topicSource,jsonResponse);

    }

    /**
     * Fetch topic data from wiki and store it as stream
     * @param topicSource with information about url and topic
     * @return InputStream from Wiki
     * @throws Exception if connection can not be established
     */

    public InputStream getInputStream(TopicSource topicSource) throws IOException {
        String readurl = String.format(topicSource.searchUrl(), topicSource.topic());
        try{
            InputStream is = new URL(readurl).openStream();
            return is;
        }
        catch (IOException e){
            throw new IOException("Unable to establish connection.", e);
        }
    }

    /***
     * Convert Input Stream to Json Object and fetch for key : Text
     * @param is input stream  of Wiki Data
     * @return filtered input stream for text key as jsonObject
     * @throws IOException
     */
    private JSONObject inputStreamToJsonObjText(InputStream is) throws IOException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonResponse = readAll(rd);
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject jsonObjectText = json.getJSONObject(KEY1).getJSONObject(KEY2);
            return jsonObjectText;
        }
        catch (IOException e) {
            throw new IOException("Unable to convert Input Stream to Json Object.", e);
        }
        finally {
            is.close();
        }
    }

    /**
     * Read BR and convert to string
     * @param rd Reader
     * @return String
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    /**
     * Calculate number of occurrences of topic in text returned from Wiki
      * @param topicSource
     * @param jsonObject to check for occurrence of topic
     * @return count of occurrences
     */
    public int countKeyword(TopicSource topicSource,JSONObject jsonObject){
        return StringUtils.countMatches(jsonObject.toString(), topicSource.topic());
    }

    /**
     * Main/Driver function
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        Solution solution = new Solution();
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the topic: ");
        String topic = reader.next();
        System.out.printf("Calculating occurrences for topic: %s, in Wiki data \n",topic);
        System.out.println(solution.getOccurrenceCount(topic) + " occurrences of search topic: " + topic);
    }
}