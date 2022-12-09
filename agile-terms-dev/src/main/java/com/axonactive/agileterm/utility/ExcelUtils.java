package com.axonactive.agileterm.utility;


import com.axonactive.agileterm.exception.InputValidation;
import com.axonactive.agileterm.service.dto.TermImportDto;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class ExcelUtils {

    public static final String XLSX = "xlsx";
    public static final String XLS = "xls";
    public static final int FIRST_SHEET = 0;
    public static final int FIRST_ROW = 0;
    public static final int ROW_STARTED_TO_COUNT = 1;
    public static final int TERM_TOPIC_COLUMN_INDEX = 0;
    public static final int DESCRIPTION_COLUMN_INDEX = 1;
    public static final int TERM_TOPIC_MAXIMUM_LENGTH = 50;
    public static final int DESCRIPTION_MAXIMUM_LENGTH = 1000;

    public static final int TERM_MINIMUM_LENGTH = 2;

    public static final String FILE_WRONG_FORMAT_EXCEPTION = "File not supported";

    public static final String FILE_TEMPLATE_NOT_SUPPORTED = "File template is not supported";

    private ExcelUtils() {
    }

    public static boolean isSheetTemplateValid(Sheet sheet) {
        Row firstRow = sheet.getRow(FIRST_ROW);
        if (isHeaderTemplateValid(firstRow)) {
            return true;
        } else {
            throw new InputValidation(FILE_TEMPLATE_NOT_SUPPORTED);
        }
    }

    public static boolean isHeaderTemplateValid(Row firstRow) {
        return null != firstRow
                && isCellTypeString(firstRow, TERM_TOPIC_COLUMN_INDEX)
                && isCellTypeString(firstRow, DESCRIPTION_COLUMN_INDEX)
                && firstRow.getCell(TERM_TOPIC_COLUMN_INDEX).getStringCellValue().trim().equalsIgnoreCase("Term")
                && firstRow.getCell(DESCRIPTION_COLUMN_INDEX).getStringCellValue().trim().equalsIgnoreCase("Description");
    }


    public static List<TermImportDto> getListOfTermAndDescriptionImportDto(MultipartFile multipartFile) throws IOException {

        List<TermImportDto> termImportDtoList = new ArrayList<>();

        Workbook workbook = convertToWorkBook(multipartFile);

        Sheet sheet = workbook.getSheetAt(FIRST_SHEET);

        isSheetTemplateValid(sheet);

        for (int i = ROW_STARTED_TO_COUNT; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isRowValid(row)) {
                TermImportDto termImportDto = getDataFromARow(row);
                termImportDto.setRowIndex(i + 1);
                termImportDtoList.add(termImportDto);
            }
        }
        return termImportDtoList;
    }


    public static boolean isRowValid(Row row) {
        return row != null &&
                (isCellValid(row, TERM_TOPIC_COLUMN_INDEX)
                        || isCellValid(row, DESCRIPTION_COLUMN_INDEX));
    }

    public static boolean isCellValid(Row row, int i) {
        return isCellTypeString(row, i) ||
                isCellTypeNumeric(row, i);
    }

    private static boolean isCellTypeNumeric(Row row, int i) {
        return row.getCell(i, Row.CREATE_NULL_AS_BLANK).getCellType() == Cell.CELL_TYPE_NUMERIC;
    }

    private static boolean isCellTypeString(Row row, int checkingCellIndex) {
        return row.getCell(checkingCellIndex, Row.CREATE_NULL_AS_BLANK).getCellType() == Cell.CELL_TYPE_STRING;
    }


    public static Map<String, List<String>> getTermAndDefinitionMap(List<TermImportDto> termImportDtoList) {
        return termImportDtoList.stream()
                .collect(Collectors.groupingBy(
                        TermImportDto::getTermString,
                        Collectors.mapping(
                                TermImportDto::getDescriptionString,
                                Collectors.toList()
                        )));
    }

    public static Workbook convertToWorkBook(MultipartFile multipartFile) throws IOException {
        String fileExtension = getFileExtension(multipartFile);
        if (XLSX.equalsIgnoreCase(fileExtension)) {
            return new XSSFWorkbook(multipartFile.getInputStream());
        } else if (XLS.equalsIgnoreCase(fileExtension)) {
            return new HSSFWorkbook(multipartFile.getInputStream());
        } else {
            throw new InputValidation(FILE_WRONG_FORMAT_EXCEPTION);
        }
    }

    public static String getFileExtension(MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    public static TermImportDto getDataFromARow(Row row) {
        TermImportDto termImportDto = new TermImportDto();
        termImportDto.setTermString(WordUtils.capitalize(getCellData(row, TERM_TOPIC_COLUMN_INDEX).toLowerCase()));
        termImportDto.setDescriptionString((getCellData(row, DESCRIPTION_COLUMN_INDEX)));
        return termImportDto;
    }


    public static String getCellData(Row row, int contentColumn) {
        Cell cell = row.getCell(contentColumn, Row.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
            return String.valueOf(cell.getNumericCellValue()).trim();
        return "";
    }


}
