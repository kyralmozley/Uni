import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class GetPersonById {
    public static void main(String[] args) {
        try (MovieDB database = MovieDB.open(args[0])) {
            int id = Integer.parseInt(args[1]);
            Person person = database.getPersonById(id);
            System.out.println(person);
        }
    }
}
