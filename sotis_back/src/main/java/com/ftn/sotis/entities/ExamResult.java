package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ExamResult {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "exam_id")
	private Exam exam;
	
	@ManyToOne
	@JoinColumn(name = "student_id")
	private User student;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<QuestionAnswer> answeres;
	
	@Column
	private Integer questionsAnswered;
	
	public ExamResult() {
		this.answeres = new ArrayList<QuestionAnswer>();
	}
	
	public ExamResult(Exam exam, User student) {
		this();
		this.exam = exam;
		this.student = student;
		this.questionsAnswered = 0;
	}
	
	public void addAnswer(QuestionAnswer answer) {
		this.questionsAnswered = this.questionsAnswered + 1;
		this.answeres.add(answer);
	}
	
	public void addAnswer(Question question, Boolean correct) {
		this.answeres.add(new QuestionAnswer(question,correct));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public List<QuestionAnswer> getAnsweres() {
		return answeres;
	}

	public void setAnsweres(List<QuestionAnswer> answeres) {
		this.answeres = answeres;
	}
	
	public String getResultsArray() {
		this.answeres.sort(new Comparator<QuestionAnswer>() {
			@Override
			public int compare(QuestionAnswer qa1, QuestionAnswer qa2) {
				return qa1.getQuestion().getId().compareTo(qa2.getQuestion().getId());
			}
		});
		
		
		return this.getResultsString();
	}
	
	public String getQuestionIdsOrdered() {
		this.answeres.sort(new Comparator<QuestionAnswer>() {
			@Override
			public int compare(QuestionAnswer qa1, QuestionAnswer qa2) {
				return qa1.getQuestion().getId().compareTo(qa2.getQuestion().getId());
			}
		});
		
		String retVal = "";
		for (QuestionAnswer qa : this.answeres) {
			retVal = retVal + Long.toString(qa.getQuestion().getId()) + ",";
		}
		
		retVal = retVal.substring(0, retVal.length() -1);
		return retVal;
	}
	
	private String getResultsString() {
		String retVal = "";

		for (int i = 0; i < this.answeres.size(); i++) {
			retVal = retVal + (this.answeres.get(i).getCorrect() ? "1" : "0") + ",";
		}
		
		retVal = retVal.substring(0, retVal.length() -1);
		return retVal;
	}

	public Integer getQuestionsAnswered() {
		return questionsAnswered;
	}

	public void setQuestionsAnswered(Integer questionsAnswered) {
		this.questionsAnswered = questionsAnswered;
	}
	
	public boolean isFinished() {
		return this.questionsAnswered == this.exam.getQuestions().size(); 
	}
}
