package com.github.undo4j.altstudents;

public final class Grade {
	public final float grade;
	
	
	public Grade(float grade) {
		this.grade = grade;
	}


	public float toFloat() {
		return grade;
	}
	
	public Grade raiseBy(float percent) {
		return new Grade(grade + grade * percent);
	}
	
	public Grade decreaseBy(float percent) {
		return new Grade(grade - grade * percent);
	}
	
	@Override
	public String toString() {
		return Float.toString(grade);
	}

}
