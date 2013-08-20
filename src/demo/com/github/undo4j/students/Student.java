package com.github.undo4j.students;

public class Student {

	private String name;
	private float grade;

	protected Student(String name, float grade) {
		this.name = name;
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public void raiseGrade(float percent) {
		grade += grade * percent;
		if (grade > 10) {
			throw new RuntimeException(name + "'s grade is greater than 10!");
		}
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", grade=" + grade + "]";
	}

}
