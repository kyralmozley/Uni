package textfarming.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomUpdateMessageController {

    @Autowired
    private CustomUpdateMessageService customUpdateMessageService;

    @PostMapping("/addCustomMessage")
    public void addCutomMessage(@RequestBody CustomUpdateMessage newMsg) {
        if (newMsg != null) {
            customUpdateMessageService.saveMessage(newMsg);
        }
    }

}
