package com.github.undo4j.students;

import com.github.undo4j.resources.NormalState;
import com.github.undo4j.transactions.ManagedResource;

public class RaiseGrade implements StudentOperation {

	private double raisePercent;

	public RaiseGrade(double raisePercent) {
		this.raisePercent = raisePercent;
	}

	@Override
	public void updateStudent(ManagedResource<StudentState> student) throws Exception {
		StudentState studentState = student.read().get();
		StudentState newStudentState = studentState.raiseGrade(raisePercent);
		student.write(new NormalState<StudentState>(newStudentState));
	}

}
