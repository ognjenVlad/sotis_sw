package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
	
	@OneToMany
	private List<QuestionAnswer> answeres;
	
	public ExamResult() {
		this.answeres = new ArrayList<QuestionAnswer>();
	}
	
	public ExamResult(Exam exam, User student) {
		this();
		this.exam = exam;
		this.student = student;
	}
	
	public void addAnswer(QuestionAnswer answer) {
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
	
	public int[] getResultsArray() {
		this.answeres.sort(new Comparator<QuestionAnswer>() {
			@Override
			public int compare(QuestionAnswer qa1, QuestionAnswer qa2) {
				return qa1.getId().compareTo(qa2.getId());
			}
		});
		
		return this.getResultsInteger();
	}
	
	private int[] getResultsInteger() {
		int[] retVal = new int[this.answeres.size()];

		for (int i = 0; i < this.answeres.size(); i++) {
			retVal[i] = this.answeres.get(i).getCorrect() ? 1 : 0;
		}
		
		return retVal;
	}
}
