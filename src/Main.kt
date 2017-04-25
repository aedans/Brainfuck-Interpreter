import java.io.FileReader

/**
 * Created by Aedan Smith.
 */

fun main(args: Array<String>) = run(FileReader(args[0]).readText(), 0, 0, Array(30000, { 0 }))

tailrec fun run(string: String, index: Int, ptr: Int, mem: Array<Int>) {
    if (index < string.length) {
        when (string[index]) {
            '>' -> run(string, index + 1, (ptr + 1).let { if (it == 30000) 0 else it }, mem)
            '<' -> run(string, index + 1, (ptr - 1).let { if (it < 0) 30000 - 1 else it }, mem)
            '+' -> run(string, index + 1, ptr, mem.also { it[ptr] = (it[ptr] + 1).let { if (it == 256) 0 else it } })
            '-' -> run(string, index + 1, ptr, mem.also { it[ptr] = (it[ptr] - 1).let { if (it == -1) 255 else it } })
            '.' -> run(string.also { print(mem[ptr].toChar()) }, index + 1, ptr, mem)
            ',' -> run(string, index + 1, ptr, mem.also { it[ptr] = System.`in`.read() })
            '[' -> run(string, if (mem[ptr] == 0) index + string.substring(index).indexOfFirst(1, { state, char -> when (char) {
                ']' -> (state - 1) to (state == 1)
                '[' -> (state + 1) to false
                else -> state to false
            } }) else index + 1, ptr, mem)
            ']' -> run(string, if (mem[ptr] != 0) index - string.substring(0, index).reversed().indexOfFirst(1, { state, char -> when (char) {
                '[' -> (state - 1) to (state == 1)
                ']' -> (state + 1) to false
                else -> state to false
            } }) else index + 1, ptr, mem)
        }
    }
}

fun <T> String.indexOfFirst(state: T, func: (T, Char) -> Pair<T, Boolean>, index: Int = 0): Int =
        func(state, this[index]).let { if (it.second) index else indexOfFirst(it.first, func, index + 1) }
