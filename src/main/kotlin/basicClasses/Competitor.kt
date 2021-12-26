package basicClasses

open class Competitor(
    var wishGroup: String,
    var surname: String,
    var name: String,
    var birth: String,
    var title: String,
    var medicalExamination: String,
    var medicalInsurance: String,
    val id: Int = 0
) {
    constructor(competitor: Competitor) : this(
        competitor.wishGroup,
        competitor.surname,
        competitor.name,
        competitor.birth,
        competitor.title,
        competitor.medicalExamination,
        competitor.medicalInsurance,
        competitor.id
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Competitor

        if (wishGroup != other.wishGroup) return false
        if (surname != other.surname) return false
        if (name != other.name) return false
        if (birth != other.birth) return false
        if (title != other.title) return false
        if (medicalExamination != other.medicalExamination) return false
        if (medicalInsurance != other.medicalInsurance) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wishGroup.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + birth.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + medicalExamination.hashCode()
        result = 31 * result + medicalInsurance.hashCode()
        result = 31 * result + id
        return result
    }
}
