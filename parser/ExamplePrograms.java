package parser;

import java.util.List;

public class ExamplePrograms {
  static Block b(Stm...ss){
    throw new Error("Code below would compile in the complete program");
    //return new Block(List.of(ss));
    }
  static final Var x= new Var("x");
  static final Var y= new Var("x");
  static final Var lr= new Var("lr");
  static final Var fb= new Var("fb");
  static final Num zero= new Num(0);
  static final Num one= new Num(1);
  static final Num minusOne= new Num(-1);
  static public Program leftRightMove(){
    throw new Error("Code below would compile in the complete program");
   // return new Program(List.of(
    //  new TurnL(),
     // new TurnR(),
     // new Move(null)
   // ));
  }
  static public Program loopLeftRightMove(){
    throw new Error("Code below would compile in the complete program");
    //return new Program(List.of(new Loop(b(
    //  new TurnL(),
    //  new TurnR(),
    //  new Move(null)
    //))));
  }
  static public Program whileLeftRightMove(){
    throw new Error("Code below would compile in the complete program");
    //return new Program(List.of(new While(new Eq(zero,zero),b(
    //  new TurnL(),
    //  new TurnR(),
    //  new Move(null)
    //))));
  }
  static public Program aimAndGo(){
    throw new Error("Code below would compile in the complete program");  
    //return new Program(List.of(new Loop(b(
    //  new If(new And(
    //    new Eq(new BarrelLR(null),zero),
    //    new Eq(new BarrelFB(null),zero)),
    //    b(new TakeFuel()),b(
    //  new If(new And(
    //    new Eq(new BarrelLR(null),zero),
    //    new Gt(new BarrelFB(null),zero)),
    //    b(new Move(null)),b(
    //  new If(new And(
    //    new Eq(new BarrelLR(null),zero),
    //    new Lt(new BarrelFB(null),zero)),
    //    b(new TurnAround()),b(
    //  new If(new Lt(new BarrelLR(null),zero),
    //    b(new TurnL()),b(
    //  new If(new Gt(new BarrelLR(null),zero),
    //    b(new TurnR()),null)
    //  ))))))))))));
      }
  static public Program aimAndGoLocVar(){
    throw new Error("Code below would compile in the complete program");
    //return new Program(List.of(new Loop(b(
    //  new Ass("lr",new BarrelLR(null)),
    //  new Ass("fb",new BarrelFB(null)),
    //  new If(new And(new Eq(lr,zero),new Eq(fb,zero)),
    //    b(new TakeFuel()),b(
    //  new If(new And(new Eq(lr,zero),new Gt(fb,zero)),
    //    b(new Move(null)),b(
    //  new If(new And(new Eq(lr,zero),new Lt(fb,zero)),
    //    b(new TurnAround()),b(
    //  new If(new Lt(lr,zero),
    //    b(new TurnL()),b(new TurnR())
    //  )))))))))));
      }
  static public Program defaultProgramCode(){
    throw new Error("Code below would compile in the complete program");
    //var z= new Num(0);
    //var x= new Var("x");
    //var y= new Var("y");
    //return new Program(List.of(new While(new Gt(new FuelLeft(),z),b(
    //  new If(new Eq(new NumBarrels(),z),
    //    b(new TakeFuel()/*,new ShieldOn()*/),//comment and uncomment this to check stealing fuel behavior.
    //    b(
    //      new Ass("x", new BarrelLR(null)),
    //      new Ass("y", new BarrelFB(null)),
    //      new If(new And(new Eq(x,z),new Eq(y,z)),b(new TakeFuel()),
    //        b(new If(new Eq(y,z),b(
    //          new If(new Gt(x,z),b(new TurnL()),b(new TurnR()))
    //          ),b(
    //          new If(new Gt(y,z),b(new Move(null)),b(new TurnAround()))
    //          ))))))))));
    }
}