package com.github.undo4j.altstudents;

import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.NormalState;
import com.github.undo4j.resources.ResourceState;

public class Student implements InternalResource<Grade> {
	private String name;
	private Grade grade;

	public Student(String name, Grade grade) {
		this.name = name;
		this.grade = grade;
	}
	
	public Student(String name, float grade) {
		this.name = name;
		this.grade = new Grade(grade);
	}

	@Override
	public boolean isValidState(ResourceState<Grade> state) {
		if (state.isNull()) { return false; }
		float value = state.get().toFloat();
		return value >= 0 && value <= 10;
	}

	@Override
	public ResourceState<Grade> buildState() {
		return new NormalState<Grade>(grade);
	}

	/**
	 * @throws GradeOutOfBoundsException when an invalid grade value is passed.
	 */
	@Override
	public void applyState(ResourceState<Grade> state)
			throws GradeOutOfBoundsException {
		checkValidState(state);
		this.grade = state.get();
	}
	
	
	@Override
	public String toString() {
		return "Student [name=" + name + ", grade=" + grade + "]";
	}
	
	
	/**
	 * @param state the state to validate.
	 * @throws GradeOutOfBoundsException when the grade's value is null,
	 * below 0, or greater than 10.
	 */
	private void checkValidState(ResourceState<Grade> state)
			throws GradeOutOfBoundsException {
		if (!this.isValidState(state)) {
			throw new GradeOutOfBoundsException(name +
					" has a grade of " + state.get());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
