package com.github.undo4j.students;

import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.NormalState;
import com.github.undo4j.resources.ResourceState;

public class StudentResource implements InternalResource<Student> {

	private Student student;

	public StudentResource(Student student) {
		this.student = student;
	}

	protected StudentResource(StudentResource studentResource) {
		this.student = studentResource.student.clone();
	}

	@Override
	public boolean isValidState(ResourceState<Student> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<Student> buildState() throws Exception {
		return new NormalState<Student>(student);
	}

	@Override
	public void applyState(ResourceState<Student> state) throws Exception {
		this.student = state.get();
	}

	@Override
	protected StudentResource clone() throws CloneNotSupportedException {
		return new StudentResource(this);
	}

}
