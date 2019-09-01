package textfarming.persistence;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUpdateMessageService {

    @Autowired
    private CustomUpdateMessageRepository customUpdateMessageRepo;

    /**
     * Saves a CustomUpdateMessage to the database.
     * @param message
     * @return the CustomUpdateMessage object for further operations,
     * since the save operation may have changed the entity.
     */
    public CustomUpdateMessage saveMessage(CustomUpdateMessage message) {
        return customUpdateMessageRepo.save(message);
    }

    public void delete(long id) {
        customUpdateMessageRepo.deleteById(id);
    }

    public Iterable<CustomUpdateMessage> getAllMessages() {
        return customUpdateMessageRepo.findAll();
    }

    public void deleteAllMessages() {
        customUpdateMessageRepo.deleteAll();
    }

    public void trimMessages() {
        Date now = new Date();

        for (CustomUpdateMessage message : getAllMessages()) {
            if (now.after(message.getExpiryDate())) {
                customUpdateMessageRepo.delete(message);
            }
        }
    }

}
