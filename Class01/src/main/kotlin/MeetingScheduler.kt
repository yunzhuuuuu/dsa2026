fun timeToDouble(meeting: Pair<String, String>): Pair<Double, Double> {
    val startParts = meeting.first.split(":")
    val startHours = startParts[0].toDouble()
    val startMinutes = startParts[1].toDouble()
    val start = startHours + startMinutes / 60.0

    val endParts = meeting.second.split(":")
    val endHours = endParts[0].toDouble()
    val endMinutes = endParts[1].toDouble()
    val end = endHours + endMinutes / 60.0

    return Pair(start, end)
}

fun meeting(start: String, end: String): Pair<Double, Double> {
    return timeToDouble(Pair(start, end))
}

fun overlap(m1: Pair<Double, Double>, m2: Pair<Double, Double>): Boolean {
    val m1BeforeM2Ends = m1.first < m2.second
    val m2BeforeM1Ends = m2.first < m1.second
    return m1BeforeM2Ends && m2BeforeM1Ends // if both true, there must be a conflict
}

/*
fun hasConflictPairwise(meetings: List<Pair<Double, Double>>): Boolean {
    for (i in meetings.indices) {
        for (j in i + 1 until meetings.size) { // every meeting after it
            val m1 = meetings[i]
            val m2 = meetings[j]
            if (overlap(m1, m2)) {
                return true
            }
        }
    }
    return false
}
*/

fun hasConflict(meetings: List<Pair<Double, Double>>): Boolean {
    val sortedMeetings = meetings.sortedBy { it.first }

    for (i in 0 until sortedMeetings.size - 1) {
        val currentMeeting = sortedMeetings[i]
        val nextMeeting = sortedMeetings[i + 1]
        if (overlap(currentMeeting, nextMeeting)) {
            return true
        }
    }
    return false
}

fun main() {
    val meetings: List<Pair<Double, Double>> = listOf(
        meeting("9:00", "10:00"),
        meeting("12:00", "13:00"),
        meeting("10:30", "11:45"),
        meeting("10:00", "11:00"),
    )
    val conflict = hasConflict(meetings)
    if (conflict) {
        println("The schedule has conflict(s).")
    } else {
        println("The schedule has no conflicts.")
    }
}
