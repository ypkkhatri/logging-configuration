package org.carlspring.logging.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author carlspring
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/logging-*-context.xml",
                                    "classpath*:/META-INF/spring/logging-*-context.xml" })
public class DeleteLoggingRestletTest
{

    private TestClient client;


    @Before
    public void setUp() throws Exception
    {
    	client = TestClient.getTestInstance();
        String url = client.getContextBaseUrl() +
                "/logger?" +
                "logger=org.carlspring.logging.test&" +
                "level=DEBUG&" +
                "appenderName=CONSOLE";

	   WebTarget resource = client.getClientInstance().target(url);
	
	   Response response = resource.request(MediaType.TEXT_PLAIN)
	                               .post(Entity.entity("Add", MediaType.TEXT_PLAIN));
	
	   int status = response.getStatus();
	
	   assertEquals("Failed to add logger!", Response.ok(), status);
    }

    @After
    public void tearDown()
            throws Exception
    {
        if (client != null)
        {
            client.close();
        }
    }
    
    @Ignore
    @Test
    public void testDeleteLogger() throws Exception
    {
        String path = "/logger?" +
                      "logger=org.carlspring.logging.test&" +
                      "level=INFO";

        Response response = client.delete(path);

        assertEquals("Failed to delete logger!", Response.ok(), response.getStatus());

        // Checking that the logback.xml contains the new logger.
        String url = client.getContextBaseUrl() + 
		           "/logger/logback";

        WebTarget resource = client.getClientInstance().target(url);

        response = resource.request(MediaType.TEXT_PLAIN)
		                    .get();

        int status = response.getStatus();
        assertEquals("Failed to get log file!", Response.ok(), status);

        assertFalse(response.toString().contains("org.carlspring.logging.test"));
    }

}