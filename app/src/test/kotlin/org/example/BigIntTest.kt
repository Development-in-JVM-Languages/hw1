import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

// Helper function: assume that your BigInt implements BigIntInterface
private fun createBigInt(value: String): BigInt = BigInt(value)

class BigIntTest {

    @Test
    fun testAddition() {
        val a = createBigInt("999999999999999999")
        val b = createBigInt("1")
        val result = a + b
        assertEquals("1000000000000000000", result.toString())

        val result2 = a + 1
        assertEquals("1000000000000000000", result2.toString())
    }

    @Test
    fun testSubtraction() {
        val a = createBigInt("1000000000000000000")
        val b = createBigInt("1")
        val result = a - b
        assertEquals("999999999999999999", result.toString())

        val result2 = a - 1
        assertEquals("999999999999999999", result2.toString())
    }

    @Test
    fun testMultiplication() {
        val a = createBigInt("123456789")
        val b = createBigInt("987654321")
        val result = a * b
        assertEquals("121932631112635269", result.toString())

        val result2 = a * 2
        assertEquals("246913578", result2.toString())
    }

    @Test
    fun testDivision() {
        val a = createBigInt("100")
        val b = createBigInt("3")
        val result = a / b
        assertEquals("33", result.toString())

        val result2 = a / 3
        assertEquals("33", result2.toString())
    }

    @Test
    fun testModulo() {
        val a = createBigInt("100")
        val b = createBigInt("3")
        val result = a % b
        assertEquals("1", result.toString())

        val result2 = a % 7
        assertEquals("2", result2.toString())
    }

    @Test
    fun testPower() {
        val a = createBigInt("2")
        val result = a.pow(10)
        assertEquals("1024", result.toString())

        val result2 = a.pow(0)
        assertEquals("1", result2.toString())
    }

    @Test
    fun testAbsAndSign() {
        val a = createBigInt("-123")
        assertEquals(-1, a.sign())
        val absA = a.abs()
        assertEquals("123", absA.toString())

        val b = createBigInt("0")
        assertEquals(0, b.sign())

        val c = createBigInt("987")
        assertEquals(1, c.sign())
    }

    @Test
    fun testComparisonAndEquality() {
        val a = createBigInt("123456")
        val b = createBigInt("123456")
        val c = createBigInt("654321")

        assertTrue(a == b)
        assertEquals(a.hashCode(), b.hashCode())

        assertTrue(a < c)

        assertTrue(c > 100000)
    }

    @Test
    fun testInvalidInput() {
        assertThrows(IllegalArgumentException::class.java) {
            createBigInt("abc")
        }
    }

    @Test
    fun testDivisionByZero() {
        val a = createBigInt("100")
        assertThrows(ArithmeticException::class.java) {
            a / 0
        }
        assertThrows(ArithmeticException::class.java) {
            a / createBigInt("0")
        }
    }

    @Test
    fun testNormalization() {
        // If your implementation normalizes inputs (removing leading zeros),
        // then "0000123" should become "123"
        val a = createBigInt("0000123")
        assertEquals("123", a.toString(), "Leading zeros should be removed")
    }

    @Test
    fun testAdditionWithNegative() {
        val a = createBigInt("100")
        val b = createBigInt("-200")
        assertEquals("-100", (a + b).toString())

        val c = createBigInt("-100")
        val d = createBigInt("200")
        assertEquals("100", (c + d).toString())
    }

    @Test
    fun testSubtractionResultingNegative() {
        val a = createBigInt("100")
        val b = createBigInt("200")
        assertEquals("-100", (a - b).toString())
    }

    @Test
    fun testSubtractionResultZero() {
        val a = createBigInt("12345")
        val result = a - a
        assertEquals("0", result.toString())
        assertEquals(0, result.sign())
    }

    @Test
    fun testMultiplicationByZero() {
        val a = createBigInt("12345")
        val zero = createBigInt("0")
        assertEquals("0", (a * zero).toString())
        assertEquals("0", (zero * a).toString())
    }

    @Test
    fun testMultiplicationWithNegatives() {
        val a = createBigInt("100")
        val b = createBigInt("-20")
        assertEquals("-2000", (a * b).toString())

        val c = createBigInt("-100")
        val d = createBigInt("-20")
        assertEquals("2000", (c * d).toString())
    }

    @Test
    fun testDivisionWithNegatives() {
        val a = createBigInt("-100")
        val b = createBigInt("20")
        assertEquals("-5", (a / b).toString())

        val c = createBigInt("-100")
        val d = createBigInt("-20")
        assertEquals("5", (c / d).toString())

        val e = createBigInt("100")
        val f = createBigInt("-20")
        assertEquals("-5", (e / f).toString())
    }

    @Test
    fun testModuloWithNegatives() {
        // In Kotlin (and Java), the remainder has the same sign as the dividend.
        // For example, (-10) % 3 should equal -1 because: -10 = (-3)*3 + (-1)
        val a = createBigInt("-10")
        val b = createBigInt("3")
        assertEquals("-1", (a % b).toString())

        // Similarly, 10 % (-3) should equal 1 because: 10 = (-3)*(-3) + 1
        val c = createBigInt("10")
        val d = createBigInt("-3")
        assertEquals("1", (c % d).toString())
    }

    @Test
    fun testPowShort() {
        val a = createBigInt("2")
        val exp: Short = 10
        val result: BigInt = a.pow(exp)
        assertEquals("1024", result.toString())
    }

    @Test
    fun testPowByte() {
        val a = createBigInt("2")
        val exp: Byte = 10
        val result: BigInt = a.pow(exp)
        assertEquals("1024", result.toString())
    }

    @Test
    fun testPowBigInt() {
        val a = createBigInt("2")
        val exp = createBigInt("10")
        val result: BigInt = a.pow(exp)
        assertEquals("1024", result.toString())
    }

    @Test
    fun testExponentiationWithNegativeBase() {
        val a = createBigInt("-2")
        assertEquals("-8", a.pow(3).toString())
        assertEquals("4", a.pow(2).toString())
        assertEquals("-2", a.pow(1).toString())
    }

    @Test
    fun testExponentiationEdgeCases() {
        val a = createBigInt("5")
        assertEquals("5", a.pow(1).toString())
        assertEquals("1", a.pow(0).toString())
    }

    @Test
    fun testOperationsUsingByteAndShort() {
        val a = createBigInt("10")
        val byteVal: Byte = 5
        val shortVal: Short = 3

        assertEquals("15", (a + byteVal).toString())
        assertEquals("13", (a + shortVal).toString())

        assertEquals("5", (a - byteVal).toString())
        assertEquals("7", (a - shortVal).toString())

        assertEquals("50", (a * byteVal).toString())
        assertEquals("30", (a * shortVal).toString())

        assertEquals("2", (a / byteVal).toString())
        assertEquals("3", (a / shortVal).toString())

        assertEquals("0", (a % byteVal).toString())
        assertEquals("1", (a % shortVal).toString())
    }

    @Test
    fun testLargeNumberArithmetic() {
        val a = createBigInt("9".repeat(100))
        val one = createBigInt("1")
        val sum = a + one
        val expected = "1" + "0".repeat(100)
        assertEquals(expected, sum.toString())
    }
}
