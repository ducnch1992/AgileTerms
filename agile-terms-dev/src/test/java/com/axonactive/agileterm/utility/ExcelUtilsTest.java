package com.axonactive.agileterm.utility;

import com.axonactive.agileterm.exception.InputValidation;
import com.axonactive.agileterm.service.dto.TermImportDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("unit-test")
@RequiredArgsConstructor
class ExcelUtilsTest {
    private final static String PATH = "./src/test/resources/";
    public static final String SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX = PATH + "AgileTerm_TermAndDefinition_Template.xlsx";
    public static final String SRC_TEST_RESOURCES_BLANK_DATA_XLSX = PATH + "BLANK_DATA.xlsx";
    public static final String SRC_TEST_RESOURCES_FILE_XLS_XLS = PATH + "file_xls.xls";
    public static final String SRC_TEST_RESOURCES_INVALID_DESCRIPTION_WITHOUT_TERM_XLSX = PATH + "INVALID_Description-Without-Term.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TYPE_XLSX = PATH + "INVALID_File-type.xlsx.dotx";
    public static final String SRC_TEST_RESOURCES_VALID_XLSX = PATH + "VALID.xlsx";

    public static final String SRC_TEST_RESOURCES_VALID_DUPLICATES_IN_FILE_XLSX = PATH + "VALID_Duplicates-in-file.xlsx";
    public static final String SRC_TEST_RESOURCES_VALID_STUPIC_INPUT_XLSX = PATH + "VALID_Stupic-Input.xlsx";
    public static final String SRC_TEST_RESOURCES_VALID_TERM_WITHOUT_DESCRIPTION_XLSX = PATH + "VALID_Term-Without-Description.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE = PATH + "AgileTerm_Topic_Template.xls";
    public static final String SRC_TEST_RESOURCES_INVALID_CUSTOM_FILE_TEMPLATE = PATH + "INVALID_File_Template.xlsx";
    public static final String SRC_TEST_RESOURCES_VALID_CUSTOM_FILE_TEMPLATE = PATH + "VALID_File_Template.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_BLANK_HEADER = PATH + "INVALID_File_TemPlate_Blank_Header.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_NUMERIC_HEADER_COL_1 = PATH + "INVALID_File_TemPlate_Numeric_Header_In_Col_1.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_NUMERIC_HEADER_COL_2 = PATH + "INVALID_File_TemPlate_Numeric_Header_In_Col_2.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_WRONG_HEADER_TEXT_COL_1 = PATH + "INVALID_File_TemPlate_Wrong_Header_Text_Col_1.xlsx";
    public static final String SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_WRONG_HEADER_TEXT_COL_2 = PATH + "INVALID_File_TemPlate_Wrong_Header_Text_Col_2.xlsx";
    private final FileTypeMap fileTypeMap = FileTypeMap.getDefaultFileTypeMap();


    MultipartFile getMultipartFile(String pathName, String originalName) throws IOException {
        File file = new File(pathName);
        FileInputStream fileInputStream = new FileInputStream(file);
        String fileType = fileTypeMap.getContentType(file);
        return new MockMultipartFile(originalName, file.getName(), fileType, fileInputStream);
    }

    Sheet getSheet(MultipartFile multipartFile) throws IOException {
        Workbook workbook = ExcelUtils.convertToWorkBook(multipartFile);
        return workbook.getSheetAt(ExcelUtils.FIRST_SHEET);
    }


    @Test
    void getFileExtension_ShouldReturnXLSX_whenInputXLSXFile() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX, "templateFile");
        assertEquals(ExcelUtils.XLSX, ExcelUtils.getFileExtension(multipartFile));

    }

    @Test
    void getFileExtension_ShouldReturnXLS_whenInputIsXLSFile() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_FILE_XLS_XLS, "file_xls.xls");
        assertEquals(ExcelUtils.XLS, ExcelUtils.getFileExtension(multipartFile));
    }

    @Test
    void convertToWorkBook_ShouldThrowAnInputValidationExceptionWithNotSupportedFile() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TYPE_XLSX, "INVALID_File-type.xlsx.dotx");
        try {
            ExcelUtils.convertToWorkBook(multipartFile);
        } catch (InputValidation e) {
            assertEquals(ExcelUtils.FILE_WRONG_FORMAT_EXCEPTION, e.getMessage());
        }
    }

    @Test
    void getDataFromARow_ShouldReturnAcceptanceCriteriaForTermString_WhenRowIsNotNull() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX, "AgileTerm_TermAndDefinition_Template.xlsx"));
        assertEquals("Acceptance Criteria", ExcelUtils
                .getDataFromARow(sheet
                        .getRow(1)).getTermString());
    }


    @Test
    void getDataFromARow_ShouldReturnNullForTermString_WhenRowIsNull() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_BLANK_DATA_XLSX, "BLANK_DATA.xlsx"));
        assertEquals("", ExcelUtils
                .getDataFromARow(sheet
                        .getRow(ExcelUtils
                                .ROW_STARTED_TO_COUNT)).getTermString());
    }

    @Test
    void getCellData_ShouldReturnAnEmptyString_WhenRowIsNull_CellIndexIs0() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_BLANK_DATA_XLSX, "BLANK_DATA.xlsx"));
        assertEquals("", ExcelUtils.getCellData(sheet.getRow(ExcelUtils.ROW_STARTED_TO_COUNT), 0));
    }

    @Test
    void getCellData_ShouldReturnAnEmptyString_WhenRowIsNull_CellIndexIs1() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_BLANK_DATA_XLSX, "BLANK_DATA.xlsx"));
        assertEquals("", ExcelUtils.getCellData(sheet.getRow(ExcelUtils.ROW_STARTED_TO_COUNT), 1));
    }

    @Test
    void getCellData_ShouldReturnAEmptyTermString_WhenReadingTheFirstRow_CellIndexIs0() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_INVALID_DESCRIPTION_WITHOUT_TERM_XLSX, "INVALID_Description-Without-Term.xlsx"));
        assertEquals("", ExcelUtils.getCellData(sheet.getRow(ExcelUtils.ROW_STARTED_TO_COUNT), 0));
    }

    @Test
    void getCellData_ShouldThrowAnException_WhenCellIsNull_CellIndexIs1() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE, "AgileTerm_Topic_Template.xls"));
        try {
            ExcelUtils.getCellData(sheet.getRow(ExcelUtils.ROW_STARTED_TO_COUNT), 1);
        } catch (InputValidation e) {
            assertEquals(ExcelUtils.FILE_TEMPLATE_NOT_SUPPORTED, e.getMessage());
        }
    }


    @Test
    void getCellData_ShouldReturnATermString_WhenCellIsFound_CellIndexIs0() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX, "AgileTerm_TermAndDefinition_Template.xlsx"));
        assertEquals("Acceptance criteria", ExcelUtils.getCellData(sheet.getRow(1), 0));
    }

    @Test
    void getCellData_ShouldReturnADescriptionString_WhenCellIsFound_CellIndexIs1() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX, "AgileTerm_TermAndDefinition_Template.xlsx"));
        assertEquals("A set of conditions that software must meet in order to be accepted by a customer or stakeholder", ExcelUtils.getCellData(sheet.getRow(1), 1));
    }

    @Test
    void getCellData_shouldReturnATermNumericType_whenCellIsFound_cellIndexIs3() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX, "AgileTerm_TermAndDefinition_Template.xlsx"));
        assertEquals("1000.0", ExcelUtils.getCellData(sheet.getRow(3), 0));
    }

    @Test
    void getCellData_shouldReturnADescriptionNumericType_whenCellIsFound_cellIndexIs4() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_AGILE_TERM_TERM_AND_DEFINITION_TEMPLATE_XLSX, "AgileTerm_TermAndDefinition_Template.xlsx"));
        assertEquals("10.0", ExcelUtils.getCellData(sheet.getRow(4), 1));
    }

    @Test
    void getCellData_shouldReturnABlankTermString_WhenRowIs14_CellIndexIs0() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_VALID_XLSX, "VALID.xlsx"));
        assertEquals("", ExcelUtils.getCellData(sheet.getRow(14), 0));
    }


    @Test
    void getCellData_ShouldReturnABlankTermString_WhenTermIsBlank() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_VALID_STUPIC_INPUT_XLSX, "VALID_Stupic-Input.xlsx"));
        assertEquals("", ExcelUtils.getCellData(sheet.getRow(8), ExcelUtils.TERM_TOPIC_COLUMN_INDEX));
    }


    @Test
    void getCellData_ShouldReturnABlankDescriptionString_WhenDescriptionIsBlank() throws IOException {
        Sheet sheet = getSheet(getMultipartFile(SRC_TEST_RESOURCES_VALID_STUPIC_INPUT_XLSX, "VALID_Stupic-Input.xlsx"));
        assertEquals("", ExcelUtils.getCellData(sheet.getRow(8), ExcelUtils.DESCRIPTION_COLUMN_INDEX));
    }


    @Test
    void getListOfTermAndDescriptionImportDto_ShouldReturnAListOf14Dto() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_VALID_XLSX, "VALID.xlsx");
        assertEquals(14, ExcelUtils.getListOfTermAndDescriptionImportDto(multipartFile).size());
    }

    /**
     * should rewrite the function again
     *
     * @throws IOException
     */
    @Test
    void getListOfTermAndDescriptionImportDto_ShouldReturnAListOf0Dto_WhenTheFileIsBlank() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_BLANK_DATA_XLSX, "VALID_File_Template.xlsx");
        assertEquals(0, ExcelUtils.getListOfTermAndDescriptionImportDto(multipartFile).size());
    }

    @Test
    void testIsValidTemplateFile_shouldReturnTrue_whenInputValidTemplateFile() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_VALID_CUSTOM_FILE_TEMPLATE, "VALID_File_Template.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertTrue(ExcelUtils.isSheetTemplateValid(firstSheet));

    }

    @Test
    void testIsValidTemplateFile_shouldThrowInputValidation_whenInputInValidTemplateFile() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_CUSTOM_FILE_TEMPLATE, "INVALID_File_Template.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        try {
            ExcelUtils.isSheetTemplateValid(firstSheet);
        } catch (InputValidation e) {
            assertEquals(ExcelUtils.FILE_TEMPLATE_NOT_SUPPORTED, e.getMessage());
        }
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnTrue_whenInputValidSheet() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_VALID_CUSTOM_FILE_TEMPLATE, "VALID_File_Template.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertTrue(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnFalse_whenInputInValidSheet() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_CUSTOM_FILE_TEMPLATE, "INVALID_File_Template.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertFalse(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }

    @Test
    void testIsThisCellValid_shouldReturnTrue_whenCellTypeIsStringOrNumeric() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_VALID_XLSX, "VALID.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertTrue(ExcelUtils.isCellValid(firstSheet.getRow(10), 0));
        assertTrue(ExcelUtils.isCellValid(firstSheet.getRow(10), 1));
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnFalse_whenInputTemplateWithBlankHeader() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_BLANK_HEADER, "INVALID_File_Template_Blank_Header.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertFalse(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnFalse_whenInputTemplateWithWrongTypeCellInFirstColumn() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_NUMERIC_HEADER_COL_1, "INVALID_File_Template_Numeric_Header_In_Col_1.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertFalse(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnFalse_whenInputTemplateWithWrongTypeCellInSecondColumn() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_NUMERIC_HEADER_COL_2, "INVALID_File_Template_Numeric_Header_In_Col_2.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertFalse(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnFalse_whenInputTemplateWithWrongTextInFirstColumn() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_WRONG_HEADER_TEXT_COL_1, "INVALID_File_Template_Wrong_Header_Text_Col_1.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertFalse(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }

    @Test
    void testIsHeaderTemplateValid_shouldReturnFalse_whenInputTemplateWithWrongTextInSecondColumn() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_INVALID_FILE_TEMPLATE_WRONG_HEADER_TEXT_COL_2, "INVALID_File_Template_Wrong_Header_Text_Col_2.xlsx");
        Sheet firstSheet = getSheet(multipartFile);
        assertFalse(ExcelUtils.isHeaderTemplateValid(firstSheet.getRow(0)));
    }


    @Test
    void getTermDefinitionMap_ShouldReturn9ForMapSize_WhenThereIs9Term() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_VALID_XLSX, "VALID.xlsx");
        List<TermImportDto> termImportDtoList = ExcelUtils.getListOfTermAndDescriptionImportDto(multipartFile);
        Map<String, List<String>> termImportDtoMap = ExcelUtils.getTermAndDefinitionMap(termImportDtoList);
        assertEquals(9, termImportDtoMap.size());
    }

    @Test
    void getTermDefinitionMap_ShouldReturn11ForMapSize_WhenDescriptionIsBlank() throws IOException {
        MultipartFile multipartFile = getMultipartFile(SRC_TEST_RESOURCES_VALID_TERM_WITHOUT_DESCRIPTION_XLSX, "VALID_Term-Without-Description.xlsx");
        List<TermImportDto> termImportDtoList = ExcelUtils.getListOfTermAndDescriptionImportDto(multipartFile);
        Map<String, List<String>> termImportDtoMap = ExcelUtils.getTermAndDefinitionMap(termImportDtoList);
        assertEquals(11, termImportDtoMap.size());
    }

}