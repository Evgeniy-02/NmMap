/**
 * ������ �������������.
 */
public class Human {
    private int id;
    private String fio;
    private String article;
        
    /**
     * ������ ����� ������ �������������
     * @param id ����
     * @param fio ��������� ������
     * @param article ����� ������
     */
    public Human(int id, String fio, String article) {
      this.id = id;
      this.fio = fio;
      this.article = article;
    }

    public int getId() {
      return id;
    }
    
    public String getFio() {
      return fio;
    }
    
    public String getArticle() {
      return article;
    }    
}