
public class TestProgram {
  public static void main(String[] args) {    
    MtStorage d = new TestMtStorage();
    
    Human[] data = d.findHumansByFio("�", null, 0, 20);
    
    for (Human human: data) {
      System.out.println(human.getFio());
    }    
  }
}
