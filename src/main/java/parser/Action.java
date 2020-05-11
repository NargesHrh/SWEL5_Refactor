package parser;

public class Action {
  private act action;
  //if action = shift : number is state
  //if action = reduce : number is number of rule
  private int number;

  public Action(act action, int number) {
    this.action = action;
    this.number = number;
  }

  public String toString() {
    switch (getAction()) {
      case accept:
        return "acc";
      case shift:
        return "s" + getNumber();
      case reduce:
        return "r" + getNumber();
    }
    return getAction().toString() + getNumber();
  }

  public act getAction() {
    return action;
  }
  public int getNumber() {
    return number;
  }


}

enum act {
  shift,
  reduce,
  accept
}
