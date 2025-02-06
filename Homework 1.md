
# Homework: Long Arithmetic in Kotlin

## Description 
You need to implement a `BigInt` class that supports working with large integers that exceed `Long.MAX_VALUE`.  

## Requirements 

### 1. Number Storage 
- The number should be stored as a string or a list of digits (without using `BigInteger`).  
- Support for both positive and negative numbers.  

### 2. Basic Operations 
Implement the following methods:  

#### Addition 
```kotlin
operator fun plus(other: BigInt): BigInt
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
```
**Example:**
```kotlin
val a = BigInt("100")
val b = BigInt("3")
println(a / b) // 33` 
```
### 3. Another Features

-   **Constructor:**
    
    ```kotlin  
    class BigInt(value: String)
    ```
    **Example:**
    
    
    ```kotlin
    val number = BigInt("12345678901234567890")
    println(number) // 12345678901234567890
    ```
    
-   **`toString()` method** to correctly display the number.
    
-   **Error Handling:**
    
    -   Division by zero should throw an `ArithmeticException`.
    -   Invalid input format (e.g., `BigInt("abc")`) should throw an `IllegalArgumentException`.

### 4. Restrictions

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
}
```
