package parser;

import java.util.List;
import java.util.Objects;

import robotGame.OuterWorld;

/**Program represents the AST of a robot program.
 * Program, ExamplePrograms and Parser are the only public class in the package parser.
 * All other types are only used inside this package.
 */
public record Program(List<Stm> ss){
  public Program{ Objects.requireNonNull(ss); }
  public Program execute(OuterWorld w){
    //Note: The code of Program is fully provided. Try to understand how it works,
    //so that you can use this understanding to complete the rest.
    var self= this;
    while(!w.used() && !self.ss.isEmpty()){
      var stm= self.ss.getFirst().execute(w);
      self = new Program(Util.updateFirst(stm,self.ss));
      }
    return self;
    }
  @Override public String toString(){ return "Program"+ss; }
  }