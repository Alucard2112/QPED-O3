package eu.qped.umr.qf;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class QfObject {


	private QFStyleConf qfStyleConf;
	private int attemptCount;
	private String[] feedback;
	private boolean showSolution;
	private QfUser user;
	private QfAssignment assignment;
	private QfBlock block;
	private String[] answers;
	private String answer;
	private String checkerClass;
	private String[] settings;
	private QFSemConfigs qfSemConfigs;
	private QFMainSettings qfMainSettings;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	
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
	public QFStyleConf getQfStyleConf() {
		return qfStyleConf;
	}

	public void setQfStyleConf(QFStyleConf qfStyleConf) {
		this.qfStyleConf = qfStyleConf;
	}


	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }



	public boolean hasProperty(String property) {
		return additionalProperties.containsKey(property);
	}

	@JsonAnySetter
	public void setAdditionalProperty(String property, Object value){
		additionalProperties.put(property, value); 
	} 
	
	@JsonIgnore
	public void setCondition(String condition, boolean satisfied) {
		additionalProperties.put(condition, satisfied);
	}

	@JsonIgnore
	public boolean isConditionSatisfied(String condition) {
		return (Boolean) additionalProperties.get(condition);
	}

	@JsonIgnore
	public void setMessage(String key, String msg) {
		additionalProperties.put(key, msg);
	}

	@JsonIgnore
	public String getMessage(String key) {
		return (String) additionalProperties.get(key);
	}

	@JsonIgnore
	public Integer getAdditionalIntProperty(String property) {
		Object value = additionalProperties.get(property);
		return Integer.parseInt(value.toString());
	}
	@JsonIgnore
	public String[] getAdditionalSettingArrProperty(String property) {
		Object value =  additionalProperties.get(property);
		return (String[]) value;
	}

	@JsonIgnore
	public String getAdditionalStringProperty(String property) {
		Object value = additionalProperties.get(property);
		return value.toString();
	}

	@JsonIgnore
	public boolean getAdditionalBooleanProperty(String property) {
		Object value = additionalProperties.get(property);
		return Boolean.getBoolean(value.toString());
	}

	@JsonIgnore
	public double getAdditionalDoubleProperty(String property) {
		Object value = additionalProperties.get(property);
		return Double.parseDouble(value.toString());
	}

	public String[] getSettings() {
		return settings;
	}

	public void setSettings(String[] settings) {
		this.settings = settings;
	}



	public QFSemConfigs getQfSemConfigs() {
		return qfSemConfigs;
	}

	public void setQfSemConfigs(QFSemConfigs qfSemConfigs) {
		this.qfSemConfigs = qfSemConfigs;
	}

	public QFMainSettings getQfMainSettings() {
		return qfMainSettings;
	}

	public void setQfMainSettings(QFMainSettings qfMainSettings) {
		this.qfMainSettings = qfMainSettings;
	}
}