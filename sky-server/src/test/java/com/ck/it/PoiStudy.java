package com.ck.it;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Package: com.ck.it
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/9 13:29
 */

public class PoiStudy {

	/**
	 * 通过POI创建excel文件并且写入文件内容
	 */
	public static void write() throws IOException {
		/// 在内存中创建一个excel文件
		XSSFWorkbook excel = new XSSFWorkbook();
		/// 在excel中创建一个sheet页
		XSSFSheet sheet = excel.createSheet("info");
		/// 创建行,从0开始
		XSSFRow row = sheet.createRow(1);
		/// 创建单元格,并写入文件内容
		row.createCell(1).setCellValue("姓名");
		row.createCell(2).setCellValue("城市");

		/// 创建一个新行
		row = sheet.createRow(2);
		row.createCell(1).setCellValue("张三");
		row.createCell(2).setCellValue("北京");
		row = sheet.createRow(3);
		row.createCell(1).setCellValue("李四");
		row.createCell(2).setCellValue("南京");
		row = sheet.createRow(4);
		row.createCell(1).setCellValue("王五");
		row.createCell(2).setCellValue("广州");

		FileOutputStream fileOutputStream = new FileOutputStream(new File("/Volumes/study/02-java/Project/sky-take-out/poiStudy.xlsx"));
		excel.write(fileOutputStream);

		fileOutputStream.close();
		excel.close();
	}

	/**
	 * 通过poi读取excel文件中的内容
	 *
	 * @throws Exception
	 */
	public static void read() throws Exception {
		FileInputStream fileInputStream = new FileInputStream(new File("/Volumes/study/02-java/Project/sky-take-out/poiStudy.xlsx"));
		XSSFWorkbook excel = new XSSFWorkbook(fileInputStream);
		/// 读取excel文件中的第一个sheet页
		XSSFSheet sheet = excel.getSheetAt(0);
		/// 获取文件中最后一行有文字的行号
		int lastRowNum = sheet.getLastRowNum();
		for (int i = 1; i < lastRowNum; i++) {
			/// 获取每一行
			XSSFRow row = sheet.getRow(i);
			///
			String stringCellValue1 = row.getCell(1).getStringCellValue();
			String stringCellValue2 = row.getCell(2).getStringCellValue();
			System.out.println(stringCellValue1+" "+stringCellValue2);
		}
		excel.close();
		fileInputStream.close();
	}

	public static void main(String[] args) throws Exception {
//		write();
		read();
	}
}
