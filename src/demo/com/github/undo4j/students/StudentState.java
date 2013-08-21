package com.github.undo4j.students;

public class StudentState {

	private final String name;
	private final double grade;

	protected StudentState(String name, double grade) {
		this.name = name;
		this.grade = grade;
	}

	protected StudentState(StudentState student) {
		this.name = student.name;
		this.grade = student.grade;
	}

	public String getName() {
		return name;
	}

	public double getGrade() {
		return grade;
	}

	@Override
	public String toString() {
		return "StudentState [name=" + name + ", grade=" + grade + "]";
	}

	public StudentState raiseGrade(double percent) {
		double newGrade = grade + grade * percent;
		/*
		 * This is one place where we can do validation If the exception is
		 * thrown then the transaction will be rolled back
		 */
		// if (newGrade > 10) {
		// throw new RuntimeException(name + "'s grade is greater than 10!");
		// }
		return new StudentState(name, newGrade);
	}

}
