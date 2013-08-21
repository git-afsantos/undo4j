package com.github.undo4j.students;

import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.NormalState;
import com.github.undo4j.resources.ResourceState;

public class Student implements InternalResource<StudentState> {

	private StudentState state;

	protected Student(String name, double grade) {
		this.state = new StudentState(name, grade);
	}

	public StudentState getState() {
		return state;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	@Override
	public boolean isValidState(ResourceState<StudentState> state) {
		/*
		 * This is another place to do validation Instead of the boilerplate
		 * code we can have custom code here, but then there is some more
		 * plumbing to do in the applyState method
		 */
		// if (state.isNull()) {
		// return false;
		// }
		// double value = state.get().getGrade();
		// return value >= 0 && value <= 10;
		return !state.isNull();
	}

	@Override
	public ResourceState<StudentState> buildState() throws Exception {
		return new NormalState<StudentState>(state);
	}

	@Override
	public void applyState(ResourceState<StudentState> state) throws Exception {
		// checkValidState(state);
		this.state = state.get();
	}

	private void checkValidState(ResourceState<StudentState> state) {
		if (!this.isValidState(state)) {
			throw new RuntimeException(this.state.getName() + "'s grade is greater than 10!");
		}
	}
}
