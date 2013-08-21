package com.github.undo4j.students;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;

public class StudentResource implements InternalResource<Student> {

	private Student student;

	public StudentResource(Student student) {
		this.student = student;
	}

	@Override
	public boolean isValidState(ResourceState<Student> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<Student> buildState() throws Exception {
		return new ImmutableState<Student>(student);
	}

	@Override
	public void applyState(ResourceState<Student> state) throws Exception {
		checkValidStudent(state);
		this.student = state.get();

	}

	private void checkValidStudent(ResourceState<Student> state) {
		Student student = state.get();
		student.checIsValid();
	}
}
