package com.mills.advent.twenty

import com.mills.advent.support.AdventOfCode

interface Rule
data class LeafRule(val c: Char): Rule
data class RuleReferences (val subRules: List<Int>): Rule
data class RuleContainer (val subRules: List<RuleReferences>): Rule

class Day19 : AdventOfCode {
    private fun getInputText(): String = Day19::class.java.getResource("day19.txt")?.readText()!!

    override fun day(): String = "19"

    override fun part1(): Int {
        val (rules, messageLines) = parseRulesAndMessages()

        return messageLines.filter { message ->
            message.length in canParseMessage(rules, rules[0]!!, message)
        }.size
    }

    override fun part2(): Int {
        val (rules, messageLines) = parseRulesAndMessages()

        val modifiedRules = rules.toMutableMap()

        modifiedRules[8] = RuleContainer(subRules = listOf(
            RuleReferences(listOf(42)),
            RuleReferences(listOf(42,8))
        ))
        modifiedRules[11] = RuleContainer(subRules = listOf(
            RuleReferences(listOf(42,31)),
            RuleReferences(listOf(42,11,31))
        ))

        return messageLines.filter { message ->
            message.length in canParseMessage(modifiedRules, modifiedRules[0]!!, message)
        }.size
    }

    fun canParseMessage(rules: Map<Int, Rule>, ruleToProcess: Rule, remainingMessage: String): List<Int> {
        if(ruleToProcess is LeafRule) {
            return if(
                remainingMessage.isNotEmpty() &&
                ruleToProcess.c == remainingMessage[0]
            ) listOf(1) else listOf()
        } else if (ruleToProcess is RuleReferences) {
            var toProcess = listOf(0 to remainingMessage)
            for(rule in ruleToProcess.subRules) {
                val newToProcess = mutableListOf<Pair<Int, String>>()
                for((processedChars, message) in toProcess) {
                    val parsedChars = canParseMessage(rules, rules[rule]!!, message)

                    if(parsedChars.isNotEmpty()) {
                        newToProcess.addAll(parsedChars.map { processedChars + it to message.substring(it) } )
                    }
                }
                toProcess = newToProcess
            }
            return toProcess.map { it.first }
        } else if (ruleToProcess is RuleContainer) {
            return ruleToProcess.subRules.flatMap {
                canParseMessage(rules, it, remainingMessage)
            }.filter { it > 0 }
        }
        return listOf()
    }

    fun parseRulesAndMessages(): Pair<Map<Int, Rule>, List<String>> {
        val (rulesLines, messageLines) = getInputText().split("\n\n").map { it.split("\n") }

        val rules = rulesLines.associate { rule ->
            val (idx, matches) = rule.split(":")

            idx.toInt() to if(matches.trim()[0] == '"') {
                LeafRule(matches.trim()[1])
            } else {
                RuleContainer(matches.split("|").map {
                    RuleReferences(it.trim().split(" ").map { it.toInt() })
                })
            }
        }

        return rules to messageLines
    }
}

fun main() {
    Day19().run()
}
