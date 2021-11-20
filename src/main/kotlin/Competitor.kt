open class Competitor(
    val name: String,
    val surname: String,
    val birth: String,
    val title: String,
    val wishGroup: String,
    val MedicalExamination: String,
    val MedicalInsurance: String,
) {
    constructor(competitor: Competitor) : this(
        competitor.name,
        competitor.surname,
        competitor.birth,
        competitor.title,
        competitor.wishGroup,
        competitor.MedicalExamination,
        competitor.MedicalInsurance
    )
}