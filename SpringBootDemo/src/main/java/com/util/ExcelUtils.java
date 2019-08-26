package com.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import lombok.extern.log4j.Log4j;

@Log4j
public class ExcelUtils {


	private static final String EXTENSION_XLS = ".xls";
	private static final String EXTENSION_XLSX = ".xlsx";
	public static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private ExcelUtils() {

	}

	/**
	 * 解析excel文件转化为实体对象
	 * 
	 * @param filePath
	 *            文件全路径
	 * @param obj
	 *            类对象
	 * @param headerMap
	 *            excel列名对应实体对象属性
	 * @return
	 * @throws Exception
	 */
	public static List<Object> transFileToList(String filePath, Class<?> obj,
			Map<String, String> headerMap) throws ExcelFormatException {
		Map<String, Object> methods = getMethods(obj, headerMap);

		Workbook book;
		Sheet sheet;
		try {
			book = getWorkbook(filePath);

		} catch (IOException e) {
			log.info("文件转化为Workbook失败", e);
			throw new ExcelFormatException();
		}
		if (book != null) {
			sheet = book.getSheetAt(0);
		} else {
			throw new ExcelFormatException();
		}
		return transWorkBookToList(sheet, obj, methods);
	}

	/**
	 * 根据文件流解析文件 转化为list对象
	 * 
	 * @param stream
	 * @param obj
	 * @param headerMap
	 * @param fileExtendType
	 *            文件扩展名
	 * @return
	 * @throws ExcelFormatException
	 */
	public static List<Object> transFileToList(InputStream stream,
			Class<?> obj, Map<String, String> headerMap, String fileExtendType)
			throws ExcelFormatException {

		Map<String, Object> methods = getMethods(obj, headerMap);
		Workbook book = null;
		try {
			if (fileExtendType.endsWith(EXTENSION_XLS)) {
				book = new HSSFWorkbook(stream);
			} else if (fileExtendType.endsWith(EXTENSION_XLSX)) {
				book = new XSSFWorkbook(stream);
			} else {
				throw new ExcelFormatException();
			}
		} catch (IOException e) {
			log.info("文件转化为Workbook失败", e);
			throw new ExcelFormatException();
		}
		Sheet sheet = book.getSheetAt(0);
		return transWorkBookToList(sheet, obj, methods);
	}

	/**
	 * 针对多sheet页解析
	 * 
	 * @param sheet
	 * @param obj
	 * @param headerMap
	 * @return
	 * @throws ExcelFormatException
	 */
	public static List<Object> transFileToList(Sheet sheet, Class<?> obj,
			Map<String, String> headerMap) throws ExcelFormatException {
		Map<String, Object> methods = getMethods(obj, headerMap);

		return transWorkBookToList(sheet, obj, methods);
	}

	/**
	 * 设置excel列名对应的对象属性map集合
	 * 
	 * @param obj
	 * @param headerMap
	 * @return
	 */
	private static Map<String, Object> getMethods(Class<?> obj,
			Map<String, String> headerMap) {
		Map<String, Object> methods = new HashMap<>();
		Method m[] = obj.getMethods();
		for (Map.Entry<String, String> entry : headerMap.entrySet()) {
			for (int i = 0; i < m.length; i++) {
				Method method = m[i];
				String methodName = method.getName().toUpperCase();
				if (methodName.startsWith("SET")
						&& StringUtils.equals(methodName.substring(3), entry
								.getKey().toUpperCase())) {
					methods.put(entry.getValue().toUpperCase(), method);
					break;
				}
			}
		}
		return methods;

	}

	/**
	 * 解析workbook
	 * 
	 * @param book
	 * @param obj
	 * @param methods
	 * @return
	 * @throws ExcelFormatException
	 */
	public static List<Object> transWorkBookToList(Sheet sheet, Class<?> obj,
			Map<String, Object> methods) throws ExcelFormatException {

		List<Object> list = new ArrayList<>();
		if (sheet != null) {
			int sheetRows = sheet.getLastRowNum() + 1;
			Row headerRow = sheet.getRow(0);

			for (int i = 1; i < sheetRows; i++) {
				Row currentRow = sheet.getRow(i);
				Object o;
				try {
					o = obj.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					throw new ExcelFormatException();
				}

				for (int j = 0; j < currentRow.getLastCellNum(); j++) {
					Cell cell = currentRow.getCell(j);
					String headerTitle = headerRow.getCell(j)
							.getStringCellValue();
					Object r = null;
					String content = getCellValue(cell, false);
					Method setMethod = (Method) methods.get(headerTitle
							.toUpperCase());
					if (setMethod != null) {
						Class<?>[] parameterTypes = setMethod
								.getParameterTypes();
						for (Class<?> c : parameterTypes) {
							if (c == Integer.class || c == int.class) {
								if (!content.isEmpty()) {
									try {
										r = Integer.parseInt(content);
									} catch (NumberFormatException e) {
										log.info("文件内容数字格式错误");
									}
								}

							} else if (c == Date.class) {
								if (!content.isEmpty())
									try {
										r = SDF.parse(content);
									} catch (ParseException e) {
										log.info("文件内容日期格式错误");
									}
							} else if (c == BigDecimal.class) {
								try {
									r = BigDecimal.valueOf(Double
											.valueOf(content));
								} catch (NumberFormatException e) {
									log.info("文件内容数字格式错误");
								}
							} else {
								r = getCellValue(cell, true);
							}
						}
					}
					if (r != null) {
						try {
							setMethod.invoke(o, r);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							log.info("文件内容与实体对象属性不匹配");
						}
					}
				}
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * 取单元格的值
	 * 
	 * @param cell
	 *            单元格对象
	 * @param treatAsStr
	 *            为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
	 * @return
	 */
	public static String getCellValue(Cell cell, boolean treatAsStr) {
		if (cell == null) {
			return "";
		}
		if (treatAsStr) {
			// 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
			// 加上下面这句，临时把它当做文本来读取
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				return SDF.format(date);
			} else {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			}

		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}

	/***
	 * 取得Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类) xls:HSSFWorkbook
	 * xlsx：XSSFWorkbook
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbook(String filePath) throws IOException {
		Workbook workbook = null;
		try (InputStream is = new FileInputStream(filePath)) {
			if (filePath.endsWith(EXTENSION_XLS)) {
				workbook = new HSSFWorkbook(is);
			} else if (filePath.endsWith(EXTENSION_XLSX)) {
				workbook = new XSSFWorkbook(is);
			}
		}
		return workbook;
	}

	
	
}
