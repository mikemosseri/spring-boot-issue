package demo;

import java.util.Date;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {
  @RequestMapping("/")
  public String home(Map<String, Object> model) {
    model.put("message", "Hello World");
    model.put("title", "Hello Home");
    model.put("date", new Date());
    return "home";
  }
}
