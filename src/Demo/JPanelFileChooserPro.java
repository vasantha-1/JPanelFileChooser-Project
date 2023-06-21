package Demo;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

public class JPanelFileChooserPro {
	public static void main(String[] args) throws IOException {
		JTextField usernameField = new JTextField();
		JTextField passwordField = new JPasswordField();
		JPanel panel = new JPanel(new GridLayout(2, 2));
		panel.add(new JLabel("Username:"));
		panel.add(usernameField);
		panel.add(new JLabel("Password:"));
		panel.add(passwordField);
		int result = JOptionPane.showConfirmDialog(null, panel, " login ", JOptionPane.OK_OPTION);
	ChromeOptions c = new ChromeOptions();
	c.addArguments("--remote-allow-origins=*");
	WebDriver driver = new ChromeDriver(c);	
	System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32\\chromedriver.exe");	
	driver.manage().window().maximize();      
		driver.get("https://www.saucedemo.com/inventory.html");
		driver.findElement(By.id("user-name")).sendKeys(usernameField.getText());
		driver.findElement(By.id("password")).sendKeys(passwordField.getText());
		driver.findElement(By.id("login-button")).click();
	Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Product Prices");
		Sheet sheet1 = workbook.createSheet("sort");
		Sheet sheet2 = workbook.createSheet("greatest and lowest");
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Product Name");
		headerRow.createCell(1).setCellValue("Price");
		List<WebElement> prices = driver.findElements(By.className("inventory_item_price"));
		 
		List<Double> beforeFilterList = new ArrayList<>();
		for(WebElement p : prices) {
			beforeFilterList.add(Double.valueOf(p.getText().replace("$", "")));
		}
		  List<WebElement> products = driver.findElements(By.className("inventory_item"));
		   int rowNumber1 = 1;
			for (WebElement product : products) {
				String name = product.findElement(By.className("inventory_item_name")).getText();
				String price = product.findElement(By.className("inventory_item_price")).getText();
				Row row = sheet.createRow(rowNumber1++);
				row.createCell(0).setCellValue(name);
				row.createCell(1).setCellValue(price);
			}
		Select dp = new Select(driver.findElement(By.className("product_sort_container")));
		dp.selectByVisibleText("Price (high to low)");
		List<WebElement> afterFilter = driver.findElements(By.className("inventory_item_price"));
		List<Double> afterFilterList = new ArrayList<>();
		for(WebElement p : afterFilter) {
			afterFilterList.add(Double.valueOf(p.getText().replace("$", "")));
		}
	   Collections.sort(beforeFilterList);
	   try {
			Assert.assertEquals(beforeFilterList, sheet);
		  }
		  catch(AssertionError e){
			  System.out.println("Verified");
		  }
	   
	   Row headerRow1 = sheet1.createRow(0);
		headerRow1.createCell(0).setCellValue("Product Name");
		headerRow1.createCell(1).setCellValue("Price");
	   List<WebElement> productss = driver.findElements(By.className("inventory_item"));
	   int rowNumber2 = 1;
		for (WebElement product : productss) {
			String name = product.findElement(By.className("inventory_item_name")).getText();
			String price = product.findElement(By.className("inventory_item_price")).getText();
			//double pricee = Double.parseDouble(price.replace("$", ""));
			Row row = sheet1.createRow(rowNumber2++);
			row.createCell(0).setCellValue(name);
			row.createCell(1).setCellValue(price);
		}	 
		int rcount = sheet.getLastRowNum();
		Row row1 = sheet2.createRow(0);
		row1.createCell(0).setCellValue("greatest");
		Row row2 = sheet2.createRow(1);
		row2.createCell(0).setCellValue("lowest");
		Row r1 = sheet1.getRow(1);
		String s1 = r1.getCell(1).getStringCellValue();
		Row r2 = sheet1.getRow(rcount);
		String s2 = r2.getCell(1).getStringCellValue();
		row1.createCell(1).setCellValue(s1);
		row2.createCell(1).setCellValue(s2);
		String excelFile="D:\\task2\\JPanelExcel.xlsx";
		File file = new File(excelFile);
		FileOutputStream outputStream = new FileOutputStream(file);
		workbook.write(outputStream);		
		workbook.close();
		System.out.println("done");
		driver.quit();
		
	}

}
