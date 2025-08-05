package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**Simple utility methods to work on unmodifiable lists*/
class Util{
  static public <T> List<T> removeFirst(List<T> ts){ return ts.subList(1, ts.size()); }
  static public <T> List<T> swapFirst(T newFirst, List<T> ts){
    var res= new ArrayList<>(ts);
    assert newFirst != null;
    res.set(0,newFirst);
    return Collections.unmodifiableList(res);
  }
  static public <T> List<T> updateFirst(T newFirst, List<T> ts){
    if (newFirst == null){ return removeFirst(ts); }
    return swapFirst(newFirst,ts);
  }
  static public <T> List<T> concat(List<T> ts1, List<T> ts2){
    var res= new ArrayList<>(ts1);
    res.addAll(ts2);
    return Collections.unmodifiableList(res);    
  }
  static public <T> List<T> appendLast(List<T> ts, T newLast){
    Objects.requireNonNull(newLast);
    var res= new ArrayList<>(ts);
    res.add(newLast);
    return Collections.unmodifiableList(res);
  }
}