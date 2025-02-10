class BigInt(value: String) : BigIntInterface {

    private var sign: Int
    private var digits: MutableList<Int>

    init {
        require(value.isNotEmpty()) { "Input string cannot be empty" }
        var s = value
        var sgn = 1
        if (s[0] == '-') {
            sgn = -1
            s = s.substring(1)
        }
        require(s.isNotEmpty() && s.all { it.isDigit() }) { "Invalid input: $value" }
        s = s.trimStart('0')
        if (s.isEmpty()) {
            s = "0"
            sgn = 0
        }
        sign = if (s == "0") 0 else sgn
        digits = s.map { it - '0' }.reversed().toMutableList()
    }

    private constructor(sign: Int, digits: MutableList<Int>, normalized: Boolean) : this("0") {
        this.digits = digits
        this.sign = if (digits.size == 1 && digits[0] == 0) 0 else sign
    }

    /**
     * Adds two numbers represented as reversed digit lists.
     */
    private fun addAbs(a: List<Int>, b: List<Int>): MutableList<Int> {
        val maxLen = maxOf(a.size, b.size)
        val result = mutableListOf<Int>()
        var carry = 0
        for (i in 0 until maxLen) {
            val d1 = a.getOrElse(i) { 0 }
            val d2 = b.getOrElse(i) { 0 }
            val sum = d1 + d2 + carry
            result.add(sum % 10)
            carry = sum / 10
        }
        if (carry > 0) result.add(carry)
        return result
    }

    /**
     * Subtracts b from a (a ≥ b) where a and b are represented as reversed digit lists.
     */
    private fun subtractAbs(a: List<Int>, b: List<Int>): MutableList<Int> {
        val result = mutableListOf<Int>()
        var borrow = 0
        for (i in a.indices) {
            val d1 = a[i]
            val d2 = b.getOrElse(i) { 0 }
            var diff = d1 - d2 - borrow
            if (diff < 0) {
                diff += 10
                borrow = 1
            } else {
                borrow = 0
            }
            result.add(diff)
        }
        while (result.size > 1 && result.last() == 0) {
            result.removeAt(result.size - 1)
        }
        return result
    }

    /**
     * Multiplies two numbers represented as reversed digit lists.
     */
    private fun multiplyAbs(a: List<Int>, b: List<Int>): MutableList<Int> {
        val result = MutableList(a.size + b.size) { 0 }
        for (i in a.indices) {
            var carry = 0
            for (j in b.indices) {
                val product = a[i] * b[j] + result[i + j] + carry
                result[i + j] = product % 10
                carry = product / 10
            }
            var pos = i + b.size
            while (carry > 0) {
                val sum = result[pos] + carry
                result[pos] = sum % 10
                carry = sum / 10
                pos++
            }
        }
        while (result.size > 1 && result.last() == 0) {
            result.removeAt(result.size - 1)
        }
        return result
    }

    /**
     * Compares two reversed digit lists representing absolute values.
     * Returns a negative number if a < b, 0 if a = b, and positive if a > b.
     */
    private fun compareAbsLists(a: List<Int>, b: List<Int>): Int {
        if (a.size != b.size) return a.size.compareTo(b.size)
        for (i in a.size - 1 downTo 0) {
            if (a[i] != b[i]) return a[i].compareTo(b[i])
        }
        return 0
    }

    private fun isZero() = (digits.size == 1 && digits[0] == 0)

    /**
     * Converts the reversed digit list to a forward-order list.
     */
    private fun toForward(digits: List<Int>): List<Int> = digits.reversed()

    /**
     * Removes any leading zeros from a forward-order list.
     */
    private fun removeLeadingZerosForward(list: MutableList<Int>) {
        while (list.size > 1 && list.first() == 0) {
            list.removeAt(0)
        }
    }

    /**
     * Compares two numbers represented in forward order.
     */
    private fun compareForward(a: List<Int>, b: List<Int>): Int {
        if (a.size != b.size) return a.size.compareTo(b.size)
        for (i in a.indices) {
            if (a[i] != b[i]) return a[i].compareTo(b[i])
        }
        return 0
    }

    /**
     * Multiplies a number in forward order by a single digit.
     */
    private fun multiplyByDigitForward(num: List<Int>, d: Int): List<Int> {
        var carry = 0
        val result = mutableListOf<Int>()
        for (i in num.indices.reversed()) {
            val prod = num[i] * d + carry
            result.add(prod % 10)
            carry = prod / 10
        }
        while (carry > 0) {
            result.add(carry % 10)
            carry /= 10
        }
        result.reverse()
        return result
    }

    /**
     * Subtracts b from a, where a and b are in forward order and a ≥ b.
     */
    private fun subtractForward(a: List<Int>, b: List<Int>): List<Int> {
        val aRev = a.reversed()
        val bRev = b.reversed()
        val diffRev = subtractAbs(aRev, bRev)
        val diff = diffRev.reversed().toMutableList()
        while (diff.size > 1 && diff.first() == 0) diff.removeAt(0)
        return diff
    }

    /**
     * Performs long division on the absolute values (both dividend and divisor in forward order).
     * Returns a Pair(quotient, remainder), both as forward-order lists.
     */
    private fun divmodAbs(dividendF: List<Int>, divisorF: List<Int>): Pair<MutableList<Int>, MutableList<Int>> {
        require(!(divisorF.size == 1 && divisorF[0] == 0)) { "Division by zero" }
        val quotient = mutableListOf<Int>()
        val remainder = mutableListOf<Int>()
        for (digit in dividendF) {
            remainder.add(digit)
            removeLeadingZerosForward(remainder)
            var qDigit = 0
            for (candidate in 0..9) {
                val product = multiplyByDigitForward(divisorF, candidate)
                if (compareForward(product, remainder) > 0) break
                qDigit = candidate
            }
            quotient.add(qDigit)
            val subtrahend = multiplyByDigitForward(divisorF, qDigit)
            val newRemainder =
                if (subtrahend.all { it == 0 }) remainder.toMutableList() else subtractForward(remainder, subtrahend)
            remainder.clear()
            remainder.addAll(newRemainder)
        }
        removeLeadingZerosForward(quotient)
        if (quotient.isEmpty()) quotient.add(0)
        if (remainder.isEmpty()) remainder.add(0)
        return Pair(quotient, remainder)
    }


    override operator fun plus(other: BigIntInterface): BigIntInterface {
        other as BigInt
        if (this.isZero()) return other
        if (other.isZero()) return this
        return if (this.sign == other.sign) {
            val sumDigits = addAbs(this.digits, other.digits)
            BigInt(this.sign, sumDigits, true)
        } else {
            val cmp = compareAbsLists(this.digits, other.digits)
            when {
                cmp == 0 -> BigInt("0")
                cmp > 0 -> {
                    val diffDigits = subtractAbs(this.digits, other.digits)
                    BigInt(this.sign, diffDigits, true)
                }

                else -> {
                    val diffDigits = subtractAbs(other.digits, this.digits)
                    BigInt(other.sign, diffDigits, true)
                }
            }
        }
    }

    override operator fun plus(other: Int): BigIntInterface = this + BigInt(other.toString())
    override operator fun plus(other: Short): BigIntInterface = this + BigInt(other.toString())
    override operator fun plus(other: Byte): BigIntInterface = this + BigInt(other.toString())

    override operator fun minus(other: BigIntInterface): BigIntInterface {
        return this + (other as BigInt).unaryMinus()
    }

    override operator fun minus(other: Int): BigIntInterface = this - BigInt(other.toString())
    override operator fun minus(other: Short): BigIntInterface = this - BigInt(other.toString())
    override operator fun minus(other: Byte): BigIntInterface = this - BigInt(other.toString())

    override operator fun times(other: BigIntInterface): BigIntInterface {
        other as BigInt
        val prodDigits = multiplyAbs(this.digits, other.digits)
        val prodSign = if (this.isZero() || other.isZero()) 0 else if (this.sign == other.sign) 1 else -1
        return BigInt(prodSign, prodDigits, true)
    }

    override operator fun times(other: Int): BigIntInterface = this * BigInt(other.toString())
    override operator fun times(other: Short): BigIntInterface = this * BigInt(other.toString())
    override operator fun times(other: Byte): BigIntInterface = this * BigInt(other.toString())

    override operator fun div(other: BigIntInterface): BigIntInterface {
        other as BigInt
        if (other.isZero()) throw ArithmeticException("Division by zero")
        val dividendF = toForward(this.digits)
        val divisorF = toForward(other.digits)
        val (quotientF, _) = divmodAbs(dividendF, divisorF)
        val quotientRev = quotientF.reversed().toMutableList()
        val resultSign = if (this.isZero() || other.isZero()) 0 else if (this.sign == other.sign) 1 else -1
        val res = BigInt(resultSign, quotientRev, true)
        if (res.toString() == "0") res.sign = 0
        return res
    }

    override operator fun div(other: Int): BigIntInterface = this / BigInt(other.toString())
    override operator fun div(other: Short): BigIntInterface = this / BigInt(other.toString())
    override operator fun div(other: Byte): BigIntInterface = this / BigInt(other.toString())

    override operator fun rem(other: BigIntInterface): BigIntInterface {
        other as BigInt
        require(!other.isZero()) { "Division by zero" }
        val dividendF = toForward(this.digits)
        val divisorF = toForward(other.digits)
        val (_, remainderF) = divmodAbs(dividendF, divisorF)
        val remainderRev = remainderF.reversed().toMutableList()
        return BigInt(this.sign, remainderRev, true)
    }

    override operator fun rem(other: Int): BigIntInterface = this % BigInt(other.toString())
    override operator fun rem(other: Short): BigIntInterface = this % BigInt(other.toString())
    override operator fun rem(other: Byte): BigIntInterface = this % BigInt(other.toString())

    override fun pow(exp: BigIntInterface): BigIntInterface {
        exp as BigInt
        val expInt = exp.toString().toIntOrNull() ?: throw IllegalArgumentException("Exponent too large")
        return this.pow(expInt)
    }

    override fun pow(exp: Int): BigIntInterface {
        require(exp >= 0) { "Negative exponent not supported" }
        var result: BigIntInterface = BigInt("1")
        var base: BigIntInterface = this
        var e = exp
        while (e > 0) {
            if ((e and 1) == 1) {
                result = result * base
            }
            base = base * base
            e = e shr 1
        }
        return result
    }

    override fun pow(exp: Short): BigIntInterface = this.pow(exp.toInt())
    override fun pow(exp: Byte): BigIntInterface = this.pow(exp.toInt())

    override fun sign(): Int = this.sign

    override fun abs(): BigIntInterface = if (this.sign >= 0) this else this.unaryMinus()

    override fun toString(): String {
        val numberStr = digits.reversed().joinToString(separator = "") { it.toString() }
        return if (sign < 0) "-$numberStr" else numberStr
    }

    /**
     * Unary minus operator.
     */
    operator fun unaryMinus(): BigInt {
        if (this.isZero()) return this
        return BigInt(-this.sign, this.digits.toMutableList(), true)
    }

    override operator fun compareTo(other: BigIntInterface): Int {
        other as BigInt
        if (this.sign != other.sign) return this.sign.compareTo(other.sign)
        if (this.isZero() && other.isZero()) return 0
        val cmpAbs = compareAbsLists(this.digits, other.digits)
        return if (this.sign > 0) cmpAbs else -cmpAbs
    }

    override operator fun compareTo(other: Int): Int = this.compareTo(BigInt(other.toString()))
    override operator fun compareTo(other: Short): Int = this.compareTo(BigInt(other.toString()))
    override operator fun compareTo(other: Byte): Int = this.compareTo(BigInt(other.toString()))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BigInt) return false
        return this.sign == other.sign && this.digits == other.digits
    }

    override fun hashCode(): Int {
        var result = sign
        result = 31 * result + digits.hashCode()
        return result
    }
}
