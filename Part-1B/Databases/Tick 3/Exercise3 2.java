import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class Exercise3 {
    public static void main(String[] args) {
        try (MovieDB database = MovieDB.open(args[0])) {
            int requiredPosition = Integer.parseInt(args[1]);
            
            // Scan over all movies
            for (Movie movie : database.getByTitlePrefix("")) {
                // Keeps track of the name of an actor with the required
                // position in the current movie
                String firstMatch = null, secondMatch = null;
                
                for (CreditActor actor : movie.getActors()) {
                    // The null check is required because some actors have a
                    // null position, and you get a NullPointerException due to
                    // auto-unboxing if you just compare getPosition() == int
                    if (actor.getPosition() != null && actor.getPosition() == requiredPosition) {
                        if (firstMatch == null) {
                            firstMatch = actor.getName();
                        } else {
                            // We have found two actors with the required position.
                            // Now order their names lexicographically.
                            if (firstMatch.compareTo(actor.getName()) < 0) {
                                secondMatch = actor.getName();
                            } else {
                                secondMatch = firstMatch;
                                firstMatch = actor.getName();
                            }
                            
                            System.out.println(firstMatch + " and " + secondMatch +
                                               " both have position " + requiredPosition + " in " +
                                               movie.getTitle());
                        }
                    }
                }
            }
        }
    }
}
