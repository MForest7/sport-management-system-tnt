package classes

open class Competitor(
    val wishGroup: String,
    val surname: String,
    val name: String,
    val birth: String,
    val title: String,
    val MedicalExamination: String,
    val MedicalInsurance: String,
) {
    constructor(competitor: Competitor) : this(
        competitor.wishGroup,
        competitor.surname,
        competitor.name,
        competitor.birth,
        competitor.title,
        competitor.MedicalExamination,
        competitor.MedicalInsurance
    )
}
