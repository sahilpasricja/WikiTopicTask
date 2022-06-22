package Task2;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.json.JSONObject;



import java.io.InputStream;

import static org.junit.Assert.assertThrows;

public class SolutionTest extends TestCase {

    static  String DEFAULT_TOPIC = "pizza";


    JSONObject jsonObject = new JSONObject()
    .put("title", "Pizza delicious")
    .put("pageid", 12)
    .put("text", "The ancient pizza art comes from land of pizza, Italy");

    Solution solution = new Solution();
    TopicSource topicSource = new TopicSource(DEFAULT_TOPIC, SearchURL.WIKI);




    @Test
    @DisplayName("Test word counter for given Json")
    public void testCountKeyword() throws Exception {
        assertEquals(2, solution.countKeyword(topicSource,jsonObject));
    }
    @Test
    @DisplayName("Test connection to Wiki")
    public void testConnection() throws Exception {
        boolean connectionEstablished = false;
        try(final InputStream is = solution.getInputStream(topicSource)){
            connectionEstablished = true;
        }
        finally {
            assertEquals(connectionEstablished,true);
        }
    }

    @Test
    @DisplayName("Test if the right exception message is thrown when connection fails.")
    public void testException() throws Exception {
        topicSource = new TopicSource(DEFAULT_TOPIC, SearchURL.WIKI_ERRONEOUS_URL);
        Throwable exception = assertThrows(Exception.class , () ->  solution.getInputStream(topicSource));
        assertEquals(java.io.IOException.class, exception.getClass());
    }
}