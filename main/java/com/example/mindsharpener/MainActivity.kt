package com.example.mindsharpener

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindsharpener.ui.theme.MindSharpenerTheme
import java.util.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MindSharpenerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ThreeRadioButtons()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreeRadioButtons() {
    var selectedOption by remember { mutableStateOf(0) }
    var question by remember { mutableStateOf(generateQuestion(selectedOption)) }
    var userAnswer by remember { mutableStateOf(TextFieldValue()) }
    var score by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {

        TopAppBar(
            title = { Text("Mind Sharpener") },
        )

        Text("Instructions:", modifier = Modifier.padding(5.dp), fontSize = 14.sp)

        Text(
            "This is simple mathematic games which is believed can train your brain." +
                    "Two numbers are given randomly based on your level choice together with the operator." +
                    "You just need to calculate the answer, write your answer and press the check button. Every correct" +
                    "answer will give you 1 point while a wrong answer will deduct 1 point",
            modifier = Modifier.padding(5.dp),
            fontSize = 14.sp
        )

        Text("Choose Level:", modifier = Modifier.padding(5.dp), fontSize = 14.sp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RadioButton(
                    selected = selectedOption == 0,
                    onClick = { selectedOption = 0 },
                    modifier = Modifier
                        .selectableGroup()
                        .weight(1f)
                )
                Text("i3")

                RadioButton(
                    selected = selectedOption == 1,
                    onClick = { selectedOption = 1 },
                    modifier = Modifier
                        .selectableGroup()
                        .weight(1f)
                )
                Text("i5")

                RadioButton(
                    selected = selectedOption == 2,
                    onClick = { selectedOption = 2 },
                    modifier = Modifier
                        .selectableGroup()
                        .weight(1f)
                )
                Text("i7")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(question.first, fontSize = 24.sp)
                Text(question.second, fontSize = 24.sp)
                Text(question.third, fontSize = 24.sp)
            }
        }

        // Replace the Text with a TextField for user input
        TextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Enter your answer") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        Button(
            onClick = {
                if (checkAnswer(question, userAnswer.text)) {
                    score++
                } else {
                    score--
                }
                // Generate a new question after submitting
                question = generateQuestion(selectedOption)
                // Clear the user's answer
                userAnswer = TextFieldValue()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Check")
        }

        // Display score
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "POINT :",
                color = Color.Blue,
                fontSize = 34.sp,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = score.toString(),
                color = Color.Red,
                fontSize = 34.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


private fun generateQuestion(level: Int): Triple<String, String, String> {
    return when (level) {
        0 -> Triple(Random().nextInt(10).toString(), "+", Random().nextInt(10).toString()) // i3 level
        1 -> Triple(Random().nextInt(100).toString(), "-", Random().nextInt(100).toString()) // i5 level
        2 -> Triple(Random().nextInt(1000).toString(), "*", Random().nextInt(10).toString()) // i7 level
        else -> Triple("", "", "")
    }
}

private fun checkAnswer(question: Triple<String, String, String>, userAnswer: String): Boolean {
    val num1 = question.first.toInt()
    val num2 = question.third.toInt()
    return when (question.second) {
        "+" -> num1 + num2 == userAnswer.toInt()
        "-" -> num1 - num2 == userAnswer.toInt()
        "*" -> num1 * num2 == userAnswer.toInt()
        else -> false
    }
}
