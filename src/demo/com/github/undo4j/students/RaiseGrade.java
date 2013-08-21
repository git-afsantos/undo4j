package com.github.undo4j.students;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.transactions.ManagedResource;

public class RaiseGrade implements StudentOperation {

	private double raisePercent;

	public RaiseGrade(double raisePercent) {
		this.raisePercent = raisePercent;
	}

	@Override
	public void updateStudent(ManagedResource<Student> student) throws Exception {
		Student s = student.read().get();
		s.raiseGrade(raisePercent);
		student.write(new ImmutableState<Student>(s));
	}

}
