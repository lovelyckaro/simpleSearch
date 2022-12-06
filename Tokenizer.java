public interface Tokenizer {
  /**
   * Splits content into terms.
   * @param content
   * @return some collection of terms.
   */
  public Iterable<String> getTerms(String content);
}
