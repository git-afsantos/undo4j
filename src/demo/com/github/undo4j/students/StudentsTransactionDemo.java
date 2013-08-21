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

		List<Student> students = new ArrayList<>(10);

		students.add(new Student("Bart", 3));
		students.add(new Student("AJ", 5));
		students.add(new Student("Georgios", 8.5));
		students.add(new Student("Alberto", 7.5));
		students.add(new Student("Anwar", 7));
		students.add(new Student("Rute", 7));
		students.add(new Student("Christiaan", 8));
		students.add(new Student("Theo", 7));
		students.add(new Student("Dimithrios", 8));
		students.add(new Student("Dennis", 9));

		runTransaction(demo, students);
	}

	private static void runTransaction(StudentsTransactionDemo demo, List<Student> students) throws IOException,
			Exception {
		List<StudentOperation> operations = new ArrayList<>();
		operations.add(new RaiseGrade(0.1));
		// operations.add(new RaiseGrade(0.5));

		printStudents(students);
		pause();

		demo.processStudents(students, operations);

		pause();

		printStudents(students);
	}

	public void processStudents(List<Student> students, List<StudentOperation> operations) throws Exception {
		List<ManagedResource<StudentState>> managedStudents = manageStudents(students);

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		try {
			Future<String> f = tm.submit(new ProcessStudentsTransaction(managedStudents, operations));

			System.out.println(f.get() + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ManagedResource<StudentState>> manageStudents(List<Student> students) {
		List<ManagedResource<StudentState>> managedStudents = new ArrayList<>(students.size());
		for (Student student : students) {
			managedStudents.add(ManagedResource.from(new ShadowResource<>(student)));
		}
		return managedStudents;
	}

	private static void pause() throws IOException {
		System.in.read();
	}

	private static void printStudents(List<Student> students) {
		for (Student student : students) {
			System.out.println(student);
		}
	}
}
