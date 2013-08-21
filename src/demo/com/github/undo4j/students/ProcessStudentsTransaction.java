package com.github.undo4j.students;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

public class ProcessStudentsTransaction implements TransactionalCallable<String> {

	private List<ManagedResource<StudentState>> students;
	private List<StudentOperation> operations;

	public ProcessStudentsTransaction(List<ManagedResource<StudentState>> students, List<StudentOperation> operations) {
		this.students = students;
		this.operations = operations;
	}

	@Override
	public String call() throws Exception {
		for (ManagedResource<StudentState> student : students) {
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
