package com.github.undo4j.altstudents;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

public class RaiseGradesTransaction implements TransactionalCallable<String> {
	private final List<ManagedResource<Grade>> students;
	private final float raisePercent;
	

	public RaiseGradesTransaction(List<ManagedResource<Grade>> students,
			float raisePercent) {
		this.students = students;
		this.raisePercent = raisePercent;
	}

	@Override
	public String call() {
		for (ManagedResource<Grade> student: students) {
			raiseGrade(student);
		}
		return "Raise operation completed, transaction will now commit.";
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>(students.size());
		for (ManagedResource<?> student: students) {
			list.add(student);
		}
		return list;
	}
	
	/**
	 * Raises the grade of the given student by the given percentage.
	 * @param student
	 * @param percent
	 */
	private void raiseGrade(ManagedResource<Grade> student) {
		Grade grade = student.read().get().raiseBy(raisePercent);
		// The Student's applyState will validate the grade, eventually.
		student.write(new ImmutableState<Grade>(grade));
	}

}
