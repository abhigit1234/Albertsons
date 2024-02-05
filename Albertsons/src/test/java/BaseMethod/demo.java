package BaseMethod;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class demo {
	@Test
	public void test() {
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost/opencart/upload/admin/");
		driver.findElement(By.id("input-username")).sendKeys("admin");
		driver.findElement(By.id("input-password")).sendKeys("admin");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		System.out.println(driver.getTitle());
		assertEquals(driver.getTitle(), "Administration");
		driver.close();
	}
}
