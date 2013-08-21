package com.github.undo4j.students;

public class Student {

	private String name;
	private double grade;

	protected Student(String name, double grade) {
		this.name = name;
		this.grade = grade;
	}

	protected Student(Student student) {
		this.name = student.name;
		this.grade = student.grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public void raiseGrade(double percent) {
		grade += grade * percent;
		if (grade > 10) {
			throw new RuntimeException(name + "'s grade is greater than 10!");
		}

	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", grade=" + grade + "]";
	}

	@Override
	protected Student clone() {
		return new Student(this);
	}

}
