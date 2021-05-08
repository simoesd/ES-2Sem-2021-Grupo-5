package unitTests;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({ConditionTest.class,
    RuleTest.class,
    RuleFileManagerTest.class,
    HelperMethodsTest.class,
    ExcelReaderTest.class,
    LineTest.class,
    MaestroTest.class})

public class AllTests {

}
