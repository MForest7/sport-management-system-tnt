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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Competitor

        if (wishGroup != other.wishGroup) return false
        if (surname != other.surname) return false
        if (name != other.name) return false
        if (birth != other.birth) return false
        if (title != other.title) return false
        if (MedicalExamination != other.MedicalExamination) return false
        if (MedicalInsurance != other.MedicalInsurance) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wishGroup.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + birth.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + MedicalExamination.hashCode()
        result = 31 * result + MedicalInsurance.hashCode()
        return result
    }
}
