import java.util.ArrayList;

public class TestMtStorage implements MtStorage {

  Human[] data = new Human[] {
      new Human(1, "���� ��������", "�� ������"),
      new Human(2, "���� ��������", "�� ����������"),
      new Human(3, "����� ���������", "�� ������"),
      new Human(4, "��������� �������", "�� ������"),
      new Human(5, "��������� �����", "�� �������")
  };
  
  @Override
  public Human getHuman(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Human[] findHumansByFio(String fioStarts, String fioContains, int skip, int take) {
    // TODO Auto-generated method stub
    ArrayList<Human> result = new ArrayList<Human>();
    for(Human h: data)
    {
      if(h.getFio().startsWith(fioStarts))
      {
        result.add(h);
      }
    }
    
    return result.toArray(new Human[0]);   
  }

  @Override
  public Human[] findHumansFullText(String query, int skip, int take) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
