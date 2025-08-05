package parser;

import java.util.List;
import java.util.Objects;

import robotGame.OuterWorld;

//Interface for statements (Stm) that can be executed within the OuterWorld.
interface Stm{ 
 Stm execute(OuterWorld w); // Defines how a statement executes in the game world.
}

//Statements (other than direct actions)

//Provided complete code for Loop
record Loop(Block b) implements Stm {
 public Stm execute(OuterWorld w) {
     return b.addLast(this); // Adds the Loop itself to the end of the block to enable iteration.
     // The handout provides details on why this works.
     // This concept is useful for implementing the 'While' statement similarly.
 }

 @Override public String toString() { return "Loop" + b; }
}

//Provided near-complete code for Block.
record Block(List<Stm> ss) implements Stm {
 Block {
     if (ss.isEmpty()) { throw new Error(); } // Ensures a Block is not created with an empty list.
     for (Stm s : ss) { Objects.requireNonNull(s); } // Ensures all statements in the Block are non-null.
 }

 public Block execute(OuterWorld w) {
     var first = ss.getFirst().execute(w); // Executes the first statement in the Block.

     // If the first statement is null and it's the only one in the Block, return null.
     if (first == null && ss.size() == 1) { return null; }

     return new Block(Util.updateFirst(first, ss)); // Updates the first statement in the list.
     // You may use Util.removeFirst and/or Util.updateFirst to complete this part.
 }

 // Concatenates another list of statements to the current block.
 Block concat(List<Stm> other) { return new Block(Util.concat(ss, other)); }

 // Adds a statement to the end of the block.
 Block addLast(Stm s) { return new Block(Util.appendLast(ss, s)); }

 @Override public String toString() { return ss.toString(); }
}

//Note: This 'If' structure can also represent 'elif' conditions.
record If(Cond cond, Block then) implements Stm {
 If { 
     Objects.requireNonNull(cond); // Ensures condition is non-null.
     Objects.requireNonNull(then); // Ensures the Block is non-null.
 }

 public Stm execute(OuterWorld w) { 
     var c = cond.evaluate(w); // Evaluates the condition.

     if (c) { 
         return then; // Executes the 'then' block if the condition is true.
     }
     return null; // Otherwise, nothing happens.
 }

 @Override public String toString() {
     return "If[" + cond + ", " + then + "]";
 }
}

//While loop that repeatedly executes its body while the condition holds true.
record While(Cond cond, Block body) implements Stm {
 While { 
     Objects.requireNonNull(cond); // Ensures condition is non-null.
     Objects.requireNonNull(body); // Ensures body is non-null.
 }

 public Stm execute(OuterWorld w) { 
     var c = cond.evaluate(w); // Evaluates the condition.

     if (c) {
         return body.concat(List.of(this)); // Recursively adds itself to continue looping.
     }
     return null; // Stops execution when the condition is false.
 }

 @Override public String toString() {
     return "While[" + cond + ", " + body + "]";
 } 
}

//Interface for actions that can be executed in the game.
interface Act extends Stm {}

//Actions

//Move action: Moves the robot in the game.
record Move() implements Act {
 public Stm execute(OuterWorld w) {
     w.doMove(); // Calls the move action in the game world.
     return null; // No further execution needed.
 }

 @Override public String toString() { return "Move"; }
}

//Wait action: Pauses the robot's movement.
record Wait() implements Act {
 public Stm execute(OuterWorld w) {
     w.doWait(); // Calls the wait action.
     return null;
 }

 @Override public String toString() { return "Wait"; }
}

//Turn right action.
record TurnR() implements Act {
 public Stm execute(OuterWorld w) { 
     w.doTurnR(); // Calls the turn right action.
     return null;
 }

 @Override public String toString() { return "TurnR"; }
}

//Turn left action.
record TurnL() implements Act {
 public Stm execute(OuterWorld w) {
     w.doTurnL(); // Calls the turn left action.
     return null;
 }

 @Override public String toString() { return "TurnL"; }
}

//Take fuel action.
record TakeFuel() implements Act {
 public Stm execute(OuterWorld w) {
     w.doTakeFuel(); // Calls the take fuel action.
     return null;
 }

 @Override public String toString() { return "TakeFuel"; }
}

//Turn around action.
record TurnAround() implements Act {
 public Stm execute(OuterWorld w) {
     w.doTurnAround(); // Calls the turn around action.
     return null;
 }

 @Override public String toString() { return "TurnAround"; }
}

//Shield activation action.
record ShieldOn() implements Act {
 public Stm execute(OuterWorld w) {
     w.setShield(true); // Activates the shield.
     return null;
 }

 @Override public String toString() { return "ShieldOn"; }
}

//Shield deactivation action.
record ShieldOff() implements Act {
 public Stm execute(OuterWorld w) {
     w.setShield(false); // Deactivates the shield.
     return null;
 }

 @Override public String toString() { return "ShieldOff"; }
}
