package com.github.undo4j.students;

public class Student {

	private String name;
	private double grade;

	protected Student(String name, double grade) {
		this.name = name;
		this.grade = grade;
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

	}

	public void checIsValid() {
		if (grade > 10) {
			throw new RuntimeException(name + "'s grade is greater than 10!");
		}
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", grade=" + grade + "]";
	}

}
