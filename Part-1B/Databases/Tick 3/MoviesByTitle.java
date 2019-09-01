import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class MoviesByTitle {
    public static void main(String[] args) {
        try(MovieDB database = MovieDB.open(args[0])) {
            System.out.println("List of Star Wars Movies:");
            
            for(Movie movie: database.getByTitlePrefix("Star Wars")) {
                System.out.println("    " + movie.getId() + ": " + movie.getTitle());
            }
        }
    }
}
