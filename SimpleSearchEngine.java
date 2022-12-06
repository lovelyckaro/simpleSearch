import java.util.Comparator;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleSearchEngine {

  // Inverted index, each term is associated with a number of documents
  private final HashMap<String, HashSet<Document>> index;
  private final Tokenizer tokenizer;

  /**
   * Returns a search engine indexed with the files specified in paths.
   * 
   * @param paths      The files to be indexed by the engine
   * @param tokenRegex A regex defining how to tokenize the input
   * @throws IOException
   */
  public SimpleSearchEngine(Path[] paths, Tokenizer tokenizer) throws IOException {
    this.tokenizer = tokenizer; 
    index = new HashMap<>();
    for (Path path : Set.of(paths)) {
      indexPath(path);
    }
  }

  /**
   * Read, tokenize and index file contents.
   * @param path Path to file.
   * @throws IOException
   */
  public void indexPath(Path path) throws IOException {
    Document doc = new Document(path, tokenizer);
    for (String term : doc.getTerms()) {
      HashSet<Document> docs = index.get(term);
      if (docs == null) {
        docs = new HashSet<>();
        index.put(term, docs);
      }
      docs.add(doc);
    }
  }

  /**
   * The Inverse document frequency is a measure of how much information a word
   * provides, in terms of whether it is rare or common among all documents
   * indexed by the engine.
   * 
   * @param term
   * @return The Inverse Document frequency of a term among the files indexed.
   */
  public double getIDF(String term) {
    double totalDocuments = index.size();
    // Plus one to avoid division by zero.
    Set<Document> docs = index.get(term);
    double documentsMatching = (docs == null) ? 1 : docs.size();
    return Math.log(totalDocuments / documentsMatching);
  }

  /**
   * The TF_IDF of a term in a document is the product of the Term frequency of
   * the term in that function, times the Inverse Document frequency of that term
   * among the entire corpus. A term which is used a lot in the document, but
   * which is uncommon in other files with have a high TF_IDF value.
   * 
   * @param doc
   * @param term
   * @return the TF-IDF of the term in the document doc.
   */
  public double getTF_IDF(Document doc, String term) {
    return doc.getTF(term) * getIDF(term);
  }

  /**
   * Search for a given term amongst the indexed files. Will return a list of
   * files containing the term, sorted in descending order of TF_IDF.
   * 
   * @param term
   * @return A List of documents containing the term, sorted by TF_IDF.
   */
  public List<Document> search(String term) {
    Comparator<Document> tfIdf = Comparator.comparingDouble(doc -> getTF_IDF(doc, term));
    return index.getOrDefault(term, new HashSet<>()).stream()
        .sorted(tfIdf.reversed())
        .collect(Collectors.toList());
  }
}
