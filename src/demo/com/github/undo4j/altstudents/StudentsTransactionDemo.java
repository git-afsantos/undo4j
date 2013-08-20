package com.github.undo4j.altstudents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

		try {
			System.out.println(demo.processStudents(students));
		} catch (ExecutionException ex) {
			System.out.println(ex.getMessage());
		}
		printStudents(students);
	}

	public String processStudents(List<Student> students)
			throws Exception {
		TransactionManager tm = TransactionManagers.newSynchronousManager();
		
		Future<String> result =
				tm.submit(new RaiseGradesTransaction(manageStudents(students), 0.1f));
		
		return result.get();
	}
	
	
	private List<ManagedResource<Grade>> manageStudents(List<Student> students) {
		List<ManagedResource<Grade>> list = new ArrayList<>(students.size());
		for (Student student: students) {
			list.add(ManagedResource.from(new ShadowResource<>(student)));
		}
		return list;
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
		students.add(new Student("Dennis", 9.5f));
		// students.add(new Student("Dennis", 7.5f));

		return students;
	}

}
