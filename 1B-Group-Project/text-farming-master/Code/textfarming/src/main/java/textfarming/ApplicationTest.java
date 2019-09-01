package textfarming;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import textfarming.persistence.CustomUpdateMessage;
import textfarming.persistence.CustomUpdateMessageController;
import textfarming.persistence.CustomUpdateMessageService;
import textfarming.sms.SMSController;
import textfarming.voice.CallController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CallController incomingCallsController;

    @Autowired
    private SMSController incomingMessagesController;

    @Autowired
    private CustomUpdateMessageController testController;

    @Autowired
    private CustomUpdateMessageService customUpdateMessageService;

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(incomingCallsController);
        assertNotNull(incomingMessagesController);
        assertNotNull(testController);
    }

    @Test
    public void dumpDB() {
        Gson gson = new Gson();
        System.out.println(gson.toJson(customUpdateMessageService.getAllMessages()));
    }

    @Test
    public void canAddMessage() throws Exception {
        for (CustomUpdateMessage m : customUpdateMessageService.getAllMessages()) {
            if (m.getMessage().equals("TESTING-MESSAGE")) {
                customUpdateMessageService.delete(m.getCustomUpdateMessageId());
            }
        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
        Random random = new Random();

        CustomUpdateMessage msg = new CustomUpdateMessage(random.nextDouble() * 180 - 90,
                random.nextDouble() * 360 - 180, random.nextDouble() * 100000, new Date(),
                "TESTING-MESSAGE");

        // this.restTemplate.postForObject("http://localhost:" + port + "/addCustomMessage", gson.toJson(msg),
        //        String.class);

        System.out.println(gson.toJson(msg));

        this.mockMvc.perform(post("http://localhost:" + port + "/addCustomMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(msg)))
        .andExpect(status().isOk());


        CustomUpdateMessage createdMsg = null;
        for (CustomUpdateMessage m : customUpdateMessageService.getAllMessages()) {
            if (m.getMessage().equals(msg.getMessage())) {
                createdMsg = m;
                break;
            }
        }

        System.out.println(customUpdateMessageService.getAllMessages());

        assertNotNull(createdMsg);
        Assert.assertEquals(msg.getLatitude(), createdMsg.getLatitude(), 0.0001);
        Assert.assertEquals(msg.getLongitude(), createdMsg.getLongitude(), 0.0001);
        Assert.assertEquals(msg.getLocRadius(), createdMsg.getLocRadius(), 1);
        Assert.assertEquals(msg.getExpiryDate().getTime(), createdMsg.getExpiryDate().getTime(), 1000);

        customUpdateMessageService.delete(createdMsg.getCustomUpdateMessageId());
    }

}
