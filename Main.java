import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

class Main {
  public static void main(String[] args) throws IOException {
    Path[] paths = Files.walk(Paths.get(args[0])).filter(Files::isRegularFile).toArray(Path[]::new);
    Tokenizer tokenizer = new RegexTokenizer("[ ,.?\n{}()\"\']+");
    SimpleSearchEngine engine = new SimpleSearchEngine(paths, tokenizer);
    Scanner scanner = new Scanner(System.in);
    System.out.println("");
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