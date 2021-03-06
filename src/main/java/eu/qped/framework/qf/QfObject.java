package eu.qped.framework.qf;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.qped.framework.Checker;



public class QfObject extends QfObjectBase {

	private String answer;
	private String[] feedback;

	private String[] answers;
	private int attemptCount;
	private boolean showSolution;
	private QfUser user;
	private QfAssignment assignment;
	private QfBlock block;

	private QFQuestion question;

	private String checkerClass;



	private String[] settings;
	
	@JsonIgnore
	private Checker checker;

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

	public QFQuestion getQuestion() {
		return question;
	}
	
	public void setQuestion(QFQuestion question) {
		this.question = question;
	}


/* TODO add specific support for files. This is how it looks in JSON:
 * 
  "files":[
      {
         "id":"620199e86364f5b97c3341fa",
         "label":"Demo",
         "extension":".java",
         "path":"submission/6201996717ef31555f7a06b9/files",
         "mimetype":"application/octet-stream",
         "url":"https://upload.quarterfall.com/submission/6201996717ef31555f7a06b9/files/620199e86364f5b97c3341fa.java"
      }
   ],
   "file":{
      "id":"620199e86364f5b97c3341fa",
      "label":"Demo",
      "extension":".java",
      "path":"submission/6201996717ef31555f7a06b9/files",
      "mimetype":"application/octet-stream",
      "url":"https://upload.quarterfall.com/submission/6201996717ef31555f7a06b9/files/620199e86364f5b97c3341fa.java"
   },
 * 
 */




}