package com.github.undo4j.students;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.resources.ShadowResource;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;

public class StudentsTransactionDemo {

	public static void main(String[] args) throws Exception {
		StudentsTransactionDemo demo = new StudentsTransactionDemo();
		List<Student> students = buildStudentsList();

		printStudents(students);
		System.in.read();

		List<Student> newStudents = demo.processStudents(students);
		printStudents(newStudents);
	}

	public List<Student> processStudents(List<Student> students)
			throws Exception {
		TransactionManager tm = TransactionManagers.newSynchronousManager();
		StudentsInternalResource resource = new StudentsInternalResource(
				students);

		tm.submit(new ProcessStudents(students, resource, ManagedResource
				.from(new ShadowResource<>(resource))));

		return resource.buildState().get();

	}

	private static void printStudents(List<Student> students) {
		for (Student student : students) {
			System.out.println(student);
		}
	}

	private static List<Student> buildStudentsList() {
		List<Student> students = new ArrayList<>(10);
		students.add(new Student("Georgios", 8.5f));
		students.add(new Student("Alberto", 7.5f));
		students.add(new Student("Anwar", 7f));
		students.add(new Student("Rute", 7f));
		students.add(new Student("AJ", 5f));
		students.add(new Student("Christiaan", 8f));
		students.add(new Student("Bart", 3f));
		students.add(new Student("Theo", 7f));
		students.add(new Student("Dimithrios", 8f));
		// students.add(new Student("Dennis", 9f));
		students.add(new Student("Dennis", 9.5f));

		return students;
	}
}
