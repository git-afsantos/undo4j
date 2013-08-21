package com.github.undo4j.students;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.github.undo4j.resources.ShadowResource;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;

public class StudentsTransactionDemo {

	public static void main(String[] args) throws Exception {
		StudentsTransactionDemo demo = new StudentsTransactionDemo();
		List<Student> students = buildStudentsList();

		runTransaction(demo, students);
	}

	private static void runTransaction(StudentsTransactionDemo demo,
			List<Student> students) throws IOException, Exception {
		List<StudentOperation> operations = buildOperationsList();

		printStudents(students);
		System.in.read();

		demo.processStudents(students, operations);

		printStudents(students);
	}

	public void processStudents(List<Student> students,
			List<StudentOperation> operations) throws Exception {
		List<ManagedResource<Student>> managedStudents = manageStudents(students);

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		Future<String> f = tm.submit(new ProcessStudents(managedStudents,
				operations));

		System.out.println(f.get() + "\n");
	}

	private List<ManagedResource<Student>> manageStudents(List<Student> students) {
		List<ManagedResource<Student>> managedStudents = new ArrayList<>(
				students.size());
		for (Student student : students) {
			managedStudents.add(ManagedResource.from(new ShadowResource<>(
					new StudentResource(student))));
		}
		return managedStudents;
	}

	private static void printStudents(List<Student> students) {
		for (Student student : students) {
			System.out.println(student);
		}
	}

	private static List<Student> buildStudentsList() {
		List<Student> students = new ArrayList<>(10);
		students.add(new Student("Georgios", 8.5));
		students.add(new Student("Alberto", 7.5));
		students.add(new Student("Anwar", 7));
		students.add(new Student("Rute", 7));
		students.add(new Student("AJ", 5));
		students.add(new Student("Christiaan", 8));
		students.add(new Student("Bart", 3));
		students.add(new Student("Theo", 7));
		students.add(new Student("Dimithrios", 8));
		students.add(new Student("Dennis", 9));

		return students;
	}

	private static List<StudentOperation> buildOperationsList() {
		List<StudentOperation> operations = new ArrayList<>();
		operations.add(new RaiseGrade(0.1));
		// uncomment next line to create an error and trigger a rollback
		// operations.add(new RaiseGrade(0.5));
		return operations;
	}
}
