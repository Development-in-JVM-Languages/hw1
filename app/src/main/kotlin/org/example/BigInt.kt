class BigInt(value: String) : Comparable<BigInt> {
    private var sign: Int
    private var digits: MutableList<Int>
    init {
        var s = value.trim()
        require(s.isNotEmpty()) { "Input string cannot be empty" }
        var sgn = 1
        if (s[0] == '-') {
            sgn = -1
            s = s.substring(1)
        }
        require(s.all { it.isDigit() }) { "Invalid input: $value" }
        s = s.trimStart('0')
        if (s.isEmpty()) {
            s = "0"
            sgn = 0
        }
        sign = if (s == "0") 0 else sgn
        digits = s.map { it - '0' }.reversed().toMutableList()
    }

    private constructor(sign: Int, digits: MutableList<Int>) : this("0") {
        this.digits = digits
        this.sign = if (digits.size == 1 && digits[0] == 0) 0 else sign
    }



    private fun isZero() = (digits.size == 1 && digits[0] == 0)

    private fun compareAbsLists(a: List<Int>, b: List<Int>): Int {
        if (a.size != b.size) return a.size.compareTo(b.size)
        for (i in a.size - 1 downTo 0) {
            if (a[i] != b[i]) return a[i].compareTo(b[i])
        }
        return 0
    }

    private fun addAbs(a: List<Int>, b: List<Int>): MutableList<Int> {
        val result = mutableListOf<Int>()
        val maxLen = maxOf(a.size, b.size)
        var carry = 0
        for (i in 0 until maxLen) {
            val d1 = if (i < a.size) a[i] else 0
            val d2 = if (i < b.size) b[i] else 0
            val sum = d1 + d2 + carry
            result.add(sum % 10)
            carry = sum / 10
        }
        if (carry > 0) result.add(carry)
        return result
    }

    private fun subtractAbs(a: List<Int>, b: List<Int>): MutableList<Int> {
        val result = mutableListOf<Int>()
        var borrow = 0
        for (i in a.indices) {
            val d1 = a[i]
            val d2 = if (i < b.size) b[i] else 0
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

    private fun multiplyAbs(a: List<Int>, b: List<Int>): MutableList<Int> {
        val result = MutableList(a.size + b.size) { 0 }
        for (i in a.indices) {
            var carry = 0
            for (j in b.indices) {
                val prod = a[i] * b[j] + result[i + j] + carry
                result[i + j] = prod % 10
                carry = prod / 10
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

    private fun toForward(list: List<Int>): List<Int> = list.reversed()

    private fun removeLeadingZerosForward(list: MutableList<Int>) {
        while (list.size > 1 && list.first() == 0) {
            list.removeAt(0)
        }
    }

    private fun compareForward(a: List<Int>, b: List<Int>): Int {
        if (a.size != b.size) return a.size.compareTo(b.size)
        for (i in a.indices) {
            if (a[i] != b[i]) return a[i].compareTo(b[i])
        }
        return 0
    }

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

    private fun subtractForward(a: List<Int>, b: List<Int>): List<Int> {
        val aRev = a.reversed()
        val bRev = b.reversed()
        val diffRev = subtractAbs(aRev, bRev)
        val diff = diffRev.reversed().toMutableList()
        while (diff.size > 1 && diff.first() == 0) diff.removeAt(0)
        return diff
    }

    private fun divmodAbs(dividendF: List<Int>, divisorF: List<Int>): Pair<MutableList<Int>, MutableList<Int>> {
        if (divisorF.size == 1 && divisorF[0] == 0) throw ArithmeticException("Division by zero")
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
            val newRem = if (subtrahend.all { it == 0 }) remainder.toMutableList()
            else subtractForward(remainder, subtrahend).toMutableList()
            remainder.clear()
            remainder.addAll(newRem)
        }
        removeLeadingZerosForward(quotient)
        if (quotient.isEmpty()) quotient.add(0)
        if (remainder.isEmpty()) remainder.add(0)
        return Pair(quotient, remainder)
    }


    operator fun plus(other: BigInt): BigInt {
        if (this.isZero()) return other
        if (other.isZero()) return this
        return if (this.sign == other.sign) {
            val sumDigits = addAbs(this.digits, other.digits)
            BigInt(this.sign, sumDigits)
        } else {
            val cmp = compareAbsLists(this.digits, other.digits)
            when {
                cmp == 0 -> BigInt("0")
                cmp > 0 -> {
                    val diffDigits = subtractAbs(this.digits, other.digits)
                    BigInt(this.sign, diffDigits)
                }
                else -> {
                    val diffDigits = subtractAbs(other.digits, this.digits)
                    BigInt(other.sign, diffDigits)
                }
            }
        }
    }
    operator fun plus(other: Int): BigInt = this + BigInt(other.toString())
    operator fun plus(other: Short): BigInt = this + BigInt(other.toString())
    operator fun plus(other: Byte): BigInt = this + BigInt(other.toString())

    operator fun minus(other: BigInt): BigInt = this + (other.unaryMinus())
    operator fun minus(other: Int): BigInt = this - BigInt(other.toString())
    operator fun minus(other: Short): BigInt = this - BigInt(other.toString())
    operator fun minus(other: Byte): BigInt = this - BigInt(other.toString())

    operator fun times(other: BigInt): BigInt {
        val prodDigits = multiplyAbs(this.digits, other.digits)
        val prodSign = if (this.isZero() || other.isZero()) 0 else if (this.sign == other.sign) 1 else -1
        return BigInt(prodSign, prodDigits)
    }
    operator fun times(other: Int): BigInt = this * BigInt(other.toString())
    operator fun times(other: Short): BigInt = this * BigInt(other.toString())
    operator fun times(other: Byte): BigInt = this * BigInt(other.toString())

    operator fun div(other: BigInt): BigInt {
        if (other.isZero()) throw ArithmeticException("Division by zero")
        val dividendF = toForward(this.digits)
        val divisorF = toForward(other.digits)
        val (quotientF, _) = divmodAbs(dividendF, divisorF)
        val quotientRev = quotientF.reversed().toMutableList()
        val resultSign = if (this.isZero() || other.isZero()) 0 else if (this.sign == other.sign) 1 else -1
        val res = BigInt(resultSign, quotientRev)
        if (res.toString() == "0") res.sign = 0
        return res
    }
    operator fun div(other: Int): BigInt = this / BigInt(other.toString())
    operator fun div(other: Short): BigInt = this / BigInt(other.toString())
    operator fun div(other: Byte): BigInt = this / BigInt(other.toString())

    operator fun rem(other: BigInt): BigInt {
        if (other.isZero()) throw ArithmeticException("Division by zero")
        val dividendF = toForward(this.digits)
        val divisorF = toForward(other.digits)
        val (_, remainderF) = divmodAbs(dividendF, divisorF)
        val remainderRev = remainderF.reversed().toMutableList()
        return BigInt(this.sign, remainderRev)
    }
    operator fun rem(other: Int): BigInt = this % BigInt(other.toString())
    operator fun rem(other: Short): BigInt = this % BigInt(other.toString())
    operator fun rem(other: Byte): BigInt = this % BigInt(other.toString())

    fun pow(exp: BigInt): BigInt {
        val expInt = exp.toString().toIntOrNull() ?: throw IllegalArgumentException("Exponent too large")
        return this.pow(expInt)
    }
    fun pow(exp: Int): BigInt {
        require(exp >= 0) { "Negative exponent not supported" }
        var result = BigInt("1")
        var base = this
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
    fun pow(exp: Short): BigInt = this.pow(exp.toInt())
    fun pow(exp: Byte): BigInt = this.pow(exp.toInt())

    fun sign(): Int = this.sign

    fun abs(): BigInt = if (this.sign >= 0) this else this.unaryMinus()

    override fun toString(): String {
        val str = digits.reversed().joinToString("") { it.toString() }
        return if (sign < 0) "-$str" else str
    }

    override operator fun compareTo(other: BigInt): Int {
        if (this.sign != other.sign) return this.sign.compareTo(other.sign)
        if (this.isZero() && other.isZero()) return 0
        val cmpAbs = compareAbsLists(this.digits, other.digits)
        return if (this.sign > 0) cmpAbs else -cmpAbs
    }
    operator fun compareTo(other: Int): Int = this.compareTo(BigInt(other.toString()))
    operator fun compareTo(other: Short): Int = this.compareTo(BigInt(other.toString()))
    operator fun compareTo(other: Byte): Int = this.compareTo(BigInt(other.toString()))

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

    operator fun unaryMinus(): BigInt {
        if (this.isZero()) return this
        return BigInt(-this.sign, this.digits.toMutableList())
    }
}
