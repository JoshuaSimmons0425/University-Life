package parser;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.StringReader;

//Note: as discussed in class, this code is intended as part of the specific
//parser and not as general reusable code

public class Tokenizer {
  private final Scanner s;
  private final Pattern delimiters= Pattern.compile("\\s+|(?=[{}(),;])|(?<=[{}(),;])");
  private final String input;
  public Tokenizer(String input){
    this.input= input;
    this.s= new Scanner(input).useDelimiter(delimiters);
    }
  /**Unobvious way to extract position using java Scanner*/
  public String pos(){    
    int remainingSize= !s.hasNext() ? 0 : s.useDelimiter("\\z").next().length();
    String allCode= new BufferedReader(new StringReader(input)).lines().collect(Collectors.joining("\n"));  
    allCode = allCode.substring(0, allCode.length() - remainingSize);
    long line = allCode.lines().count(); 
    int lastNewlineIndex = allCode.lastIndexOf('\n');
    int col = allCode.length();
    if(lastNewlineIndex != -1){ col -= lastNewlineIndex; }
    return "\n\nat line "+line+", position " + col;
  }
  private Pattern patternOf(String str){ return Pattern.compile(Pattern.quote(str)); }
  
  public boolean hasNext(){ return s.hasNext(); }
  public boolean hasNext(String s){ return hasNext(patternOf(s)); }
  public boolean hasNext(Pattern p){ return s.hasNext(p); }
  public String next(){ //or try-catch
    if (s.hasNext()){ return s.next(); }
    throw fail("End of tokens");
    }
  public String next(String expectedValue) {
    return next(patternOf(expectedValue), expectedValue);
  }
  public String next(Pattern p, String humanReadable){
    if (s.hasNext(p)){ return s.next(p); }
    String next= s.hasNext()?s.next():"END OF INPUT";
    throw fail(next,humanReadable);
  }
  public Error fail(String msg) {throw new ParserFailureException(msg + pos()); }
  public Error fail(String token, List<String> expected) {
    throw fail("Unexpected token ["+token+"] Expected one of "+expected);
  }
  public Error fail(String token, String expected) {
    throw fail("Unexpected token ["+token+"] Expected ["+expected+"]");
  }
  public boolean hasNext(List<String> tokens){
    if (!hasNext()){ return false; }
    Pattern namesPat = Pattern.compile(String.join("|", tokens));
    return hasNext(namesPat);
  }
  public String next(List<String> tokens){
    var name= next();//this throws if end of tokens
    if (!tokens.contains(name)){ throw fail(name,tokens); }
    return name;
  }
  //- Below here you can add more custom methods to handle specific tokens and error messages
  void or(){ next("("); }
  void cr(){ next(")"); }//add your methods here
  void sc(){ next(";");}
  void loop() {next("loop");}
  void oc() {next("{");}
  void cc() {next("}");}
  void ifstm() {next("if");}
  void whilestm() {next("while");}
  void comma() {next(",");}
 
  //example code for variable tokens
  //private static final Pattern varPat = Pattern.compile("\\$[A-Za-z][A-Za-z0-9]*");
  //boolean matchVar(String s){ return varPat.matcher(s).matches(); }
  //boolean hasNextVar(){ return hasNext(varPat); }
  //String nextVar() { return next(varPat,"variable name"); }  

  //can you do it similarly for ints?
  //private static final Pattern numPat = Pattern.compile("-?[1-9][0-9]*|0");
  //boolean hasNextInt(){ return hasNext(numPat) && s.hasNextInt(); }
  //int nextInt() {
    //...
    //return s.nextInt();
  //}
  //Example of simplifying error reporting in the tokenizer.
  //returns an error so that you can call it as 'throw errNoExpr();'
  //in the user position
  //You can/should define a couple more
  Error errNoStmt(String token){
    var options= List.of("variable name","loop","if","while","move","turnL",
      "turnR","turnAround","shieldOn","shieldOff","takeFuel","wait");
    throw fail(token,options);
  }
  Error emptyBlock(){ throw fail("Block needs a non empty list of statements"); }
}