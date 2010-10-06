package de.java2html.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Markus Gebhard
 */
public abstract class HorizontalAlignment {
  private final static Map byName = new HashMap();
  private final static List all = new ArrayList();

  public final static HorizontalAlignment LEFT = new HorizontalAlignment("left") {
    public void accept(IHorizontalAlignmentVisitor visitor) {
      visitor.visitLeftAlignment(this);
    }
  };
  public final static HorizontalAlignment CENTER = new HorizontalAlignment("center") {
    public void accept(IHorizontalAlignmentVisitor visitor) {
      visitor.visitCenterAlignment(this);
    }
  };
  public final static HorizontalAlignment RIGHT = new HorizontalAlignment("right") {
    public void accept(IHorizontalAlignmentVisitor visitor) {
      visitor.visitRightAlignment(this);
    }
  };
  
  public static HorizontalAlignment getByName(String name) {
    return (HorizontalAlignment) byName.get(name);
  }

  public static HorizontalAlignment[] getAll() {
    return (HorizontalAlignment[]) all.toArray(new HorizontalAlignment[all.size()]);
  }
  
  private String name;

  public HorizontalAlignment(String name) {
    this.name = name;
    byName.put(name, this);
    all.add(this);
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return "HorizontalAlignment{" + getName() + "}";
  }

  public abstract void accept(IHorizontalAlignmentVisitor visitor);

}