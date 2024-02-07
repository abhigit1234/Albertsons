package BaseMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
//
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class demo {
	ExtentReports extent;
	ExtentSparkReporter spark;
	static WebDriver driver;
	static ExtentTest extenttest;
	static String testDate;
	static String repName;

	@BeforeTest
	public void initialiseBrowser(ITestContext context) {
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		extenttest = extent.createTest(context.getName());
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		String device = cap.getBrowserName() + " "
				+ cap.getBrowserVersion().substring(0, cap.getBrowserVersion().indexOf("."));
		String author = context.getCurrentXmlTest().getName();
		extenttest.assignAuthor(author);
		extenttest.assignDevice(device);
	}

	@AfterTest
	public void tearDown() {
		driver.close();
	}

	@BeforeSuite
	public void initialiseReports() {
		testDate = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
		repName = testDate + ".html";
		extent = new ExtentReports();
		spark = new ExtentSparkReporter(("ExtentReports//") + repName);
		extent.attachReporter(spark);
		extent.setSystemInfo("user country", System.getProperty("user.country"));
		extent.setSystemInfo("java version", System.getProperty("java.version"));
		extent.setSystemInfo("user name", System.getProperty("user.name"));
		extent.setSystemInfo("os name", System.getProperty("os.name"));
		extent.setSystemInfo("os version", System.getProperty("os.version"));
		spark.config().setDocumentTitle("opencart");
		spark.config().setReportName("final test run");
		spark.config().setTheme(Theme.DARK);
		spark.config().setTimeStampFormat("dd/MMM/yyyy");

	}

	@AfterSuite
	public void flushReports() throws Exception {
		extent.flush();
		Desktop.getDesktop().browse(new File(("ExtentReports//") + repName).toURI());

	}

	@BeforeMethod
	public void getStatus(Method m) {
		extenttest.assignCategory(m.getAnnotation(Test.class).groups());
	}

	@AfterMethod
	public void getGroupStatus(Method m, ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String path = capture(
					result.getTestContext().getName() + " " + result.getMethod().getMethodName() + ".png");
			extenttest.addScreenCaptureFromPath(path);
			extenttest.info(result.getThrowable());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			extenttest.pass(m.getName() + "is passed ");
		}
	}

	public String capture(String fileName) {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyyyyHHmmss");
		String date = ldt.format(dtf);
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File dest = new File(System.getProperty("user.dir") + date + fileName);
		try {
			FileUtils.copyFile(source, dest);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return dest.getAbsolutePath();

	}

	@Test(groups = { "smoke", "sanity" })
	public void test1() {

		driver.get("http://localhost/opencart/upload/admin/");
		driver.findElement(By.id("input-username")).sendKeys("admin");
		driver.findElement(By.id("input-password")).sendKeys("admin");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		System.out.println(driver.getTitle());
		assertEquals(driver.getTitle(), "Administration");
		driver.close();

	}

	@Test(groups = { "functional", "regression", "smoke" })
	public void test2() {
		driver = new ChromeDriver();
		driver.get("http://localhost/opencart/upload/admin/");
		System.out.println(driver.getTitle());
		assertEquals(driver.getTitle(), "Administration");

	}
}
