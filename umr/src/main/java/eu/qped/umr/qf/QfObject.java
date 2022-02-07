package eu.qped.umr.qf;

import com.fasterxml.jackson.annotation.JsonIgnore;



public class QfObject extends QfObjectBase {

	private String answer;
	private QFMainSettings qfMainSettings;
	private QFStyleSettings qfStyleSettings;
	private String[] feedback;

	private QFSemSettings qfSemSettings;
	private String[] answers;
	private int attemptCount;
	private boolean showSolution;
	private QfUser user;
	private QfAssignment assignment;
	private QfBlock block;

	private QfObjectBase question;

	private String checkerClass;



	private String[] settings;

	public int getAttemptCount() {
		return attemptCount;
	}
	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}
	public String[] getFeedback() {
		return feedback;
	}
	public void setFeedback(String[] feedback) {
		this.feedback = feedback;
	}
	public boolean isShowSolution() {
		return showSolution;
	}
	public void setShowSolution(boolean showSolution) {
		this.showSolution = showSolution;
	}
	public QfUser getUser() {
		return user;
	}
	public void setUser(QfUser user) {
		this.user = user;
	}
	public QfAssignment getAssignment() {
		return assignment;
	}
	public void setAssignment(QfAssignment assignment) {
		this.assignment = assignment;
	}
	public QfBlock getBlock() {
		return block;
	}
	public void setBlock(QfBlock block) {
		this.block = block;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}	
	public String getCheckerClass() {
		return checkerClass;
	}
	public void setCheckerClass(String checkerClass) {
		this.checkerClass = checkerClass;
	}
	public QFStyleSettings getQfStyleConf() {
		return qfStyleSettings;
	}

	public void setQfStyleConf(QFStyleSettings qfStyleSettings) {
		this.qfStyleSettings = qfStyleSettings;
	}

	@JsonIgnore
	public void setCondition(String condition, boolean satisfied) {
		setAdditionalProperty(condition, satisfied);
	}

	@JsonIgnore
	public boolean isConditionSatisfied(String condition) {
		return getAdditionalProperty(condition);
	}

	@JsonIgnore
	public void setMessage(String key, String msg) {
		setAdditionalProperty(key, msg);
	}

	@JsonIgnore
	public String getMessage(String key) {
		return getAdditionalProperty(key);
	}

	public String[] getSettings() {
		return settings;
	}

	public void setSettings(String[] settings) {
		this.settings = settings;
	}



	public QFSemSettings getQfSemConfigs() {
		return qfSemSettings;
	}

	public void setQfSemConfigs(QFSemSettings qfSemSettings) {
		this.qfSemSettings = qfSemSettings;
	}

	public QFMainSettings getQfMainSettings() {
		return qfMainSettings;
	}

	public void setQfMainSettings(QFMainSettings qfMainSettings) {
		this.qfMainSettings = qfMainSettings;
	}
	public QfObjectBase getQuestion() {
		return question;
	}
	public void setQuestion(QfObjectBase question) {
		this.question = question;
	}







}