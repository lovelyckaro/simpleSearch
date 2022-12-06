import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

public class Document {
  private final HashMap<String, Integer> termFrequency;
  private int numTerms = 0;
  private final Path path;

  public Document(Path path, Tokenizer tokenizer) throws IOException {
    this.path = path;
    termFrequency = new HashMap<>();
    String content = Files.readString(path);
    for (String term : tokenizer.getTerms(content)) {
      numTerms++;
      termFrequency.compute(term, (t, prev) -> (prev == null) ? 1 : prev + 1);
    }
  }

  /**
   * @return the terms in the document
   */
  public Set<String> getTerms() {
    return termFrequency.keySet();
  }

  /**
   * @param term
   * @return How many times the given term appears in a document
   */
  public int getCount(String term) {
    return termFrequency.get(term);
  }

  /**
   * The Term Frequency, TF, is calculated by counting the number of times a given
   * term appears in a document, divided by the total amount of terms in the
   * document.
   * 
   * @param term
   * @return The term frequency of a term in the document
   */
  public double getTF(String term) {
    return (double) termFrequency.get(term) / (double) numTerms;
  }

  @Override
  public String toString() {
    return path.toString();
  }
}
