package pstgu.NmMap.application;

import java.util.*;

/**
 * Поисковый образ текста. Знает, где в тексте находятся искомые слова и умеет выдавать вырезку из
 * текста, в которой встречаются все искомые слова. Также умеет оценивать релевантность текста
 * запросу, т.е. степень "полезности" совпадения.
 * 
 */
public class TextSearchImage {
  // текст, в котором ищем
  String text;

  // слова, которые ищем
  List<String> words;

  /**
   * словарь, где каждому искомому слову сопоставлен его индекс в тексте. если искомого слова в
   * тексте нет, то и в словаре нет записи про это слово.
   */
  Map<String, Integer> index = new HashMap<String, Integer>();

  /**
   * Строит поисковый образ текста.
   * 
   * @param text текст, в котором ищем
   * @param searchWords слова, которые ищем
   */
  public TextSearchImage(String text, Collection<String> searchWords) {
    this.text = text;
    this.words = new ArrayList<String>(searchWords);

    // строим индекс
    fillIndex();
  }

  /**
   * Возвращает значение, показывающее, подходит ли текст под запрос, т.е. найдены ли в нём все
   * искомые слова.
   */
  public boolean isTextFitsQuery() {
    // ВРЕМЕННАЯ РЕАЛИЗАЦИЯ - ЧТОБЫ РАБОТАЛО (КОСТЫЛЬ)

    // Проверяем что в тексте встречаются все слова из массива words
    // Чтобы не учитывать регистр, приводим всё к верхнему регистру
    // return words.stream().allMatch(word ->
    // text.toUpperCase().contains(word.toUpperCase()));

    // ПРАВИЛЬНАЯ РЕАЛИЗАЦИЯ - раскомментировать, когда будет реализован fillIndex
    // если в индексе столько же записей, сколько и искомых слов, то значит для
    // каждого
    // искомого слова нашлось положение в тексте.
    return index.size() == words.size() && words.size() != 0;
  }

  /**
   * Оценка степени полезности текста для данного запроса. Чем больше, тем лучше. Поисковую выдачу
   * полезно сортировать по убыванию релевантности, чтобы в начале оказывались более полезные
   * совпадения. Если текст не удовлетворяет запросу - 0.
   * 
   * @return
   */
  public double getRelevance() {

    if (!isTextFitsQuery()) {
      return 0;
    }

    // если слово одно, оцениваем степень близости его к началу текста
    if (words.size() == 1)
      return (double) text.length()/index.values().toArray(new Integer[0])[0];

    int start = index.values().stream().min(Comparator.naturalOrder()).get();
    int end = index.values().stream().max(Comparator.naturalOrder()).get();

    // Оценивать релевантность предлагается исходя из следующей эвристики:
    // совпадение лучше, когда искомые слова находятся недалеко друг друга, а не на
    // разных концах
    // текста.

    // Посему:
    // нам нужно взять расстояние между первым и последним совпадением в тексте.
    // А потом разделить единицу на это число.
    // Тем самым получим, что для радом стоящих слов делитель будет не очень большой
    // и релевантность
    // ближе к 1,
    // а для далеко разнесённых слов наоборот - ближе к 0.
    // Только надо не забыть про случай, когда искомое слово всего одно...
    return 1. / (end - start);
  }

  /**
   * Возвращает вырезку из текста, которая содержит все искомые слова. Сами искомые слова
   * заключаются в < >. Если текст не удовлетворяет запросу, возвращает null
   * 
   * @param maxLength максимальная длина вырезки
   * @return строка, в которой искомые слова заключены в угловые скобки &lt;слово&gt;
   */
  public String getTextSnippet(int maxLength) {
    if (!isTextFitsQuery()) {
      return null;
    }
    //TODO не сделана подсветка искомых слов
    int start = index.values().stream().min(Comparator.naturalOrder()).get();
    int end = index.values().stream().max(Comparator.naturalOrder()).get();
    
    for(; start > 0; start--) {
      if(".?!".indexOf(text.charAt(start))!=-1) {
       start ++;
       break;
      }
    }
    
    for(; end<text.length(); end++ ) {
      if(".?!".indexOf(text.charAt(end))!=-1) {
        break;
      }
    }
    var result = text.substring(start, end);
    if(result.length()>maxLength) {
      result = result.substring(0,maxLength-6)+" <...>";
    }
    // Надо как-то построить...
    // Взять для начала текст между первым и последним совпадением. А если слишком
    // длинный, то
    // укорачивать....
    return result;
  }

  private void fillIndex() {
    String t1 = text.toLowerCase();
    for (var word : words) {
      word = word.toLowerCase();
      // int index1 = str.indexOf('l'); // 2
      if (t1.contains(word)) {
        index.put(word, t1.indexOf(word));
        // Здесь нужно для каждого слова найти его положение в тексте и положить эту
        // информацию
        // в словарь index - ключом выступает слово, а значением - индекс символа, с
        // которого оно
        // начинается.
      }
    }
  }

  ///// TEST /////
  public static void main(String[] args) {
    var q1 = new String[] {"зелёный", "дуб"};
    var q2 = new String[] {"Лукоморья", "Пушкин"};

    // Здесь должно быть isTextSatisfiesQuery=true, а также ненулевая релевантность
    // и вырезка текста
    String testText = "У! Лукоморья дуб. зелёный";
    var t = new TextSearchImage(testText, Arrays.asList(q1));
    System.out.println(testText);
    System.out.printf("isTextSatisfiesQuery=%b\tRelevance=%f\n", t.isTextFitsQuery(),
        t.getRelevance());
    System.out.println(t.getTextSnippet(100));
    System.out.println(t.getTextSnippet(15));

    // А здесь должно быть isTextSatisfiesQuery=false, так что релевантность 0 и
    // вырезка null
    t = new TextSearchImage(testText, Arrays.asList(q2));
    System.out.printf("isTextSatisfiesQuery=%b\tRelevance=%f\n", t.isTextFitsQuery(),
        t.getRelevance());
    System.out.println(t.getTextSnippet(100));

    t = new TextSearchImage(testText, Arrays.asList("дуб"));
    System.out.printf("isTextSatisfiesQuery=%b\tRelevance=%f\n", t.isTextFitsQuery(),
        t.getRelevance());
    System.out.println(t.getTextSnippet(100));

    t = new TextSearchImage(testText, new ArrayList<String>());
    System.out.printf("isTextSatisfiesQuery=%b\tRelevance=%f\n", t.isTextFitsQuery(),
        t.getRelevance());
    System.out.println(t.getTextSnippet(100));

  }
}
