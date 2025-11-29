package tests;

import configurator.BaseClass;
import org.testng.annotations.Test;

public class basic extends BaseClass{

    @Test
    public void sampleTest() {
        initializeDriver();
        System.out.println("Sample Test Executed");
    }
}
