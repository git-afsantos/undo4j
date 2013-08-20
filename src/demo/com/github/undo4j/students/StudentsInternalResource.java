package com.github.undo4j.students;

import java.util.List;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;

public class StudentsInternalResource implements InternalResource<List<Student>> {
	private List<Student> dataObject;

	@Override
	public boolean isValidState(ResourceState<List<Student>> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<List<Student>> buildState() throws Exception {
		return new ImmutableState<List<Student>>(dataObject);
	}

	@Override
	public void applyState(ResourceState<List<Student>> state) throws Exception {
		dataObject = state.get();
	}

}
