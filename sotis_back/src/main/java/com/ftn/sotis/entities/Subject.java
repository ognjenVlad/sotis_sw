package com.ftn.sotis.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Subject {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User teacher;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<User> students;
	
	@Column(nullable = false)
	private String title;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Exam> exams;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Domain domain;
	
	public Subject(){
		this.students =  new ArrayList<User>();
		this.exams =  new ArrayList<Exam>();
	}
	
	public Subject(String title, User teacher){
		this();
		this.title = title;
		this.teacher = teacher;
	}

	public void addExam(Exam exam) {
		this.exams.add(exam);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public List<User> getStudents() {
		return students;
	}

	public void setStudents(List<User> students) {
		this.students = students;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
}
