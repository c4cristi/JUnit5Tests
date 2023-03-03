package com.codemonkey.tdd.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD) this is the default value anyway
@DisplayName("When running MathUtilsTest")
class MathUtilsTest {

    public static final String MATH = "Math";
    private static final String GEOMETRY = "Geometry";
    /*in this case it is ok to have state outside the @Test methods .
             This state is setup in the @BeforeEach method and not in any of the test methods
            which i have no guarantee in what order get executed.*/
    MathUtils mathUtils;
    TestInfo testInfo;
    TestReporter testReporter;

    @BeforeAll
    static void beforeAllInit() {
        System.out.println("This needs to run before all");
    }

    @BeforeEach
    void init(TestInfo testInfo, TestReporter testReporter) {
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        //System.out.println("Running " + testInfo.getDisplayName() + " wtih tags " + testInfo.getTags());
        this.testReporter.publishEntry("Running " + testInfo.getDisplayName() + " wtih tags " + testInfo.getTags());
        mathUtils = new MathUtils();
    }

    @AfterEach
    void cleanup() {
        System.out.println("Cleaning up ...");
    }

    @Test
    @DisplayName("Testing add method")
//shown
    void testAdd() {

        int expected = 3;
        int actual = mathUtils.add(2, 1);
        assertEquals(expected, actual, "The add method should add two numbers");
    }

    @Test
    @Tag(GEOMETRY)
    void testComputeCircleRadius() {
        assertEquals(314.1592653589793, mathUtils.computeCircleArea(10), "Should return right circle area");
    }


    @Test
    @Tag(MATH)
    @EnabledOnOs(OS.WINDOWS)
        //redundant
    void testDivide() {
        boolean isServerUp = false;
        assumeTrue(isServerUp);//redundant
        assertThrows(ArithmeticException.class, () -> mathUtils.divide(1, 0), "Divide by 0 should throw");
    }

    @Test
    @Disabled
    @DisplayName("TDD methods . Should not run ")
    void testDisabled() {
        fail("This test should be disabled");
    }


    @Test
    @DisplayName("multiply method")
    @Tag(MATH)
    void testMultiply() {

        assertAll(() -> assertEquals(4, mathUtils.multiply(2, 2)),
                () -> assertEquals(0, mathUtils.multiply(2, 0)),
                () -> assertEquals(-2, mathUtils.multiply(2, -1))
        );
    }


    @Nested
    @DisplayName("add method")
    @Tag(MATH)
    class AddTest {
        @Test
        @DisplayName("when adding two positive numbers")
        void testAddPositive() {
            assertEquals(2, mathUtils.add(1, 1), "should return the right sum");
        }

        @Test
        @DisplayName("when adding two negative numbers")
        void testAddNegative() {
            int expected = -2;
            int actual = mathUtils.add(-1, -1);
            //we can use a supplier for assert messages to have lazy compute of these messages .
            //will only compute message IF the assert fails, so should be used when meessage build process is expensive
            assertEquals(expected, actual, () -> "should return sum " + expected + " but returns " + actual);
        }
    }
}