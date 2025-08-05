package parser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {
  private final Tokenizer t;
  public Parser(String text){ t= new Tokenizer(text); }
  public Parser(Path path){ t= new Tokenizer(readFromPath(path)); }
  static String readFromPath(Path path){//This code show how to read a file
    try {return Files.readString(path); }//in modern Java
    catch (IOException ieo){ throw new UncheckedIOException(ieo); }
  }
  public Program parse(){
    ArrayList<Stm> ss= new ArrayList<>();
    while (t.hasNext()){ ss.add(parseSingleStmt()); }
    return new Program(Collections.unmodifiableList(ss));
  }
  // Returns the appropriate actions from the given token
  Stm parseSingleStmt(){ 
	  String token = t.next();
	  switch(token) {
	  case "move":
		  return parseMove();
	  case "turnL": 
		  return parseTurnL();
	  case "turnR": 
		  return parseTurnR();
	  case "turnAround":
		  return parseTurnAround();
	  case "takeFuel":
		  return parseTakeFuel();
	  case "wait": 
		  return parseWait();
	  case "loop":
		  return parseLoop();
	  case "shieldOn": 
		  return parseShieldOn();
	  case "shieldOff":
		  return parseShieldOff();
	  case "if":
	      return parseIf();
	  case "while":
		 return parseWhile();
	  default:
		  throw new ParserFailureException("Parsing falied");
	  }
  }
  
//declare methods like
  //Stm parseMove() {..}
  //and so on, for many, many methods
  
  Stm parseLoop() {
	  return new Loop(parseBlock()); // parses loop
  }
  
  Block parseBlock() {
	  if (!t.hasNext("{")) {
	       throw new ParserFailureException("Expected '{' to start a block");
	  }
	  t.oc(); // Consume '{' 
	  List<Stm> commands = new ArrayList<>();
	  do {
		  commands.add(parseSingleStmt());  // Ensure we don't stop early
	  } while (t.hasNext() && !t.hasNext("}"));
	  if (!t.hasNext("}")) {
	      throw new ParserFailureException("Expected '}' to end a block");
	  }
	  t.cc();
	  return new Block(Collections.unmodifiableList(commands));
  }
  
  // For each parseAction, it consumes a semi-colon and then parses then corresponding action
  Stm parseMove() {
      t.sc(); 
	  return new Move();
  }
  Stm parseWait() {
	  t.sc();
	  return new Wait();
  }
  Stm parseTurnL() {
	  t.sc();
	  return new TurnL();
  }
  Stm parseTurnR() {
	  t.sc();
	  return new TurnR();
  }
  Stm parseTakeFuel() {
	  t.sc();
	  return new TakeFuel();
  }
  Stm parseTurnAround() {
	  t.sc();
	  return new TurnAround();
  }
  Stm parseShieldOn() {
	  t.sc();
	  return new ShieldOn();
  }
  Stm parseShieldOff() {
	  t.sc();
	  return new ShieldOff();
  }

  // Parsing the While statement
  While parseWhile() {
	  t.or(); // Consume '('
	  Cond cond = parseCond();
	  if (!t.hasNext(")")) {  // Ensure ) exists
	        throw new ParserFailureException("Missing closing ')' in if condition");
	  }
	  t.cr(); // Consume ')'
	    
	  // Ensure block is enclosed in '{...}'
	  Block body = parseBlock();
	  return new While(cond, body); 
  }
  // Parsing the If statement
  If parseIf() {
	  t.or(); // Consume '('
	  Cond cond = parseCond();
	  if (!t.hasNext(")")) {  // Ensure ) exists
		  throw new ParserFailureException("Missing closing ')' in if condition");
	  }
	  t.cr(); // Consume ')'
	    
	  Block body = parseBlock();
	  return new If(cond, body);
  }
  
  // Parsing the condition
  Cond parseCond() {
	String token = t.next();
	if(!token.equals("lt") && !token.equals("gt") && !token.equals("eq")) {
		throw new ParserFailureException("Invalid token");
	}
	t.or();
	Sens left = parseSensor();
	t.comma();
	Exp right = parseExpression();
	t.cr();
	
	// Returns appropriate condition
	switch(token) {
	case "lt":
		return new Lt(left, right);
	case "gt":
		return new Gt(left, right);
	case "eq": 
		return new Eq(left, right);
	default:
		throw new ParserFailureException(token);
	}
  }
  // Parsing the Sensor
  Sens parseSensor() {
	  if(!t.hasNext()) {
		  throw new ParserFailureException("Unexpected token");
	  }
	  String token = t.next();
	  
	  // returns appropriate sensor
	  switch(token) {
	  case "fuelLeft":
		  return new FuelLeft();
	  case "oppLR":
		  return new OppLR();
	  case "oppFB":
		  return new OppFB();
	  case "numBarrels":
		  return new NumBarrels();
	  case "barrelLR":
		  return new BarrelLR();
	  case "barrelFB":
		  return new BarrelFB();
	  case "wallDist":
		  return new WallDistance();
	  default:
		  throw new ParserFailureException("Unexpected sensor: " + token);
	  }
	  
  }
  // Parsing the Expression
  Exp parseExpression() {
	 if(!t.hasNext()) {
		  throw new ParserFailureException("Unexpected token");
	 }
	 String token = t.next();
	    
	    // If the token is a number, return a NumberExpression
	 if (token.matches("-?[1-9][0-9]*|0")) {
	     return new Num(Integer.parseInt(token));
	 }
	 throw new ParserFailureException("Invalid expression: " + token);
  }
  
  // Parsing the arithmetic operations
  Exp parseOp() {
	  String token = t.next();
		if(!token.equals("add") && !token.equals("sub") && !token.equals("mul") && !token.equals("div")){
			throw new ParserFailureException("Invalid token");
		}
	  t.or();
	  Exp left = parseExpression();
	  t.comma();
	  Exp right = parseExpression();
	  t.cr();
	  
	// Returns the appropriate arithmetic operation object
	  switch(token) {
	  case "add":
		  return new Add(left, right);
	  case "sub":
		  return new Sub(left, right);
	  case "mul":
		  return new Mul(left, right);
	  case "div":
		  return new Div(left, right);
	  default:
		  throw new ParserFailureException("Expected an operation but instead got: "+token);
	  }
  } 
}
 
  
 
 
