import classes.*


class DatabaseController(
    private val db: DB,
    model: Model
) : Controller(model) {
    fun set(newValue: Competitor) {
        val oldValue = db.getAllCompetitors().find {
            it.id == newValue.id
        }
        require(oldValue != null) { "no competitor with such id" }
        require(oldValue.id == newValue.id) { "id is not mutable" }
        require(oldValue.wishGroup == newValue.wishGroup) { "wishGroup is not mutable" }
        db.updateCompetitor(newValue)
    }

    fun set(competitor: CompetitorInCompetition, time: Time, checkpoint: CheckPoint, number: Int) {
        require(number >= 0) { "number is less then 0" }

        val competition = db.getCompetition()
        require(competition != null) { "competition is null" }

        val found = competition.checkpoints.find { it.name == checkpoint.name }
        require(found != null) { "no such checkpoint in list" }
        val list = found.timeMatching[competitor]
        require(list != null)
        list[number] = time

        db.setCompetition(competition)
    }

    fun uploadGroups(groups: List<Group>) {

    }

    override fun uploadApplications() {
        val listOfTeams = db.getAllApplications()
        model.loadApplications(listOfTeams)
    }

    override fun loadResults() {

    }
}
