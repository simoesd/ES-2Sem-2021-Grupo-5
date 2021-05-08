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
    MaestroTest.class,
    CYCLO_methodTest.class,
    LOC_classTest.class,
    LOC_methodTest.class,
    MetricaTest.class})

public class AllTests {

}
