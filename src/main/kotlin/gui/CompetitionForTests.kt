package gui

import classes.*

fun getCompetitionForTests(): Competition {
    val competitors = listOf(
        Competitor("VIP", "НИКИТИН", "НИКИТОС", "1941", "", "", "", 0),
        Competitor("VIP", "НИКИТИНА", "АЛЛА", "1939", "", "", "", 1),
        Competitor("М12", "ТИХОМИРОВ", "ИВАН", "2007", "КМС", "", "", 2),
        Competitor("М12", "Анисимов", "Роман", "2010", "", "", "", 3),
        Competitor("М12", "Анисимова", "Кристина", "2009", "", "", "", 4),
        Competitor("М12", "ВАСИЛЕНКО", "ГРИГОРИЙ", "2007", "", "", "", 5),
        Competitor("М12", "ВАСИЛЕНКО", "ЛИЗА", "2007", "", "", "", 6),
    )
    val groups = listOf(
        Group("VIP", listOf("1km", "2km", "Finish")),
        Group("М12", listOf("1km", "2km", "Finish")),
    )
    val teams = listOf(
        Team("ПСКОВ,РУСЬ", mutableListOf(competitors[0], competitors[1], competitors[2])),
        Team("ЦДиЮТиЭ", mutableListOf(competitors[3], competitors[4])),
        Team("ПСКОВ", mutableListOf(competitors[5], competitors[6]))
    )
    val competitorsInCompetition = listOf(
        CompetitorInCompetition(competitors[0], "7", groups[0], teams[0]),
        CompetitorInCompetition(competitors[1], "1", groups[0], teams[0]),
        CompetitorInCompetition(competitors[2], "4", groups[1], teams[0]),
        CompetitorInCompetition(competitors[3], "6", groups[1], teams[1]),
        CompetitorInCompetition(competitors[4], "2", groups[1], teams[1]),
        CompetitorInCompetition(competitors[5], "3", groups[1], teams[2]),
        CompetitorInCompetition(competitors[6], "5", groups[1], teams[2]),
    )
    val checkpointStart = CheckPoint(
        timeMatching = mutableMapOf(
            competitorsInCompetition[0] to mutableListOf(Time("12:05:00")),
            competitorsInCompetition[1] to mutableListOf(Time("12:04:00")),
            competitorsInCompetition[2] to mutableListOf(Time("12:02:00")),
            competitorsInCompetition[3] to mutableListOf(Time("12:00:00")),
            competitorsInCompetition[4] to mutableListOf(Time("12:01:00")),
            competitorsInCompetition[5] to mutableListOf(Time("12:06:00")),
            competitorsInCompetition[6] to mutableListOf(Time("12:03:00")),
        )
    )
    val checkpoint1km = CheckPoint(
        "1km",
        timeMatching = mutableMapOf(
            competitorsInCompetition[0] to mutableListOf(Time("12:06:00")),
            competitorsInCompetition[1] to mutableListOf(Time("12:05:00")),
            competitorsInCompetition[2] to mutableListOf(Time("12:03:00")),
            competitorsInCompetition[3] to mutableListOf(Time("12:01:00")),
            competitorsInCompetition[4] to mutableListOf(Time("12:02:00")),
            competitorsInCompetition[5] to mutableListOf(Time("12:07:00")),
            competitorsInCompetition[6] to mutableListOf(Time("12:04:00")),
        )
    )
    val checkpoint2km = CheckPoint(
        "2km",
        timeMatching = mutableMapOf(
            competitorsInCompetition[0] to mutableListOf(Time("12:07:00")),
            competitorsInCompetition[1] to mutableListOf(Time("12:06:00")),
            competitorsInCompetition[2] to mutableListOf(Time("12:04:00")),
            competitorsInCompetition[3] to mutableListOf(Time("12:02:00")),
            competitorsInCompetition[4] to mutableListOf(Time("12:03:00")),
            competitorsInCompetition[5] to mutableListOf(Time("12:08:00")),
            competitorsInCompetition[6] to mutableListOf(Time("12:05:00")),
        )
    )
    val checkpointFinish = CheckPoint(
        "Finish",
        timeMatching = mutableMapOf(
            competitorsInCompetition[0] to mutableListOf(Time("12:08:00")),
            competitorsInCompetition[1] to mutableListOf(Time("12:07:00")),
            competitorsInCompetition[2] to mutableListOf(Time("12:05:00")),
            competitorsInCompetition[3] to mutableListOf(Time("12:03:00")),
            competitorsInCompetition[4] to mutableListOf(Time("12:04:00")),
            competitorsInCompetition[5] to mutableListOf(Time("12:09:00")),
            competitorsInCompetition[6] to mutableListOf(Time("12:06:00")),
        )
    )
    val checkpoints = mutableListOf(checkpointStart, checkpoint1km, checkpoint2km, checkpointFinish)
    val numberMatching = mapOf(
        "7" to competitorsInCompetition[0],
        "1" to competitorsInCompetition[1],
        "4" to competitorsInCompetition[2],
        "6" to competitorsInCompetition[3],
        "2" to competitorsInCompetition[4],
        "3" to competitorsInCompetition[5],
        "5" to competitorsInCompetition[6],
    )
    return Competition(checkpoints, competitorsInCompetition, numberMatching)
}