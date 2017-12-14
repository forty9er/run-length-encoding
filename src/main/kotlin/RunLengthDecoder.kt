object RunLengthDecoder {
    operator fun invoke(s: String) = s.let {
        when {
            it.isEmpty() -> ""
            else -> decode(it)
        }
    }

    private fun decode(s: String, processed: String = ""): String {
        val partitionedString = partition(s)
        val times = repeatTimes(partitionedString)
        val characterToRepeat = partitionedString.repeatedChars.first { !it.isDigit() }
        val repeatedChar = (1..times).map { characterToRepeat }.joinToString("")
        val decoded = "$processed$repeatedChar"
        return when {
            partitionedString.remainder.isNotEmpty() -> decode(partitionedString.remainder.toString(), decoded)
            else -> decoded
        }
    }

    private fun partition(s: String): Partition {
        val nextNonDigit = s.find { !it.isDigit() }
        return nextNonDigit?.let {
            val nextGroupingIndex = s.indexOf(it) + 1
            val encodedChar = s.subSequence(0, nextGroupingIndex)
            val remainder = s.subSequence(nextGroupingIndex, s.length)
            Partition(encodedChar, remainder)
        } ?: Partition(s, "")
    }

    private fun repeatTimes(partitionedString: Partition): Int {
        val stringRepeats = partitionedString.repeatedChars.filter { it.isDigit() }.toList().joinToString("")
        return when {
            stringRepeats.isEmpty() -> 1
            else -> Integer.parseInt(stringRepeats)
        }
    }
}