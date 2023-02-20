import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {
    static List<File> headersList;
    String ADDRESS = "https://github.com/rvkR13/AllureReports/blob/main/file.txt";
    File download;
    String result;

    /**
     * Тест на загрузку файла txt и проверки содежимого
     */
    @Test
    @DisplayName("Загрузка файла .txt и его проверка")
    void downloadFileTest() throws Exception {
        step("Открыть страницу" + ADDRESS, () -> {
            open(ADDRESS);
        });
        step("загрузка файла в формате .txt  из репозитория", () -> {
            download = $("#raw-url").download();
        });
        try (InputStream is = new FileInputStream(download)) {
            result = new String(is.readAllBytes(), "UTF-8");
            step("в скаченном файле присутствует текст # AllureReports", () -> {
                assertThat(result).contains("# AllureReports");
            });
        }
    }
    /**
     * Тест на загрузку файла pdf и проверки содежимого
     */
    @Test
    void downloadPdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File file = $(byText("PDF download")).download();
        PDF pdf = new PDF(file);
        assertThat(pdf.title).contains("JUnit 5 User Guide");
        assertThat(pdf.author).contains("Stefan Bechtold, Sam Brannen, Johannes Link");
        assertThat(pdf.numberOfPages).isEqualTo(180);
        assertThat(pdf.text.contains("By default, it will only include test classes whose names either begin")).isTrue();
    }

    @Test
    void downloadXlsTest() throws Exception {
        open("https://github.com/rvkR13/AllureReports/blob/main/1с.xls");
        File download = $("#raw-url").download();
        XLS xls = new XLS(download);
        assertThat(download.getName()).isEqualTo("1_D1_81.xls");
        assertThat(xls.excel.getNumberOfSheets()).isEqualTo(1);
        //getRow номера ячеек в экселе слева(числа)
        //getCell лючи ячеек вверху(буквы)
        assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(5).getStringCellValue()).isEqualTo("Junior");
    }

    //пароль example
    @Test
    void openZip() throws Exception {
        ZipFile zipFile = new ZipFile("C:\\Users\\user\\IdeaProjects\\qaa-training-kondrakov-roman\\SelenideFiles\\src\\test\\resources\\example.zip");
        if (zipFile.isEncrypted())
            zipFile.setPassword("example".toCharArray());
        zipFile.extractAll("C:\\Users\\user\\Downloads\\result");

        assertThat(zipFile.isEncrypted()).isTrue();
        assertThat(zipFile.getFile().getName()).isEqualTo("example.zip");
        assertThat(zipFile.getFile().canRead()).isTrue();

    }
    /**
     *
     */
}
