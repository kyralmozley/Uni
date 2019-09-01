package uk.ac.cam.km687.oop.tick3;
import java.io.*;
import java.net.*;
import java.util.*;

public class PatternStore {

  private List<Pattern> mPatterns = new LinkedList<>();
  private Map<String,List<Pattern>> mMapAuths = new HashMap<>();
  private Map<String,Pattern> mMapName = new HashMap<>();

   public PatternStore(String source) throws IOException {
       if (source.startsWith("http://")) {
          loadFromURL(source);
       }
       else {
          loadFromDisk(source);
    }

   }

   public PatternStore(Reader source) throws IOException {
      load(source);
   }

   private void load(Reader r) throws IOException {
     BufferedReader b = new BufferedReader(r);
 		 String line = b.readLine();
 		 int lineNo = 1;
 		 Pattern p;

 		 while (line != null) {
 			//System.out.println(line);

 			//read the data into these new structures. Remember that you add to lists but put to maps.
 			try {
         p = new Pattern(line);
         mPatterns.add(p);

         List<Pattern> pList = (mMapAuths.containsKey(p.getAuthor())) ?	 mMapAuths.remove(p.getAuthor()) : new LinkedList<Pattern>();
         pList.add(p);
         mMapAuths.put(p.getAuthor(), pList);
         mMapName.put(p.getName(), p);

 			} catch (PatternFormatException e) {//If a line in the file is malformed you should print out the offending line as a warning
 				System.out.println("Error reading line "+lineNo+": "+e.getMessage());
 			}

 			line = b.readLine();
 			lineNo++;
 		}
    }


   private void loadFromURL(String url) throws IOException {
    // TODO: Create a Reader for the URL and then call load on it
    URL destination = new URL(url);
    URLConnection conn = destination.openConnection();
    Reader r = new java.io.InputStreamReader(conn.getInputStream());
    BufferedReader b = new BufferedReader(r);
    load(b);
   }

   private void loadFromDisk(String filename) throws IOException {
    // TODO: Create a Reader for the file and then call load on it
    try {
    Reader r = new FileReader(filename);
    load(r);

    //Convert FileNotFoundException to IOException to conform with constructor throws clause
    } catch (FileNotFoundException e) {
      throw new IOException("No file could be found at "+filename);
    }
   }


   public List<Pattern> getPatternsNameSorted() {
      // TODO: Get a list of all patterns sorted by name
      List<Pattern> lstCopy = new LinkedList<Pattern>(mPatterns);
      Collections.sort(lstCopy);
      return lstCopy;
   }

   public List<Pattern> getPatternsAuthorSorted() {
      // TODO: Get a list of all patterns sorted by author then name
      List<Pattern> lstCopy = new LinkedList<Pattern>(mPatterns);

            Collections.sort(lstCopy, new Comparator<Pattern>() {
              public int compare(Pattern p1, Pattern p2) {
                if (p1.getAuthor().compareTo(p2.getAuthor()) == 0) {
                  return p1.compareTo(p2);
                } else {
                  return p1.getAuthor().compareTo(p2.getAuthor());
                }
              }
            });

          return lstCopy;
   }

   //failing
   public List<Pattern> getPatternsByAuthor(String author) throws PatternNotFound {
     if(!mMapAuths.containsKey(author)) throw new PatternNotFound("Could not find a pattern belonging to " + author);
     List<Pattern> p = mMapAuths.get(author);
     Collections.sort(p);
     return p;
   }


   public Pattern getPatternByName(String name) throws PatternNotFound {
      // TODO: Get a particular pattern by name
      if (!mMapName.containsKey(name)) throw new PatternNotFound("Could not find the pattern with name "+name);
		  return mMapName.get(name);
   }

   public List<String> getPatternAuthors() {
      // TODO: Get a sorted list of all pattern authors in the store
      List<String> authors = new LinkedList<>(mMapAuths.keySet());
		  Collections.sort(authors);
		  return authors;
   }

   public List<String> getPatternNames() {
      // TODO: Get a list of all pattern names in the store,
      // sorted by name
      List<String> patternNames = new LinkedList<>(mMapName.keySet());
		  Collections.sort(patternNames);
		  return patternNames;
   }


   public static void main(String args[]) throws IOException {
      PatternStore p = new PatternStore(args[0]);


   }
}
