import java.util.*;
import uk.ac.cam.cl.databases.moviedb.MovieDB;
import uk.ac.cam.cl.databases.moviedb.model.*;

public class Exercise3 {
    public static void main(String[] args) {
        // The set of person_id's that we want to count
        // If you want to add id (of type int) to the set
        // simply use pid_set.add(id)
        Set<Integer> pid_set = new HashSet<Integer>();
        // open database
        try (MovieDB database = MovieDB.open(args[0])) {
            //
            // YOUR CODE GOES HERE
            //
            for(Person jl: database.getByNamePrefix("Lawrence, Jennifer (III)")) {
            
                for(Role role: jl.getActorIn()) {
                    for(Movie movie: database.getByTitlePrefix(role.getTitle())) {
                        for(CreditActor actor: movie.getActors()) {
                            int p_id = actor.getPersonId();
                            if(p_id != jl.getId() && !pid_set.contains(p_id)) {
                                pid_set.add(p_id);
                            }
                        }
                    }
                }
                System.out.println(pid_set.size());
            }
        }
    }
}
