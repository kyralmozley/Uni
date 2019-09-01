package textfarming;

import static textfarming.InfoMessageGenerator.generateUserMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import textfarming.persistence.CustomUpdateMessageService;
import textfarming.persistence.User;
import textfarming.persistence.UserService;
import textfarming.sms.SMSSender;

@Component
public class ScheduledUpdateGenerator {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomUpdateMessageService customUpdateMessageService;

    //TODO: wire in some API that gets timezone from latlong, and run this every hour
    @Scheduled(cron = "0 0 8 * * *") // 08:00:00 UTC every day
    public void scheduledUpdate() {
        customUpdateMessageService.trimMessages();

        for (User user : userService.getAllUsers()) {
            SMSSender.sendSMS(user.tel, 
                    generateUserMessage(user.tel, userService, customUpdateMessageService), 
                    user.getLang());
        }
    }
}
