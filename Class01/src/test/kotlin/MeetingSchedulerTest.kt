import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MeetingSchedulerTest {
    private val algorithms: List<(List<Pair<Double, Double>>) -> Boolean> =
        listOf(::hasConflict)

    @Test
    fun emptyListNoConflict() {
        assertNoConflict(emptyList())
    }

    @Test
    fun oneMeetingNoConflict() {
        assertNoConflict(listOf(meeting("9:00", "10:00")))
    }

    @Test
    fun separatedMeetingsNoConflict() {
        val meetings = listOf(
            meeting("13:00", "14:00"),
            meeting("9:00", "10:00"),
            meeting("11:00", "12:00"),
        )
        assertNoConflict(meetings)
    }

    @Test
    fun sameEndStartNoConflict() {
        val meetings = listOf(
            meeting("9:00", "10:00"),
            meeting("10:00", "11:00"),
        )
        assertNoConflict(meetings)
    }

    @Test
    fun partiallyOverlapConflict() {
        val meetings = listOf(
            meeting("9:00", "11:00"),
            meeting("10:00", "12:00"),
        )
        assertConflict(meetings)
    }

    @Test
    fun completelyOverlapConflict() {
        val meetings = listOf(
            meeting("9:00", "10:00"),
            meeting("9:00", "10:00"),
        )
        assertConflict(meetings)
    }

    @Test
    fun nestedMeetingsConflict() {
        val meetings = listOf(
            meeting("8:00", "14:00"),
            meeting("10:00", "11:00"),
        )
        assertConflict(meetings)
    }


    @Test
    fun unsortedScheduleConflict() {
        val meetings = listOf(
            meeting("15:00", "16:00"),
            meeting("8:00", "9:00"),
            meeting("12:00", "14:00"),
            meeting("13:00", "17:00"),
        )
        assertConflict(meetings)
    }

    private fun assertNoConflict(meetings: List<Pair<Double, Double>>) {
        algorithms.forEach { algorithm ->
            assertFalse(algorithm(meetings))
        }
    }

    private fun assertConflict(meetings: List<Pair<Double, Double>>) {
        algorithms.forEach { algorithm ->
            assertTrue(algorithm(meetings))
        }
    }
}
