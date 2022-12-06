import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class Main {
  /**
   * @param args Paths to files which should be included in the corpus.
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    Path[] paths = Arrays.stream(args).map(p -> Path.of(p)).toArray(Path[]::new);
    Tokenizer tokenizer = new RegexTokenizer("[ ,.?\n{}()\"\']+");
    SimpleSearchEngine engine = new SimpleSearchEngine(paths, tokenizer);
    Scanner scanner = new Scanner(System.in);
    String input = "";
    while (true) {
      System.out.print("Search: ");
      input = scanner.nextLine();
      if (input.equals("quit")) break;
      List<Document> searchResults = engine.search(input);
      for (Document result : searchResults) {
        System.out.println(String.format("%s, TF-IDF: %.3f", result, engine.getTF_IDF(result, input)));
      }
    }
    scanner.close();
  }
}