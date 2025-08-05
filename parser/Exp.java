package parser;

import robotGame.OuterWorld;

interface Exp{
  int evaluate(OuterWorld w);
}
//expressions
record Num(int inner) implements Exp{ //full provided code for one example expression
  public int evaluate(OuterWorld w){ return inner; }
  @Override public String toString(){ return "Num["+inner+"]"; }
}
record Var(String name) implements Exp{
  public int evaluate(OuterWorld w){ throw new Error("TODO"); }
  @Override public String toString(){ throw new Error("TODO"); }
}
record Add(Exp left, Exp right) implements Exp{
  public int evaluate(OuterWorld w){ return left.evaluate(w) + right.evaluate(w); }
  @Override public String toString(){ return "Add[" + left + ", " + right + "]";}
}
record Sub(Exp left, Exp right) implements Exp{
  public int evaluate(OuterWorld w){  return left.evaluate(w) - right.evaluate(w); }
  @Override public String toString(){return "Sub[" + left + ", " + right + "]";}
}
record Mul(Exp left, Exp right) implements Exp{
  public int evaluate(OuterWorld w){  return left.evaluate(w) * right.evaluate(w); }
  @Override public String toString(){ return "Mul[" + left + ", " + right + "]"; }
}
record Div(Exp left, Exp right) implements Exp{
  public int evaluate(OuterWorld w){  return left.evaluate(w) / right.evaluate(w); }
  @Override public String toString(){ return "Div[" + left + ", " + right + "]"; }
}
/*TODO: record FuelLeft... and many other types*/

interface Sens extends Exp{}

record FuelLeft() implements Sens {
	public int evaluate(OuterWorld w) {
		return w.readFuelLeft();
	}
	@Override 
	public String toString() {return "FuelLeft";}
}

record OppLR() implements Sens {
	public int evaluate(OuterWorld w) {
		return w.readOppLR();
	}
	@Override
	public String toString() {return "OppLR";}
}
record OppFB() implements Sens {
	public int evaluate(OuterWorld w) {
		return w.readOppFB();
	}
	@Override
	public String toString() {return "OppFB";}
}
record NumBarrels() implements Sens {
	public int evaluate(OuterWorld w) {
		return w.readNumBarrels();
	}
	@Override
	public String toString() {return "NumBarrels";}
}
record BarrelLR() implements Sens {
	public int evaluate(OuterWorld w) {
		return w.readBarrelLR(0);
	}
	@Override
	public String toString() {return "BarrelLR";}
}
record BarrelFB() implements Sens{
	public int evaluate(OuterWorld w) {
		return w.readBarrelFB(0);
	}
	@Override
	public String toString() {return "BarrelFB";}
}
record WallDistance() implements Sens {
	public int evaluate(OuterWorld w) {
		return w.readWallDist();
	}
	@Override
	public String toString() {return "WallDist";}
}