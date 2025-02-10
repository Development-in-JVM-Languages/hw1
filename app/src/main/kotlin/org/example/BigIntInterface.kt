interface BigIntInterface : Comparable<BigIntInterface> {
    operator fun plus(other: BigIntInterface): BigIntInterface
    operator fun plus(other: Int): BigIntInterface
    operator fun plus(other: Short): BigIntInterface
    operator fun plus(other: Byte): BigIntInterface

    operator fun minus(other: BigIntInterface): BigIntInterface
    operator fun minus(other: Int): BigIntInterface
    operator fun minus(other: Short): BigIntInterface
    operator fun minus(other: Byte): BigIntInterface

    operator fun times(other: BigIntInterface): BigIntInterface
    operator fun times(other: Int): BigIntInterface
    operator fun times(other: Short): BigIntInterface
    operator fun times(other: Byte): BigIntInterface

    operator fun div(other: BigIntInterface): BigIntInterface
    operator fun div(other: Int): BigIntInterface
    operator fun div(other: Short): BigIntInterface
    operator fun div(other: Byte): BigIntInterface

    operator fun rem(other: BigIntInterface): BigIntInterface
    operator fun rem(other: Int): BigIntInterface
    operator fun rem(other: Short): BigIntInterface
    operator fun rem(other: Byte): BigIntInterface

    fun pow(exp: BigIntInterface): BigIntInterface
    fun pow(exp: Int): BigIntInterface
    fun pow(exp: Short): BigIntInterface
    fun pow(exp: Byte): BigIntInterface

    fun sign(): Int           // returns -1, 0, or 1
    fun abs(): BigIntInterface

    override fun toString(): String

    override operator fun compareTo(other: BigIntInterface): Int

    operator fun compareTo(other: Int): Int
    operator fun compareTo(other: Short): Int
    operator fun compareTo(other: Byte): Int
}
