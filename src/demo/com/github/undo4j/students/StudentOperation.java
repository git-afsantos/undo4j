package com.github.undo4j.students;

import com.github.undo4j.transactions.ManagedResource;

public interface StudentOperation {

	public void updateStudent(ManagedResource<Student> student) throws Exception;

}
