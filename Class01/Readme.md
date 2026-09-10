# Learning Strategies

### 1. Choose a moment in your educational career (it could be an assignment or a full course) where learning went really well. What strategies did you employ that worked particularly well (e.g., working with others, trying work on your own before asking a friend, or going to office hours)?

**My answer:** I remember I learned gradient descent well in QEA2. A mix of class materials, math theory, exercises, questions and answers, and hands-on homework worked well for me. The homework came right after the lesson, so I could practice what I had learned on my own while it was still fresh.

### 2. Similar to (1), which sorts of strategies have led to either less effective learning or less enjoyment of the learning experience? Feel free to describe a few examples of what does not work for you.

**My answer:** I did not like how we did assignments in Computer Architecture. I took good notes during lectures and understood the lecture content, but the assignments were large projects that often felt very different from what we learned in class. I had to ask friends or go to office hours often. When other people helped me, I did not always feel that I learned much. I also did not understand why we learned some class topics until much later in the course.

### 3. As this course is foundational for many aspects of computer science, the problems in this class can be easily solved with modern AI systems (e.g., ChatGPT, Gemini, etc.). One of my foundational assumptions is that the process of grappling with a problem helps you internalize the important concepts, gives you more insight into how the tools you are learning can be applied in other contexts, helps you more realistically assess your own abilities, and helps you learn to better communicate your knowledge to others. Particular methods of using AI (e.g., prompting the AI to provide answers to questions and thoughtlessly copying the answers) are unlikely to achieve the learning goals articulated previously. Do you agree with this framing? How are you thinking about AI tools with respect to this course?

**My answer:** I agree. I like to use AI to explain code and help me understand unfamiliar ideas. When I'm confident that I already understand a task and finishing it is mainly a matter of time, I may use AI to help. However, I still review everything it writes and make sure I understand it fully.

### 4. What strategies will you use in this course to be successful? With respect to AI, what principles or strategies will you use during this course?

**My answer:** I will follow along in class and learn by doing the homework. When I use AI, I will use the approach I described above.

### 5. What do you think of some of the proposed activities for the oral quizzes? Are these activities ones that would give you helpful feedback as to how you are performing with respect to the course material? Would you add or subtract any of the proposed activities?

**My answer:** Yes, I think these activities would give me helpful feedback. I especially like the technical-interview-style activity where I solve a problem by talking through my thoughts.

### 6. How can the teaching team support you?

**My answer:** I like what we are doing in class so far, so I would like the teaching team to keep doing it. Timely feedback and grades on homework and projects would also be helpful.


# Kotlin Tour

### 1. What features do you like about Kotlin?

**My answer:** Kotlin is very similar to Python, which I like. I am not sure whether this is an IntelliJ feature or a Kotlin feature, but I also like the automatic tracking of function usage and the parameter hints. For example, this code:

```kotlin
private val board: Array<Array<String>> =
    Array(3) { Array(3) { BLANK_MARK } }
```

shows like this in IntelliJ:
```kotlin
private val board: Array<Array<String>> =
    Array("size="3) { Array("size="3) { BLANK_MARK } }
```
This explains what the number means.

### 2. Are there things you were expecting to find that you have not?

**My answer:** I expected a similar command style in the terminal, but it seems different from using WSL.

### 3. What questions do you have?

**My answer:** I don't have any questions right now:)

### 4. Try using the debugger (see the Getting Set with Kotlin page) for some very basic information on the debugger. Do you have experience using interactive debuggers like this one? Were you able to successfully launch the debugger?

**My answer:** I don't use the debugger a lot, but it looks pretty similar to the one in VSCode that you can follow every step and stop at a given line. It could be helpful.


# Translating Old Code

### Include a writeup that describes the purpose of the code and how you found the process of translating the code to Kotlin (or the language you wind up choosing). Feel free to touch on the good, the bad, and the ugly.

I translated the tic-tac-toe game we did in SoftDes. The code contains a main game script, board, controller, and view. The process is generally smooth, and the main difficulty was the syntaxes and format for me. I also noticed some similarities with C++. For example, clarifying function input and output variable types is required.


# Meeting Scheduler

### Implement an algorithm that determines whether a collection of meetings contains a conflict.

**My answer:** My program represents each meeting as a pair of start and end times. I enter times as strings such as `"9:00"`. The `timeToDouble` function changes each string into a number. For example, `"10:30"` becomes `10.5`. This makes the times easy to compare.

The `overlap` function checks whether two meetings conflict. It checks whether the first meeting starts before the second one ends and whether the second meeting starts before the first one ends. Both conditions must be true for the meetings to overlap. Because the comparisons use `<` instead of `<=`, a meeting that ends exactly when another starts is not a conflict.

**Straightforward algorithm**

The commented-out `hasConflictPairwise` function shows the straightforward approach. It checks every different pair of meetings. It returns `true` as soon as it finds an overlap. If it does not find one, it returns `false`.

In the worst case, the function checks about n² pairs. Therefore, its running time is **Θ(n²)**.

**Sorting-based algorithm**

The `hasConflict` function first sorts the meetings by start time with Kotlin's built-in `sortedBy` function. Once the meetings are sorted, it only needs to compare each meeting with the meeting directly after it.

This works because the next meeting has the earliest start time out of all the meetings that come later. If the current meeting does not overlap with the next one, it cannot overlap with any meeting after that one either.

Sorting takes Θ(n log n) time. Comparing each meeting with its next neighbor takes **Θ(n)** time because there is only one loop. Together, the running time is **Θ(n log n) + Θ(n) = Θ(n log n)**

So sorting makes the algorithm faster by replacing the two nested loops with one loop that only checks neighboring meetings.

**Unit tests**

My tests include an empty list, one meeting, separated meetings, meetings that touch at one endpoint, partially overlapping meetings, completely overlapping meetings, nested meetings, and an unsorted schedule with a conflict. The tests run the sorting-based `hasConflict` function. All of them passed.
