package com.github.undo4j.students;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

public class ProcessStudents implements TransactionalCallable<Boolean> {

	private ManagedResource<List<Student>> managedResource;
	private List<Student> students;

	protected ProcessStudents(List<Student> students,
			ManagedResource<List<Student>> managedResource) {
		this.students = students;
		this.managedResource = managedResource;
	}

	@Override
	public Boolean call() throws Exception {
		managedResource.write(new ImmutableState<List<Student>>(students));

		for (Student student : students) {
			student.raiseGrade(0.1f);
		}

		managedResource.write(new ImmutableState<List<Student>>(students));

		return null;
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>(1);
		list.add(managedResource);
		return list;
	}
}
