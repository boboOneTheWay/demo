package org.effectivejava.examples.chapter02.item02.builder;

public class Student {

	public Student(Build build) {
		age = build.age;
		name = build.name;
		sex = build.sex;
		phone = build.phone;
		adress = build.adress;
	}

	private int age;
	private String name;
	private boolean sex;
	private String phone;
	private String adress;
	
	public static class Build{
		private final String name;
		private final String phone;
		private int age;
		private boolean sex;
		private String adress;
		
		Build(String name, String phone){
			this.name = name;
			this.phone = phone;
		}
		
		public Build age(int age) {
			this.age = age;
			return this;
		}
		
		public Build sex(boolean sex) {
			this.sex = sex;
			return this;
		}
		
		public Build adress(String adress) {
			this.adress = adress;
			return this;
		}
		
		public Student build() {
			return new Student(this);
		}
	}
	
	public static void main(String[] args) {
		Student student = new Student.Build("zhangsan", "13720069898").adress("北京市朝阳区").age(18).sex(true).build();
	}
}
