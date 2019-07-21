package fastaparser;

import bedparser.BedParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sequence.FastaSequence;
import sequence.ListOfIntervals;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

class FastaParserTest {
  private static final String FASTA_FILE_NAME = "ex.fa";
  private static final String BED_FILE_NAME = "ex.bed";
  private static final String CHROMOSOME_NAME_1 = "chr1";
  private static final String CHROMOSOME_NAME_2 = "chr2";

  private static List<FastaSequence> fastaSequenceList = new ArrayList<>();

  @BeforeAll
  @DisplayName("Init fastaParser")
  static void initFastaParser() throws URISyntaxException {
    FastaParser fastaParser =
        FastaParser.parseFasta(
            Paths.get(
                Objects.requireNonNull(
                    FastaParserTest.class.getClassLoader().getResource(FASTA_FILE_NAME))
                    .toURI()));
    Map<String, ListOfIntervals> bedData =
        BedParser.collectIntervals(
            Paths.get(
                Objects.requireNonNull(
                    FastaParserTest.class.getClassLoader().getResource(BED_FILE_NAME))
                    .toURI()));
    bedData.forEach(
        (s, intervals) -> fastaSequenceList.add(fastaParser.getRegionsForChromosome(s, intervals)));
  }

  @Test
  @DisplayName("Parse unexciting fasta file")
  void parseUnexcitingFastaFile() {
    Assertions.assertThrows(
        NoSuchElementException.class, () -> FastaParser.parseFasta(Paths.get("i_dont_exist.fa")));
  }

  @Test
  @DisplayName("Check correctness of the parsed names of sequences")
  void checkParsedSequenceNames() {
    Assertions.assertEquals(2, (long) fastaSequenceList.size());
    Assertions.assertEquals(
        1,
        fastaSequenceList.stream()
            .filter(fastaSequence -> fastaSequence.getChromosomeName().equals(CHROMOSOME_NAME_1))
            .count());
    Assertions.assertEquals(
        1,
        fastaSequenceList.stream()
            .filter(fastaSequence -> fastaSequence.getChromosomeName().equals(CHROMOSOME_NAME_2))
            .count());
  }
}
