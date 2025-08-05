package parser;

import robotGame.OuterWorld;


//TODO: define a Cond interface with an evaluate method
//With such interface, declare records like the one below
//record Lt(Exp left, Exp right) implements Cond{
//  public boolean evaluate(OuterWorld w){ return left.evaluate(w) < right.evaluate(w); }
//  @Override public String toString(){ return "Lt["+left+", "+right+"]"; }
//}

interface Cond {
	boolean evaluate(OuterWorld w);
}

record Lt(Exp left, Exp right) implements Cond{
  public boolean evaluate(OuterWorld w){ 
	  return left.evaluate(w) < right.evaluate(w); 
  }
 @Override 
  public String toString(){ return "Lt["+left+", "+right+"]"; }
}
record Gt(Exp left, Exp right) implements Cond {
    public boolean evaluate(OuterWorld w) {
        return left.evaluate(w) > right.evaluate(w);
    }
    @Override
    public String toString(){ return "Gt["+left+", "+right+"]"; }
}
record Eq(Exp left, Exp right) implements Cond {
    public boolean evaluate(OuterWorld w) {
        return left.evaluate(w) == right.evaluate(w);
    }
    @Override
    public String toString(){ return "Eq["+left+", "+right+"]"; }
}

record And(Cond left, Cond right) implements Cond{
	public boolean evaluate(OuterWorld w) {
		return left.evaluate(w) && right.evaluate(w);
	}
	@Override public String toString() {return "And[" + left + ", " + right + "]";}
}

record Or(Cond left, Cond right) implements Cond{
	public boolean evaluate(OuterWorld w) {
		return left.evaluate(w) || right.evaluate(w);
	}
	@Override public String toString() {return "Or[" + left + ", " + right + "]";}
}

record Not(Cond inner) implements Cond{
	public boolean evaluate(OuterWorld w) {
		return !inner.evaluate(w);
	}
	@Override public String toString() {return "Not[" +inner+ "]";}
}

