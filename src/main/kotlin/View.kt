import basicClasses.Applications
import basicClasses.Competition
import basicClasses.Rules
import standings.StandingsInGroups
import standings.StandingsInTeams

interface ModelViewer {
    fun groupsLoaded(rules: Rules)
    fun applicationsLoaded(applications: Applications)
    fun sortitionGenerated(competition: Competition)
    fun sortitionLoaded(competition: Competition)
    fun resultsLoaded(competition: Competition)
    fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams)
    fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups)
}

class GUIViewer : ModelViewer {
    var competition: Competition? = null
        private set
    var applications: Applications? = null
        private set
    var rules: Rules? = null
        private set
    var standingsInTeams: StandingsInTeams? = null
        private set
    var standingsInGroups: StandingsInGroups? = null
        private set

    override fun groupsLoaded(rules: Rules) {
        this.rules = rules
    }

    override fun applicationsLoaded(applications: Applications) {
        println("update applications")
        this.applications = applications
        println(this.applications?.teams)
    }

    override fun sortitionGenerated(competition: Competition) {
        this.competition = competition
    }

    override fun sortitionLoaded(competition: Competition) {
        this.competition = competition
    }

    override fun resultsLoaded(competition: Competition) {
        this.competition = competition
    }

    override fun standingsInGroupsGenerated(standingsInGroups: StandingsInGroups) {
        this.standingsInGroups = standingsInGroups
    }

    override fun standingsInTeamsGenerated(standingsInTeams: StandingsInTeams) {
        this.standingsInTeams = standingsInTeams
    }
}