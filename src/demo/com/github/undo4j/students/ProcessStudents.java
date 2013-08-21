package com.github.undo4j.students;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

public class ProcessStudents implements TransactionalCallable<String> {

	private List<ManagedResource<Student>> students;
	private List<StudentOperation> operations;

	public ProcessStudents(List<ManagedResource<Student>> students,
			List<StudentOperation> operations) {
		this.students = students;
		this.operations = operations;
	}

	@Override
	public String call() throws Exception {
		for (ManagedResource<Student> student : students) {
			for (StudentOperation operation : operations) {
				operation.updateStudent(student);
			}
		}

		return "Operations completed, transaction will now commit.";
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>(students.size());
		list.addAll(students);
		return list;
	}
}
