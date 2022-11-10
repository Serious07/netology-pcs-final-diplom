import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> allEntry = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
        List<File> files = Files.walk(Paths.get(pdfsDir.getPath()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        for (File pdfFile : files) {
            getWordsFromPage(pdfFile);
        }
    }

    private void getWordsFromPage(File pdfFile) throws IOException {
        PdfDocument doc = new PdfDocument(new PdfReader(pdfFile));

        for (int pageNumber = 1; pageNumber < doc.getNumberOfPages(); pageNumber++) {
            PdfPage page = doc.getPage(pageNumber);
            String textFromPage = PdfTextExtractor.getTextFromPage(page);
            String[] pageWords = textFromPage.split("\\P{IsAlphabetic}+");

            pageWords = allWordsToLowerCase(pageWords).toArray(pageWords);

            Map<String, Integer> wordsFrequency = getWordFrequency(pageWords);

            for (String word : pageWords) {
                setPageEntryForWord(word, wordsFrequency, pageNumber, pdfFile);
            }
        }
    }

    private List<PageEntry> pagesEntryForWord;
    private List<PageEntry> sortedPageEntry;

    private void setPageEntryForWord(String word, Map<String, Integer> wordsFrequency, int pageNumber, File pdfFile) {
        if (!wordsFrequency.containsKey(word)) return;
        PageEntry pageEntry = new PageEntry(pdfFile.getName(), pageNumber, wordsFrequency.get(word));
        if (allEntry.containsValue(pageEntry)) return;

        if (allEntry.containsKey(word)) {
            pagesEntryForWord = allEntry.get(word);

            if (pagesEntryForWord.contains(pageEntry)) return;

            pagesEntryForWord.add(pageEntry);

            // Сортируем
            sortedPageEntry = pagesEntryForWord.
                    stream().
                    sorted().
                    collect(Collectors.toList());

            allEntry.put(word, sortedPageEntry);
        } else {
            pagesEntryForWord = new ArrayList<>();
            pagesEntryForWord.add(pageEntry);
            allEntry.put(word, pagesEntryForWord);
        }
    }

    private Map<String, Integer> getWordFrequency(String[] words) {
        Map<String, Integer> frequency = new HashMap<>();
        for (var word : words) {
            if (word.isEmpty()) {
                continue;
            }
            word = word.toLowerCase();
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }
        return frequency;
    }

    private List<String> allWordsToLowerCase(String[] words) {
        List<String> result = Arrays.stream(words).
                peek(word -> word.toLowerCase()).
                collect(Collectors.<String>toList());

        return result;
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        return allEntry.get(word);
    }
}
