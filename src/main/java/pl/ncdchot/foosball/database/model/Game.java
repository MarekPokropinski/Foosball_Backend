package pl.ncdchot.foosball.database.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Game extends BaseModel {

	private Date startDate;
	private Date endDate;
	private GameType type;

	@OneToOne
	private Statistics stats;

	@OneToOne
	private Team redTeam;

	@OneToOne
	private Team blueTeam;

	@ManyToOne
	private Rules rules;

	public Game() {

	}

	public Game(GameType type, Rules rules, Statistics stats, Team redTeam, Team blueTeam) {
		this(type,rules,stats);
		this.startDate = new Date();		
		this.blueTeam = blueTeam;
		this.redTeam = redTeam;
	}

	public Game(GameType type, Rules rules, Statistics stats) {
		this.startDate = new Date();
		this.type = type;
		this.rules = rules;
		this.stats = stats;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public Statistics getStats() {
		return stats;
	}

	public void setStats(Statistics stats) {
		this.stats = stats;
	}

	public Team getRedTeam() {
		return redTeam;
	}

	public void setRedTeam(Team redTeam) {
		this.redTeam = redTeam;
	}

	public Team getBlueTeam() {
		return blueTeam;
	}

	public void setBlueTeam(Team blueTeam) {
		this.blueTeam = blueTeam;
	}

	public Rules getRules() {
		return rules;
	}

	public void setRules(Rules rules) {
		this.rules = rules;
	}
}
