# Homework: Long Arithmetic in Kotlin

## Description 
- Implement a `BigInt` class that supports working with arbitrarily large integers (beyond `Long.MAX_VALUE`) without using Java’s built-in `BigInteger`.
-  The class must support basic arithmetic operations, comparisons (with both `BigInt` and primitive types), and utility functions.
## Requirements 

### 1. Number Representation
- The number should be stored as a **string** or **list of digits** .
-  The class must support **both positive and negative numbers**.
- The internal representation must be normalized:
  -   **No leading zeros** (except for the number zero itself, which should be represented as `"0"`).
  -   Use a separate sign indicator (e.g., `-1` for negative, `0` for zero, and `1` for positive).
-   **Input Validation:**
    -   The constructor must validate that the input string contains only numeric characters (with an optional leading `-`).
    -   Invalid input (e.g. `"abc"`) should throw an `IllegalArgumentException`.
### 2. Supported Operations
Implement the following operators and methods:

#### Addition 
```kotlin
operator fun plus(other: BigInt): BigInt
operator fun plus(other: Int): BigInt
operator fun plus(other: Short): BigInt
operator fun plus(other: Byte): BigInt
``` 
**Example:**

```kotlin
val a = BigInt("999999999999999999")
val b = BigInt("1")
println(a + b) // 1000000000000000000
```

#### Subtraction
```kotlin
operator fun minus(other: BigInt): BigInt
operator fun minus(other: Int): BigInt
operator fun minus(other: Short): BigInt
operator fun minus(other: Byte): BigInt
```
**Example:**

```kotlin
val a = BigInt("1000000000000000000")
val b = BigInt("1")
println(a - b) // 999999999999999999
```
#### Multiplication


```kotlin
operator fun times(other: BigInt): BigInt
operator fun times(other: Int): BigInt
operator fun times(other: Short): BigInt
operator fun times(other: Byte): BigInt
```
**Example:**

```kotlin
val a = BigInt("123456789")
val b = BigInt("987654321")
println(a * b) // 121932631112635269
```
#### Integer Division

```kotlin
operator fun div(other: BigInt): BigInt
operator fun div(other: Int): BigInt
operator fun div(other: Short): BigInt
operator fun div(other: Byte): BigInt
```
**Example:**
```kotlin
val a = BigInt("100")
val b = BigInt("3")
println(a / b) // 33` 
```
#### Modulo (Remainder)
```kotlin
operator fun rem(other: BigInt): BigInt
operator fun rem(other: Int): BigInt
operator fun rem(other: Short): BigInt
operator fun rem(other: Byte): BigInt
```
**Example**
```kotlin
val a = BigInt("100")
val b = BigInt("3")
println(a % b)  // 1
println(a % 7)  // 2
```

#### Power (Exponentiation)
```kotlin
fun pow(exp: BigInt): BigInt
fun pow(exp: Int): BigInt
fun pow(exp: Short): BigInt
fun pow(exp: Byte): BigInt
```
**Example**
```kotlin
val a = BigInt("2")
println(a.pow(10))  // 1024
println(a.pow(0))   // 1
```

### 3. Another Features

-   **Constructor:**
    
    ```kotlin
    class BigInt(value: String)
    ```
    **Example:**
- 
    ```kotlin
    val number = BigInt("12345678901234567890")
    println(number) // 12345678901234567890
    ```

- **Sign handling:**
	   
    ```kotlin  
	 fun sign(): Int  // Returns -1 for negative numbers, 0 for zero, 1 for positive numbers
	```
	**Example:**
    ```kotlin  
	 println(BigInt("-123").sign())  // -1
	 println(BigInt("0").sign())     // 0
	 println(BigInt("987").sign())   // 1
	```
- **Absolute Value:**
	   
    ```kotlin  
	 fun abs(): BigInt
	```
	**Example:**
    ```kotlin  
	 println(BigInt("-12345").abs())  // 12345
	```
-   **`toString()` method** to correctly display the number.
    
-   **Error Handling:**
    
    -   Division by zero should throw an `ArithmeticException`.
    -   Invalid input format (e.g., `BigInt("abc")`) should throw an `IllegalArgumentException`.

### 4. Comparable and Equality
#### Comparison (`Comparable` Interface)
```kotlin
class BigInt : Comparable<BigInt>
```
- Implement comparison operators:
  ```kotlin  
	 operator fun compareTo(other: BigInt): Int
	 operator fun compareTo(other: Int): Int
	 operator fun compareTo(other: Short): Int
	 operator fun compareTo(other: Byte): Int
	```
**Example:**
```kotlin
val a = BigInt("12345")
val b = BigInt("54321")
println(a < b)  // true
println(a > 1000)  // true
```
#### Equality and Hash Code
```kotlin
override fun equals(other: Any?): Boolean
override fun hashCode(): Int
```
**Example:**
```kotlin
val a = BigInt("123456")
val b = BigInt("123456")
println(a == b)  // true
println(a.hashCode() == b.hashCode())  // true
```
  


### 5. Restrictions

-   **Using `BigInteger` is prohibited.**
-   Implement algorithms without using external libraries for long arithmetic.
-   Operations should work correctly for numbers with different signs.

----------

## Usage Examples

```kotlin
fun main() {
    val a = BigInt("98765432109876543210")
    val b = BigInt("12345678901234567890")

    println(a + b)  // 111111111011111111100
    println(a - b)  // 86419753208641975320
    println(a * b)  // 1219326311370217952237463801111263526900
    println(a / b)  // 8
    println(a % b)  // 9000000000900000000
    println(BigInt("2").pow(10))  // 1024
    println(BigInt("-123").abs())  // 123
    println(BigInt("500") > BigInt("300"))  // true
}

```
