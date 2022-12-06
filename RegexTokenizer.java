import java.util.List;

public class RegexTokenizer implements Tokenizer {
  private final String regex;

  /**
   * A tokenizer which accepts a regex string. Any matches to the regex in the
   * content will be used to split into terms.
   * 
   * @param regex
   */
  public RegexTokenizer(String regex) {
    this.regex = regex;
  }

  @Override
  public Iterable<String> getTerms(String content) {
    return List.of(content.split(regex));
  }
}
