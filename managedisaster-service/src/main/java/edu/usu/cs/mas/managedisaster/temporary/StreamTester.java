package edu.usu.cs.mas.managedisaster.temporary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

public class StreamTester {
  
  public static void main(String[] args) {
    StreamTester streamTester = new StreamTester();
    streamTester.filterCurrentTweets();
  }
  
  public void filterCurrentTweets() {
    DateTime dateTime = new DateTime();
    Date today = new DateTime().withTimeAtStartOfDay().toDate();
    Date tomorrow = dateTime.plusDays(1).toDate();
    Date yesterday = dateTime.plusDays(-1).toDate();
    List<Tweet> tweets = new ArrayList<>();
    long latestId = 0;
    tweets.add(new Tweet(1L, "Today tweet", dateTime.toDate()));
    tweets.add(new Tweet(2L, "Yesterday tweet", yesterday));
    tweets.add(new Tweet(3L, "Tomorrow tweet", tomorrow));
    
    List<Tweet> filteredTweets = tweets.stream()
          .filter(t -> today.before(t.getTimeStamp()) && t.getId() > latestId)
          .collect(Collectors.toList());
    
    System.out.println("Filtered tweets: "+filteredTweets);
  }
}
