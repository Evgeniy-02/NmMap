package pstgu.NmMap.testJSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Пример конвертации предоставленных JSON-данных в формат, эквивалентный классу
 * Human.
 */
public class TestConverter {
	/**
	 * Выполняет конвертацию.
	 * 
	 * @param datareader поток для чтения входных данных
	 * @return сконвертированные данные в формате JSON
	 * @throws IOException если что-то с чтением пошло не так
	 */
	public ObjectNode convert(InputStream datareader) throws IOException {
		// Чтение данных в дерево data
		var rdr = new ObjectMapper();
		JsonNode data = rdr.readTree(datareader);

		// Создаём пустое дерево result
		var wrtr = new ObjectMapper();
		ObjectNode result = wrtr.createObjectNode();

		// Копируем ключ записи
		result.set("id", data.get("Номер"));

		// @Заголовок = Канонизация.Чин_святости + ФИО + Сан_ЦеркСлужение
		// Пробуем читать все необходимые поля и собираем из них итоговый текст
		String articleTitle = data.get("ФИО").asText();
		JsonNode san = data.get("Сан_ЦеркСлужение");
		JsonNode sainthood = data.at("/Канонизация/Чин_святости");

		if (san != null) {
			articleTitle += ", " + san.asText();
		}

		if (sainthood != null) {
			articleTitle = sainthood.asText() + " " + articleTitle;
		}

		// result.set("fio", result.textNode(article_title));
		result.put("fio", articleTitle.strip());

		// TODO Написать код конвертации остальных полей

		String biographyFacts = "";
		JsonNode events = data.withArray("События");
		for (JsonNode event : events) {
			var date = event.get("Датировка");
			var text = event.get("Текст");
			
			var eventStr = Optional.ofNullable(date).map(t -> t.asText() + " — ").orElse("") 
			    + text.asText() + "\n";

			/*String eventStr = Optional.ofNullable(day).map(n -> month.asText()).map(n -> year.asText())
					.map(n -> day.asText() + "." + month.asText() + "." + year.asText() + " — ")
					.orElse(Optional.ofNullable(date).map(n -> n.asText() + " — ").orElse("? — "))
					+ Optional.ofNullable(text).map(t -> t.asText()).orElse("") + "\n";
            */
			biographyFacts += eventStr;
		}

		String comment = Optional.ofNullable(data.get("Комментарий")).map(JsonNode::asText).orElse("") + "\n";

		String bibliography = "**Библиография:\n";
		JsonNode list = data.withArray("Библиография");
		for (JsonNode text : list) {
			JsonNode name = text.get("Название");
			JsonNode type = text.get("Тип");
			String document = text.get("NUM").asText() + ". \"" + name.asText() + "\" ("
					+ Optional.ofNullable(type).map(JsonNode::asText).orElse("документ") + ")\n";
			bibliography += document;
		}

		result.put("article", biographyFacts + comment + bibliography);
//		System.out.println(biographyFacts+comment+bibliography);

		return result;

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// Создаём конвертер - по совместительству это наш текущий класс
		TestConverter converter = new TestConverter();
		ObjectNode result;

		// Для каждого файла в каталоге открываем поток чтения жизнеописания и передаём
		// конвертеру.
		// Блок try автоматически закроет файл при окончании работы
		File inputFiles = new File("resources/input/");
		for (File inputFile : inputFiles.listFiles()) {
			try (FileInputStream inp = new FileInputStream(inputFile)) {
				result = converter.convert(inp);

				// Выводим на экран получившийся в результате конвертации JSON
				System.out.println(result);

				// Выводим результат конвертирования в файлы
				TestJackson.writeToFile("resources/output/" + inputFile.getName(), result.toPrettyString());
			}
		}

//		System.out.println(result.toPrettyString());
	}

}
