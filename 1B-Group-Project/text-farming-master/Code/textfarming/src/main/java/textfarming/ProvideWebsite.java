package textfarming;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProvideWebsite {

    @RequestMapping("/exampleWebsite")
    public ResponseEntity<InputStreamResource> dealWithIncomingMessage(HttpServletRequest request, HttpServletResponse response) {
        //add the html file
        return null;
    }
}
