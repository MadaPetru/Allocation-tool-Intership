package ro.fortech.allocation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testApplication() throws URISyntaxException, ParseException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/employees/";
        URI createUri = new URI(baseUrl);
        ResponseEntity<String> result = this.restTemplate.getForEntity(createUri, String.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
    }
}