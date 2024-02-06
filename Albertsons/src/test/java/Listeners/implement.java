package Listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

import BaseMethod.demo;

public class implement extends demo implements ITestListener {

	@Override
	public void onTestFailure(ITestResult result) {
		capture(result.getTestContext().getName()+" "+result.getMethod().getMethodName()+".png");
	}
	
}
