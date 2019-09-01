import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class CountPeople {
    public static void main(String[] args) {
        int count = 0;
        try (MovieDB database = MovieDB.open(args[0])) {
            for(Person person: database.getByNamePrefix("")) {
                count++;
            }
            System.out.println("Number of people: " + count);
            
        }
    }
}
