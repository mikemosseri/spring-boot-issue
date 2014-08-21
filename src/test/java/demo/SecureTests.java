package demo;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Basic integration tests for demo application.
 *
 * @author Dave Syer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@DirtiesContext
public class SecureTests {

  private static final String HTTP_LOCALHOST = "http://localhost:";
  @Value("${local.server.port}")
  private int port;
  
  @Test
  public void testHome() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
    ResponseEntity<String> entity = new TestRestTemplate().exchange(
        HTTP_LOCALHOST + this.port, HttpMethod.GET, new HttpEntity<Void>(
            headers), String.class);
    assertEquals(HttpStatus.FOUND, entity.getStatusCode());
    assertTrue("Wrong location:\n" + entity.getHeaders(), entity.getHeaders()
        .getLocation().toString().endsWith(port + "/login"));
  }
  
  @Test
  public void testLogin() throws Exception {
    HttpHeaders headers = getHeaders();
    headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
    form.set("username", "test");
    form.set("password", "test");
    ResponseEntity<String> entity = new TestRestTemplate().exchange(
        HTTP_LOCALHOST + this.port + "/login", HttpMethod.POST,
        new HttpEntity<MultiValueMap<String, String>>(form, headers),
        String.class);
    assertEquals(HttpStatus.FOUND, entity.getStatusCode());
    assertTrue("Wrong location:\n" + entity.getHeaders(), entity.getHeaders()
        .getLocation().toString().endsWith(port + "/"));
    assertNotNull("Missing cookie:\n" + entity.getHeaders(),
        entity.getHeaders().get("Set-Cookie"));
  }
  
  /**
   * Test the same login credentials so it hits the cache. This is testing
   * if it throws a lazy init exception.
   * @throws Exception
   */
  @Test
  public void testLoginHitCache() throws Exception {
    HttpHeaders headers = getHeaders();
    headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
    form.set("username", "test");
    form.set("password", "test");
    ResponseEntity<String> entity = new TestRestTemplate().exchange(
        HTTP_LOCALHOST + this.port + "/login", HttpMethod.POST,
        new HttpEntity<MultiValueMap<String, String>>(form, headers),
        String.class);
    assertEquals(HttpStatus.FOUND, entity.getStatusCode());
    assertTrue("Wrong location:\n" + entity.getHeaders(), entity.getHeaders()
        .getLocation().toString().endsWith(port + "/"));
    assertNotNull("Missing cookie:\n" + entity.getHeaders(),
        entity.getHeaders().get("Set-Cookie"));
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    ResponseEntity<String> page = new TestRestTemplate().getForEntity(
        HTTP_LOCALHOST + this.port + "/login", String.class);
    assertEquals(HttpStatus.OK, page.getStatusCode());
    String cookie = page.getHeaders().getFirst("Set-Cookie");
    headers.set("Cookie", cookie);
    Matcher matcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*")
        .matcher(page.getBody());
    assertTrue("No csrf token: " + page.getBody(), matcher.matches());
    headers.set("X-CSRF-TOKEN", matcher.group(1));
    return headers;
  }
}