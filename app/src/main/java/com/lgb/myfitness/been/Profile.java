package com.lgb.myfitness.been;

public class Profile {
	private String name;
	private int age;
	private int ID;
	/**
	 * gender
	 * 1: male
	 * 0: female
	 */
	private int gender;
	/**
	 * cm
	 */
	private double height;
	/**
	 * kg
	 */
	private double weight;

	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
}
