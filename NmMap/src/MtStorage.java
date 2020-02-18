public interface MtStorage {
    /**
     * ���������� ������������� �� �����
     * @param id ����
     * @return Human
     */
    Human getHuman(int id);
    
    /**
     * ���������� ����� �� ��������� ��� � ���������� take �������, ��������� ������ skip �������
     * @param fioStarts � ����� ���� ������ ���������� �������� ���
     * @param fioContains ����� ����� ������ ��������� �������� ���
     * @param skip ������� ������� �� ������ ������ ����������� ����������
     * @param take ������� ������� �������
     * @return ������ ������� ������� take
     */
    Human[] findHumansByFio(String fioStarts, String fioContains, int skip, int take);
    
    /**
     * ��������� �������������� ����� �������������
     * @param query ������
     * @param skip ������� ������� �� ������ ������ ����������� ����������
     * @param take ������� ������� �������
     * @return ������ ������� ������� take
     */
    Human[] findHumansFullText(String query, int skip, int take);    
}