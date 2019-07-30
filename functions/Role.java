/*
 * COMP6231 A1
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */
package functions;


public enum Role {

	Customer("C"), Manager("M");

	private String value;

	Role(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public static Role fromString(String text) {
		for (Role b : Role.values()) {
			if (b.value.equalsIgnoreCase(text)) {
				return b;
			}
		}
		throw new IllegalArgumentException(text);
	}
}
