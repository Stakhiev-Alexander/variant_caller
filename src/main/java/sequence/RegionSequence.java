package sequence;

import java.util.Map;
import java.util.stream.Collectors;

public class RegionSequence {
  private final ListOfIntervals listOfIntervals;
  private final Map<Interval, String> nucleotidesIntervals;

  /**
   * Private constructor.
   * Use createInstance() method to get instance of the class.
   *
   * @param listOfIntervals      ListOfIntervals that contains intervals for which the string was cut
   * @param nucleotidesIntervals map with intervals and nucleotide substrings in each interval
   */
  private RegionSequence(ListOfIntervals listOfIntervals, Map<Interval, String> nucleotidesIntervals) {
    this.listOfIntervals = listOfIntervals;
    this.nucleotidesIntervals = nucleotidesIntervals;
  }

  /**
   * This method slices the passed string,
   * making the substrings for each of the intervals
   * from the ListOfIntervals.
   * <p>
   * String with nucleotides must fit passed interval list.
   *
   * @param intervals   ListOfIntervals that contains intervals
   *                    for which the string will be cut
   * @param nucleotides string with nucleotides
   * @return instance of RegionSequence
   * @throws RuntimeException if nucleotides string is bigger
   *                          than intervals it should be covered with
   */
  public static RegionSequence createInstance(ListOfIntervals intervals, String nucleotides) {
    if (intervals.getLength() > nucleotides.length()) {
      throw new RuntimeException("Nucleotides do not fit intervals");
    }

    Map<Interval, String> nucleotidesIntervals = intervals.asList().stream().collect(
        Collectors.toMap(interval -> interval,
            interval -> nucleotides.substring(interval.getBegin(), interval.getEnd() + 1)));

    return new RegionSequence(intervals, nucleotidesIntervals);
  }

  /**
   * Gets the list of intervals representing intervals which
   * are covered by current regionSequence class.
   *
   * @return a list of intervals
   */
  public ListOfIntervals getIntervalList() {
    return listOfIntervals;
  }

  /**
   * Gets the map with intervals and strings with nucleotides in this intervals.
   *
   * @return a map with intervals and strings
   */
  public Map<Interval, String> getNucleotidesIntervals() {
    return nucleotidesIntervals;
  }

  /**
   * Returns nucleotide in the given position.
   *
   * @param position position to be found in the nucleotidesIntervals
   * @return nucleotide in this position if it exists
   * @throws RuntimeException if position is not covered by any region
   */
  public Nucleotide getNucleotideAt(int position) {
    Interval interval = listOfIntervals.getIntervalByPosition(position).orElseThrow(() -> new RuntimeException("Current position is not covered by any region"));
    final int positionInSubstring = position - interval.getBegin();
    return Nucleotide.fromCharacter(nucleotidesIntervals.get(interval).charAt(positionInSubstring));
  }

  /**
   * Returns region in the given interval.
   *
   * @param interval interval to be found in the nucleotidesIntervals
   * @param position position to be found in the interval
   * @return nucleotide list in this interval if it is present
   * @throws RuntimeException if position is not covered by any region
   */
  public Nucleotide getNucleotideAt(Interval interval, int position) {
    if (!listOfIntervals.intervalIsPresent(interval)) {
      throw new RuntimeException("Interval is not present");
    }
    return Nucleotide.fromCharacter(nucleotidesIntervals.get(interval).charAt(position));
  }

  /**
   * Returns region in the given interval.
   *
   * @param interval interval to be found in the nucleotidesIntervals
   * @return nucleotide list in this interval if it is present
   * @throws RuntimeException if position is not covered by any region
   */
  public Region getRegion(Interval interval) {
    if (!listOfIntervals.intervalIsPresent(interval)) {
      throw new RuntimeException("Current interval is not covered by any region");
    }
    return new Region(Nucleotide.fromString(nucleotidesIntervals.get(interval)), interval.getBegin());
  }
}
